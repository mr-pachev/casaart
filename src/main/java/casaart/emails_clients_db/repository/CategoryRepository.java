package casaart.emails_clients_db.repository;

import casaart.emails_clients_db.model.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
