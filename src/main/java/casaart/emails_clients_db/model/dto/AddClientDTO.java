package casaart.emails_clients_db.model.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.List;

public class AddClientDTO {

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

    private String phoneNumber;

    @NotEmpty
    private List<String> sourceTypes;

    @NotBlank
    private String nationality;

    private String loyaltyLevel;

    private LocalDate accommodationDate;

    private String comment;

    private String ratingFood;

    private String ratingQualityPrice;

    private String ratingPoliteness;

    private String ratingCleanTidy;

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

    public List<String> getSourceTypes() {
        return sourceTypes;
    }

    public void setSourceTypes(List<String> sourceTypes) {
        this.sourceTypes = sourceTypes;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getLoyaltyLevel() {
        return loyaltyLevel;
    }

    public void setLoyaltyLevel(String loyaltyLevel) {
        this.loyaltyLevel = loyaltyLevel;
    }

    public LocalDate getAccommodationDate() {
        return accommodationDate;
    }

    public void setAccommodationDate(LocalDate accommodationDate) {
        this.accommodationDate = accommodationDate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getRatingFood() {
        return ratingFood;
    }

    public void setRatingFood(String ratingFood) {
        this.ratingFood = ratingFood;
    }

    public String getRatingQualityPrice() {
        return ratingQualityPrice;
    }

    public void setRatingQualityPrice(String ratingQualityPrice) {
        this.ratingQualityPrice = ratingQualityPrice;
    }

    public String getRatingPoliteness() {
        return ratingPoliteness;
    }

    public void setRatingPoliteness(String ratingPoliteness) {
        this.ratingPoliteness = ratingPoliteness;
    }

    public String getRatingCleanTidy() {
        return ratingCleanTidy;
    }

    public void setRatingCleanTidy(String ratingCleanTidy) {
        this.ratingCleanTidy = ratingCleanTidy;
    }
}
