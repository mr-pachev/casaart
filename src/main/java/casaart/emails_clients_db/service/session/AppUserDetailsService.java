package casaart.emails_clients_db.service.session;



import casaart.emails_clients_db.model.entity.Role;
import casaart.emails_clients_db.model.entity.User;
import casaart.emails_clients_db.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class AppUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public AppUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return userRepository
                .findByUsername(username)
                .map(this::mapToUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " not found"));
    }

    private UserDetails mapToUserDetails(User user) {
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(mapToGrantedAuthorities(user))
                .build();
    }

    private List<GrantedAuthority> mapToGrantedAuthorities(User user) {
        Role role = user.getRole();

        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role.getRoleName().name());

        return Collections.singletonList(authority);
    }
}
