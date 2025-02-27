package casaart.emails_clients_db.service;

public interface EditDataBaseService {

    // remove duplicate clients
    void removeDuplicateClients();

    // update all client names
    void updateAllClientNames();
}
