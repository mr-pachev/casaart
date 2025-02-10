package casaart.emails_clients_db.web;

import casaart.emails_clients_db.model.dto.AddCompanyDTO;
import casaart.emails_clients_db.model.dto.CompanyDTO;
import casaart.emails_clients_db.model.dto.PersonDTO;
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
public class ContactPersonController {
    private final CompanyService companyService;
    private final ContactPersonService contactPersonService;
    private final CompanyManagerService companyManagerService;

    public ContactPersonController(CompanyService companyService, ContactPersonService contactPersonService, CompanyManagerService companyManagerService) {
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

    //view contact persons
    @PostMapping("/current-contact-persons/{id}")
    public String referenceToViewContactPersonsForm(@PathVariable("id") Long id) {

        return "redirect:/current-contact-persons/" + id;
    }

    @GetMapping("/current-contact-persons/{id}")
    public String fillEditContactPersonsForm(@PathVariable("id") Long id, Model model) {
        List<PersonDTO> contactPersonsDTOS = contactPersonService.allContactPersons(id);
        CompanyDTO companyDTO = companyService.findCompanyById(id);

        model.addAttribute("contactPersons", contactPersonsDTOS);
        model.addAttribute("companyDTO", companyDTO);

        return "current-contact-persons";
    }

    // add contact person
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
            model.addAttribute("personDTO", personDTO);
            model.addAttribute("isExist", true);
            model.addAttribute("org.springframework.validation.BindingResult.personDTO", bindingResult);

            return "add-contact-person";
        }

        boolean isExistPersonLikeManager = companyManagerService.isExistCompanyManager(personDTO);

        if (isExistPersonLikeManager) {
            model.addAttribute("companyDTO", companyDTO);
            model.addAttribute("personDTO", personDTO);
            model.addAttribute("isExistLikeManager", true);
            model.addAttribute("org.springframework.validation.BindingResult.personDTO", bindingResult);

            return "add-contact-person";
        }

        personDTO.setCompany(companyDTO.getName());
        contactPersonService.addContactPerson(personDTO, id);

        return "redirect:/current-contact-persons/" + id;
    }

    // edit contact person
    @PostMapping("/contact-person-details/{id}")
    public String referenceToEditContactPersonForm(@PathVariable("id") Long id) {

        return "redirect:/contact-person-details/" + id;
    }

    @GetMapping("/contact-person-details/{id}")
    public String fillEditCompanyManagerForm(@PathVariable("id") Long id, Model model) {
        PersonDTO personDTO = contactPersonService.getContactPersonById(id);

        model.addAttribute(personDTO);

        return "contact-person-details";
    }

    @PostMapping("/contact-person-details")
    public String editContactPerson(@Valid PersonDTO personDTO,
                                     BindingResult bindingResult,
                                     RedirectAttributes rAtt,
                                     Model model) {

        if (bindingResult.hasErrors()) {
            rAtt.addFlashAttribute(personDTO);
            rAtt.addFlashAttribute("org.springframework.validation.BindingResult.personDTO", bindingResult);

            return "contact-person-details";
        }

        companyManagerService.editCompanyManager(personDTO);

        CompanyDTO findCompany = companyService.findCompanyByName(personDTO.getCompany().trim().replaceAll(",$", ""));

        return "redirect:/current-contact-persons/" + findCompany.getId();
    }
}
