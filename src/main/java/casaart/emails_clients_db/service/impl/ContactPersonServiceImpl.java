package casaart.emails_clients_db.service.impl;

import casaart.emails_clients_db.model.dto.PersonDTO;
import casaart.emails_clients_db.model.entity.ContactPerson;
import casaart.emails_clients_db.repository.ContactPersonRepository;
import casaart.emails_clients_db.service.ContactPersonService;
import org.springframework.stereotype.Service;

@Service
public class ContactPersonServiceImpl implements ContactPersonService {
    private final ContactPersonRepository contactPersonRepository;

    public ContactPersonServiceImpl(ContactPersonRepository contactPersonRepository) {
        this.contactPersonRepository = contactPersonRepository;
    }

    // check is exist contact person
    @Override
    public boolean isExistContactPerson(PersonDTO personDTO) {
        return contactPersonRepository.findByFirstNameAndLastNameAndPhoneNumber(personDTO.getFirstName(),
                personDTO.getLastName(),
                personDTO.getPhoneNumber()).isPresent();
    }
}
