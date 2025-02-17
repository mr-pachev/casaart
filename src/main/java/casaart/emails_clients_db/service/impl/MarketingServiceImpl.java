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

    // register first email for company manager
    @Override
    public void registerFirstEmailManager(long id) {
        CompanyManager companyManager = companyManagerRepository.findById(id);
        companyManager.setFirstEmail(LocalDate.now());

        companyManagerRepository.save(companyManager);
    }

    // register first call for company manager
    @Override
    public void registerFirstCallManager(long id) {
        CompanyManager companyManager = companyManagerRepository.findById(id);
        companyManager.setFirstCall(LocalDate.now());

        companyManagerRepository.save(companyManager);
    }

    // register second email for company manager
    @Override
    public void registerSecondEmailManager(long id) {
        CompanyManager companyManager = companyManagerRepository.findById(id);
        companyManager.setSecondEmail(LocalDate.now());

        companyManagerRepository.save(companyManager);
    }

    // register second call for company manager
    @Override
    public void registerSecondCallManager(long id) {
        CompanyManager companyManager = companyManagerRepository.findById(id);
        companyManager.setSecondCall(LocalDate.now());

        companyManagerRepository.save(companyManager);
    }

    // register first email for contact person
    @Override
    public void registerFirstEmailContactPerson(long id) {
        ContactPerson contactPerson = contactPersonRepository.findById(id).get();
        contactPerson.setFirstEmail(LocalDate.now());

        contactPersonRepository.save(contactPerson);
    }

    // register first call for contact person
    @Override
    public void registerFirstCallContactPerson(long id) {
        ContactPerson contactPerson = contactPersonRepository.findById(id).get();
        contactPerson.setFirstCall(LocalDate.now());

        contactPersonRepository.save(contactPerson);
    }

    // register second email for contact person
    @Override
    public void registerSecondEmailContactPerson(long id) {
        ContactPerson contactPerson = contactPersonRepository.findById(id).get();
        contactPerson.setSecondEmail(LocalDate.now());

        contactPersonRepository.save(contactPerson);
    }

    // register second call for contact person
    @Override
    public void registerSecondCallContactPerson(long id) {
        ContactPerson contactPerson = contactPersonRepository.findById(id).get();
        contactPerson.setSecondCall(LocalDate.now());

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
