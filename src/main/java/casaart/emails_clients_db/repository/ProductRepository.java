package casaart.emails_clients_db.repository;

import casaart.emails_clients_db.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByName(String name);
    Optional<Product> findByProductCode(String code);
    List<Product> findAllByCategoryName(String name);
}
