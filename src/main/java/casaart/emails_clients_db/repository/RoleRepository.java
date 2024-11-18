package casaart.emails_clients_db.repository;

import casaart.emails_clients_db.model.entity.Role;
import casaart.emails_clients_db.model.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRoleName(RoleName roleName);
}
