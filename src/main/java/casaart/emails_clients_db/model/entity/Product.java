package casaart.emails_clients_db.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.util.concurrent.atomic.AtomicLong;

@Entity
@Table(name = "products")
public class Product extends BaseEntity {
    @Column
    private String name;
    @Column(name = "providers_prices")
    private Double providerPrice;
    @Column(name = "clients_prices")
    private Double clientPrice;
    @Column
    private String imagePath;
    @Column(name = "products_codes")
    private String productCode;
    @Column(name = "providers_products_codes")
    private String providerProductCode;
    @ManyToOne
    Provider provider;
    @ManyToOne
    Dimensions dimensions;

    @ManyToOne
    private Category category;

    @ManyToOne
    private Type type;

    private static final AtomicLong counter = new AtomicLong(1); // Уникален брояч

    private void generateProductCode() {
        if (this.productCode == null) {
            this.productCode = generateCode();
        }
    }

    private String generateCode() {
        String categoryCode = category.getCode(); // CE, DC, TX
        String typeCode = type.getCode();         // EC, HL, BC
        String productNameCode = abbreviateProductName(this.name);
        Long uniqueNumber = counter.getAndIncrement();
        return String.format("%s-%s-%s-%04d", categoryCode, typeCode, productNameCode, uniqueNumber);
    }

    private String abbreviateProductName(String productName) {
        return productName.replaceAll("\\s+", "") // Премахва интервали
                .toUpperCase()         // Капитализира
                .substring(0, Math.min(6, productName.length())); // Ограничение до 6 символа
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getProviderPrice() {
        return providerPrice;
    }

    public void setProviderPrice(Double providerPrice) {
        this.providerPrice = providerPrice;
    }

    public Double getClientPrice() {
        return clientPrice;
    }

    public void setClientPrice(Double clientPrice) {
        this.clientPrice = clientPrice;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProviderProductCode() {
        return providerProductCode;
    }

    public void setProviderProductCode(String providerProductCode) {
        this.providerProductCode = providerProductCode;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public Dimensions getDimensions() {
        return dimensions;
    }

    public void setDimensions(Dimensions dimensions) {
        this.dimensions = dimensions;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
