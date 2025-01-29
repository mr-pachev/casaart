package casaart.emails_clients_db.repository;

import casaart.emails_clients_db.model.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
   Optional<Person> findByFirstNameAndLastName(String firsName, String lastName);
}
