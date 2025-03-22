package casaart.emails_clients_db.repository;

import casaart.emails_clients_db.model.entity.ContactPerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContactPersonRepository extends JpaRepository<ContactPerson, Long> {
    Optional<ContactPerson> findByFirstNameAndLastNameAndPhoneNumber(String firstName, String lastName,String phoneNumber);
    Optional<ContactPerson> findById(long id);

    List<ContactPerson> findAllByCompanyId(long id);

    // sort options
    @Query("SELECT cp FROM ContactPerson cp WHERE cp.company.companyType = 'ПАРТНЬОР' ORDER BY cp.id DESC")
    List<ContactPerson> findAllPartnersContactPersonsOrderByIdDesc();

    @Query("SELECT cp FROM ContactPerson cp WHERE cp.company.companyType = 'ПАРТНЬОР' ORDER BY cp.firstName ASC, cp.middleName ASC, cp.lastName ASC")
    List<ContactPerson> findAllPartnersContactPersonsOrderByFirstNameAscMiddleNameAscLastNameAsc();

    @Query("SELECT cp FROM ContactPerson cp WHERE cp.company.companyType = 'ПАРТНЬОР' ORDER BY cp.firstCall DESC")
    List<ContactPerson> findAllPartnersContactPersonsOrderByFirstCallDesc();

    @Query("SELECT cp FROM ContactPerson cp WHERE cp.company.companyType = 'ПАРТНЬОР' ORDER BY cp.sendEmail DESC")
    List<ContactPerson> findAllPartnersContactPersonsOrderBySendEmailDesc();

    @Query("SELECT cp FROM ContactPerson cp WHERE cp.company.companyType = 'ПАРТНЬОР' ORDER BY cp.sendLetter DESC")
    List<ContactPerson> findAllPartnersContactPersonsOrderBySendLetterDesc();

    @Query("SELECT cp FROM ContactPerson cp WHERE cp.company.companyType = 'ПАРТНЬОР' ORDER BY cp.secondCall DESC")
    List<ContactPerson> findAllPartnersContactPersonsOrderBySecondCallDesc();

    @Query("SELECT cp FROM ContactPerson cp WHERE cp.company.companyType = 'ПАРТНЬОР' ORDER BY cp.presence DESC")
    List<ContactPerson> findAllPartnersContactPersonsOrderByPresenceDesc();



    List<ContactPerson> findAllByFirstName(String firstName);
    List<ContactPerson> findAllByFirstNameAndLastName(String firstName, String lastName);
    List<ContactPerson> findByEmailStartingWithIgnoreCase(String email);
}
