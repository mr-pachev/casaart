package casaart.emails_clients_db.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "industries")
public class Industry extends BaseEntity{

    @Column
    private String name;

    @ManyToOne
    @JoinColumn(name = "company_id", referencedColumnName = "id")
    private Company company;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}
