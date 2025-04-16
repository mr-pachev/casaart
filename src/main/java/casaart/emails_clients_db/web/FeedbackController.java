package casaart.emails_clients_db.web;

import casaart.emails_clients_db.model.dto.CompanyDTO;
import casaart.emails_clients_db.model.dto.FeedbackDTO;
import casaart.emails_clients_db.model.entity.Feedback;
import casaart.emails_clients_db.service.FeedbackService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class FeedbackController {
    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @GetMapping("/feedback")
    public String showForm(Model model) {

        model.addAttribute("feedback", new Feedback());
        return "feedback-form";
    }

    @PostMapping("/feedback")
    public String submitForm(@ModelAttribute Feedback feedback, Model model) {
        feedbackService.saveFeedback(feedback);
        model.addAttribute("feedback", new Feedback()); // за нова празна форма
        model.addAttribute("message", "Благодарим за обратната връзка!");

        return "feedback-form";
    }

}
