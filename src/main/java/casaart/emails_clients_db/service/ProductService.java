package casaart.emails_clients_db.service;

import casaart.emails_clients_db.model.dto.*;

import java.util.List;

public interface ProductService {
    //get all products
    List<ProductDTO> getAllProducts();
    //get sorted products
    List<ProductDTO> sortedProducts(String sourceTypeName);
    //checking is exist product by name
    boolean isExistProductName(String name);
    //checking is exist product by code
    boolean isExistProductCode(String code);
    //add product
    void addProduct(AddProductDTO addProductDTO);
    //find product by id
    ProductDTO findProductById(long id);
    //edit product
    void editProduct(ProductDTO productDTO);
    //delete product
    void deleteProduct(long id);
}
