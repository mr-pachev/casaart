package casaart.emails_clients_db.service;

import casaart.emails_clients_db.model.dto.AddCompanyDTO;
import casaart.emails_clients_db.model.dto.AddProviderDTO;

public interface CompanyService {
    //checking is exist company
    boolean isExistCompany(String name);

    //add company
    void addCompany(AddCompanyDTO addCompanyDTO);
}
