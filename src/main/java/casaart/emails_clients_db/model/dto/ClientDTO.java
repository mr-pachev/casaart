package casaart.emails_clients_db.model.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public class ClientDTO {
    long id;
    @NotBlank
    @Size(min = 2, max = 30)
    private String firstName;
    @Size(max = 30)
    private String middleName;
    @NotBlank
    @Size(min = 2, max = 30)
    private String lastName;
    @Size(max = 15)
    private String companyName;
    @NotBlank@Email
    private String email;
    @Size(max = 10)
    private String phoneNumber;
    @NotBlank
    private String sourceType;

    @NotBlank
    private String loyaltyLevel;

    private LocalDate firstCall;

    private LocalDate firstEmail;

    private LocalDate secondCall;

    private LocalDate secondEmail;

    private String addedFrom;

    private String modifyFrom;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public String getLoyaltyLevel() {
        return loyaltyLevel;
    }

    public void setLoyaltyLevel(String loyaltyLevel) {
        this.loyaltyLevel = loyaltyLevel;
    }

    public LocalDate getFirstCall() {
        return firstCall;
    }

    public void setFirstCall(LocalDate firstCall) {
        this.firstCall = firstCall;
    }

    public LocalDate getFirstEmail() {
        return firstEmail;
    }

    public void setFirstEmail(LocalDate firstEmail) {
        this.firstEmail = firstEmail;
    }

    public LocalDate getSecondCall() {
        return secondCall;
    }

    public void setSecondCall(LocalDate secondCall) {
        this.secondCall = secondCall;
    }

    public LocalDate getSecondEmail() {
        return secondEmail;
    }

    public void setSecondEmail(LocalDate secondEmail) {
        this.secondEmail = secondEmail;
    }

    public String getAddedFrom() {
        return addedFrom;
    }

    public void setAddedFrom(String addedFrom) {
        this.addedFrom = addedFrom;
    }

    public String getModifyFrom() {
        return modifyFrom;
    }

    public void setModifyFrom(String modifyFrom) {
        this.modifyFrom = modifyFrom;
    }
}
