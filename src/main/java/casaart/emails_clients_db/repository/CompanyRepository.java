package casaart.emails_clients_db.repository;

import casaart.emails_clients_db.model.entity.Company;
import casaart.emails_clients_db.model.enums.IndustryType;
import casaart.emails_clients_db.model.enums.LocationType;
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
    List<Company> findAllByOrderByIdDesc();
    List<Company> findByIndustryTypes(IndustryType industryType);
    List<Company> findByLocationType(LocationType locationType);
    List<Company> findAllByOrderByNameAsc();
}
