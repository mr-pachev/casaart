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

import java.util.List;

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

    // view all company managers
    @GetMapping("/all-company-managers")
    public String viewAllManagerForm(Model model) {
        List<PersonDTO> allCompanyManagers = companyManagerService.allCompanyManagers();

        model.addAttribute("allCompanyManagers", allCompanyManagers);

        return "all-company-managers";
    }

    // view all sorted company managers
    @PostMapping("/sort-company-managers")
    public String sortCompanyManagers(@RequestParam("type") String type, Model model) {
        List<PersonDTO> allCompanyManagers = companyManagerService.sortedCompanyManagersByType(type);

        model.addAttribute("allCompanyManagers", allCompanyManagers);

        return "all-company-managers"; // Връщаме същия шаблон с актуализиран списък
    }

    // view current manager
    @GetMapping("/current-company-manager/{id}")
    public String viewCurrentManagerForm(@PathVariable("id") Long id, Model model) {
        PersonDTO personDTO = companyManagerService.findCompanyManagerByCompany(id);
        CompanyDTO companyDTO = companyService.findCompanyById(id);

        boolean isPartner = companyDTO.getCompanyType().equals("ПАРТНЬОР");

        model.addAttribute("personDTO", personDTO);
        model.addAttribute("isPartner", isPartner);

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

        return "redirect:/current-company-manager/" + id;
    }

    // edit company manager
    @PostMapping("/company-manager-details/{id}")
    public String referenceToEditCompanyManagerForm(@PathVariable("id") Long id) {

        return "redirect:/company-manager-details/" + id;
    }

    @GetMapping("/company-manager-details/{id}")
    public String fillEditCompanyManagerForm(@PathVariable("id") Long id, Model model) {
        PersonDTO personDTO = companyManagerService.findCompanyManagerById(id);
        CompanyDTO companyDTO = companyService.findCompanyByName(personDTO.getCompany());

        boolean isPartner = companyDTO.getCompanyType().equals("ПАРТНЬОР");

        model.addAttribute(personDTO);
        model.addAttribute("isPartner", isPartner);

        return "company-manager-details";
    }

    @PostMapping("/company-manager-details")
    public String editCompanyManager(@Valid PersonDTO personDTO,
                                     BindingResult bindingResult,
                                     RedirectAttributes rAtt,
                                     Model model) {

        CompanyDTO companyDTO = companyService.findCompanyByName(personDTO.getCompany());

        boolean isPartner = companyDTO.getCompanyType().equals("ПАРТНЬОР");

        if (bindingResult.hasErrors()) {
            model.addAttribute(personDTO);
            model.addAttribute("isPartner", isPartner);
            rAtt.addFlashAttribute("org.springframework.validation.BindingResult.personDTO", bindingResult);

            return "company-manager-details";
        }

        companyManagerService.editCompanyManager(personDTO);

        CompanyDTO findCompany = companyService.findCompanyByName(personDTO.getCompany());

        return "redirect:/current-company-manager/" + findCompany.getId();
    }

    // delete company manager
    @PostMapping("/delete-company-manager/{id}")
    public String removeCompanyManager(@PathVariable("id") Long id) {

        CompanyDTO companyDTO = companyService.findByCompanyManagerId(id);

        companyManagerService.removeCompanyManager(id);

        if (companyDTO.getCompanyType().equals("ПАРТНЬОР")){
            return "redirect:/partners";
        }

        return "redirect:/suppliers";
    }
}
