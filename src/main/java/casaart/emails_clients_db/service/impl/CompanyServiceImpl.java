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
            industryTypes.add(IndustryType.valueOf(industry));
        }

        company.setIndustryTypes(industryTypes);
        companyRepository.save(company);
        return company.getId();
    }

    // add company manager
    @Override
//    @Transactional
    public void addCompanyManager(PersonDTO personDTO, long companyId) {
        Company company = companyRepository.findById(companyId).get();

        boolean isManagerInContactList = company.getContactPersons().stream()
                .anyMatch(person -> person.getFullName().equals(personDTO.getFullName()));

        if (isManagerInContactList) {
            company.getContactPersons().removeIf(person -> person.getFullName().equals(personDTO.getFullName()));
        }

        companyRepository.save(company);

        if (isManagerInContactList) {
            ContactPerson contactPerson = contactPersonRepository.findByFirstNameAndLastNameAndPhoneNumber(personDTO.getFirstName(),
                    personDTO.getLastName(),
                    personDTO.getPhoneNumber()).get();
            contactPerson.setCompany(null);
            contactPersonRepository.save(contactPerson);

            contactPersonRepository.deleteById(contactPerson.getId());
        }

        CompanyManager manager = mapper.map(personDTO, CompanyManager.class);

        manager.setId(null);
        manager.setCompany(company);
        companyManagerRepository.save(manager);

        company.setCompanyManager(manager);
        companyRepository.save(company);
    }

    //add contact person
    @Override
    public void addContactPerson(PersonDTO personDTO, long companyId) {
        Company company = companyRepository.findById(companyId).get();

        ContactPerson contactPerson = personDTOMapToContactPerson(personDTO);
        contactPerson.setCompany(company);

        contactPersonRepository.save(contactPerson);
    }

    //edit company
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

        List<PersonDTO> contactPersons = company.getContactPersons().stream()
                .map(contactPerson -> mapper.map(contactPerson, PersonDTO.class))
                .collect(Collectors.toList());

        companyDTO.setContactPerson(contactPersons);

        if (company.getCompanyManager() != null) {
            PersonDTO companyManager = mapper.map(company.getCompanyManager(), PersonDTO.class);
            companyDTO.setCompanyManager(companyManager);
        }

        List<String> industries = company.getIndustryTypes().stream()
                .map(Enum::name)
                .collect(Collectors.toList());

        companyDTO.setIndustries(industries);

        return companyDTO;
    }

    // PersonDTO map to CompanyManager
    CompanyManager personDTOMapToCompanyManager(PersonDTO personDTO) {
        CompanyManager companyManager = new CompanyManager();

        companyManager.setId(null);
        companyManager.setFirstName(personDTO.getFirstName());
        if (personDTO.getMiddleName() != null) {
            companyManager.setMiddleName(personDTO.getMiddleName());
        }
        companyManager.setLastName(personDTO.getLastName());
        companyManager.setEmail(personDTO.getEmail());
        companyManager.setPhoneNumber(personDTO.getPhoneNumber());


        return companyManager;
    }

    // PersonDTO map to ContactPerson
    ContactPerson personDTOMapToContactPerson(PersonDTO personDTO) {
        ContactPerson contactPerson = new ContactPerson();

        contactPerson.setId(null);
        contactPerson.setFirstName(personDTO.getFirstName());
        if (personDTO.getMiddleName() != null) {
            contactPerson.setMiddleName(personDTO.getMiddleName());
        }
        contactPerson.setLastName(personDTO.getLastName());
        contactPerson.setEmail(personDTO.getEmail());
        contactPerson.setPhoneNumber(personDTO.getPhoneNumber());


        return contactPerson;
    }
}
