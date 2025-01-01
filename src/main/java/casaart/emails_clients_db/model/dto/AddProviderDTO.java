package casaart.emails_clients_db.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AddProviderDTO {
    @NotBlank
    @Size(min = 2, max = 30)
    private String name;
    @NotBlank
    @Size(min = 4, max = 255)
    private String description;
    @NotBlank
    private String contacts;

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
}
