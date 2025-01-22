package casaart.emails_clients_db.repository;

import casaart.emails_clients_db.model.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {
}
