package casaart.emails_clients_db.web;

import casaart.emails_clients_db.model.dto.AddCompanyDTO;
import casaart.emails_clients_db.model.dto.CompanyDTO;
import casaart.emails_clients_db.model.dto.PersonDTO;
import casaart.emails_clients_db.model.enums.IndustryType;
import casaart.emails_clients_db.model.enums.LocationType;
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

    public CompanyController(CompanyService companyService, ContactPersonService contactPersonService) {
        this.companyService = companyService;
        this.contactPersonService = contactPersonService;
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

    //view all companies
    @GetMapping("/companies")
    public String getAllCompanies(Model model) {
        List<CompanyDTO> companyDTOS = companyService.getAllCompanies();

        model.addAttribute("allCompanies", companyDTOS);

        return "companies";
    }

    //create new company
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
            model.addAttribute("allLocations", LocationType.values());
            model.addAttribute("allIndustries", IndustryType.values());
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

        long companyId = companyService.addCompany(addCompanyDTO);
        return "redirect:/add-person/" + companyId;
    }

    @GetMapping("/add-person/{id}")
    public String fillAddContactPersonForm(@PathVariable("id") Long id, Model model) {
        CompanyDTO companyDTO = companyService.findCompanyById(id);

        model.addAttribute(companyDTO);
        model.addAttribute("allLocations", companyDTO.getLocationType());
        model.addAttribute("currentIndustry", companyDTO.getIndustries());

        return "add-person";
    }

    //add company manager
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
        companyService.addCompanyManager(personDTO, id);

        return "redirect:/add-person/" + id;
    }

    //add contact person
    @PostMapping("/add-contact-person-with-com-id/{id}")
    public String referenceToAddContactPersonForm(@PathVariable("id") Long id) {

        return "redirect:/add-contact-person/" + id;
    }

    @GetMapping("/add-contact-person/{id}")
    public String showAddContactPersonForm(@PathVariable("id") Long id, Model model) {
        CompanyDTO companyDTO = companyService.findCompanyById(id);
        model.addAttribute("companyDTO", companyDTO);

        return "add-contact-person";
    }

    @PostMapping("/add-contact-person")
    public String addContactPerson(@RequestParam("id") Long id,
                                    @Valid PersonDTO personDTO,
                                    BindingResult bindingResult,
                                    RedirectAttributes rAtt,
                                    Model model) {

        CompanyDTO companyDTO = companyService.findCompanyById(id);

        if (bindingResult.hasErrors()) {
            model.addAttribute("companyDTO", companyDTO);
            rAtt.addFlashAttribute("personDTO", personDTO);
            rAtt.addFlashAttribute("org.springframework.validation.BindingResult.personDTO", bindingResult);

            return "add-contact-person";
        }

        boolean isExistPerson = contactPersonService.isExistContactPerson(personDTO);

        if (isExistPerson) {
            model.addAttribute("companyDTO", companyDTO);
            model.addAttribute("isExist", true);
            model.addAttribute("personDTO", personDTO);
            model.addAttribute("org.springframework.validation.BindingResult.personDTO", bindingResult);

            return "add-contact-person";
        }

        personDTO.setCompany(companyDTO.getName());
        companyService.addContactPerson(personDTO, id);

        return "redirect:/add-person/" + id;
    }

    //delete company by id
    @PostMapping("/delete-company/{id}")
    public String removeCompany(@PathVariable("id") Long id) {

        companyService.removeCompany(id);

        return "redirect:/companies";
    }
}
