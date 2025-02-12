package casaart.emails_clients_db.web;

import casaart.emails_clients_db.model.dto.CompanyDTO;
import casaart.emails_clients_db.model.dto.PersonDTO;
import casaart.emails_clients_db.service.CompanyManagerService;
import casaart.emails_clients_db.service.CompanyService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CompanyManagerController {
    private final CompanyManagerService companyManagerService;
    private final CompanyService companyService;

    @ModelAttribute("companyDTO")
    public CompanyDTO companyDTO() {
        return new CompanyDTO();
    }

    @ModelAttribute("personDTO")
    public PersonDTO personDTO() {
        return new PersonDTO();
    }

    public CompanyManagerController(CompanyManagerService companyManagerService, CompanyService companyService) {
        this.companyManagerService = companyManagerService;
        this.companyService = companyService;
    }

    // view current manager
    @GetMapping("/current-manager/{id}")
    public String viewCurrentManagerForm(@PathVariable("id") Long id, Model model) {
        PersonDTO personDTO = companyManagerService.findCompanyManagerByCompany(id);

        model.addAttribute(personDTO);

        return "current-company-manager";
    }

    // add company manager
    @PostMapping("/add-company-manager-with-com-id/{id}")
    public String referenceToAddCompanyManagerForm(@PathVariable("id") Long id) {

        return "redirect:/add-company-manager/" + id;
    }

    @GetMapping("/add-company-manager/{id}")
    public String showAddCompanyManagerForm(@PathVariable("id") Long id, Model model) {
        CompanyDTO companyDTO = companyService.findCompanyById(id);
        model.addAttribute("companyDTO", companyDTO);

        return "add-company-manager";
    }

    @PostMapping("/add-company-manager")
    public String addCompanyManager(@RequestParam("id") Long id,
                                    @Valid PersonDTO personDTO,
                                    BindingResult bindingResult,
                                    RedirectAttributes rAtt,
                                    Model model) {

        CompanyDTO companyDTO = companyService.findCompanyById(id);

        if (bindingResult.hasErrors()) {
            model.addAttribute("companyDTO", companyDTO);
            rAtt.addFlashAttribute("personDTO", personDTO);
            rAtt.addFlashAttribute("org.springframework.validation.BindingResult.personDTO", bindingResult);

            return "add-company-manager";
        }

        personDTO.setCompany(companyDTO.getName());
        companyManagerService.addCompanyManager(personDTO, id);

        return "redirect:/companies";
    }

    // edit company manager
    @PostMapping("/company-manager-details/{id}")
    public String referenceToEditCompanyManagerForm(@PathVariable("id") Long id) {

        return "redirect:/company-manager-details/" + id;
    }

    @GetMapping("/company-manager-details/{id}")
    public String fillEditCompanyManagerForm(@PathVariable("id") Long id, Model model) {
        PersonDTO personDTO = companyManagerService.findCompanyManagerById(id);

        model.addAttribute(personDTO);

        return "company-manager-details";
    }

    @PostMapping("/company-manager-details")
    public String editCompanyManager(@Valid PersonDTO personDTO,
                                     BindingResult bindingResult,
                                     RedirectAttributes rAtt,
                                     Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute(personDTO);
            rAtt.addFlashAttribute("org.springframework.validation.BindingResult.personDTO", bindingResult);

            return "company-manager-details";
        }

        companyManagerService.editCompanyManager(personDTO);

        CompanyDTO findCompany = companyService.findCompanyByName(personDTO.getCompany().trim().replaceAll(",$", ""));

        return "redirect:/current-manager/" + findCompany.getId();
    }

    // delete company manager
    @PostMapping("/delete-company-manager/{id}")
    public String removeCompanyManager(@PathVariable("id") Long id) {

        companyManagerService.removeCompanyManager(id);

        return "redirect:/companies";
    }
}
