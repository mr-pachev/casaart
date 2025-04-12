package casaart.emails_clients_db.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

public class ClientDTO {

    long id;

    @NotBlank
    @Size(min = 2, max = 30)
    private String firstName;

    @Size(max = 30)
    private String middleName;

    @NotBlank
    @Size(min = 2, max = 30)
    private String lastName;

    @NotBlank@Email
    private String email;

    @Size(max = 10)
    private String phoneNumber;

    int counterStay;

    private List<String> sourceTypes;

    private String loyaltyLevel;

    @NotBlank
    private String nationality;

    private String rating;

    private LocalDate firstCall;

    private LocalDate firstEmail;

    private LocalDate secondCall;

    private LocalDate secondEmail;

    private String addedFrom;

    private String modifyFrom;

    private LocalDate accommodationDate;

    private List<OrderDTO> orders;

    private String comment;

    private String ratingFood;

    private String ratingQualityPrice;

    private String ratingPoliteness;

    private String ratingCleanTidy;

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

    public int getCounterStay() {
        return counterStay;
    }

    public void setCounterStay(int counterStay) {
        this.counterStay = counterStay;
    }

    public List<String> getSourceTypes() {
        return sourceTypes;
    }

    public void setSourceTypes(List<String> sourceTypes) {
        this.sourceTypes = sourceTypes;
    }

    public String getLoyaltyLevel() {
        return loyaltyLevel;
    }

    public void setLoyaltyLevel(String loyaltyLevel) {
        this.loyaltyLevel = loyaltyLevel;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
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

    public String getAddedFrom() {
        return addedFrom;
    }

    public void setAddedFrom(String addedFrom) {
        this.addedFrom = addedFrom;
    }

    public String getModifyFrom() {
        return modifyFrom;
    }

    public void setModifyFrom(String modifyFrom) {
        this.modifyFrom = modifyFrom;
    }

    public LocalDate getAccommodationDate() {
        return accommodationDate;
    }

    public void setAccommodationDate(LocalDate accommodationDate) {
        this.accommodationDate = accommodationDate;
    }

    public List<OrderDTO> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderDTO> orders) {
        this.orders = orders;
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
