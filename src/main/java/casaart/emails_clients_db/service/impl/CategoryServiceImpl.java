package casaart.emails_clients_db.service.impl;

import casaart.emails_clients_db.model.dto.CategoryDTO;
import casaart.emails_clients_db.model.entity.Category;
import casaart.emails_clients_db.model.entity.Client;
import casaart.emails_clients_db.repository.CategoryRepository;
import casaart.emails_clients_db.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    //get all categories
    @Override
    public List<CategoryDTO> getAllCategory() {
        List<Category> categories = categoryRepository.findAll();



        return null;
    }
}
