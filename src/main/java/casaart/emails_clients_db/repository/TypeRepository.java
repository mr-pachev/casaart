package casaart.emails_clients_db.repository;

import casaart.emails_clients_db.model.entity.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TypeRepository extends JpaRepository<Type, Long> {
    Optional<Type> findByName(String name);
    Optional<Type> findByCode(String code);
}
