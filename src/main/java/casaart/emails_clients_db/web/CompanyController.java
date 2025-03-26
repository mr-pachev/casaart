package casaart.emails_clients_db.web;

import casaart.emails_clients_db.model.dto.AddCompanyDTO;
import casaart.emails_clients_db.model.dto.CompanyDTO;
import casaart.emails_clients_db.model.dto.PersonDTO;
import casaart.emails_clients_db.model.enums.*;
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

    // view all partners
    @GetMapping("/partners")
    public String getAllCompanies(Model model) {
        List<CompanyDTO> companyDTOS = companyService.getAllPartners();

        model.addAttribute("allPartners", companyDTOS);
        model.addAttribute("allPartnerTypes", PartnerType.values());
        model.addAttribute("allLocations", LocationType.values());

        return "partners";
    }

    // view all suppliers
    @GetMapping("/suppliers")
    public String getAllSuppliers(Model model) {
        List<CompanyDTO> companyDTOS = companyService.getAllSuppliers();

        model.addAttribute("allSuppliers", companyDTOS);
        model.addAttribute("allUnits", UnitType.values());
        model.addAttribute("allIndustries", IndustryType.values());
        model.addAttribute("allCompanyTypes", CompanyType.values());
        model.addAttribute("allLocations", LocationType.values());

        return "suppliers";
    }

    // view all sorted suppliers
    @PostMapping("/sort-suppliers")
    public String sortSuppliers(@RequestParam("sortBy") String sortBy,
                                @RequestParam(value = "unitType", required = false) String unitType,
                                @RequestParam(value = "industryType", required = false) String industryType,
                                @RequestParam(value = "locationType", required = false) String locationType,
                                Model model) {
        List<CompanyDTO> sortedSuppliers;

        switch (sortBy) {
            case "unitType":
                if (unitType != null && !unitType.isEmpty()) {
                    if (industryType != null && !industryType.isEmpty()) {
                        sortedSuppliers = companyService.sortedCompaniesByUnitAndIndustry(unitType, industryType);
                    } else {
                        sortedSuppliers = companyService.sortedCompaniesByUnit(unitType);
                    }

                } else {
                    sortedSuppliers = companyService.getAllSuppliers();
                }
                break;

            case "locationType":
                if (locationType != null && !locationType.isEmpty()) {
                    sortedSuppliers = companyService.sortedSuppliersByLocationType(locationType);
                } else {
                    sortedSuppliers = companyService.getAllSuppliers();
                }
                break;

            default:
                sortedSuppliers = companyService.sortedSuppliers(sortBy);
                break;
        }

        model.addAttribute("allSuppliers", sortedSuppliers);
        model.addAttribute("allUnits", UnitType.values());
        model.addAttribute("allIndustries", IndustryType.values());
        model.addAttribute("allCompanyTypes", CompanyType.values());
        model.addAttribute("allLocations", LocationType.values());

        return "suppliers";
    }

    // view all sorted partners
    @PostMapping("/sort-partners")
    public String sortPartners(@RequestParam(value = "sortBy", required = false) String sortBy,
                               @RequestParam(value = "partnerTypes", required = false) String partnerType,
                               @RequestParam(value = "locationType", required = false) String locationType,
                               Model model) {
        List<CompanyDTO> sortedPartners;

        switch (sortBy) {
            case "partnerTypes":
                if (partnerType != null && !partnerType.isEmpty()) {
                    sortedPartners = companyService.sortedPartnersByPartnerType(partnerType);
                } else {
                    sortedPartners = companyService.getAllPartners();
                }
                break;

            case "locationType":
                if (locationType != null && !locationType.isEmpty()) {
                    sortedPartners = companyService.sortedPartnersByLocationType(locationType);
                } else {
                    sortedPartners = companyService.getAllPartners();
                }
                break;

            default:
                sortedPartners = companyService.sortedPartners(sortBy);
                break;
        }

        model.addAttribute("allPartners", sortedPartners);
        model.addAttribute("allPartnerTypes", PartnerType.values());
        model.addAttribute("allLocations", LocationType.values());

        return "partners";
    }

    // add new company
    @GetMapping("/add-company")
    public String viewAddCompanyForm(@RequestParam("companyType") String companyType, Model model) {

        model.addAttribute("allUnits", UnitType.values());
        model.addAttribute("allLocations", LocationType.values());
        model.addAttribute("allIndustries", IndustryType.values());
        model.addAttribute("allCompanyTypes", CompanyType.values());
        model.addAttribute("allPartnerTypes", PartnerType.values());

        if (!model.containsAttribute("isExistCompany")) {
            model.addAttribute("isExistCompany", false);
        }

        if ("ПАРТНЬОР".equals(companyType)) {
            return "add-partner";
        }

        return "add-company";
    }

    @PostMapping("/add-company")
    public String addCompany(
            @RequestParam("companyType") String companyType,
            @Valid @ModelAttribute("addCompanyDTO") AddCompanyDTO addCompanyDTO,
            BindingResult bindingResult,
            RedirectAttributes rAtt) {

        if (bindingResult.hasErrors()) {
            rAtt.addFlashAttribute("allUnits", UnitType.values());
            rAtt.addFlashAttribute("allLocations", LocationType.values());
            rAtt.addFlashAttribute("allIndustries", IndustryType.values());
            rAtt.addFlashAttribute("allCompanyTypes", CompanyType.values());
            rAtt.addFlashAttribute("allPartnerTypes", PartnerType.values());
            rAtt.addFlashAttribute("addCompanyDTO", addCompanyDTO);
            rAtt.addFlashAttribute("org.springframework.validation.BindingResult.addCompanyDTO", bindingResult);

            rAtt.addAttribute("companyType", companyType);

            return "redirect:/add-company";
        }

        if (companyService.isExistCompany(addCompanyDTO.getName())) {
            rAtt.addFlashAttribute("isExistCompany", true);
            rAtt.addFlashAttribute("allUnits", UnitType.values());
            rAtt.addFlashAttribute("allLocations", LocationType.values());
            rAtt.addFlashAttribute("allIndustries", IndustryType.values());
            rAtt.addFlashAttribute("allCompanyTypes", CompanyType.values());
            rAtt.addFlashAttribute("allPartnerTypes", PartnerType.values());
            rAtt.addFlashAttribute("addCompanyDTO", addCompanyDTO);

            rAtt.addAttribute("companyType", companyType);

            return "redirect:/add-company";
        }

        companyService.addCompany(addCompanyDTO);

        if ("ПАРТНЬОР".equals(addCompanyDTO.getCompanyType())) {
            return "redirect:/partners";
        }

        return "redirect:/suppliers";
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
        model.addAttribute("allUnits", UnitType.values());
        model.addAttribute("allLocations", LocationType.values());
        model.addAttribute("allIndustries", IndustryType.values());
        model.addAttribute("allPartnerTypes", PartnerType.values());
        model.addAttribute("allCompanyTypes", CompanyType.values());
        model.addAttribute("contactsPersons", companyDTO.getContactPerson());

        // Логика за обработка на партньор или доставчик
        if (companyDTO.getCompanyType().equals("ПАРТНЬОР")) {
            return "partner-details";
        }

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
            model.addAttribute("allUnits", UnitType.values());
            model.addAttribute("allLocations", LocationType.values());
            model.addAttribute("allIndustries", IndustryType.values());
            model.addAttribute("allPartnerTypes", PartnerType.values());
            model.addAttribute("allCompanyTypes", CompanyType.values());
            model.addAttribute("contactsPersons", companyDTO.getContactPerson());
            rAtt.addFlashAttribute("org.springframework.validation.BindingResult.providerDTO", bindingResult);

            return "company-details";
        }

        boolean isChangedCompanyName = !companyDTO.getName().equals(companyService.findCompanyById(id).getName());

        if (isChangedCompanyName && companyService.isExistCompany(companyDTO.getName())) {
            model.addAttribute("companyDTO", companyDTO);
            model.addAttribute("allUnits", UnitType.values());
            model.addAttribute("allLocations", LocationType.values());
            model.addAttribute("allIndustries", IndustryType.values());
            model.addAttribute("allPartnerTypes", PartnerType.values());
            model.addAttribute("allCompanyTypes", CompanyType.values());
            model.addAttribute("contactsPersons", companyDTO.getContactPerson());
            model.addAttribute("isExistCompany", true);
            rAtt.addFlashAttribute("org.springframework.validation.BindingResult.companyDTO", bindingResult);

            return "company-details";
        }

        companyService.editCompany(companyDTO);

        if (companyService.findCompanyById(id).getCompanyType().equals("ПАРТНЬОР")){
            return "redirect:/partners";
        }

        return "redirect:/suppliers";
    }


    // delete company by id
    @PostMapping("/delete-company/{id}")
    public String removeCompany(@PathVariable("id") Long id) {
        CompanyDTO companyDTO = companyService.findCompanyById(id);

        companyService.removeCompany(id);

        if (companyDTO.getCompanyType().equals("ПАРТНЬОР")){
            return "redirect:/partners";
        }
        return "redirect:/suppliers";
    }
}
