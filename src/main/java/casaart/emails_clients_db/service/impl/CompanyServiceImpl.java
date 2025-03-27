package casaart.emails_clients_db.service.impl;

import casaart.emails_clients_db.model.dto.AddCompanyDTO;
import casaart.emails_clients_db.model.dto.CompanyDTO;
import casaart.emails_clients_db.model.dto.PersonDTO;
import casaart.emails_clients_db.model.entity.Company;
import casaart.emails_clients_db.model.entity.CompanyManager;
import casaart.emails_clients_db.model.entity.ContactPerson;
import casaart.emails_clients_db.model.enums.*;
import casaart.emails_clients_db.repository.CompanyManagerRepository;
import casaart.emails_clients_db.repository.CompanyRepository;
import casaart.emails_clients_db.repository.ContactPersonRepository;
import casaart.emails_clients_db.service.CompanyService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.lang.model.type.UnionType;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompanyServiceImpl implements CompanyService {
    private final CompanyRepository companyRepository;
    private final CompanyManagerRepository companyManagerRepository;
    private final ContactPersonRepository contactPersonRepository;
    private final ModelMapper mapper;

    public CompanyServiceImpl(CompanyRepository companyRepository, CompanyManagerRepository companyManagerRepository, ContactPersonRepository contactPersonRepository, ModelMapper mapper) {
        this.companyRepository = companyRepository;
        this.companyManagerRepository = companyManagerRepository;
        this.contactPersonRepository = contactPersonRepository;
        this.mapper = mapper;
    }

    // get all partners
    @Override
    public List<CompanyDTO> getAllPartners() {
        List<Company> allCompanies = companyRepository.findAllPartnersOrderedByIdDesc();
        List<CompanyDTO> companyDTOS = new ArrayList<>();

        for (Company company : allCompanies) {
            CompanyDTO companyDTO = mapCompanyToCompanyDTO(company);
            companyDTOS.add(companyDTO);
        }

        return companyDTOS;
    }

    // get all suppliers
    @Override
    public List<CompanyDTO> getAllSuppliers() {
        List<Company> allCompanies = companyRepository.findAllSuppliersOrderedByIdDesc();
        List<CompanyDTO> companyDTOS = new ArrayList<>();

        for (Company company : allCompanies) {
            CompanyDTO companyDTO = mapCompanyToCompanyDTO(company);
            companyDTOS.add(companyDTO);
        }

        return companyDTOS;
    }

    // get sorted suppliers
    @Override
    public List<CompanyDTO> sortedSuppliers(String sortBy) {
        String input = sortBy.trim().replaceAll("\\s+", " "); // Премахване на излишни интервали

        List<Company> companyList = new ArrayList<>();

        if (input.equals("name")) {
            companyList = companyRepository.findAllSuppliersOrderedByNameAsc();

        } else if (input.equals("allSuppliers")) {
            companyList = companyRepository.findAllSuppliersOrderedByIdDesc();

        } else if (input.contains("@")) {
            companyList = companyRepository.findByEmailStartingWithIgnoreCase(input);

        } else {
            // Разделяне на входния низ на думи (разделители: интервал или тире)
            String[] words = input.split("[-\s]+");

            for (String word : words) {
                List<Company> matchedCompanies = companyRepository.findByNameStartingWithIgnoreCase(word);

                for (Company company : matchedCompanies) {
                    if (!companyList.contains(company)) {
                        companyList.add(company);
                    }
                }
            }

            if (companyList.isEmpty()) {
                companyList = companyRepository.findAllSuppliersOrderedByIdDesc();
            }

        }

        List<CompanyDTO> companyDTOS = companyListMapToCompanyDTOS(companyList);
        return companyDTOS;
    }

    // get sorted partners
    @Override
    public List<CompanyDTO> sortedPartners(String sortBy) {
        String input = sortBy.trim().replaceAll("\\s+", " "); // Премахване на излишни интервали

        List<Company> companyList = new ArrayList<>();

        if (input.equals("name")) {
            companyList = companyRepository.findAllPartnersOrderedByNameAsc();

        } else if (input.equals("allPartners")) {
            companyList = companyRepository.findAllPartnersOrderedByIdDesc();

        } else if (input.contains("@")) {
            companyList = companyRepository.findByEmailStartingWithIgnoreCase(input);

        } else {
            // Разделяне на входния низ на думи (разделители: интервал или тире)
            String[] words = input.split("[-\s]+");

            for (String word : words) {
                List<Company> matchedCompanies = companyRepository.findByNameStartingWithIgnoreCase(word);

                for (Company company : matchedCompanies) {
                    if (!companyList.contains(company)) {
                        companyList.add(company);
                    }
                }
            }

            if (companyList.isEmpty()) {
                companyList = companyRepository.findAllPartnersOrderedByIdDesc();
            }

        }

        List<CompanyDTO> companyDTOS = companyListMapToCompanyDTOS(companyList);
        return companyDTOS;
    }

    // get sorted suppliers by unitType
    @Override
    public List<CompanyDTO> sortedCompaniesByUnit(String unitType) {
        UnitType unit = UnitType.valueOf(unitType);

        return companyRepository.findByUnitTypes(unit)
                .stream()
                .map(this::mapCompanyToCompanyDTO) // Използване на референция към метод за по-четим код
                .collect(Collectors.toList());
    }

    // get sorted suppliers by industryType
    @Override
    public List<CompanyDTO> sortedCompaniesByUnitAndIndustry(String unitType, String industryType) {
        UnitType unit = UnitType.valueOf(unitType);
        IndustryType industry = IndustryType.valueOf(industryType);

        return companyRepository.findByUnitTypesAndIndustryTypes(unit, industry)
                .stream()
                .map(this::mapCompanyToCompanyDTO) // Използване на референция към метод за по-четим код
                .collect(Collectors.toList());
    }

    // get sorted suppliers by locationType
    @Override
    public List<CompanyDTO> sortedSuppliersByLocationType(String locationType) {
        LocationType supplierLocationType = LocationType.valueOf(locationType);

        return companyRepository.findSuppliersByLocationType(supplierLocationType)
                .stream()
                .map(this::mapCompanyToCompanyDTO) // Преобразуване в DTO директно в ламбда израза
                .collect(Collectors.toList());
    }

    // get sorted partners by locationType
    @Override
    public List<CompanyDTO> sortedPartnersByLocationType(String locationType) {
        LocationType partnerLocationType = LocationType.valueOf(locationType);

        return companyRepository.findPartnersByLocationType(partnerLocationType)
                .stream()
                .map(this::mapCompanyToCompanyDTO) // Преобразуване в DTO директно в ламбда израза
                .collect(Collectors.toList());
    }

    // get sorted partners by partnerType
    @Override
    public List<CompanyDTO> sortedPartnersByPartnerType(String partnerType) {
        PartnerType partner = PartnerType.valueOf(partnerType);

        return companyRepository.findByPartnerTypes(partner)
                .stream()
                .map(this::mapCompanyToCompanyDTO) // Преобразуване в DTO директно в ламбда израза
                .collect(Collectors.toList());
    }

    // checking if company exists
    @Override
    public boolean isExistCompany(String name) {
        return companyRepository.findByName(name).isPresent();
    }

    // find company by id
    @Override
    public CompanyDTO findCompanyById(long id) {
        Company company = companyRepository.findById(id).get();

        return mapCompanyToCompanyDTO(company);
    }

    // find company by company manager id
    @Override
    public CompanyDTO findByCompanyManagerId(long id) {
        Company company = companyRepository.findByCompanyManagerId(id).get();

        return mapCompanyToCompanyDTO(company);
    }

    // find company by name
    @Override
    public CompanyDTO findCompanyByName(String name) {
        name = name.trim().replaceAll(",", "");

        CompanyDTO companyDTO = mapCompanyToCompanyDTO(companyRepository.findByName(name).get());
        return companyDTO;
    }

    // add company
    @Override
    public void addCompany(AddCompanyDTO addCompanyDTO) {

        Company company = mapper.map(addCompanyDTO, Company.class);
        List<IndustryType> industryTypes = new ArrayList<>();

        for (String industry : addCompanyDTO.getIndustries()) {
            industryTypes.add(IndustryType.valueOf(industry));
        }

        List<UnitType> unitTypes = new ArrayList<>();

        for (String unit : addCompanyDTO.getUnits()) {
            unitTypes.add(UnitType.valueOf(unit));
        }

        company.setIndustryTypes(industryTypes);
        company.setUnitTypes(unitTypes);

        companyRepository.save(company);
    }

    // edit company
    @Override
    public void editCompany(CompanyDTO companyDTO) {
        Company company = companyRepository.findById(companyDTO.getId()).get();

        if(company.getCompanyType().equals(CompanyType.ДОСТАВЧИК) && companyDTO.getCompanyType().equals("ПАРТНЬОР")){
            company.setUnitTypes(null);
            company.setIndustryTypes(null);

            companyDTO.setUnits(null);
            companyDTO.setIndustries(null);

        }else if(company.getCompanyType().equals(CompanyType.ПАРТНЬОР) && companyDTO.getCompanyType().equals("ДОСТАВЧИК")){
            company.setPartnerTypes(null);

            companyDTO.setPartnerTypes(null);
        }

        company.setName(companyDTO.getName());
        company.setAddress(companyDTO.getAddress());
        company.setPhoneNumber(companyDTO.getPhoneNumber());
        company.setEmail(companyDTO.getEmail());
        company.setUrl(companyDTO.getUrl());
        company.setLocationType(LocationType.valueOf(companyDTO.getLocationType()));

        if (companyDTO.getCompanyType() != null) {
            company.setCompanyType(CompanyType.valueOf(companyDTO.getCompanyType()));
        }

        if (companyDTO.getIndustries() != null) {
            List<IndustryType> industryTypes = companyDTO.getIndustries().stream()
                    .map(IndustryType::valueOf)
                    .collect(Collectors.toList());

            company.setIndustryTypes(industryTypes);
        }

        if (companyDTO.getUnits() != null) {
            List<UnitType> unitTypes = new ArrayList<>();

            for (String unit : companyDTO.getUnits()) {
                unitTypes.add(UnitType.valueOf(unit));
            }

            company.setUnitTypes(unitTypes);
        }

        if (companyDTO.getPartnerTypes() != null) {
            List<PartnerType> partnerTypes = new ArrayList<>();

            for (String type : companyDTO.getPartnerTypes()) {
                partnerTypes.add(PartnerType.valueOf(type));
            }

            company.setPartnerTypes(partnerTypes);
        }

        companyRepository.save(company);
    }

    // delete company by id
    @Override
    public void removeCompany(long id) {
        Company company = companyRepository.findById(id).get();

        // Ръчно премахване на свързани контактни лица
        if (company.getContactPersons() != null && !company.getContactPersons().isEmpty()) {
            for (ContactPerson contactPerson : company.getContactPersons()) {
                contactPerson.setCompany(null); // Изчистване на връзката към компанията

                contactPersonRepository.deleteById(contactPerson.getId()); // Изтриване на обекта Person
            }
        }

        // Ръчно изтриване на мениджъра на компанията
        if (company.getCompanyManager() != null) {
            CompanyManager manager = company.getCompanyManager();
            company.setCompanyManager(null); // Изчистване на връзката
            companyRepository.save(company); // Синхронизиране на промяната
            companyManagerRepository.delete(manager); // Изтриване на мениджъра
        }

        companyRepository.delete(company);
    }

    // Company map to CompanyDTO
    CompanyDTO mapCompanyToCompanyDTO(Company company) {
        CompanyDTO companyDTO = new CompanyDTO();

        companyDTO.setId(company.getId());
        companyDTO.setName(company.getName());
        companyDTO.setAddress(company.getAddress());
        companyDTO.setPhoneNumber(company.getPhoneNumber());
        companyDTO.setEmail(company.getEmail());
        companyDTO.setUrl(company.getUrl());
        companyDTO.setLocationType(company.getLocationType().name());
        companyDTO.setCompanyType(company.getCompanyType().name());

        List<String> contactPersons = company.getContactPersons().stream()
                .map(ContactPerson::getFullName)
                .collect(Collectors.toList());

        companyDTO.setContactPerson(contactPersons);

        if (company.getCompanyManager() != null) {
            PersonDTO companyManager = mapper.map(company.getCompanyManager(), PersonDTO.class);
            companyDTO.setCompanyManager(companyManager.getFullName());
        }

        List<String> partnersTypes = company.getPartnerTypes().stream()
                .map(Enum::name)
                .collect(Collectors.toList());

        List<String> units = company.getUnitTypes().stream()
                .map(Enum::name)
                .collect(Collectors.toList());

        List<String> industries = company.getIndustryTypes().stream()
                .map(Enum::name)
                .collect(Collectors.toList());

        companyDTO.setPartnerTypes(partnersTypes);
        companyDTO.setUnits(units);
        companyDTO.setIndustries(industries);

        return companyDTO;
    }

    // List<Company> map to List<CompanyDTO>
    List<CompanyDTO> companyListMapToCompanyDTOS(List<Company> companyList) {
        List<CompanyDTO> allCompanyDTOS = new ArrayList<>();

        for (Company company : companyList) {
            CompanyDTO companyDTO = mapCompanyToCompanyDTO(company);

            allCompanyDTOS.add(companyDTO);
        }

        return allCompanyDTOS;
    }
}
