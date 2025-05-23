package casaart.emails_clients_db.service;

public interface MarketingService {

    /* ----- MANAGER ----- */

    // register first call for company manager
    void registerFirstCallManager(long id);

    // register send email for company manager
    void registerSendEmailManager(long id);

    // register send letter for company manager
    void registerSendLetterManager(long id);

    // register second call for manager
    void registerSecondCallManager(long id);

    // register presence for manager
    void registerPresenceManager(long id);

    /* ----- CONTACT PERSON ----- */

    // register first call for contact person
    void registerFirstCallContactPerson(long id);

    // register send email for contact person
    void registerSendEmailContactPerson(long id);

    // register send letter for contact person
    void registerSendLetterContactPerson(long id);

    // register second call for contact person
    void registerSecondCallContactPerson(long id);

    // register presence for contact person
    void registerPresenceContactPerson(long id);

    // register first email for client
    void registerFirstEmailClient(long id);

    // register first call for client
    void registerFirstCallClient(long id);

    // register second email for client
    void registerSecondEmailClient(long id);

    // register second call for client
    void registerSecondCallClient(long id);
}
