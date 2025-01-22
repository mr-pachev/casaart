package casaart.emails_clients_db.model.entity;

import casaart.emails_clients_db.model.enums.LocationType;
import jakarta.persistence.*;

import java.time.LocalDate;
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

    @OneToMany(mappedBy = "company", fetch = FetchType.EAGER)
    private List<Person> contactPerson;

    @OneToOne
    private Person companyManager;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(
            name = "company_industry",
            joinColumns = @JoinColumn(name = "company_id"),
            inverseJoinColumns = @JoinColumn(name = "industry_id")
    )
    private List<Industry> industries;

    public void addIndustry(Industry industry) {
        this.industries.add(industry);
        industry.getCompanies().add(this); // Актуализира връзката от другата страна
    }

    public void removeIndustry(Industry industry) {
        this.industries.remove(industry);
        industry.getCompanies().remove(this); // Премахва връзката от другата страна
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

    public List<Person> getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(List<Person> contactPerson) {
        this.contactPerson = contactPerson;
    }

    public Person getCompanyManager() {
        return companyManager;
    }

    public void setCompanyManager(Person companyManager) {
        this.companyManager = companyManager;
    }

    public List<Industry> getIndustries() {
        return industries;
    }

    public void setIndustries(List<Industry> industries) {
        this.industries = industries;
    }
}

