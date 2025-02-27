package casaart.emails_clients_db.service;

public interface EditDataBaseService {

    // remove duplicate clients
    void removeDuplicateClients();

    // remove clients who email ended on @guest.booking.com
    void removedClientsWithFalseEmail();

    // update all client names
    void updateAllClientNames();
}
