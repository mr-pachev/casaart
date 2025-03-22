package casaart.emails_clients_db.repository;

import casaart.emails_clients_db.model.entity.CompanyManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyManagerRepository extends JpaRepository<CompanyManager, Long> {
   Optional<CompanyManager> findByFirstNameAndLastNameAndPhoneNumber(String firstName,
                                                                     String lastName,
                                                                     String phoneNumber);

   Optional<CompanyManager> findById(long id);
   CompanyManager findByCompanyId(long id);

   // sort options
   @Query("SELECT c.companyManager FROM Company c WHERE c.companyType = 'ПАРТНЬОР' ORDER BY c.id DESC")
   List<CompanyManager> findAllCompanyManagersForPartnersOrderedByIdDesc();

   @Query("SELECT cm FROM CompanyManager cm WHERE cm.company.companyType = 'ПАРТНЬОР' ORDER BY cm.firstName ASC, cm.middleName ASC, cm.lastName ASC")
   List<CompanyManager> findAllPartnersOrderByFirstNameAscMiddleNameAscLastNameAsc();

   @Query("SELECT cm FROM CompanyManager cm WHERE cm.company.companyType = 'ПАРТНЬОР' ORDER BY cm.firstCall DESC")
   List<CompanyManager> findAllPartnersOrderByFirstCallDesc();

   @Query("SELECT cm FROM CompanyManager cm WHERE cm.company.companyType = 'ПАРТНЬОР' ORDER BY cm.sendEmail DESC")
   List<CompanyManager> findAllPartnersOrderBySendEmailDesc();

   @Query("SELECT cm FROM CompanyManager cm WHERE cm.company.companyType = 'ПАРТНЬОР' ORDER BY cm.sendLetter DESC")
   List<CompanyManager> findAllPartnersOrderBySendLetterDesc();

   @Query("SELECT cm FROM CompanyManager cm WHERE cm.company.companyType = 'ПАРТНЬОР' ORDER BY cm.secondCall DESC")
   List<CompanyManager> findAllPartnersOrderBySecondCallDesc();

   @Query("SELECT cm FROM CompanyManager cm WHERE cm.company.companyType = 'ПАРТНЬОР' ORDER BY cm.presence DESC")
   List<CompanyManager> findAllPartnersOrderByPresenceDesc();

   // from search field
   List<CompanyManager> findAllByFirstName(String firstName);
   List<CompanyManager> findAllByFirstNameAndLastName(String firstName, String lastName);
   List<CompanyManager> findByEmailStartingWithIgnoreCase(String email);
}
