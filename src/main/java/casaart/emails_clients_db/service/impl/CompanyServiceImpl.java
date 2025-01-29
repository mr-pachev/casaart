package casaart.emails_clients_db.service.impl;

import casaart.emails_clients_db.model.dto.AddCompanyDTO;
import casaart.emails_clients_db.model.dto.CompanyDTO;
import casaart.emails_clients_db.model.dto.PersonDTO;
import casaart.emails_clients_db.model.entity.Company;
import casaart.emails_clients_db.model.entity.Person;
import casaart.emails_clients_db.model.enums.IndustryType;
import casaart.emails_clients_db.repository.CompanyRepository;
import casaart.emails_clients_db.repository.PersonRepository;
import casaart.emails_clients_db.service.CompanyService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CompanyServiceImpl implements CompanyService {
    private final CompanyRepository companyRepository;
    private final PersonRepository personRepository;
    private final ModelMapper mapper;

    public CompanyServiceImpl(CompanyRepository companyRepository, PersonRepository personRepository, ModelMapper mapper) {
        this.companyRepository = companyRepository;
        this.personRepository = personRepository;
        this.mapper = mapper;
    }


    // get all companies
    @Override
    public List<CompanyDTO> getAllCompanies() {
        List<Company> allCompanies = companyRepository.findAllByOrderByCreatedAtDesc();
        List<CompanyDTO> companyDTOS = new ArrayList<>();

        for (Company company : allCompanies) {
            CompanyDTO companyDTO = mapCompanyToCompanyDTO(company);
            companyDTOS.add(companyDTO);
        }

        return companyDTOS;
    }

    // checking if company exists
    @Override
    public boolean isExistCompany(String name) {
        log("Checking if company exists with name: " + name);
        boolean exists;
        try {
            exists = companyRepository.findByName(name).isPresent();
        } catch (Exception e) {
            logError("Error while checking company existence: " + e.getMessage());
            throw e;
        }
        log("Company existence check completed: " + exists);
        return exists;
    }

    // find company by id
    @Override
    public CompanyDTO findCompanyById(long id) {
        Company company = companyRepository.findById(id).get();

        return mapCompanyToCompanyDTO(company);
    }

    // add company
    @Override
    public long addCompany(AddCompanyDTO addCompanyDTO) {

        Company company = mapper.map(addCompanyDTO, Company.class);
        List<IndustryType> industryTypes = new ArrayList<>();

        for (String industry : addCompanyDTO.getIndustries()) {
            industryTypes.add(IndustryType.fromCyrillicName(industry));
        }

        company.setIndustryTypes(industryTypes);
        companyRepository.save(company);
        return company.getId();
    }

    // add company manager
    @Override
    public void addCompanyManger(PersonDTO personDTO, long id) {
        Company company = companyRepository.findById(id).get();

        // Мапване на PersonDTO към Person
        Person person = mapper.map(personDTO, Person.class);
        person.setCompany(company);

        // Запазване на Person в базата данни
        Person savedPerson = personRepository.save(person);
        if (savedPerson == null || savedPerson.getId() == null) {
            throw new RuntimeException("Failed to save Person entity");
        }

        // Задаване на мениджъра на компанията и запазване на промените
        company.setCompanyManager(savedPerson);
        companyRepository.save(company);
    }

    // delete company by id
    @Override
    @Transactional
    public void removeCompany(long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Company not found with id: " + id));

        // Ръчно премахване на свързани контактни лица
        if (company.getContactPersons() != null && !company.getContactPersons().isEmpty()) {
            for (Person person : company.getContactPersons()) {
                person.setCompany(null); // Изчистване на връзката към компанията
                personRepository.save(person); // Синхронизиране на промяната
                personRepository.delete(person); // Изтриване на обекта Person
            }
        }

        // Ръчно изтриване на мениджъра на компанията
        if (company.getCompanyManager() != null) {
            Person manager = company.getCompanyManager();
            company.setCompanyManager(null); // Изчистване на връзката
            companyRepository.save(company); // Синхронизиране на промяната
            personRepository.delete(manager); // Изтриване на мениджъра
        }

        // Накрая изтриване на самата компания
        companyRepository.delete(company);
    }

    // mapCompanyToCompanyDTO
    CompanyDTO mapCompanyToCompanyDTO(Company company) {
        CompanyDTO companyDTO = new CompanyDTO();

        companyDTO.setId(company.getId());
        companyDTO.setName(company.getName());
        companyDTO.setAddress(company.getAddress());
        companyDTO.setPhoneNumber(company.getPhoneNumber());
        companyDTO.setEmail(company.getEmail());
        companyDTO.setLocationType(company.getLocationType().name());

//        List<PersonDTO> personDTOS = new ArrayList<>();
//        companyDTO.setContactPerson(personDTOS);

        if(company.getCompanyManager() != null){
            PersonDTO companyManager = mapper.map(company.getCompanyManager(), PersonDTO.class);
            companyDTO.setCompanyManager(companyManager);
        }

        List<String> industryList = new ArrayList<>();
        for (IndustryType industry : company.getIndustryTypes()) {
            industryList.add(industry.getDisplayName());
        }
        companyDTO.setIndustries(industryList);

        return companyDTO;
    }

    // Helper method for logging
    private void log(String message) {
        System.out.println("[INFO] " + message);
    }

    // Helper method for error logging
    private void logError(String message) {
        System.err.println("[ERROR] " + message);
    }

    // Helper method for validation
    private void validateAddCompany(AddCompanyDTO addCompanyDTO) {
        if (addCompanyDTO.getName() == null || addCompanyDTO.getName().isEmpty()) {
            throw new IllegalArgumentException("Company name cannot be null or empty");
        }
    }
}
