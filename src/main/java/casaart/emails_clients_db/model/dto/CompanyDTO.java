package casaart.emails_clients_db.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

public class CompanyDTO {
    long id;

    @NotBlank
    @Size(min = 2, max = 50)
    private String name;

    @NotBlank
    @Size(min = 5)
    private String address;

    @NotBlank
    private String phoneNumber;

    @Email
    private String email;

    @NotBlank
    private String locationType;

    private List<PersonDTO> contactPerson = new ArrayList<>();

    @NotBlank
    @Size(min = 5)
    private PersonDTO companyManager;

    private List<String> industries;

    private String firstCall;

    private String sentEmail;

    private String letterSent;

    private String secondCall;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLocationType() {
        return locationType;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    public List<PersonDTO> getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(List<PersonDTO> contactPerson) {
        this.contactPerson = contactPerson;
    }

    public PersonDTO getCompanyManager() {
        return companyManager;
    }

    public void setCompanyManager(PersonDTO companyManager) {
        this.companyManager = companyManager;
    }

    public List<String> getIndustries() {
        return industries;
    }

    public void setIndustries(List<String> industries) {
        this.industries = industries;
    }

    public String getFirstCall() {
        return firstCall;
    }

    public void setFirstCall(String firstCall) {
        this.firstCall = firstCall;
    }

    public String getSentEmail() {
        return sentEmail;
    }

    public void setSentEmail(String sentEmail) {
        this.sentEmail = sentEmail;
    }

    public String getLetterSent() {
        return letterSent;
    }

    public void setLetterSent(String letterSent) {
        this.letterSent = letterSent;
    }

    public String getSecondCall() {
        return secondCall;
    }

    public void setSecondCall(String secondCall) {
        this.secondCall = secondCall;
    }
}
