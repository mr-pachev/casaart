package casaart.emails_clients_db.model.dto;

import jakarta.validation.constraints.*;

public class AddClientDTO {
    @NotBlank
    @Size(min = 2, max = 15)
    private String firstName;
    @Size(max = 15)
    private String middleName;
    @NotBlank
    @Size(min = 2, max = 15)
    private String lastName;
    @Size(max = 15)
    private String companyName;
    @NotBlank@Email
    private String email;
    @NotBlank
    @Size(min = 9, max = 9)
    private String phoneNumber;
    @NotBlank
    private String sourceType;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }
}
