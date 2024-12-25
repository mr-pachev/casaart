package casaart.emails_clients_db.service.impl;

import casaart.emails_clients_db.model.dto.AddProductDTO;
import casaart.emails_clients_db.model.dto.ProductDTO;
import casaart.emails_clients_db.model.entity.Category;
import casaart.emails_clients_db.model.entity.Product;
import casaart.emails_clients_db.model.entity.Type;
import casaart.emails_clients_db.repository.CategoryRepository;
import casaart.emails_clients_db.repository.ProductRepository;
import casaart.emails_clients_db.repository.ProviderRepository;
import casaart.emails_clients_db.repository.TypeRepository;
import casaart.emails_clients_db.service.ProductService;
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
    private final ModelMapper mapper;

    public ProductServiceImpl(ProductRepository productRepository, ProviderRepository providerRepository, CategoryRepository categoryRepository, TypeRepository typeRepository, ModelMapper mapper) {
        this.productRepository = productRepository;
        this.providerRepository = providerRepository;
        this.categoryRepository = categoryRepository;
        this.typeRepository = typeRepository;
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

        productRepository.save(product);
    }

    //find product by id
    @Override
    public ProductDTO findProductById(long id) {
        Product product = productRepository.findById(id).get();
        ProductDTO productDTO = mapper.map(product, ProductDTO.class);
        productDTO.setCategory(product.getCategory().getName());
        productDTO.setType(product.getType().getName());

        return productDTO;
    }

    //edit product
    @Override
    public void editProduct(ProductDTO productDTO) {
        Product product = productRepository.findById(productDTO.getId()).get();
        product = mapper.map(productDTO, Product.class);

        product.setCategory(categoryRepository.findByName(productDTO.getCategory()).get());
        product.setType(typeRepository.findByName(productDTO.getType()).get());
        product.setProvider(providerRepository.findByName(productDTO.getProvider()).get());

        productRepository.save(product);
    }

    //delete product
    @Override
    public void deleteProduct(long id) {
        productRepository.deleteById(id);
    }

    List<ProductDTO> productListToProductDTOList(List<Product> products) {
        List<ProductDTO> productDTOS = new ArrayList<>();

        for (Product product : products) {
            ProductDTO productDTO = mapper.map(product, ProductDTO.class);

            productDTO.setCategory(product.getCategory().getName());
            productDTO.setType(product.getType().getName());
            productDTOS.add(productDTO);
        }
        return productDTOS;
    }
}
