package casaart.emails_clients_db.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class PersonDTO {
    long id;

    @NotBlank
    @Size(min = 2, max = 30)
    private String firstName;

    @Size(max = 30)
    private String middleName;

    @NotBlank
    @Size(min = 2, max = 30)
    private String lastName;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(max = 10)
    private String phoneNumber;

    private String company;

    private LocalDate firstEmail;

    private LocalDate firstCall;

    private LocalDate secondEmail;

    private LocalDate secondCall;

    public String getFullName() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getFirstName()).append(" ");

        if (this.getMiddleName() != null && !this.getMiddleName().isEmpty()) {
            sb.append(this.getMiddleName()).append(" ");
        }

        sb.append(this.getLastName());
        return sb.toString();
    }

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

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public LocalDate getFirstEmail() {
        return firstEmail;
    }

    public void setFirstEmail(LocalDate firstEmail) {
        this.firstEmail = firstEmail;
    }

    public LocalDate getFirstCall() {
        return firstCall;
    }

    public void setFirstCall(LocalDate firstCall) {
        this.firstCall = firstCall;
    }

    public LocalDate getSecondEmail() {
        return secondEmail;
    }

    public void setSecondEmail(LocalDate secondEmail) {
        this.secondEmail = secondEmail;
    }

    public LocalDate getSecondCall() {
        return secondCall;
    }

    public void setSecondCall(LocalDate secondCall) {
        this.secondCall = secondCall;
    }
}
