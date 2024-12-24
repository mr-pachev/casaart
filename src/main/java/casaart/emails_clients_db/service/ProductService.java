package casaart.emails_clients_db.service;

import casaart.emails_clients_db.model.dto.AddClientDTO;
import casaart.emails_clients_db.model.dto.AddProductDTO;
import casaart.emails_clients_db.model.dto.ClientDTO;
import casaart.emails_clients_db.model.dto.ProductDTO;

import java.util.List;

public interface ProductService {
    //get all products
    List<ProductDTO> getAllProducts();
    //get sorted products
    List<ProductDTO> sortedClients(String sourceTypeName);
    //checking is exist product by name
    boolean isExistProductName(String name);
    //checking is exist product by code
    boolean isExistProductCode(String code);
    //add product
    void addProduct(AddProductDTO addProductDTO);
}
