package casaart.emails_clients_db.service;

import casaart.emails_clients_db.model.dto.AddCategoryDTO;
import casaart.emails_clients_db.model.dto.CategoryDTO;

import java.util.List;

public interface TypeService {
    //get all types
    List<CategoryDTO> getAllTypes();
    //checking is exist category
    boolean isExistType(String name);
    //add category
    void addCategory(AddCategoryDTO addCategoryDTO);
}
