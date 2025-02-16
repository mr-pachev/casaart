package casaart.emails_clients_db.web;

import casaart.emails_clients_db.model.dto.AddCompanyDTO;
import casaart.emails_clients_db.model.dto.CompanyDTO;
import casaart.emails_clients_db.model.dto.PersonDTO;
import casaart.emails_clients_db.model.enums.IndustryType;
import casaart.emails_clients_db.model.enums.LocationType;
import casaart.emails_clients_db.service.CompanyManagerService;
import casaart.emails_clients_db.service.CompanyService;
import casaart.emails_clients_db.service.ContactPersonService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class CompanyController {
    private final CompanyService companyService;
    private final ContactPersonService contactPersonService;
    private final CompanyManagerService companyManagerService;

    public CompanyController(CompanyService companyService, ContactPersonService contactPersonService, CompanyManagerService companyManagerService) {
        this.companyService = companyService;
        this.contactPersonService = contactPersonService;
        this.companyManagerService = companyManagerService;
    }

    @ModelAttribute("addCompanyDTO")
    public AddCompanyDTO addCompanyDTO() {
        return new AddCompanyDTO();
    }

    @ModelAttribute("companyDTO")
    public CompanyDTO companyDTO() {
        return new CompanyDTO();
    }

    @ModelAttribute("personDTO")
    public PersonDTO personDTO() {
        return new PersonDTO();
    }

    // view all companies
    @GetMapping("/companies")
    public String getAllCompanies(Model model) {
        List<CompanyDTO> companyDTOS = companyService.getAllCompanies();

        model.addAttribute("allCompanies", companyDTOS);
        model.addAttribute("allIndustries", IndustryType.values());

        return "companies";
    }

    //view all sorted companies
    @PostMapping("/sort-compnies")
    public String sortCompanies(@RequestParam("companyType") String companyType, Model model) {
        List<CompanyDTO> sortedCompanies = companyService.sortedCompanies(companyType);

        model.addAttribute("allCompanies", sortedCompanies);

        return "companies"; // Връщаме същия шаблон с актуализиран списък
    }

    // create new company
    @GetMapping("/add-company")
    public String viewAddCompanyForm(Model model) {
        model.addAttribute("allLocations", LocationType.values());
        model.addAttribute("allIndustries", IndustryType.values());

        if (!model.containsAttribute("isExistCompany")) {
            model.addAttribute("isExistCompany", false);
        }

        return "add-company";
    }

    @PostMapping("/add-company")
    public String addCompany(
            @Valid AddCompanyDTO addCompanyDTO,
            BindingResult bindingResult,
            RedirectAttributes rAtt, Model model) {

        if (bindingResult.hasErrors()) {
            rAtt.addFlashAttribute("allLocations", LocationType.values());
            rAtt.addFlashAttribute("allIndustries", IndustryType.values());
            rAtt.addFlashAttribute("addCompanyDTO", addCompanyDTO);
            rAtt.addFlashAttribute("org.springframework.validation.BindingResult.addCompanyDTO", bindingResult);

            return "redirect:/add-company";
        }

        if (companyService.isExistCompany(addCompanyDTO.getName())) {
            rAtt.addFlashAttribute("addCompanyDTO", addCompanyDTO);
            rAtt.addFlashAttribute("isExistCompany", true);
            rAtt.addFlashAttribute("org.springframework.validation.BindingResult.addCompanyDTO", bindingResult);

            return "redirect:/add-company";
        }

        companyService.addCompany(addCompanyDTO);

        return "redirect:/companies";
    }

    // edit current company
    @PostMapping("/company-details/{id}")
    public String referenceToEditCompanyForm(@PathVariable("id") Long id) {

        return "redirect:/company-details/" + id;
    }

    @GetMapping("/company-details/{id}")
    public String fillEditCompanyForm(@PathVariable("id") Long id, Model model) {
        CompanyDTO companyDTO = companyService.findCompanyById(id);

        model.addAttribute("companyDTO", companyDTO);
        model.addAttribute("allLocations", LocationType.values());
        model.addAttribute("allIndustries", IndustryType.values());
        model.addAttribute("contactsPersons", companyDTO.getContactPerson());

        return "company-details";
    }

    @PostMapping("/company-details")
    public String editCompany(@RequestParam("id") Long id,
                              @Valid CompanyDTO companyDTO,
                              BindingResult bindingResult,
                              RedirectAttributes rAtt,
                              Model model) {

        companyDTO.setId(id);

        if (bindingResult.hasErrors()) {
            rAtt.addFlashAttribute("companyDTO", companyDTO);
            model.addAttribute("allLocations", LocationType.values());
            model.addAttribute("allIndustries", IndustryType.values());
            model.addAttribute("contactsPersons", companyDTO.getContactPerson());
            rAtt.addFlashAttribute("org.springframework.validation.BindingResult.providerDTO", bindingResult);

            return "company-details";
        }

        boolean isChangedCompanyName = !companyDTO.getName().equals(companyService.findCompanyById(id).getName());

        if (isChangedCompanyName && companyService.isExistCompany(companyDTO.getName())) {
            model.addAttribute("companyDTO", companyDTO);
            model.addAttribute("allLocations", LocationType.values());
            model.addAttribute("allIndustries", IndustryType.values());
            model.addAttribute("contactsPersons", companyDTO.getContactPerson());
            model.addAttribute("isExistCompany", true);
            rAtt.addFlashAttribute("org.springframework.validation.BindingResult.companyDTO", bindingResult);

            return "company-details";
        }

        companyService.editCompany(companyDTO);
        return "redirect:/companies";
    }


    // delete company by id
    @PostMapping("/delete-company/{id}")
    public String removeCompany(@PathVariable("id") Long id) {

        companyService.removeCompany(id);

        return "redirect:/companies";
    }
}
