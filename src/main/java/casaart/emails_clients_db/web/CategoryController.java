package casaart.emails_clients_db.web;

import casaart.emails_clients_db.model.dto.AddCategoryDTO;
import casaart.emails_clients_db.model.dto.CategoryDTO;
import casaart.emails_clients_db.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

        return "categories";
    }

    //create new category
    @GetMapping("/add-category")
    public String viewAddCategoryForm(Model model) {

        if (!model.containsAttribute("isExistCategory")) {
            model.addAttribute("isExistCategory", false);
        }

        if (!model.containsAttribute("isExistCategoryCode")) {
            model.addAttribute("isExistCategoryCode", false);
        }

        return "add-category";
    }

    @PostMapping("/add-category")
    public String addCategory(
            @Valid AddCategoryDTO addCategoryDTO,
            BindingResult bindingResult,
            RedirectAttributes rAtt, Model model) {

        if (bindingResult.hasErrors()) {
            rAtt.addFlashAttribute("addCategoryDTO", addCategoryDTO);
            rAtt.addFlashAttribute("org.springframework.validation.BindingResult.addCategoryDTO", bindingResult);

            return "redirect:/add-category";
        }

        if (categoryService.isExistCategory(addCategoryDTO.getName())) {
            rAtt.addFlashAttribute("addCategoryDTO", addCategoryDTO);
            rAtt.addFlashAttribute("isExistCategory", true);
            rAtt.addFlashAttribute("org.springframework.validation.BindingResult.addCategoryDTO", bindingResult);

            return "redirect:/add-category";
        }

        if (categoryService.isExistCategoryCode(addCategoryDTO.getCode())) {
            rAtt.addFlashAttribute("addCategoryDTO", addCategoryDTO);
            rAtt.addFlashAttribute("isExistCategoryCode", true);
            rAtt.addFlashAttribute("org.springframework.validation.BindingResult.addCategoryDTO", bindingResult);

            return "redirect:/add-category";
        }

        categoryService.addCategory(addCategoryDTO);
        return "redirect:/categories";
    }
}
