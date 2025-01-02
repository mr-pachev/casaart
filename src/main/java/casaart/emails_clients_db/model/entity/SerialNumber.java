package casaart.emails_clients_db.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "serial_number")
public class SerialNumber extends BaseEntity{
    @Column(name = "serials_numbers")
    private String serialNumber;
    @ManyToOne
    private Product product;

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
