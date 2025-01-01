package casaart.emails_clients_db.model.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "providers")
public class Provider extends BaseEntity{
    @Column
    String name;
    @Column
    String description;
    @Column
    String contacts;
    @OneToMany(mappedBy = "provider", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    List<Product> products;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
