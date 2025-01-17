package casaart.emails_clients_db.model.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "companies")
public class Company extends BaseEntity{
    private String name;
    private String address;
    private String phoneNumber;
    private String email;
    private LocalDate firstCall;
    private LocalDate sentEmail;
    private LocalDate letterSent;
    private LocalDate secondCall;
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ContactPerson> contactPerson;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "companyManager_id", referencedColumnName = "id")
    private CompanyManager companyManager;
    @ManyToMany
    private List<Industry> industries;
}
