package casaart.emails_clients_db.service;

public interface MarketingService {

    // register first email for company manager
    void registerFirstEmailManager(long id);

    // register first call for company manager
    void registerFirstCallManager(long id);

    // register second email for company manager
    void registerSecondEmailManager(long id);

    // register second call for manager
    void registerSecondCallManager(long id);

    // register first email for contact person
    void registerFirstEmailContactPerson(long id);

    // register first call for contact person
    void registerFirstCallContactPerson(long id);

    // register second email for contact person
    void registerSecondEmailContactPerson(long id);

    // register second call for contact person
    void registerSecondCallContactPerson(long id);
}
