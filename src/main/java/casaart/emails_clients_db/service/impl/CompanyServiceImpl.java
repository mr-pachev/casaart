package casaart.emails_clients_db.service.impl;

import casaart.emails_clients_db.model.dto.AddCompanyDTO;
import casaart.emails_clients_db.model.dto.CompanyDTO;
import casaart.emails_clients_db.model.dto.PersonDTO;
import casaart.emails_clients_db.model.entity.Company;
import casaart.emails_clients_db.model.entity.CompanyManager;
import casaart.emails_clients_db.model.entity.ContactPerson;
import casaart.emails_clients_db.model.enums.IndustryType;
import casaart.emails_clients_db.model.enums.LocationType;
import casaart.emails_clients_db.repository.CompanyManagerRepository;
import casaart.emails_clients_db.repository.CompanyRepository;
import casaart.emails_clients_db.repository.ContactPersonRepository;
import casaart.emails_clients_db.service.CompanyService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    // get all companies
    @Override
    public List<CompanyDTO> getAllCompanies() {
        List<Company> allCompanies = companyRepository.findAllByOrderByIdDesc();
        List<CompanyDTO> companyDTOS = new ArrayList<>();

        for (Company company : allCompanies) {
            CompanyDTO companyDTO = mapCompanyToCompanyDTO(company);
            companyDTOS.add(companyDTO);
        }

        return companyDTOS;
    }

    // get sorted companies
    @Override
    public List<CompanyDTO> sortedCompanies(String companyType) {
        String input = companyType.trim();

        List<Company> companyList = new ArrayList<>();

        if (input.equals("name")) {
            companyList = companyRepository.findAllByOrderByNameAsc();

        } else if (input.equals("allCompanies")) {
            companyList = companyRepository.findAllByOrderByIdDesc();

        } else {
           if (companyRepository.findByName(input).isEmpty()){
               companyList = companyRepository.findAllByOrderByIdDesc();
            }else {
                companyList.add(companyRepository.findByName(input).get());
           }

        }

        List<CompanyDTO> companyDTOS = companyListMapToCompanyDTOS(companyList);
        return companyDTOS;
    }

    // get sorted companies by industryType
    @Override
    public List<CompanyDTO> sortedCompaniesByIndustryType(String industry) {
        IndustryType industryType = IndustryType.valueOf(industry);

        return companyRepository.findByIndustryTypes(industryType)
                .stream()
                .map(company -> mapCompanyToCompanyDTO(company)) // Преобразуване в DTO директно в ламбда израза
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

    // find company by name
    @Override
    public CompanyDTO findCompanyByName(String name) {
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

        company.setIndustryTypes(industryTypes);
        companyRepository.save(company);
    }

    // edit company
    @Override
    public void editCompany(CompanyDTO companyDTO) {
        Company company = companyRepository.findById(companyDTO.getId()).get();

        company.setName(companyDTO.getName());
        company.setAddress(companyDTO.getAddress());
        company.setPhoneNumber(companyDTO.getPhoneNumber());
        company.setEmail(companyDTO.getEmail());
        company.setLocationType(LocationType.valueOf(companyDTO.getLocationType()));

        List<IndustryType> industries = companyDTO.getIndustries().stream()
                .map(IndustryType::valueOf)
                .collect(Collectors.toList());

        company.setIndustryTypes(industries);
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
        companyDTO.setLocationType(company.getLocationType().name());

        List<String> contactPersons = company.getContactPersons().stream()
                .map(ContactPerson::getFullName)
                .collect(Collectors.toList());

        companyDTO.setContactPerson(contactPersons);

        if (company.getCompanyManager() != null) {
            PersonDTO companyManager = mapper.map(company.getCompanyManager(), PersonDTO.class);
            companyDTO.setCompanyManager(companyManager.getFullName());
        }

        List<String> industries = company.getIndustryTypes().stream()
                .map(Enum::name)
                .collect(Collectors.toList());

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
