package casaart.emails_clients_db.repository;

import casaart.emails_clients_db.model.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackRepository extends JpaRepository <Feedback, Long>{

}
