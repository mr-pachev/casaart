package casaart.emails_clients_db.service;

import casaart.emails_clients_db.model.dto.AddCompanyDTO;
import casaart.emails_clients_db.model.dto.AddProviderDTO;
import casaart.emails_clients_db.model.dto.CompanyDTO;

import java.util.List;

public interface CompanyService {

    //get all companies
    List<CompanyDTO> getAllCompanies();

    //checking is exist company
    boolean isExistCompany(String name);

    //find company by id
    CompanyDTO findCompanyById(long id);

    //add company
    long addCompany(AddCompanyDTO addCompanyDTO);

    //delete company by id
    void removeCompany(long id);
}
