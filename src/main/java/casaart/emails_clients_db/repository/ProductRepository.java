package casaart.emails_clients_db.repository;

import casaart.emails_clients_db.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByName(String name);
    Optional<Product> findByTypeNameAndProductCode(String typeName, String productCode);
    Optional<Product> findByProviderProductCode(String providreProductCode);
    List<Product> findAllByCategoryName(String name);
    List<Product> findAllByTypeName(String name);

    List<Product> findAllByOrderByNameAsc();
    List<Product> findAllByOrderByTypeAsc();
    List<Product> findAllByOrderByCategoryAsc();

    @Query("SELECT p FROM Product p ORDER BY p.provider.name")
    List<Product> findAllOrderedByProvider();

    @Query("SELECT p FROM Product p LEFT JOIN p.serialNumbers s GROUP BY p ORDER BY COUNT(s) DESC")
    List<Product> findAllOrderedBySerialNumbersCount();

    @Query("SELECT p FROM Product p ORDER BY p.clientPrice DESC")
    List<Product> findAllOrderedByClientPriceDesc();

    List<Product> findAllByOrderByCreatedAtDesc();

    List<Product> findAllByOrderByUpdatedAtDesc();

    // Търсене по пълен imagePath (примерно: /uploads/abc.jpg)
    Optional<Product> findByImagePath(String imagePath);
}
