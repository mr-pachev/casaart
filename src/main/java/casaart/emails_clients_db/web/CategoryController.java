package casaart.emails_clients_db.web;

import casaart.emails_clients_db.model.dto.AddCategoryDTO;
import casaart.emails_clients_db.model.dto.CategoryDTO;
import casaart.emails_clients_db.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Controller
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @ModelAttribute("addCategoryDTO")
    public AddCategoryDTO addCategoryDTO() {
        return new AddCategoryDTO();
    }

    @ModelAttribute("categoryDTO")
    public CategoryDTO categoryDTO() {
        return new CategoryDTO();
    }

    //view all categories
    @GetMapping("/categories")
    public String getAllCategories(Model model) {
        List<CategoryDTO> categoryDTOS = categoryService.getAllCategory();

        model.addAttribute("allCategories", categoryDTOS);

        return "clients";
    }
}
