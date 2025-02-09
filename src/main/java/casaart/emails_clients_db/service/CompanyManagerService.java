package casaart.emails_clients_db.service;

import casaart.emails_clients_db.model.dto.CompanyDTO;
import casaart.emails_clients_db.model.dto.PersonDTO;

public interface CompanyManagerService {
    // check is exist company manager
    boolean isExistCompanyManager(PersonDTO personDTO);

    // find company manager by id
    PersonDTO findCompanyManagerById(long id);

    // find company manager by company id
    PersonDTO findCompanyManagerByCompany(long id);

    // add company manager
    void addCompanyManager(PersonDTO personDTO, long id);

    // edit company manager
    void editCompanyManager(PersonDTO personDTO);

    //delete company manager
    void removeCompanyManager(long id);
}
