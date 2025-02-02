package casaart.emails_clients_db.service;

import casaart.emails_clients_db.model.dto.PersonDTO;

public interface ContactPersonService {

    // check is exist contact person
    boolean isExistContactPerson(PersonDTO personDTO);
}
