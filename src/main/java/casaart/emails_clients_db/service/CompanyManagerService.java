package casaart.emails_clients_db.service;

import casaart.emails_clients_db.model.dto.PersonDTO;

public interface CompanyManagerService {
    // check is exist company manager
    boolean isExistCompanyManager(PersonDTO personDTO);
}
