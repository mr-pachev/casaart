package casaart.emails_clients_db.repository;

import casaart.emails_clients_db.model.entity.Company;
import casaart.emails_clients_db.model.enums.CompanyType;
import casaart.emails_clients_db.model.enums.IndustryType;
import casaart.emails_clients_db.model.enums.LocationType;
import casaart.emails_clients_db.model.enums.UnitType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    Optional<Company> findByName(String name);
    Optional<Company> findByCompanyManagerId(long id);

    @Query("SELECT c FROM Company c WHERE c.companyType = 'ПАРТНЬОР' ORDER BY c.id DESC")
    List<Company> findAllPartnersOrderedByIdDesc();

    @Query("SELECT c FROM Company c WHERE c.companyType = 'ДОСТАВЧИК' ORDER BY c.id DESC")
    List<Company> findAllSuppliersOrderedByIdDesc();

    List<Company> findByUnitTypes(UnitType unitType);
    List<Company> findByUnitTypesAndIndustryTypes(UnitType unitType, IndustryType industryType);
    List<Company> findByLocationType(LocationType locationType);
    List<Company> findAllByOrderByNameAsc();
    List<Company> findByNameStartingWithIgnoreCase(String name);
    List<Company> findByEmailStartingWithIgnoreCase(String email);
    }
