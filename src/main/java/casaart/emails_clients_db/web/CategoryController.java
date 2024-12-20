package casaart.emails_clients_db.web;

import casaart.emails_clients_db.model.dto.AddCategoryDTO;
import casaart.emails_clients_db.model.dto.AddClientDTO;
import casaart.emails_clients_db.model.dto.CategoryDTO;
import casaart.emails_clients_db.model.enums.SourceType;
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

        return "clients";
    }

    //create new category
    @GetMapping("/add-category")
    public String viewAddCategoryForm(Model model) {
        model.addAttribute("sourceType", SourceType.values());

        if (!model.containsAttribute("isExistCategory")) {
            model.addAttribute("isExistCategory", false);
        }

        return "add-category";
    }

//    @PostMapping("/add-client")
//    public String addClient(
//            @Valid AddClientDTO addClientDTO,
//            BindingResult bindingResult,
//            RedirectAttributes rAtt, Model model) {
//
//        if (bindingResult.hasErrors()) {
//            rAtt.addFlashAttribute("addClientDTO", addClientDTO);
//            rAtt.addFlashAttribute("org.springframework.validation.BindingResult.addClientDTO", bindingResult);
//
//            return "redirect:/add-client";
//        }
//
//        if (clientService.isExistClientEmail(addClientDTO.getEmail())) {
//            rAtt.addFlashAttribute("addClientDTO", addClientDTO);
//            rAtt.addFlashAttribute("isExistEmail", true);
//            rAtt.addFlashAttribute("org.springframework.validation.BindingResult.addClientDTO", bindingResult);
//
//            return "redirect:/add-client";
//        }
//
//        clientService.addClient(addClientDTO);
//
//        return "redirect:/clients";
//    }
}
