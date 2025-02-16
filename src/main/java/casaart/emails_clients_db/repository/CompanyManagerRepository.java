package casaart.emails_clients_db.repository;

import casaart.emails_clients_db.model.entity.CompanyManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyManagerRepository extends JpaRepository<CompanyManager, Long> {
   Optional<CompanyManager> findByFirstNameAndLastNameAndPhoneNumber(String firstName,
                                                                     String lastName,
                                                                     String phoneNumber);

   CompanyManager findById(long id);
   CompanyManager findByCompanyId(long id);
   List<CompanyManager> findAllByOrderByIdDesc();
   List<CompanyManager> findAllByOrderByFirstNameAscMiddleNameAscLastNameAsc();
   List<CompanyManager> findAllByOrderByFirstEmailDesc();
   List<CompanyManager> findAllByOrderByFirstCallDesc();
   List<CompanyManager> findAllByOrderBySecondEmailDesc();
   List<CompanyManager> findAllByOrderBySecondCallDesc();
   List<CompanyManager> findAllByOrderByFirstName();
   List<CompanyManager> findAllByOrderByFirstNameAscLastNameAsc();
}
