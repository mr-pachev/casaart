package casaart.emails_clients_db.repository;

import casaart.emails_clients_db.model.entity.Product;
import casaart.emails_clients_db.model.entity.SerialNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SerialNumberRepository extends JpaRepository<SerialNumber, Long> {
    List<SerialNumber> findAllByProductName(String name);
    SerialNumber findById(long id);
    Optional<SerialNumber> findBySerialNumber(String serialNumber);
}
