package casaart.emails_clients_db.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.List;

@Entity
@Table(name = "industries")
public class Industry extends BaseEntity{
    private String name;

    private List<Company> companies;
}
