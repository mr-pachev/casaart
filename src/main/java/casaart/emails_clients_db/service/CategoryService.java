package casaart.emails_clients_db.service;

import casaart.emails_clients_db.model.dto.AddCategoryDTO;
import casaart.emails_clients_db.model.dto.CategoryDTO;

import java.util.List;

public interface CategoryService {
    //get all categories
    List<CategoryDTO> getAllCategory();
    //checking is exist category by name
    boolean isExistCategory(String name);
    //checking is exist category by code
    boolean isExistCategoryCode(String code);
    //find category by id
    CategoryDTO findCategoryById(long id);
    //add category
    void addCategory(AddCategoryDTO addCategoryDTO);
    //edit category
    void editCategory(CategoryDTO categoryDTO);
    //delete category
    void deleteCategory(long id);
}
