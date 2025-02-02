package casaart.emails_clients_db.repository;

import casaart.emails_clients_db.model.entity.CompanyManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyManagerRepository extends JpaRepository<CompanyManager, Long> {
   Optional<CompanyManager> findByFirstNameAndLastName(String firsName, String lastName);
   @Query("SELECT p FROM CompanyManager p WHERE p.email = :email OR p.phoneNumber = :phone")
   CompanyManager findByEmailOrPhoneNumber(@Param("email") String email, @Param("phone") String phone);
}
