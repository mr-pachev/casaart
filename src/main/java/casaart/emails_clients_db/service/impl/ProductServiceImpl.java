package casaart.emails_clients_db.service.impl;

import casaart.emails_clients_db.model.dto.AddProductDTO;
import casaart.emails_clients_db.model.dto.ProductDTO;
import casaart.emails_clients_db.model.entity.Category;
import casaart.emails_clients_db.model.entity.Product;
import casaart.emails_clients_db.model.entity.SerialNumber;
import casaart.emails_clients_db.model.entity.Type;
import casaart.emails_clients_db.repository.*;
import casaart.emails_clients_db.service.ProductService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    //get all products
    @Override
    public List<ProductDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();

        return productListToProductDTOList(products);
    }


    //get sorted products
    @Override
    public List<ProductDTO> sortedProducts(String sourceTypeName) {
        return null;
    }

    //checking is exist product by name
    @Override
    public boolean isExistProductName(String name) {

        return productRepository.findByName(name).isPresent();
    }

    //checking is exist product by code
    @Override
    public boolean isExistProductCode(String code) {
        return productRepository.findByProductCode(code).isPresent();
    }

    //add product
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

    //find product by id
    @Override
    public ProductDTO findProductById(long id) {
        Product product = productRepository.findById(id).get();
        ProductDTO productDTO = mapper.map(product, ProductDTO.class);

        productDTO.setProvider(product.getProvider().getName());
        productDTO.setCategory(product.getCategory().getName());
        productDTO.setType(product.getType().getName());
        productDTO.setSn(product.getSerialNumbers());

        return productDTO;
    }

    //edit product
    @Override
    public void editProduct(ProductDTO productDTO) {
        Product product = productRepository.findById(productDTO.getId()).get();
        product = mapper.map(productDTO, Product.class);

        product.setSerialNumbers(productRepository.findById(productDTO.getId()).get().getSerialNumbers());
        product.setCategory(categoryRepository.findByName(productDTO.getCategory()).get());
        product.setType(typeRepository.findByName(productDTO.getType()).get());
        product.setProvider(providerRepository.findByName(productDTO.getProvider()).get());

        if (productDTO.getPcs() > 0) {
            generateAndSaveSerialNumbers(product, productDTO.getPcs());
        }

        productRepository.save(product);
    }

    //delete product
    @Override
    public void deleteProduct(long id) {
        productRepository.deleteById(id);
    }

    //delete serial number by id
    @Override
    public void deleteSerialNumber(long id, long prodId) {
        Product product = productRepository.findById(prodId).get();
        SerialNumber serialNumber = serialNumberRepository.findById(id);

        List<SerialNumber> serialNumbers = product.getSerialNumbers();
        boolean removed = serialNumbers.removeIf(sn ->
                sn.getSerialNumber().equals(serialNumber.getSerialNumber())
        );

        if (removed) {
            productRepository.save(product);
        }
    }

    List<ProductDTO> productListToProductDTOList(List<Product> products) {
        List<ProductDTO> productDTOS = new ArrayList<>();

        for (Product product : products) {
            ProductDTO productDTO = mapper.map(product, ProductDTO.class);

            productDTO.setCategory(product.getCategory().getName());
            productDTO.setType(product.getType().getName());
            productDTO.setProvider(product.getProvider().getName());
            productDTO.setSn(product.getSerialNumbers());

            productDTOS.add(productDTO);
        }
        return productDTOS;
    }

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
}
