package casaart.emails_clients_db.init;

import casaart.emails_clients_db.model.entity.Role;
import casaart.emails_clients_db.model.enums.RoleName;
import casaart.emails_clients_db.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class InitService implements CommandLineRunner {
    private final RoleRepository roleRepository;

    public InitService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        long countRole = this.roleRepository.count();

        if (countRole > 0) {
            return;
        }

        List<Role> toInsertRole = Arrays.stream(RoleName.values())
                .map(Role::new)
                .toList();

        this.roleRepository.saveAll(toInsertRole);
    }
}
