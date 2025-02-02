package casaart.emails_clients_db.model.entity;

import casaart.emails_clients_db.model.enums.IndustryType;
import casaart.emails_clients_db.model.enums.LocationType;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "companies")
public class Company extends BaseEntity{

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "email")
    private String email;

    @Enumerated(EnumType.STRING)
    private LocationType locationType;

    @Column(name = "first_call")
    private LocalDate firstCall;

    @Column(name = "sent_email")
    private LocalDate sentEmail;

    @Column(name = "letter_sent")
    private LocalDate letterSent;

    @Column(name = "second_call")
    private LocalDate secondCall;

    @OneToMany(mappedBy = "company", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<ContactPerson> contactPersons = new ArrayList<>();

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "company_manager_id")
    private CompanyManager companyManager;

    @ElementCollection(targetClass = IndustryType.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "company_industries", joinColumns = @JoinColumn(name = "company_id"))
    @Enumerated(EnumType.STRING)
    private List<IndustryType> industryTypes;

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

    public LocationType getLocationType() {
        return locationType;
    }

    public void setLocationType(LocationType locationType) {
        this.locationType = locationType;
    }

    public LocalDate getFirstCall() {
        return firstCall;
    }

    public void setFirstCall(LocalDate firstCall) {
        this.firstCall = firstCall;
    }

    public LocalDate getSentEmail() {
        return sentEmail;
    }

    public void setSentEmail(LocalDate sentEmail) {
        this.sentEmail = sentEmail;
    }

    public LocalDate getLetterSent() {
        return letterSent;
    }

    public void setLetterSent(LocalDate letterSent) {
        this.letterSent = letterSent;
    }

    public LocalDate getSecondCall() {
        return secondCall;
    }

    public void setSecondCall(LocalDate secondCall) {
        this.secondCall = secondCall;
    }

    public List<ContactPerson> getContactPersons() {
        return contactPersons;
    }

    public void setContactPersons(List<ContactPerson> contactPersons) {
        this.contactPersons = contactPersons;
    }

    public void setCompanyManager(CompanyManager companyManager) {
        this.companyManager = companyManager;
    }

    public CompanyManager getCompanyManager() {
        return companyManager;
    }

    public List<IndustryType> getIndustryTypes() {
        return industryTypes;
    }

    public void setIndustryTypes(List<IndustryType> industryTypes) {
        this.industryTypes = industryTypes;
    }
}

