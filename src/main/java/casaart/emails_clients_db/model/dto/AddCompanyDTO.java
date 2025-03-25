package casaart.emails_clients_db.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

public class AddCompanyDTO {

    @NotBlank
    @Size(min = 2, max = 50)
    private String name;

    @NotBlank
    @Size(min = 5, max = 255)
    private String address;

    @NotBlank
    @Size(min=10, max = 10)
    private String phoneNumber;

    @Email
    private String email;

    @NotBlank
    private String locationType;

    private List<String> units = new ArrayList<>();;

    private List<String> industries = new ArrayList<>();

    private List<String> partnerTypes = new ArrayList<>();

    @NotBlank
    private String companyType;

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

    public List<String> getUnits() {
        return units;
    }

    public void setUnits(List<String> units) {
        this.units = units;
    }

    public List<String> getIndustries() {
        return industries;
    }

    public void setIndustries(List<String> industries) {
        this.industries = industries;
    }

    public List<String> getPartnerTypes() {
        return partnerTypes;
    }

    public void setPartnerTypes(List<String> partnerTypes) {
        this.partnerTypes = partnerTypes;
    }

    public String getCompanyType() {
        return companyType;
    }

    public void setCompanyType(String companyType) {
        this.companyType = companyType;
    }
}
