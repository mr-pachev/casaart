package casaart.emails_clients_db.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserDTO {
    private long id;
    @NotBlank
    @Size(min = 3, max = 15)
    private String username;
    @NotBlank
    private String role;
    @NotBlank
    @Size(min = 2, max = 15)
    private String firstName;
    @Size(min = 2, max = 15)
    private String middleName;
    @Size(min = 2, max = 15)
    @NotBlank
    private String lastName;
    @NotBlank
    @Size(min = 3, max = 15)
    private String located;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLocated() {
        return located;
    }

    public void setLocated(String located) {
        this.located = located;
    }
}
