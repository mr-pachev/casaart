package casaart.emails_clients_db.model.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "companies_managers")
public class CompanyManager extends BaseEntity{

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @OneToOne(mappedBy = "companyManager", cascade = CascadeType.ALL)
    private Company company;

    @Column(name = "first_call")
    private LocalDate firstCall;

    @Column(name = "first_email")
    private LocalDate firstEmail;

    @Column(name = "second_call")
    private LocalDate secondCall;

    @Column(name = "second_email")
    private LocalDate secondEmail;


    public String getFullName() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getFirstName()).append(" ");

        if (this.getMiddleName() != null && !this.getMiddleName().isEmpty()) {
            sb.append(this.getMiddleName()).append(" ");
        }

        sb.append(this.getLastName());
        return sb.toString();
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

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
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
