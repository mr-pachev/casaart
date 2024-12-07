package casaart.emails_clients_db.repository;

import casaart.emails_clients_db.model.entity.Client;
import casaart.emails_clients_db.model.enums.SourceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByEmail(String email);
    Client findById(long id);
    List<Client> findAllBySourceType(SourceType sourceType);
    List<Client> findAllByOrderByCreatDateDesc();
    List<Client> findAllByOrderByModifyDateDesc();
    List<Client> findAllByOrderByUserUsernameAsc();
}
