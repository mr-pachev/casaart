package casaart.emails_clients_db.repository;

import casaart.emails_clients_db.model.entity.Industry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IndustryRepository extends JpaRepository<Industry, Long> {
}
