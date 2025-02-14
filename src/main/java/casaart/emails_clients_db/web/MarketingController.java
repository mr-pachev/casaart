package casaart.emails_clients_db.web;

import casaart.emails_clients_db.model.dto.PersonDTO;
import casaart.emails_clients_db.service.CompanyManagerService;
import casaart.emails_clients_db.service.MarketingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class MarketingController {
    private final MarketingService marketingService;
    private final CompanyManagerService companyManagerService;

    public MarketingController(MarketingService marketingService, CompanyManagerService companyManagerService) {
        this.marketingService = marketingService;
        this.companyManagerService = companyManagerService;
    }

    //register first email
    @GetMapping("/first-email-manager/{id}")
    public String registerFirstEmailManager(@PathVariable("id") Long id, Model model) {

        marketingService.registerFirstEmailManager(id);
        PersonDTO personDTO = companyManagerService.findCompanyManagerById(id);
        model.addAttribute("personDTO", personDTO);
        return "current-company-manager";
    }

    //register first call
    @GetMapping("/first-call-manager/{id}")
    public String registerFirstCallManager(@PathVariable("id") Long id, Model model) {

        marketingService.registerFirstCallManager(id);
        PersonDTO personDTO = companyManagerService.findCompanyManagerById(id);
        model.addAttribute("personDTO", personDTO);
        return "current-company-manager";
    }

    //register second email
    @GetMapping("/second-email-manager/{id}")
    public String registerSecondEmailManager(@PathVariable("id") Long id, Model model) {

        marketingService.registerSecondEmailManager(id);
        PersonDTO personDTO = companyManagerService.findCompanyManagerById(id);
        model.addAttribute("personDTO", personDTO);
        return "current-company-manager";
    }

    //register second call
    @GetMapping("/second-call-manager/{id}")
    public String registerSecondCallManager(@PathVariable("id") Long id, Model model) {

        marketingService.registerSecondCallManager(id);
        PersonDTO personDTO = companyManagerService.findCompanyManagerById(id);
        model.addAttribute("personDTO", personDTO);
        return "current-company-manager";
    }
}
