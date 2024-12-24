package casaart.emails_clients_db.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class AddProductDTO {
    @NotBlank
    @Size(min = 2, max = 30)
    private String name;
    @NotNull
    private Double providerPrice;
    @NotNull
    private Double clientPrice;
    @NotBlank
    private String imagePath;
    @NotBlank
    @Size(min = 2, max = 10)
    private String productCode;
    @NotBlank
    @Size(min = 2, max = 10)
    private String providerProductCode;
    @NotBlank
    private String provider;
    private String dimensions;
    @NotBlank
    private String category;
    @NotBlank
    private String type;

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

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getDimensions() {
        return dimensions;
    }

    public void setDimensions(String dimensions) {
        this.dimensions = dimensions;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
