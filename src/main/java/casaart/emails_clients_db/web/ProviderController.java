package casaart.emails_clients_db.web;

import casaart.emails_clients_db.model.dto.AddProviderDTO;
import casaart.emails_clients_db.model.dto.ProviderDTO;
import casaart.emails_clients_db.model.dto.TypeDTO;
import casaart.emails_clients_db.service.ProviderService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class ProviderController {
    private final ProviderService providerService;

    public ProviderController(ProviderService providerService) {
        this.providerService = providerService;
    }

    @ModelAttribute("addProviderDTO")
    public AddProviderDTO addProviderDTO() {
        return new AddProviderDTO();
    }

    @ModelAttribute("providerDTO")
    public ProviderDTO providerDTO() {
        return new ProviderDTO();
    }

    //view all providers
    @GetMapping("/providers")
    public String getAllProviders(Model model) {
        List<ProviderDTO> providerDTOS = providerService.allProviders();

        model.addAttribute("allProviders", providerDTOS);

        return "providers";
    }

    //create new provider
    @GetMapping("/add-provider")
    public String viewAddProviderForm(Model model) {
        model.addAttribute("allProviders", providerService.allProviders());

        if (!model.containsAttribute("isExistProvider")) {
            model.addAttribute("isExistProvider", false);
        }

        return "add-provider";
    }

    @PostMapping("/add-provider")
    public String addProvider(
            @Valid AddProviderDTO addProviderDTO,
            BindingResult bindingResult,
            RedirectAttributes rAtt, Model model) {

        if (bindingResult.hasErrors()) {
            rAtt.addFlashAttribute("addProviderDTO", addProviderDTO);
            rAtt.addFlashAttribute("org.springframework.validation.BindingResult.addProviderDTO", bindingResult);

            return "redirect:/add-provider";
        }

        if (providerService.isExistProvider(addProviderDTO.getName())) {
            rAtt.addFlashAttribute("addProviderDTO", addProviderDTO);
            rAtt.addFlashAttribute("isExistProvider", true);
            rAtt.addFlashAttribute("org.springframework.validation.BindingResult.addProviderDTO", bindingResult);

            return "redirect:/add-provider";
        }

        providerService.addProvider(addProviderDTO);
        return "redirect:/providers";
    }

    //edit current provider
    @PostMapping("/provider-details/{id}")
    public String referenceToEditProviderForm(@PathVariable("id") Long id) {

        return "redirect:/provider-details/" + id;
    }

    @GetMapping("/provider-details/{id}")
    public String fillEditProviderForm(@PathVariable("id") Long id, Model model) {
        TypeDTO typeDTO = typeService.findTypeById(id);
        model.addAttribute(typeDTO);
        model.addAttribute("allCategories", categoryService.getAllCategory());

        return "provider-details";
    }

    @PostMapping("/provider-details")
    public String editProvider(@RequestParam("id") Long id,
                           @Valid TypeDTO typeDTO,
                           BindingResult bindingResult,
                           RedirectAttributes rAtt,
                           Model model) {

        typeDTO.setId(id);

        if (bindingResult.hasErrors()) {
            rAtt.addFlashAttribute("typeDTO", typeDTO);
            rAtt.addFlashAttribute("org.springframework.validation.BindingResult.typeDTO", bindingResult);
            model.addAttribute("allCategories", categoryService.getAllCategory());

            return "provider-details";
        }

        boolean isChangedTypeName = !typeDTO.getName().equals(typeService.findTypeById(id).getName());
        boolean isChangedTypeCode = !typeDTO.getCode().equals(typeService.findTypeById(id).getCode());

        if (isChangedTypeName && typeService.isExistType(typeDTO.getName())) {
            rAtt.addFlashAttribute("typeDTO", typeDTO);
            rAtt.addFlashAttribute("isExistType", true);
            rAtt.addFlashAttribute("org.springframework.validation.BindingResult.typeDTO", bindingResult);
            model.addAttribute("allCategories", categoryService.getAllCategory());

            return "redirect:/provider-details/" + id;
        }

        if (isChangedTypeCode && typeService.isExistTypeCode(typeDTO.getCode())) {
            rAtt.addFlashAttribute("typeDTO", typeDTO);
            rAtt.addFlashAttribute("isExistTypeCode", true);
            rAtt.addFlashAttribute("org.springframework.validation.BindingResult.typeDTO", bindingResult);
            model.addAttribute("allCategories", categoryService.getAllCategory());

            return "redirect:/provider-details/" + id;
        }

        typeService.editType(typeDTO);
        return "redirect:/providers";
    }

    //delete provider by id
    @PostMapping("/delete-provider/{id}")
    public String removeProvider(@PathVariable("id") Long id) {

        typeService.deleteType(id);

        return "redirect:/providers";
    }
}
