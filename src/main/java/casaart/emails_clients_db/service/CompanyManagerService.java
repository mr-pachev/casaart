package casaart.emails_clients_db.service;

import casaart.emails_clients_db.model.dto.PersonDTO;
import casaart.emails_clients_db.model.entity.CompanyManager;

import java.util.List;

public interface CompanyManagerService {
    // get all company managers
    List<PersonDTO> allCompanyManagers();

    // sort company managers
    List<PersonDTO> sortedCompanyManagersByType(String type);

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

    // delete company manager
    void removeCompanyManager(long id);
}
