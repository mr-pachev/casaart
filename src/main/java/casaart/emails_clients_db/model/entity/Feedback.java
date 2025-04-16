package casaart.emails_clients_db.model.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "feedback")
public class Feedback extends BaseEntity{
    @Column(columnDefinition = "TEXT")
    private String impression;

    private String foundUs;

    @Column(columnDefinition = "TEXT")
    private String suggestions;

    @ElementCollection
    @CollectionTable(name = "feedback_activities", joinColumns = @JoinColumn(name = "feedback_id"))
    @Column(name = "activity")
    private List<String> activities;

    private String customActivity;

    private String themeNightSuggestion;

    private String name;

    private String phone;

    private String email;

    private boolean newsletter;

    private LocalDate date;

    public String getImpression() {
        return impression;
    }

    public void setImpression(String impression) {
        this.impression = impression;
    }

    public String getFoundUs() {
        return foundUs;
    }

    public void setFoundUs(String foundUs) {
        this.foundUs = foundUs;
    }

    public String getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(String suggestions) {
        this.suggestions = suggestions;
    }

    public List<String> getActivities() {
        return activities;
    }

    public void setActivities(List<String> activities) {
        this.activities = activities;
    }

    public String getCustomActivity() {
        return customActivity;
    }

    public void setCustomActivity(String customActivity) {
        this.customActivity = customActivity;
    }

    public String getThemeNightSuggestion() {
        return themeNightSuggestion;
    }

    public void setThemeNightSuggestion(String themeNightSuggestion) {
        this.themeNightSuggestion = themeNightSuggestion;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isNewsletter() {
        return newsletter;
    }

    public void setNewsletter(boolean newsletter) {
        this.newsletter = newsletter;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
