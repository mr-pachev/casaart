package casaart.emails_clients_db.service;

import casaart.emails_clients_db.model.dto.AddCompanyDTO;
import casaart.emails_clients_db.model.dto.ClientDTO;
import casaart.emails_clients_db.model.dto.CompanyDTO;
import casaart.emails_clients_db.model.dto.PersonDTO;

import java.util.List;

public interface CompanyService {

    // get all companies
    List<CompanyDTO> getAllCompanies();

    // get sorted companies
    List<CompanyDTO> sortedCompanies(String companyType);

    // get sorted companies by unitType
    List<CompanyDTO> sortedCompaniesByUnit(String unitType);

    // get sorted companies by unitType and industryType
    List<CompanyDTO> sortedCompaniesByUnitAndIndustry(String unitType, String industryType);

    // get sorted companies by locationType
    List<CompanyDTO> sortedCompaniesByLocationType(String locationType);

    // checking is exist company
    boolean isExistCompany(String name);

    // find company by id
    CompanyDTO findCompanyById(long id);

    // find company by name
    CompanyDTO findCompanyByName(String name);

    // add company
    void addCompany(AddCompanyDTO addCompanyDTO);

    // edit company
    void editCompany(CompanyDTO companyDTO);

    // delete company by id
    void removeCompany(long id);
}
