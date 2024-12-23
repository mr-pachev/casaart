package casaart.emails_clients_db.service.impl;

import casaart.emails_clients_db.model.dto.AddCategoryDTO;
import casaart.emails_clients_db.model.dto.CategoryDTO;
import casaart.emails_clients_db.model.entity.Category;
import casaart.emails_clients_db.model.entity.Type;
import casaart.emails_clients_db.repository.CategoryRepository;
import casaart.emails_clients_db.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper mapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper mapper) {
        this.categoryRepository = categoryRepository;
        this.mapper = mapper;
    }

    //get all categories
    @Override
    public List<CategoryDTO> getAllCategory() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryDTO> categoryDTOS = new ArrayList<>();

        for (Category category : categories) {
            CategoryDTO categoryDTO = mapper.map(category, CategoryDTO.class);

            categoryDTOS.add(categoryDTO);
        }

        return categoryDTOS;
    }

    //checking is exist category
    @Override
    public boolean isExistCategory(String name) {
        return categoryRepository.findByName(name).isPresent();
    }

    //add category
    @Override
    public void addCategory(AddCategoryDTO addCategoryDTO) {
        Category category = mapper.map(addCategoryDTO, Category.class);

        categoryRepository.save(category);
    }
}
