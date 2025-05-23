package casaart.emails_clients_db.service.impl;

import casaart.emails_clients_db.model.entity.Client;
import casaart.emails_clients_db.model.entity.CompanyManager;
import casaart.emails_clients_db.model.entity.ContactPerson;
import casaart.emails_clients_db.repository.ClientRepository;
import casaart.emails_clients_db.repository.CompanyManagerRepository;
import casaart.emails_clients_db.repository.ContactPersonRepository;
import casaart.emails_clients_db.service.MarketingService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class MarketingServiceImpl implements MarketingService {
    private final CompanyManagerRepository companyManagerRepository;
    private final ContactPersonRepository contactPersonRepository;
    private final ClientRepository clientRepository;

    public MarketingServiceImpl(CompanyManagerRepository companyManagerRepository, ContactPersonRepository contactPersonRepository, ClientRepository clientRepository) {
        this.companyManagerRepository = companyManagerRepository;
        this.contactPersonRepository = contactPersonRepository;
        this.clientRepository = clientRepository;
    }

    /* ----- MANAGER ----- */

    // register first call for company manager
    @Override
    public void registerFirstCallManager(long id) {
        CompanyManager companyManager = companyManagerRepository.findById(id).get();
        companyManager.setFirstCall(LocalDate.now());

        companyManagerRepository.save(companyManager);
    }

    // register send email for company manager
    @Override
    public void registerSendEmailManager(long id) {
        CompanyManager companyManager = companyManagerRepository.findById(id).get();
        companyManager.setSendEmail(LocalDate.now());

        companyManagerRepository.save(companyManager);
    }

    // register send letter for company manager
    @Override
    public void registerSendLetterManager(long id) {
        CompanyManager companyManager = companyManagerRepository.findById(id).get();
        companyManager.setSendLetter(LocalDate.now());

        companyManagerRepository.save(companyManager);
    }

    // register second call for manager
    @Override
    public void registerSecondCallManager(long id) {
        CompanyManager companyManager = companyManagerRepository.findById(id).get();
        companyManager.setSecondCall(LocalDate.now());

        companyManagerRepository.save(companyManager);
    }

    // register presence for manager
    @Override
    public void registerPresenceManager(long id) {
        CompanyManager companyManager = companyManagerRepository.findById(id).get();
        companyManager.setPresence(LocalDate.now());

        companyManagerRepository.save(companyManager);
    }

    /* ----- CONTACT PERSON ----- */

    // register first call for contact person
    @Override
    public void registerFirstCallContactPerson(long id) {
        ContactPerson contactPerson = contactPersonRepository.findById(id).get();
        contactPerson.setFirstCall(LocalDate.now());

        contactPersonRepository.save(contactPerson);
    }

    // register send email for contact person
    @Override
    public void registerSendEmailContactPerson(long id) {
        ContactPerson contactPerson = contactPersonRepository.findById(id).get();
        contactPerson.setSendEmail(LocalDate.now());

        contactPersonRepository.save(contactPerson);
    }

    // register send letter for contact person
    @Override
    public void registerSendLetterContactPerson(long id) {
        ContactPerson contactPerson = contactPersonRepository.findById(id).get();
        contactPerson.setSendLetter(LocalDate.now());

        contactPersonRepository.save(contactPerson);
    }

    // register second call for contact person
    @Override
    public void registerSecondCallContactPerson(long id) {
        ContactPerson contactPerson = contactPersonRepository.findById(id).get();
        contactPerson.setSecondCall(LocalDate.now());

        contactPersonRepository.save(contactPerson);
    }

    // register presence for contact person
    @Override
    public void registerPresenceContactPerson(long id) {
        ContactPerson contactPerson = contactPersonRepository.findById(id).get();
        contactPerson.setPresence(LocalDate.now());

        contactPersonRepository.save(contactPerson);
    }

    // register first email for client
    @Override
    public void registerFirstEmailClient(long id) {
        Client client = clientRepository.findById(id);
        client.setFirstEmail(LocalDate.now());

        clientRepository.save(client);
    }

    // register first call for client
    @Override
    public void registerFirstCallClient(long id) {
        Client client = clientRepository.findById(id);
        client.setFirstCall(LocalDate.now());

        clientRepository.save(client);
    }

    // register second email for client
    @Override
    public void registerSecondEmailClient(long id) {
        Client client = clientRepository.findById(id);
        client.setSecondEmail(LocalDate.now());

        clientRepository.save(client);
    }

    // register second call for client
    @Override
    public void registerSecondCallClient(long id) {
        Client client = clientRepository.findById(id);
        client.setSecondCall(LocalDate.now());

        clientRepository.save(client);
    }


}
