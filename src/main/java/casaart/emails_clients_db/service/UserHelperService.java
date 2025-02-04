package casaart.emails_clients_db.service;


import casaart.emails_clients_db.model.entity.User;
import casaart.emails_clients_db.repository.UserRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class UserHelperService {
    private static final String ROLE_PREFIX = "ROLE_";
    private final UserRepository userRepository;

    public UserHelperService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public User getUser() {
        return userRepository.findByUsername(getUserDetails().getUsername())
                .orElse(null);
    }

    public boolean hasRole(String role) {
        return getAuthentication().getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(ROLE_PREFIX + role));
    }

    public UserDetails getUserDetails() {
        return (UserDetails) getAuthentication().getPrincipal();
    }

    public void updateCurrentUserUsername(String newUsername) {
        UserDetails userDetails = getUserDetails();

        if (newUsername == null || newUsername.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }

        //Finding current user from database
        User currentUser = userRepository.findByUsername(newUsername)
                .orElseThrow(() -> new RuntimeException("User not found"));

        //Creating new UserDetails with new username
        UserDetails updatedUserDetails = new org.springframework.security.core.userdetails.User(
                newUsername,
                currentUser.getPassword(),
                userDetails.getAuthorities()
        );

        //Creating new Authentication token
        Authentication newAuth = new UsernamePasswordAuthenticationToken(
                updatedUserDetails, null, updatedUserDetails.getAuthorities());

        //Set new Authentication in SecurityContext
        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }

    public boolean isAuthenticated() {
        //Spring security sets default user with Role ANONYMOUS when no user is authenticated.
        return !hasRole("ANONYMOUS");
    }

    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
