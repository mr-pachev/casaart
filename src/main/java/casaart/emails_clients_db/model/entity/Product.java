package casaart.emails_clients_db.model.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
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
    @JoinColumn(name = "provider_id", referencedColumnName = "id")
    Provider provider;
    private String dimensions;
    @ManyToOne
    private Category category;
    @ManyToOne
    private Type type;

    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER)
    private List<SerialNumber> serialNumbers = new ArrayList<>();
    private void generateProductCode() {
        if (this.productCode == null) {
            this.productCode = generateCode();
        }
    }

    public String generateCode() {
        String categoryCode = category.getCode(); // CE, DC, TX
        String typeCode = type.getCode();        // EC, HL, BC
        String productNameCode = productCode;

        //Вземете следващото число въз основа на serialNumbers size + 1
        Long nextNumber;
        if (serialNumbers == null || serialNumbers.isEmpty()) {
            nextNumber = 1L;
        } else {
            //Намира най-големия сериен номер и го увеличава с 1
            nextNumber = serialNumbers.stream()
                    .map(sn -> {
                        String code = sn.getSerialNumber();
                        //Извлича цифровата част от края на серийния номер
                        if (code != null && code.matches(".*-\\d+$")) {
                            return Long.parseLong(code.substring(code.lastIndexOf("-") + 1));
                        }
                        return 0L;
                    })
                    .max(Long::compareTo)
                    .orElse(0L) + 1;
        }

        return String.format("%s-%s-%s-%04d", categoryCode, typeCode, productNameCode, nextNumber);
    }

//    private String abbreviateProductName(String productName) {
//        return productName.replaceAll("\\s+", "") // Премахва интервали
//                .toUpperCase()         // Капитализира
//                .substring(0, Math.min(6, productCode.length())); // Ограничение до 6 символа
//    }

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

    public String getDimensions() {
        return dimensions;
    }

    public void setDimensions(String dimensions) {
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

    public List<SerialNumber> getSerialNumbers() {
        return serialNumbers;
    }

    public void setSerialNumbers(List<SerialNumber> serialNumbers) {
        this.serialNumbers = serialNumbers;
    }
}
