package casaart.emails_clients_db.model.entity;

import casaart.emails_clients_db.model.enums.*;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "clients")
public class Client extends BaseEntity{
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

    @Column(name = "counter_stay")
    Integer counterStay;

    @ElementCollection(targetClass = SourceType.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "client_sources", joinColumns = @JoinColumn(name = "client_id"))
    @Enumerated(EnumType.STRING)
    @Column(length = 100)
    private List<SourceType> sourceTypes;

    @Enumerated(EnumType.STRING)
    private LoyaltyLevel loyaltyLevel;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private Nationality nationality;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private Rating ratingFood;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private Rating ratingQualityPrice;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private Rating ratingPoliteness;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private Rating ratingCleanTidy;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    private String modifyFrom;

    @Column(name = "accommodation_date")
    private LocalDate accommodationDate;

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Order> orders = new ArrayList<>();

    @Column(name = "first_call")
    private LocalDate firstCall;

    @Column(name = "first_email")
    private LocalDate firstEmail;

    @Column(name = "second_call")
    private LocalDate secondCall;

    @Column(name = "second_email")
    private LocalDate secondEmail;

    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;

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

    public Integer getCounterStay() {
        return counterStay;
    }

    public void setCounterStay(Integer counterStay) {
        this.counterStay = counterStay;
    }

    public List<SourceType> getSourceTypes() {
        return sourceTypes;
    }

    public void setSourceTypes(List<SourceType> sourceTypes) {
        this.sourceTypes = sourceTypes;
    }

    public LoyaltyLevel getLoyaltyLevel() {
        return loyaltyLevel;
    }

    public void setLoyaltyLevel(LoyaltyLevel loyaltyLevel) {
        this.loyaltyLevel = loyaltyLevel;
    }

    public Nationality getNationality() {
        return nationality;
    }

    public void setNationality(Nationality nationality) {
        this.nationality = nationality;
    }

    public Rating getRatingFood() {
        return ratingFood;
    }

    public void setRatingFood(Rating ratingFood) {
        this.ratingFood = ratingFood;
    }

    public Rating getRatingQualityPrice() {
        return ratingQualityPrice;
    }

    public void setRatingQualityPrice(Rating ratingQualityPrice) {
        this.ratingQualityPrice = ratingQualityPrice;
    }

    public Rating getRatingPoliteness() {
        return ratingPoliteness;
    }

    public void setRatingPoliteness(Rating ratingPoliteness) {
        this.ratingPoliteness = ratingPoliteness;
    }

    public Rating getRatingCleanTidy() {
        return ratingCleanTidy;
    }

    public void setRatingCleanTidy(Rating ratingCleanTidy) {
        this.ratingCleanTidy = ratingCleanTidy;
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

    public LocalDate getAccommodationDate() {
        return accommodationDate;
    }

    public void setAccommodationDate(LocalDate accommodationDate) {
        this.accommodationDate = accommodationDate;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
