package casaart.emails_clients_db.service;

import casaart.emails_clients_db.model.dto.AddCompanyDTO;
import casaart.emails_clients_db.model.dto.CompanyDTO;
import casaart.emails_clients_db.model.dto.PersonDTO;

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

    //add company manager
    void addCompanyManager(PersonDTO personDTO, long id);

    //add contact person
    void addContactPerson(PersonDTO personDTO, long id);

    //edit company
    void editCompany(CompanyDTO companyDTO);

    //delete company by id
    void removeCompany(long id);
}
