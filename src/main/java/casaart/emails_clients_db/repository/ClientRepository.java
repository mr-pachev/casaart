package casaart.emails_clients_db.repository;

import casaart.emails_clients_db.model.entity.Client;
import casaart.emails_clients_db.model.enums.LoyaltyLevel;
import casaart.emails_clients_db.model.enums.Nationality;
import casaart.emails_clients_db.model.enums.Rating;
import casaart.emails_clients_db.model.enums.SourceType;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByEmail(String email);

    Optional<Client> findByFirstNameAndLastNameAndEmail(String firstName, String lastName, String email);

    Client findById(long id);

    // Сортира
    List<Client> findAllByOrderByIdDesc();

    List<Client> findAllByOrderByFirstNameAscMiddleNameAscLastNameAsc();

    List<Client> findAllByOrderByFirstEmailDesc();

    List<Client> findAllByOrderByFirstCallDesc();

    List<Client> findAllByOrderBySecondEmailDesc();

    List<Client> findAllByOrderBySecondCallDesc();

    List<Client> findAllByFirstName(String firstName);

    List<Client> findAllByFirstNameAndLastName(String firstName, String lastName);

    List<Client> findAllBySourceTypes(SourceType sourceType);

    List<Client> findAllByLoyaltyLevel(LoyaltyLevel loyaltyLevel);

    List<Client> findAllByNationality(Nationality nationality);

    List<Client> findAllByRatingFood(Rating ratingFood);

    List<Client> findAllByRatingQualityPrice(Rating ratingQualityPrice);

    List<Client> findAllByRatingPoliteness(Rating ratingPoliteness);

    List<Client> findAllByRatingCleanTidy(Rating ratingCleanTidy);

    @Query("SELECT c FROM Client c " +
            "JOIN c.orders o " +
            "GROUP BY c.id " +
            "ORDER BY MAX(o.year) DESC")
    List<Client> findAllClientsOrderByLatestOrderYearDesc();

    @Query("SELECT c FROM Client c " +
            "JOIN c.orders o " +
            "WHERE o.year = :year " +
            "GROUP BY c.id " +
            "ORDER BY MAX(o.id) ASC")
    List<Client> findAllClientsByOrderYear(@Param("year") String year);


    @Query("SELECT c.email FROM Client c")
    Set<String> findAllEmails();

    // Изтрива всички дубликати, оставяйки само един запис за всеки email
    @Modifying
    @Transactional
    @Query(value = "DELETE c1 FROM clients c1 JOIN clients c2 ON c1.email = c2.email AND c1.id > c2.id", nativeQuery = true)
    void removeDuplicateClientsEmail();

}

