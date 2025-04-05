package casaart.emails_clients_db.service.impl;

import casaart.emails_clients_db.model.dto.AddProductDTO;
import casaart.emails_clients_db.model.dto.ProductDTO;
import casaart.emails_clients_db.model.entity.*;
import casaart.emails_clients_db.repository.*;
import casaart.emails_clients_db.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProviderRepository providerRepository;
    private final CategoryRepository categoryRepository;
    private final TypeRepository typeRepository;
    private final SerialNumberRepository serialNumberRepository;
    private final ModelMapper mapper;

    public ProductServiceImpl(ProductRepository productRepository, ProviderRepository providerRepository, CategoryRepository categoryRepository, TypeRepository typeRepository, SerialNumberRepository serialNumberRepository, ModelMapper mapper) {
        this.productRepository = productRepository;
        this.providerRepository = providerRepository;
        this.categoryRepository = categoryRepository;
        this.typeRepository = typeRepository;
        this.serialNumberRepository = serialNumberRepository;
        this.mapper = mapper;
    }

    // get all products
    @Override
    public List<ProductDTO> getAllProducts() {
        List<Product> products = productRepository.findAllByOrderByCreatedAtDesc();

        return productListMapToProductDTOList(products);
    }

    // get sorted products
    @Override
    public List<ProductDTO> sortedProducts(String sourceTypeName) {
        List<Product> sortedProductList = new ArrayList<>();

        if ("name".equals(sourceTypeName)) {
            sortedProductList = productRepository.findAllByOrderByNameAsc();
        } else if ("type".equals(sourceTypeName)) {
            sortedProductList = productRepository.findAllByOrderByTypeAsc();
        } else if ("category".equals(sourceTypeName)) {
            sortedProductList = productRepository.findAllByOrderByCategoryAsc();
        } else if ("provider".equals(sourceTypeName)) {
            sortedProductList = productRepository.findAllOrderedByProvider();
        } else if ("pcs".equals(sourceTypeName)) {
            sortedProductList = productRepository.findAllOrderedBySerialNumbersCount();
        } else if ("clientPrice".equals(sourceTypeName)) {
            sortedProductList = productRepository.findAllOrderedByClientPriceDesc();
        } else if ("createDate".equals(sourceTypeName)) {
            sortedProductList = productRepository.findAllByOrderByCreatedAtDesc();
        } else if ("modifyDate".equals(sourceTypeName)) {
            sortedProductList = productRepository.findAllByOrderByUpdatedAtDesc();
        } else {
            sortedProductList = productRepository.findAllByOrderByCreatedAtDesc();
        }

        return productListMapToProductDTOList(sortedProductList);
    }

    // find by productIdentifier
    @Override
    public ProductDTO findByproductIdentifier(String productIdentifier) {
        Product product = null;
        if (productIdentifier.length() < 10) {
            if (productRepository.findByProviderProductCode(productIdentifier).isPresent()) {
                product = productRepository.findByProviderProductCode(productIdentifier).get();
            }
        } else if (productIdentifier.matches(".*\\d.*")) {
            if (findProductBySerialNumber(productIdentifier).isPresent()) {
                product = findProductBySerialNumber(productIdentifier).get();
            }
        } else {
            if (productRepository.findByName(productIdentifier).isPresent()) {
                product = productRepository.findByName(productIdentifier).get();
            }
        }

        return (product == null) ? new ProductDTO() : productMapToProductDTO(product);
    }

    // find by imagePath
    @Override
    public boolean isExistImage(String imagePath) {
        return productRepository.findByImagePath(imagePath).isPresent();
    }

    // checking is exist product by name
    @Override
    public boolean isExistProductName(String name) {

        return productRepository.findByName(name).isPresent();
    }

    // checking is exist product by code and type name
    @Override
    public boolean isExistProductCodeWithType(String typeName, String code) {
        return productRepository.findByTypeNameAndProductCode(typeName, code).isPresent();
    }

    // add product
    @Override
    public void addProduct(AddProductDTO addProductDTO) {
        Product product = mapper.map(addProductDTO, Product.class);
        Category category = categoryRepository.findByName(addProductDTO.getCategory()).get();
        Type type = typeRepository.findByName(addProductDTO.getType()).get();

        product.setCategory(category);
        product.setType(type);
        product.setProvider(providerRepository.findByName(addProductDTO.getProvider()).get());

        // Запазване на продукта в базата данни
        productRepository.save(product);

        // Създаване и запазване на серийните номера
        int pcs = addProductDTO.getPcs();
        if (pcs > 0) {
            generateAndSaveSerialNumbers(product, pcs);
        }

        // Актуализиране на продукта с новите серийни номера
        productRepository.save(product);
    }

    // find product by id
    @Override
    public ProductDTO findProductById(long id) {
        Product product = productRepository.findById(id).get();

        return productMapToProductDTO(product);
    }

    // edit product
    @Override
    public void editProduct(ProductDTO productDTO) {
        Product product = productRepository.findById(productDTO.getId()).get();
        product = mapper.map(productDTO, Product.class);

        product.setSerialNumbers(productRepository.findById(productDTO.getId()).get().getSerialNumbers());
        product.setCategory(categoryRepository.findByName(productDTO.getCategory()).get());
        product.setType(typeRepository.findByName(productDTO.getType()).get());
        product.setProvider(providerRepository.findByName(productDTO.getProvider()).get());

        //промяна на серийните номера според productCode
        product.updateSerialNumbersOnProductCodeChange();

        if (productDTO.getPcs() > 0) {
            product.setUpdatedAt(LocalDateTime.now());
            generateAndSaveSerialNumbers(product, productDTO.getPcs());
        }

        productRepository.save(product);
    }

    // delete product
    @Override
    public void deleteProduct(long id) {
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) return;

        // 1. Изтриване на изображението от диска
        String imagePath = product.getImagePath(); // Пример: "uploads/product123.jpg"
        if (imagePath != null && !imagePath.isEmpty()) {

            File imageFile = new File("C:\\" + imagePath);

            System.out.println("Deleting image: " + imageFile.getAbsolutePath());

            if (imageFile.exists() && imageFile.isFile()) {
                boolean deleted = imageFile.delete();
                System.out.println("Image deleted: " + deleted);
            } else {
                System.out.println("Image file not found or not a file.");
            }
        }

        // 2. Премахване на продукта от доставчика
        Provider provider = providerRepository.findByName(product.getProvider().getName()).orElse(null);
        if (provider != null) {
            provider.getProducts().removeIf(p -> p.getId().equals(id));
            providerRepository.save(provider);
        }

        // 3. Изтриване от базата
        productRepository.deleteById(id);
    }

    // delete serial number by id
    @Override
    public void deleteSerialNumber(long id, long prodId) {
        Product product = productRepository.findById(prodId).get();
        SerialNumber serialNumber = serialNumberRepository.findById(id);

        List<SerialNumber> serialNumbers = product.getSerialNumbers();
        boolean removed = serialNumbers.removeIf(sn ->
                sn.getSerialNumber().equals(serialNumber.getSerialNumber())
        );

        if (removed) {
            product.setUpdatedAt(LocalDateTime.now());
            productRepository.save(product);
        }
    }

    // selling products
    @Override
    public void sellingProducts(List<String> sn) {

        for (String serialNumber : sn) {
            String serialNumberUpperCase = serialNumber.toUpperCase();

            Product product = findProductBySerialNumber(serialNumberUpperCase).get();

            List<SerialNumber> serialNumbers = product.getSerialNumbers();
            boolean removed = serialNumbers.removeIf(sN ->
                    sN.getSerialNumber().equals(serialNumberUpperCase));

            if (removed) {
                product.setUpdatedAt(LocalDateTime.now());
                productRepository.save(product);
            }
        }

    }

    // generate and save serial numbers
    private void generateAndSaveSerialNumbers(Product product, int pcs) {

        for (int i = 0; i < pcs; i++) {
            // Създаване на нов сериен номер
            SerialNumber serialNumber = new SerialNumber();
            serialNumber.setSerialNumber(product.generateCode());
            serialNumber.setProduct(product);

            // Запазване в хранилището
            serialNumberRepository.save(serialNumber);

            // Добавяне към списъка със серийни номера на продукта
            product.getSerialNumbers().add(serialNumber);
        }
    }

    // find product by serial number
    public Optional<Product> findProductBySerialNumber(String serialNumber) {
        return serialNumberRepository.findBySerialNumber(serialNumber)
                .map(SerialNumber::getProduct);
    }

    // product map to productDTO
    ProductDTO productMapToProductDTO(Product product) {
        ProductDTO productDTO = mapper.map(product, ProductDTO.class);

        productDTO.setProvider(product.getProvider().getName());
        productDTO.setCategory(product.getCategory().getName());
        productDTO.setType(product.getType().getName());
        productDTO.setSn(product.getSerialNumbers());
        return productDTO;
    }

    // productList map to productDTOList
    List<ProductDTO> productListMapToProductDTOList(List<Product> productList) {
        List<ProductDTO> allProductDTOS = new ArrayList<>();

        for (Product product : productList) {
            ProductDTO productDTO = productMapToProductDTO(product);

            allProductDTOS.add(productDTO);
        }

        return allProductDTOS;
    }
}
