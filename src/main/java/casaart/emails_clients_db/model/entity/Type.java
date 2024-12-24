package casaart.emails_clients_db.model.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "types")
public class Type extends BaseEntity {
    @Column
    private String name;
    @Column
    private String code; // EC, WC, HL

    @ManyToOne
    private Category category;

    @OneToMany(mappedBy = "type")
    List<Product> products;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
