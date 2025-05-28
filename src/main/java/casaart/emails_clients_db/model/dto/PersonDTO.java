package casaart.emails_clients_db.model.dto;

import jakarta.persistence.Column;
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
    private String phoneNumber;

    private String company;

    private LocalDate firstCall;

    private LocalDate sendEmail;

    private LocalDate sendLetter;

    private LocalDate secondCall;

    private LocalDate presence;

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

    public LocalDate getFirstCall() {
        return firstCall;
    }

    public void setFirstCall(LocalDate firstCall) {
        this.firstCall = firstCall;
    }

    public LocalDate getSendEmail() {
        return sendEmail;
    }

    public void setSendEmail(LocalDate sendEmail) {
        this.sendEmail = sendEmail;
    }

    public LocalDate getSendLetter() {
        return sendLetter;
    }

    public void setSendLetter(LocalDate sendLetter) {
        this.sendLetter = sendLetter;
    }

    public LocalDate getSecondCall() {
        return secondCall;
    }

    public void setSecondCall(LocalDate secondCall) {
        this.secondCall = secondCall;
    }

    public LocalDate getPresence() {
        return presence;
    }

    public void setPresence(LocalDate presence) {
        this.presence = presence;
    }
}
