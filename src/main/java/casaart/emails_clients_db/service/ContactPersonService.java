package casaart.emails_clients_db.service;

import casaart.emails_clients_db.model.dto.CompanyDTO;
import casaart.emails_clients_db.model.dto.PersonDTO;

import java.util.List;

public interface ContactPersonService {

    // all contact persons
    List<PersonDTO> allContactPersons();

    // all contact persons by company id
    List<PersonDTO> currentContactPersons(long id);

    // find contact person by id
    PersonDTO getContactPersonById(long id);

    // check is exist contact person
    boolean isExistContactPerson(PersonDTO personDTO);

    // add contact person
    void addContactPerson(PersonDTO personDTO, long id);

    // edit contact person
    void editContactPerson(PersonDTO personDTO);

    // delete contact person by id
    void removeContactPerson(long id);
}
