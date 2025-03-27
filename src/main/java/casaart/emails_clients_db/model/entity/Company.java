package casaart.emails_clients_db.model.entity;

import casaart.emails_clients_db.model.enums.*;
import jakarta.persistence.*;

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

    @Column(name = "url")
    private String url;

    @Enumerated(EnumType.STRING)
    private LocationType locationType;

    @OneToMany(mappedBy = "company", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<ContactPerson> contactPersons = new ArrayList<>();

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "company_manager_id")
    private CompanyManager companyManager;

    @ElementCollection(targetClass = UnitType.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "company_units", joinColumns = @JoinColumn(name = "company_id"))
    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private List<UnitType> unitTypes;

    @ElementCollection(targetClass = IndustryType.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "company_industries", joinColumns = @JoinColumn(name = "company_id"))
    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private List<IndustryType> industryTypes;

    @ElementCollection(targetClass = PartnerType.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "company_partners", joinColumns = @JoinColumn(name = "company_id"))
    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private List<PartnerType> partnerTypes;

    @Enumerated(EnumType.STRING)
    private CompanyType companyType;

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public LocationType getLocationType() {
        return locationType;
    }

    public void setLocationType(LocationType locationType) {
        this.locationType = locationType;
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

    public List<UnitType> getUnitTypes() {
        return unitTypes;
    }

    public void setUnitTypes(List<UnitType> unitTypes) {
        this.unitTypes = unitTypes;
    }

    public List<IndustryType> getIndustryTypes() {
        return industryTypes;
    }

    public void setIndustryTypes(List<IndustryType> industryTypes) {
        this.industryTypes = industryTypes;
    }

    public List<PartnerType> getPartnerTypes() {
        return partnerTypes;
    }

    public void setPartnerTypes(List<PartnerType> partnerTypes) {
        this.partnerTypes = partnerTypes;
    }

    public CompanyType getCompanyType() {
        return companyType;
    }

    public void setCompanyType(CompanyType companyType) {
        this.companyType = companyType;
    }
}

