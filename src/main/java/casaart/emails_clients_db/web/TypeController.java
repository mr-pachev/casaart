package casaart.emails_clients_db.web;

import casaart.emails_clients_db.model.dto.AddTypeDTO;
import casaart.emails_clients_db.model.dto.CategoryDTO;
import casaart.emails_clients_db.model.dto.TypeDTO;
import casaart.emails_clients_db.model.enums.SourceType;
import casaart.emails_clients_db.service.CategoryService;
import casaart.emails_clients_db.service.TypeService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class TypeController {
    private final TypeService typeService;
    private final CategoryService categoryService;

    public TypeController(TypeService typeService, CategoryService categoryService) {
        this.typeService = typeService;
        this.categoryService = categoryService;
    }

    @ModelAttribute("addTypeDTO")
    public AddTypeDTO addTypeDTO() {
        return new AddTypeDTO();
    }

    @ModelAttribute("typeDTO")
    public TypeDTO typeDTO() {
        return new TypeDTO();
    }

    //view all types
    @GetMapping("/types")
    public String getAllTypes(Model model) {
        List<TypeDTO> typeDTOS = typeService.getAllTypes();

        model.addAttribute("allTypes", typeDTOS);

        return "types";
    }

    //create new type
    @GetMapping("/add-type")
    public String viewAddTypeForm(Model model) {
        model.addAttribute("allCategories", categoryService.getAllCategory());

        if (!model.containsAttribute("isExistType")) {
            model.addAttribute("isExistType", false);
        }

        if (!model.containsAttribute("isExistTypeCode")) {
            model.addAttribute("isExistTypeCode", false);
        }

        return "add-type";
    }

    @PostMapping("/add-type")
    public String addType(
            @Valid AddTypeDTO addTypeDTO,
            BindingResult bindingResult,
            RedirectAttributes rAtt) {

        if (bindingResult.hasErrors()) {
            rAtt.addFlashAttribute("addTypeDTO", addTypeDTO);
            rAtt.addFlashAttribute("org.springframework.validation.BindingResult.addTypeDTO", bindingResult);

            return "redirect:/add-type";
        }

        if (typeService.isExistType(addTypeDTO.getName())) {
            rAtt.addFlashAttribute("addTypeDTO", addTypeDTO);
            rAtt.addFlashAttribute("isExistType", true);
            rAtt.addFlashAttribute("org.springframework.validation.BindingResult.addTypeDTO", bindingResult);

            return "redirect:/add-type";
        }

        if (typeService.isExistTypeCode(addTypeDTO.getCode())) {
            rAtt.addFlashAttribute("addTypeDTO", addTypeDTO);
            rAtt.addFlashAttribute("isExistTypeCode", true);
            rAtt.addFlashAttribute("org.springframework.validation.BindingResult.addTypeDTO", bindingResult);

            return "redirect:/add-type";
        }

        typeService.addType(addTypeDTO);
        return "redirect:/types";
    }

    //edit current type
    @PostMapping("/type-details/{id}")
    public String referenceToEdithTypeForm(@PathVariable("id") Long id) {

        return "redirect:/type-details/" + id;
    }

    @GetMapping("/type-details/{id}")
    public String fillEditTypeForm(@PathVariable("id") Long id, Model model) {
        TypeDTO typeDTO = typeService.findTypeById(id);
        model.addAttribute(typeDTO);
        model.addAttribute("allCategories", categoryService.getAllCategory());

        return "type-details";
    }

    @PostMapping("/type-details")
    public String editType(@RequestParam("id") Long id,
                               @Valid TypeDTO typeDTO,
                               BindingResult bindingResult,
                               RedirectAttributes rAtt,
                               Model model) {

        typeDTO.setId(id);

        if (bindingResult.hasErrors()) {
            rAtt.addFlashAttribute("typeDTO", typeDTO);
            rAtt.addFlashAttribute("org.springframework.validation.BindingResult.typeDTO", bindingResult);
            model.addAttribute("allCategories", categoryService.getAllCategory());

            return "type-details";
        }

        boolean isChangedTypeName = !typeDTO.getName().equals(typeService.findTypeById(id).getName());
        boolean isChangedTypeCode = !typeDTO.getCode().equals(typeService.findTypeById(id).getCode());

        if (isChangedTypeName && typeService.isExistType(typeDTO.getName())) {
            rAtt.addFlashAttribute("typeDTO", typeDTO);
            rAtt.addFlashAttribute("isExistType", true);
            rAtt.addFlashAttribute("org.springframework.validation.BindingResult.typeDTO", bindingResult);
            model.addAttribute("allCategories", categoryService.getAllCategory());

            return "redirect:/type-details/" + id;
        }

        if (isChangedTypeCode && typeService.isExistTypeCode(typeDTO.getCode())) {
            rAtt.addFlashAttribute("typeDTO", typeDTO);
            rAtt.addFlashAttribute("isExistTypeCode", true);
            rAtt.addFlashAttribute("org.springframework.validation.BindingResult.typeDTO", bindingResult);
            model.addAttribute("allCategories", categoryService.getAllCategory());

            return "redirect:/type-details/" + id;
        }

        typeService.editType(typeDTO);
        return "redirect:/types";
    }

    //delete type by id
    @PostMapping("/delete-type/{id}")
    public String removeType(@PathVariable("id") Long id) {

        typeService.deleteType(id);

        return "redirect:/types";
    }
}
