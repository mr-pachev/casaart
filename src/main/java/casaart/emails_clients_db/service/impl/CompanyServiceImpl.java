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
import casaart.emails_clients_db.service.PersonService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompanyServiceImpl implements CompanyService {
    private final CompanyRepository companyRepository;
    private final PersonRepository personRepository;
    private final PersonService personService;
    private final ModelMapper mapper;

    public CompanyServiceImpl(CompanyRepository companyRepository, PersonRepository personRepository, PersonService personService, ModelMapper mapper) {
        this.companyRepository = companyRepository;
        this.personRepository = personRepository;
        this.personService = personService;
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
        return companyRepository.findByName(name).isPresent();
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

    //add contact person
    @Override
    public void addContactPerson(PersonDTO personDTO, long companyId) {
        // Намиране на компанията по ID с обработка на грешки
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found with id: " + companyId));

        // Мапване на PersonDTO към Person
        Person person = mapper.map(personDTO, Person.class);
        person.setCompany(company);

        // Запазване на Person в базата данни с обработка на грешки
        Person savedPerson = personRepository.save(person);
        if (savedPerson.getId() == null) {
            throw new RuntimeException("Failed to save Person entity");
        }

        // Добавяне на контактното лице към компанията, ако вече не съществува
        List<Person> contactPersons = company.getContactPersons();

        boolean isPersonAlreadyContact = contactPersons.stream()
                .anyMatch(contactPerson -> contactPerson.getFullName().equals(personDTO.getFullName()));

        if (!isPersonAlreadyContact) {
            contactPersons.add(savedPerson);
            company.setContactPersons(contactPersons);
            companyRepository.save(company);
        }
    }

    // delete company by id
    @Override
//    @Transactional
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

        companyRepository.delete(company);
    }

    // mapCompanyToCompanyDTO
    CompanyDTO mapCompanyToCompanyDTO(Company company) {
        CompanyDTO companyDTO = new CompanyDTO();

        // Мапване на основните полета
        companyDTO.setId(company.getId());
        companyDTO.setName(company.getName());
        companyDTO.setAddress(company.getAddress());
        companyDTO.setPhoneNumber(company.getPhoneNumber());
        companyDTO.setEmail(company.getEmail());
        companyDTO.setLocationType(company.getLocationType().name());

        // Мапване на контактните лица
        List<PersonDTO> contactPersons = company.getContactPersons().stream()
                .map(contactPerson -> mapper.map(contactPerson, PersonDTO.class))
                .collect(Collectors.toList());
        companyDTO.setContactPerson(contactPersons);

        // Мапване на мениджъра на компанията
        if (company.getCompanyManager() != null) {
            PersonDTO companyManager = mapper.map(company.getCompanyManager(), PersonDTO.class);
            companyDTO.setCompanyManager(companyManager);
        }

        // Мапване на индустриите
        List<String> industries = company.getIndustryTypes().stream()
                .map(IndustryType::getDisplayName)
                .collect(Collectors.toList());
        companyDTO.setIndustries(industries);

        return companyDTO;
    }
}
