package casaart.emails_clients_db.web;

import casaart.emails_clients_db.model.dto.AddCategoryDTO;
import casaart.emails_clients_db.model.dto.CategoryDTO;
import casaart.emails_clients_db.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
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

    //edit current category
    @PostMapping("/category-details/{id}")
    public String referenceToEdithCategoryForm(@PathVariable("id") Long id) {

        return "redirect:/category-details/" + id;
    }

    @GetMapping("/category-details/{id}")
    public String fillEditCategoryForm(@PathVariable("id") Long id, Model model) {
        CategoryDTO categoryDTO = categoryService.findCategoryById(id);
        model.addAttribute(categoryDTO);

        return "category-details";
    }

    @PostMapping("/category-details")
    public String editCategory(@RequestParam("id") Long id,
                              @Valid CategoryDTO categoryDTO,
                              BindingResult bindingResult,
                              RedirectAttributes rAtt,
                              Model model) {

        categoryDTO.setId(id);

        if (bindingResult.hasErrors()) {
            rAtt.addFlashAttribute("categoryDTO", categoryDTO);
            rAtt.addFlashAttribute("org.springframework.validation.BindingResult.categoryDTO", bindingResult);

            return "category-details";
        }

        boolean isChangedCategoryName = !categoryDTO.getName().equals(categoryService.findCategoryById(id).getName());
        boolean isChangedCategoryCode = !categoryDTO.getCode().equals(categoryService.findCategoryById(id).getCode());

        if (isChangedCategoryName && categoryService.isExistCategoryCode(categoryDTO.getCode())) {
            rAtt.addFlashAttribute("categoryDTO", categoryDTO);
            rAtt.addFlashAttribute("isExistCategory", true);
            rAtt.addFlashAttribute("org.springframework.validation.BindingResult.categoryDTO", bindingResult);

            return "redirect:/category-details/" + id;
        }

        if (isChangedCategoryCode && categoryService.isExistCategoryCode(categoryDTO.getCode())) {
            rAtt.addFlashAttribute("categoryDTO", categoryDTO);
            rAtt.addFlashAttribute("isExistCategoryCode", true);
            rAtt.addFlashAttribute("org.springframework.validation.BindingResult.categoryDTO", bindingResult);

            return "redirect:/category-details/" + id;
        }

        categoryService.editCategory(categoryDTO);
        return "redirect:/categories";
    }
}
