package casaart.emails_clients_db.repository;

import casaart.emails_clients_db.model.entity.Company;
import casaart.emails_clients_db.model.enums.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query("SELECT c FROM Company c WHERE c.companyType = 'ДОСТАВЧИК' AND c.locationType = :locationType ORDER BY c.name ASC")
    List<Company> findSuppliersByLocationType(@Param("locationType") LocationType locationType);

    @Query("SELECT c FROM Company c WHERE c.companyType = 'ПАРТНЬОР' AND c.locationType = :locationType ORDER BY c.name ASC")
    List<Company> findPartnersByLocationType(@Param("locationType") LocationType locationType);

    List<Company> findByPartnerTypes(PartnerType partnerType);

    @Query("SELECT c FROM Company c WHERE c.companyType = 'ПАРТНЬОР' ORDER BY c.name ASC")
    List<Company> findAllPartnersOrderedByNameAsc();

    @Query("SELECT c FROM Company c WHERE c.companyType = 'ДОСТАВЧИК' ORDER BY c.name ASC")
    List<Company> findAllSuppliersOrderedByNameAsc();

    List<Company> findByNameStartingWithIgnoreCase(String name);
    List<Company> findByEmailStartingWithIgnoreCase(String email);
    }
