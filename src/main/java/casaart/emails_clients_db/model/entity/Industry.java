package casaart.emails_clients_db.model.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "industries")
public class Industry extends BaseEntity{

    @Column
    private String name;

    @ManyToMany(mappedBy = "industries")
    private List<Company>  companies;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Company> getCompanies() {
        return companies;
    }

    public void setCompanies(List<Company> companies) {
        this.companies = companies;
    }
}
