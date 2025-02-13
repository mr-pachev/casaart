package casaart.emails_clients_db.repository;

import casaart.emails_clients_db.model.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    Optional<Company> findByName(String name);

    Optional<Company> findByCompanyManagerId(long id);
    List<Company> findAllByOrderByCreatedAtDesc();
}
