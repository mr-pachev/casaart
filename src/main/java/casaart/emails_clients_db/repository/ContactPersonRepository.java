package casaart.emails_clients_db.repository;

import casaart.emails_clients_db.model.entity.CompanyManager;
import casaart.emails_clients_db.model.entity.ContactPerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactPersonRepository extends JpaRepository<ContactPerson, Long> {
    @Query("SELECT p FROM ContactPerson p WHERE p.email = :email OR p.phoneNumber = :phone")
    ContactPerson findByEmailOrPhoneNumber(@Param("email") String email, @Param("phone") String phone);
}
