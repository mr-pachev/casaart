package casaart.emails_clients_db.model.entity;

import casaart.emails_clients_db.model.enums.SourceType;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "clients")
public class Client extends BaseEntity{
    @Column(name = "first_name")
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private SourceType sourceType;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    private String modifyFrom;

    @Column(name = "first_call")
    private LocalDate firstCall;

    @Column(name = "first_email")
    private LocalDate firstEmail;

    @Column(name = "second_call")
    private LocalDate secondCall;

    @Column(name = "second_email")
    private LocalDate secondEmail;

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

    public SourceType getSourceType() {
        return sourceType;
    }

    public void setSourceType(SourceType sourceType) {
        this.sourceType = sourceType;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getModifyFrom() {
        return modifyFrom;
    }

    public void setModifyFrom(String modifyFrom) {
        this.modifyFrom = modifyFrom;
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
}
