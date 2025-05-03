package casaart.emails_clients_db.web;

import casaart.emails_clients_db.model.dto.ClientDTO;
import casaart.emails_clients_db.model.dto.CompanyDTO;
import casaart.emails_clients_db.model.dto.PersonDTO;
import casaart.emails_clients_db.model.enums.SourceType;
import casaart.emails_clients_db.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class MarketingController {
    private final MarketingService marketingService;
    private final CompanyManagerService companyManagerService;
    private final ContactPersonService contactPersonService;
    private final ClientService clientService;

    private final CompanyService companyService;

    public MarketingController(MarketingService marketingService, CompanyManagerService companyManagerService, ContactPersonService contactPersonService, ClientService clientService, CompanyService companyService) {
        this.marketingService = marketingService;
        this.companyManagerService = companyManagerService;
        this.contactPersonService = contactPersonService;
        this.clientService = clientService;
        this.companyService = companyService;
    }

    /* ----- MANAGER ----- */

    // register first call for company manager
    @PostMapping("/first-call-manager/{id}")
    public String registerFirstCallManager(@PathVariable("id") Long id, Model model) {

        marketingService.registerFirstCallManager(id);

        PersonDTO personDTO = companyManagerService.findCompanyManagerById(id);
        CompanyDTO companyDTO = companyService.findByCompanyManagerId(id);
        model.addAttribute("personDTO", personDTO);
        return "redirect:/current-company-manager/" + companyDTO.getId();
    }

    // register send email for company manager
    @PostMapping("/send-email-manager/{id}")
    public String registerFirstEmailManager(@PathVariable("id") Long id, Model model) {

        marketingService.registerSendEmailManager(id);
        PersonDTO personDTO = companyManagerService.findCompanyManagerById(id);
        CompanyDTO companyDTO = companyService.findByCompanyManagerId(id);
        model.addAttribute("personDTO", personDTO);
        return "redirect:/current-company-manager/" + companyDTO.getId();
    }

    // register send letter for company manager
    @PostMapping("/send-letter-manager/{id}")
    public String registerSendLetterManager(@PathVariable("id") Long id, Model model) {

        marketingService.registerSendLetterManager(id);

        PersonDTO personDTO = companyManagerService.findCompanyManagerById(id);
        CompanyDTO companyDTO = companyService.findByCompanyManagerId(id);
        model.addAttribute("personDTO", personDTO);
        return "redirect:/current-company-manager/" + companyDTO.getId();
    }

    // register second call for manager
    @PostMapping("/second-call-manager/{id}")
    public String registerSecondCallManager(@PathVariable("id") Long id, Model model) {

        marketingService.registerSecondCallManager(id);

        PersonDTO personDTO = companyManagerService.findCompanyManagerById(id);
        CompanyDTO companyDTO = companyService.findByCompanyManagerId(id);
        model.addAttribute("personDTO", personDTO);
        return "redirect:/current-company-manager/" + companyDTO.getId();
    }

    // register presence for manager
    @PostMapping("/presence-manager/{id}")
    public String registerPresenceManager(@PathVariable("id") Long id, Model model) {

        marketingService.registerPresenceManager(id);

        PersonDTO personDTO = companyManagerService.findCompanyManagerById(id);
        CompanyDTO companyDTO = companyService.findByCompanyManagerId(id);
        model.addAttribute("personDTO", personDTO);
        return "redirect:/current-company-manager/" + companyDTO.getId();
    }

    /* ----- CONTACT PERSON ----- */

    // register first call for contact person
    @PostMapping("/first-call-contact-person/{id}")
    public String registerFirstCallContactPerson(@PathVariable("id") Long id, Model model) {

        marketingService.registerFirstCallContactPerson(id);

        PersonDTO personDTO = companyManagerService.findCompanyManagerById(id);
        CompanyDTO companyDTO = companyService.findByContactPersonId(id);
        model.addAttribute("personDTO", personDTO);
        return "redirect:/current-contact-persons/" + companyDTO.getId();
    }

    // register send email for contact person
    @PostMapping("/send-email-contact-person/{id}")
    public String registerSendEmailContactPerson(@PathVariable("id") Long id, Model model) {
        marketingService.registerSendEmailContactPerson(id);

        PersonDTO personDTO = companyManagerService.findCompanyManagerById(id);
        CompanyDTO companyDTO = companyService.findByContactPersonId(id);
        model.addAttribute("personDTO", personDTO);
        return "redirect:/current-contact-persons/" + companyDTO.getId();
    }

    // register send letter for contact person
    @PostMapping("/send-letter-contact-person/{id}")
    public String registerSendLetterContactPerson(@PathVariable("id") Long id, Model model) {
        marketingService.registerSendLetterContactPerson(id);

        PersonDTO personDTO = companyManagerService.findCompanyManagerById(id);
        CompanyDTO companyDTO = companyService.findByContactPersonId(id);
        model.addAttribute("personDTO", personDTO);
        return "redirect:/current-contact-persons/" + companyDTO.getId();
    }

    // register second call for contact person
    @PostMapping("/second-call-contact-person/{id}")
    public String registerSecondCallContactPerson(@PathVariable("id") Long id, Model model) {
        marketingService.registerSecondCallContactPerson(id);

        PersonDTO personDTO = companyManagerService.findCompanyManagerById(id);
        CompanyDTO companyDTO = companyService.findByContactPersonId(id);
        model.addAttribute("personDTO", personDTO);
        return "redirect:/current-contact-persons/" + companyDTO.getId();
    }

    // register presence for contact presence
    @PostMapping("/presence-contact-person/{id}")
    public String registerPresenceContactPerson(@PathVariable("id") Long id, Model model) {
        marketingService.registerPresenceContactPerson(id);

        PersonDTO personDTO = companyManagerService.findCompanyManagerById(id);
        CompanyDTO companyDTO = companyService.findByContactPersonId(id);
        model.addAttribute("personDTO", personDTO);
        return "redirect:/current-contact-persons/" + companyDTO.getId();
    }

    /* ----- CLIENT ----- */

    // register first email for client
    @GetMapping("/first-email-client/{id}")
    public String registerFirstEmailClient(@PathVariable("id") Long id, Model model) {

        marketingService.registerFirstEmailClient(id);
        return "redirect:/marketing/clients";
    }

    // register first call for client
    @GetMapping("/first-call-client/{id}")
    public String registerFirstCallClient(@PathVariable("id") Long id, Model model) {
        marketingService.registerFirstCallClient(id);
        return "redirect:/marketing/clients";
    }

    // register second email for client
    @GetMapping("/second-email-client/{id}")
    public String registerSecondEmailClient(@PathVariable("id") Long id, Model model) {
        marketingService.registerSecondEmailClient(id);
        return "redirect:/marketing/clients";
    }

    // register second call for client
    @GetMapping("/second-call-client/{id}")
    public String registerSecondCallClient(@PathVariable("id") Long id, Model model) {
        marketingService.registerSecondCallClient(id);
        return "redirect:/marketing/clients";
    }

    //view all clients
    @GetMapping("/marketing/clients")
    public String getAllClients(Model model) {
        List<ClientDTO> clientDTOS = clientService.getAllClients();

        model.addAttribute("allClients", clientDTOS);
        model.addAttribute("allSourceType", SourceType.values());

        return "clients";
    }
}
