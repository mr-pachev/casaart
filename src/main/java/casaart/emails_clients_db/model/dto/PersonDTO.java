package casaart.emails_clients_db.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

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

    @Email
    private String email;

    @Size(max = 10)
    private String phoneNumber;

    private String company;

    public String getFullName(){
        StringBuilder sb = new StringBuilder();
        sb.append(this.getFirstName());
        sb.append(" ");

        if (!this.getMiddleName().isEmpty()){
            sb.append(this.getMiddleName());
            sb.append(" ");
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
}
