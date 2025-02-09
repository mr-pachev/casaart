package casaart.emails_clients_db.service.impl;

import casaart.emails_clients_db.model.dto.PersonDTO;
import casaart.emails_clients_db.model.entity.Company;
import casaart.emails_clients_db.model.entity.ContactPerson;
import casaart.emails_clients_db.repository.CompanyRepository;
import casaart.emails_clients_db.repository.ContactPersonRepository;
import casaart.emails_clients_db.service.ContactPersonService;
import org.springframework.stereotype.Service;

@Service
public class ContactPersonServiceImpl implements ContactPersonService {
    private final ContactPersonRepository contactPersonRepository;
    private final CompanyRepository companyRepository;

    public ContactPersonServiceImpl(ContactPersonRepository contactPersonRepository, CompanyRepository companyRepository) {
        this.contactPersonRepository = contactPersonRepository;
        this.companyRepository = companyRepository;
    }

    // check is exist contact person
    @Override
    public boolean isExistContactPerson(PersonDTO personDTO) {
        return contactPersonRepository.findByFirstNameAndLastNameAndPhoneNumber(personDTO.getFirstName(),
                personDTO.getLastName(),
                personDTO.getPhoneNumber()).isPresent();
    }

    // add contact person
    @Override
    public void addContactPerson(PersonDTO personDTO, long companyId) {
        Company company = companyRepository.findById(companyId).get();

        ContactPerson contactPerson = personDTOMapToContactPerson(personDTO);
        contactPerson.setCompany(company);

        contactPersonRepository.save(contactPerson);
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
