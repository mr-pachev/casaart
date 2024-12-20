package casaart.emails_clients_db.service;

import casaart.emails_clients_db.model.dto.CategoryDTO;
import casaart.emails_clients_db.model.dto.ClientDTO;

import java.util.List;

public interface CategoryService {
    //get all categories
    List<CategoryDTO> getAllCategory();
}
