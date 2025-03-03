package casaart.emails_clients_db.service;

public interface EditDataBaseService {

    // remove duplicate clients
    void removeDuplicateClients();

    // remove duplicate clients same email
    void removeDuplicateClientsSameEmail();

    // remove clients who email ended on @guest.booking.com and @m.expediapartnercentral.com
    void removedClientsWithFalseEmail();

    // update all client names
    void updateAllClientNames();
}
