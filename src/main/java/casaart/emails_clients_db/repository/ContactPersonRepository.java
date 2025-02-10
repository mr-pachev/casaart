package casaart.emails_clients_db.repository;

import casaart.emails_clients_db.model.entity.Company;
import casaart.emails_clients_db.model.entity.CompanyManager;
import casaart.emails_clients_db.model.entity.ContactPerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContactPersonRepository extends JpaRepository<ContactPerson, Long> {
    Optional<ContactPerson> findByFirstNameAndLastNameAndPhoneNumber(String firstName, String lastName,String phoneNumber);
    Optional<ContactPerson> findById(long id);
    List<ContactPerson> findAllByCompanyId(long id);
}
