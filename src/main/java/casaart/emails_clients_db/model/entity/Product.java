package casaart.emails_clients_db.model.entity;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    private String dimensions;

    @ManyToOne
    private Category category;

    @ManyToOne
    private Type type;

    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
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

    public void updateSerialNumbersOnProductCodeChange() {
        if (serialNumbers == null || serialNumbers.isEmpty() || productCode == null || productCode.isEmpty()) {
            return; // Няма серийни номера или нов код на продукта за актуализиране
        }

        for (SerialNumber serialNumber : serialNumbers) {
            String oldSerialNumber = serialNumber.getSerialNumber();
            if (oldSerialNumber != null && oldSerialNumber.split("-").length >= 3) {
                // Изгражда новия сериен номер с новия код на продукта
                String[] parts = oldSerialNumber.split("-");
                parts[2] = productCode;
                String newSerialNumber = String.join("-", parts);
                serialNumber.setSerialNumber(newSerialNumber);
            }
        }
    }


    public void updateSerialNumbersOnCategoryChange() {
        if (serialNumbers == null || serialNumbers.isEmpty() || category == null) {
            return; // Няма серийни номера или категория за актуализиране
        }

        String newCategoryCode = category.getCode();
        if (newCategoryCode == null || newCategoryCode.isEmpty()) {
            return; // Новият код на категорията е невалиден
        }

        String currentCategoryCode = serialNumbers.get(0).getSerialNumber().split("-")[0]; // Предполага се, че всички серийни номера имат един и същ код на категорията
        if (currentCategoryCode.equals(newCategoryCode)) {
            return; // Няма промяна в кода на категорията
        }

        // Актуализира серийните номера
        for (SerialNumber serialNumber : serialNumbers) {
            String oldSerialNumber = serialNumber.getSerialNumber();
            if (oldSerialNumber != null && oldSerialNumber.startsWith(currentCategoryCode + "-")) {
                // Изгражда новия сериен номер с новия код на категорията
                String newSerialNumber = oldSerialNumber.replaceFirst(
                        "^" + currentCategoryCode + "-",
                        newCategoryCode + "-"
                );
                serialNumber.setSerialNumber(newSerialNumber);
            }
        }
    }

    public void updateSerialNumbersOnTypeChange() {
        if (serialNumbers == null || serialNumbers.isEmpty() || type == null) {
            return; // Няма серийни номера или тип за актуализиране
        }

        String newTypeCode = type.getCode();
        if (newTypeCode == null || newTypeCode.isEmpty()) {
            return; // Новият код на типа е невалиден
        }

        String currentTypeCode = serialNumbers.get(0).getSerialNumber().split("-")[1]; // Предполага се, че всички серийни номера имат един и същ код на типа
        if (currentTypeCode.equals(newTypeCode)) {
            return; // Няма промяна в кода на типа
        }

        // Актуализира серийните номера
        for (SerialNumber serialNumber : serialNumbers) {
            String oldSerialNumber = serialNumber.getSerialNumber();
            if (oldSerialNumber != null && oldSerialNumber.split("-")[1].equals(currentTypeCode)) {
                // Изгражда новия сериен номер с новия код на типа
                String[] parts = oldSerialNumber.split("-");
                parts[1] = newTypeCode;
                String newSerialNumber = String.join("-", parts);
                serialNumber.setSerialNumber(newSerialNumber);
            }
        }
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
