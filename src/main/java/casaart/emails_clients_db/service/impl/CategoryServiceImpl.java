package casaart.emails_clients_db.service.impl;

import casaart.emails_clients_db.model.dto.AddCategoryDTO;
import casaart.emails_clients_db.model.dto.CategoryDTO;
import casaart.emails_clients_db.model.entity.Category;
import casaart.emails_clients_db.model.entity.Product;
import casaart.emails_clients_db.repository.CategoryRepository;
import casaart.emails_clients_db.repository.ProductRepository;
import casaart.emails_clients_db.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    private final ProductRepository productRepository;
    private final ModelMapper mapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, ProductRepository productRepository, ModelMapper mapper) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
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

    //checking is exist category by name
    @Override
    public boolean isExistCategory(String name) {
        return categoryRepository.findByName(name).isPresent();
    }
    //checking is exist category by code
    @Override
    public boolean isExistCategoryCode(String code) {
        return categoryRepository.findByCode(code).isPresent();
    }
    //find category by id
    @Override
    public CategoryDTO findCategoryById(long id) {
        Category category = categoryRepository.findById(id);
        CategoryDTO categoryDTO = mapper.map(category, CategoryDTO.class);

        return categoryDTO;
    }

    //add category
    @Override
    public void addCategory(AddCategoryDTO addCategoryDTO) {
        Category category = mapper.map(addCategoryDTO, Category.class);

        categoryRepository.save(category);
    }
    //edit category
    @Override
    public void editCategory(CategoryDTO categoryDTO) {
        Category category = categoryRepository.findById(categoryDTO.getId());
        category = mapper.map(categoryDTO, Category.class);

        categoryRepository.save(category);

        List<Product> products = productRepository.findAllByCategoryName(categoryDTO.getName());

        for (Product product : products) {
            product.updateSerialNumbersOnCategoryChange();

            productRepository.save(product);
        }
    }
}
