package casaart.emails_clients_db.service;

import casaart.emails_clients_db.model.entity.Feedback;

public interface FeedbackService {
    Feedback saveFeedback(Feedback feedback);
}
