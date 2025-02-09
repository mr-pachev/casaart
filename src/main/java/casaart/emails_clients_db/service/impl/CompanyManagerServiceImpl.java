package casaart.emails_clients_db.service.impl;

import casaart.emails_clients_db.model.dto.PersonDTO;
import casaart.emails_clients_db.model.entity.Company;
import casaart.emails_clients_db.model.entity.CompanyManager;
import casaart.emails_clients_db.model.entity.ContactPerson;
import casaart.emails_clients_db.repository.CompanyManagerRepository;
import casaart.emails_clients_db.repository.CompanyRepository;
import casaart.emails_clients_db.repository.ContactPersonRepository;
import casaart.emails_clients_db.service.CompanyManagerService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CompanyManagerServiceImpl implements CompanyManagerService {
    private final CompanyManagerRepository companyManagerRepository;
    private final CompanyRepository companyRepository;
    private final ContactPersonRepository contactPersonRepository;
    private final ModelMapper mapper;

    public CompanyManagerServiceImpl(CompanyManagerRepository companyManagerRepository, CompanyRepository companyRepository, ContactPersonRepository contactPersonRepository, ModelMapper mapper) {
        this.companyManagerRepository = companyManagerRepository;
        this.companyRepository = companyRepository;
        this.contactPersonRepository = contactPersonRepository;
        this.mapper = mapper;
    }

    // check is exist company manager
    @Override
    public boolean isExistCompanyManager(PersonDTO personDTO) {
        return companyManagerRepository.findByFirstNameAndLastNameAndPhoneNumber(personDTO.getFirstName(),
                personDTO.getLastName(),
                personDTO.getPhoneNumber()).isPresent();
    }

    // find company manager by id
    @Override
    public PersonDTO findCompanyManagerById(long id) {
        CompanyManager companyManager = companyManagerRepository.findById(id);

        PersonDTO personDTO = mapper.map(companyManager, PersonDTO.class);
        personDTO.setCompany(companyManager.getCompany().getName());

        return personDTO;
    }

    // find company manager by company id
    @Override
    public PersonDTO findCompanyManagerByCompany(long id) {
        CompanyManager companyManager = companyManagerRepository.findByCompanyId(id);

        PersonDTO personDTO = mapper.map(companyManager, PersonDTO.class);
        personDTO.setCompany(companyManager.getCompany().getName());

        return personDTO;
    }

    // add company manager
    @Override
    @Transactional
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

    // edit company manager
    @Override
    public void editCompanyManager(PersonDTO personDTO) {
        CompanyManager companyManager = personDTOMapToCompanyManager(personDTO);

        companyManagerRepository.save(companyManager);
    }

    // PersonDTO map to CompanyManager
    CompanyManager personDTOMapToCompanyManager(PersonDTO personDTO) {
        CompanyManager companyManager = new CompanyManager();

        companyManager.setId(personDTO.getId());
        companyManager.setFirstName(personDTO.getFirstName());
        if (personDTO.getMiddleName() != null) {
            companyManager.setMiddleName(personDTO.getMiddleName());
        }
        companyManager.setLastName(personDTO.getLastName());
        companyManager.setEmail(personDTO.getEmail());
        companyManager.setPhoneNumber(personDTO.getPhoneNumber());


        return companyManager;
    }

}
