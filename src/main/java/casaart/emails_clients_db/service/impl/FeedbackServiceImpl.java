package casaart.emails_clients_db.service.impl;

import casaart.emails_clients_db.model.entity.Feedback;
import casaart.emails_clients_db.repository.FeedbackRepository;
import casaart.emails_clients_db.service.FeedbackService;
import org.springframework.stereotype.Service;

@Service
public class FeedbackServiceImpl implements FeedbackService {
    private final FeedbackRepository feedbackRepository;

    public FeedbackServiceImpl(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    @Override
    public Feedback saveFeedback(Feedback feedback) {
        return feedbackRepository.save(feedback);
    }
}
