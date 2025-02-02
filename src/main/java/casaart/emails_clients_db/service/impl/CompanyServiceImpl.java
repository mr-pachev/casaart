package casaart.emails_clients_db.service.impl;

import casaart.emails_clients_db.model.dto.AddCompanyDTO;
import casaart.emails_clients_db.model.dto.CompanyDTO;
import casaart.emails_clients_db.model.dto.PersonDTO;
import casaart.emails_clients_db.model.entity.Company;
import casaart.emails_clients_db.model.entity.CompanyManager;
import casaart.emails_clients_db.model.entity.ContactPerson;
import casaart.emails_clients_db.model.enums.IndustryType;
import casaart.emails_clients_db.repository.CompanyRepository;
import casaart.emails_clients_db.repository.CompanyManagerRepository;
import casaart.emails_clients_db.repository.ContactPersonRepository;
import casaart.emails_clients_db.service.CompanyService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

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
            industryTypes.add(IndustryType.fromCyrillicName(industry));
        }

        company.setIndustryTypes(industryTypes);
        companyRepository.save(company);
        return company.getId();
    }

    // add company manager
    @Override
    public void addCompanyManager(PersonDTO personDTO, long companyId) {
        Company company = companyRepository.findById(companyId).get();

        CompanyManager manager = mapper.map(personDTO, CompanyManager.class);
        manager.setCompany(company);

        boolean isManagerInContactList = company.getContactPersons().stream()
                .anyMatch(person -> person.getFullName().equals(personDTO.getFullName()));

        if (isManagerInContactList) {
            company.getContactPersons().removeIf(person -> person.getFullName().equals(personDTO.getFullName()));
        }

        CompanyManager savedManager = companyManagerRepository.save(manager);

        company.setCompanyManager(savedManager);
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

    // delete company by id
//    @Transactional
    @Override
    public void removeCompany(long id) {
        Company company = companyRepository.findById(id).get();

        // Ръчно премахване на свързани контактни лица
        if (company.getContactPersons() != null && !company.getContactPersons().isEmpty()) {
//            for (CompanyManager companyManager : company.getContactPersons()) {
//                companyManager.setCompany(null); // Изчистване на връзката към компанията
//                companyManagerRepository.save(companyManager); // Синхронизиране на промяната
//                companyManagerRepository.delete(companyManager); // Изтриване на обекта Person
//            }
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

    //PersonDTO map to ContactPerson
    ContactPerson personDTOMapToContactPerson(PersonDTO personDTO){
        ContactPerson contactPerson = new ContactPerson();

        contactPerson.setFirstName(personDTO.getFirstName());
        contactPerson.setLastName(personDTO.getLastName());
        contactPerson.setEmail(personDTO.getEmail());
        contactPerson.setPhoneNumber(personDTO.getPhoneNumber());

        // Уверяваме се, че ID-то е null, за да се създаде нов запис
        contactPerson.setId(null);

        return contactPerson;
    }
}
