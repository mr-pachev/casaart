package casaart.emails_clients_db.repository;

import casaart.emails_clients_db.model.entity.Client;
import casaart.emails_clients_db.model.enums.LoyaltyLevel;
import casaart.emails_clients_db.model.enums.SourceType;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByEmail(String email);
    Optional<Client> findByFirstNameAndLastNameAndEmail(String firstName, String lastName, String email);
    Client findById(long id);
    List<Client> findAllByOrderByIdDesc();
    List<Client> findAllByOrderByFirstNameAscMiddleNameAscLastNameAsc();
    List<Client> findAllByOrderByFirstEmailDesc();
    List<Client> findAllByOrderByFirstCallDesc();
    List<Client> findAllByOrderBySecondEmailDesc();
    List<Client> findAllByOrderBySecondCallDesc();
    List<Client> findAllByFirstName(String firstName);
    List<Client> findAllByFirstNameAndLastName(String firstName, String lastName);
    List<Client> findAllBySourceType(SourceType sourceType);
    List<Client> findAllByLoyaltyLevel(LoyaltyLevel loyaltyLevel);

    // Изтрива всички дубликати, оставяйки само един запис за всеки email
    @Modifying
    @Transactional
    @Query(value = "DELETE c1 FROM clients c1 JOIN clients c2 ON c1.email = c2.email AND c1.id > c2.id", nativeQuery = true)
    void removeDuplicateClientsEmail();

}

