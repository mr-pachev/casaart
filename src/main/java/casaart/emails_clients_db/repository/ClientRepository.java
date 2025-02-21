package casaart.emails_clients_db.repository;

import casaart.emails_clients_db.model.entity.Client;
import casaart.emails_clients_db.model.enums.SourceType;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    // Премахва дублирането на клиенти
    @Transactional
    default void removeDuplicateClients() {
        List<Client> allClients = findAll();
        allClients.stream()
                .collect(Collectors.groupingBy(c -> c.getFirstName() + "|" + c.getLastName() + "|" + c.getEmail()))
                .values()
                .forEach(duplicates -> {
                    if (duplicates.size() > 1) {
                        duplicates.stream().skip(1).forEach(this::delete);
                    }
                });
    }
}
