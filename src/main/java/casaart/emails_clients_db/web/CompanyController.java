package casaart.emails_clients_db.web;

import casaart.emails_clients_db.model.dto.AddCompanyDTO;
import casaart.emails_clients_db.model.dto.CompanyDTO;
import casaart.emails_clients_db.model.dto.ProviderDTO;
import casaart.emails_clients_db.model.entity.Industry;
import casaart.emails_clients_db.model.enums.LocationType;
import casaart.emails_clients_db.repository.IndustryRepository;
import casaart.emails_clients_db.service.CompanyService;
import casaart.emails_clients_db.service.IndustryService;
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
public class CompanyController {
    private final CompanyService companyService;
    private final IndustryService industryService;

    public CompanyController(CompanyService companyService, IndustryService industryService) {
        this.companyService = companyService;
        this.industryService = industryService;
    }

    @ModelAttribute("addCompanyDTO")
    public AddCompanyDTO addCompanyDTO() {
        return new AddCompanyDTO();
    }

    @ModelAttribute("companyDTO")
    public CompanyDTO companyDTO() {
        return new CompanyDTO();
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
        model.addAttribute("allIndustries", industryService.getAllIndustries());

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
}
