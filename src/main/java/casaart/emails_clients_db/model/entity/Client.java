package casaart.emails_clients_db.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "clients")
public class Client extends BaseEntity{
    private String firstName;
    private String middleName;
    private String lastName;
    private String companyName;
    private String email;
    private int phoneNumber;
}
