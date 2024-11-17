package casaart.emails_clients_db.model.entity;

import casaart.emails_clients_db.model.enums.RoleName;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "users")
public class User extends BaseEntity{
    @Column(name = "username", unique = true)
    private String username;
    private String password;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "middle_name")
    private String middleName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "located")
    private String located;
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
    @OneToMany(targetEntity = Client.class, mappedBy = "user")
    private List<Client> clients;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<Client> getClients() {
        return clients;
    }

    public void setClients(List<Client> clients) {
        this.clients = clients;
    }
}
