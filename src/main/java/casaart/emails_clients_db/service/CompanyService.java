package casaart.emails_clients_db.service;

import casaart.emails_clients_db.model.dto.AddCompanyDTO;
import casaart.emails_clients_db.model.dto.ClientDTO;
import casaart.emails_clients_db.model.dto.CompanyDTO;
import casaart.emails_clients_db.model.dto.PersonDTO;

import java.util.List;

public interface CompanyService {

    // get all partners
    List<CompanyDTO> getAllPartners();

    // get all suppliers
    List<CompanyDTO> getAllSuppliers();

    // get sorted suppliers
    List<CompanyDTO> sortedSuppliers(String sortBy);

    // get sorted partners
    List<CompanyDTO> sortedPartners(String sortBy);

    // get sorted suppliers by unitType
    List<CompanyDTO> sortedCompaniesByUnit(String unitType);

    // get sorted suppliers by unitType and industryType
    List<CompanyDTO> sortedCompaniesByUnitAndIndustry(String unitType, String industryType);

    // get sorted suppliers by locationType
    List<CompanyDTO> sortedSuppliersByLocationType(String locationType);

    // get sorted partners by locationType
    List<CompanyDTO> sortedPartnersByLocationType(String locationType);

    // get sorted partners by partnerType
    List<CompanyDTO> sortedPartnersByPartnerType(String partnerType);

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
