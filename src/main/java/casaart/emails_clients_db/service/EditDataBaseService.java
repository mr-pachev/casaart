package casaart.emails_clients_db.service;

public interface EditDataBaseService {

    // remove duplicate clients by firstName, lastName and email
    void removeDuplicateClients();

    // remove duplicate clients same email
    void removeDuplicateClientsSameEmail();

    // remove clients who email ended on @guest.booking.com and @m.expediapartnercentral.com
    void removedClientsWithFalseEmail();

    // edit all emails in lower case for clients
    void normalizeEmailsForClients();

    // edit all emails in lower case for company
    void normalizeEmailsForCompanies();

    // fill empty counterStay
    void initializeCounterStayIfNull();

    // update all client names
    void updateAllClientNames();
}
