package casaart.emails_clients_db.repository;

import casaart.emails_clients_db.model.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByFirstName(String firstName);
    Optional<Client> findByFirstNameAndLastName(String firstName, String lastName);
    Optional<Client> findByFirstNameAndMiddleNameAndLastName(String firstName, String middleName, String lastName);
}
