package casaart.emails_clients_db.service.impl;

import casaart.emails_clients_db.model.dto.AddProductDTO;
import casaart.emails_clients_db.model.dto.ProductDTO;
import casaart.emails_clients_db.repository.ProductRepository;
import casaart.emails_clients_db.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ModelMapper mapper;

    public ProductServiceImpl(ProductRepository productRepository, ModelMapper mapper) {
        this.productRepository = productRepository;
        this.mapper = mapper;
    }
    //get all products
    @Override
    public List<ProductDTO> getAllProducts() {
        return null;
    }
    //get sorted products
    @Override
    public List<ProductDTO> sortedClients(String sourceTypeName) {
        return null;
    }
    //checking is exist product by name
    @Override
    public boolean isExistProductName(String name) {
        return false;
    }
    //checking is exist product by code
    @Override
    public boolean isExistProductCode(String code) {
        return false;
    }
    //add product
    @Override
    public void addProduct(AddProductDTO addProductDTO) {

    }
}
