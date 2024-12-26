package casaart.emails_clients_db.repository;

import casaart.emails_clients_db.model.entity.SerialNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SerialNumberRepository extends JpaRepository<SerialNumber, Long> {
}
