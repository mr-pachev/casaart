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
    public void addCompanyManager(PersonDTO personDTO, long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found with id: " + companyId));

        // Проверка дали компанията вече има управител
        if (company.getCompanyManager() != null) {
            throw new RuntimeException("Company already has a manager.");
        }

        // Мапване на PersonDTO към Person
        Person manager = mapper.map(personDTO, Person.class);
        manager.setCompany(company); // Задаване на компанията за управителя

        // Проверка дали управителят вече съществува в списъка с контактни лица
        boolean isManagerInContactList = company.getContactPersons().stream()
                .anyMatch(person -> person.getEmail().equals(personDTO.getEmail()) ||
                        person.getPhoneNumber().equals(personDTO.getPhoneNumber()));

        if (isManagerInContactList) {
            // Премахване на управителя от списъка с контактни лица
            company.getContactPersons().removeIf(person -> person.getEmail().equals(personDTO.getEmail()) ||
                    person.getPhoneNumber().equals(personDTO.getPhoneNumber()));
        }

        // Запазване на управителя в базата данни
        Person savedManager = personRepository.save(manager);
        if (savedManager == null || savedManager.getId() == null) {
            throw new RuntimeException("Failed to save manager entity");
        }

        // Задаване на управителя на компанията и запазване на промените
        company.setCompanyManager(savedManager);
        companyRepository.save(company);
    }

    //add contact person
    @Override
    public void addContactPerson(PersonDTO personDTO, long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found with id: " + companyId));

        // Мапване на PersonDTO към Person
        Person contactPerson = mapper.map(personDTO, Person.class);
        contactPerson.setCompany(company); // Задаване на компанията за контактното лице

        // Проверка дали лицето е управител на компанията
        if (company.getCompanyManager() != null &&
                (company.getCompanyManager().getEmail().equals(personDTO.getEmail()) ||
                        company.getCompanyManager().getPhoneNumber().equals(personDTO.getPhoneNumber()))) {
            throw new RuntimeException("The manager cannot be added as a contact person.");
        }

        // Проверка дали лицето вече съществува в списъка с контактни лица
        boolean isPersonAlreadyContact = company.getContactPersons().stream()
                .anyMatch(person -> person.getEmail().equals(personDTO.getEmail()) ||
                        person.getPhoneNumber().equals(personDTO.getPhoneNumber()));

        if (isPersonAlreadyContact) {
            throw new RuntimeException("Person already exists in the contact list.");
        }

        // Добавяне на лицето към списъка с контактни лица на компанията
        company.getContactPersons().add(contactPerson);

        // Запазване на компанията (което ще запази и лицето, благодарение на cascade)
        companyRepository.save(company);
    }

    // delete company by id
//    @Transactional
    @Override
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
