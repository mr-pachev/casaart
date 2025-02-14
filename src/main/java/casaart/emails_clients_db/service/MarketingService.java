package casaart.emails_clients_db.service;

public interface MarketingService {

    // register first email for manager
    void registerFirstEmailManager(long id);

    // register first call for manager
    void registerFirstCallManager(long id);

    // register second email for manager
    void registerSecondEmailManager(long id);

    // register second call for manager
    void registerSecondCallManager(long id);
}
