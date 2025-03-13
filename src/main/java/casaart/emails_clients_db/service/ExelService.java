package casaart.emails_clients_db.service;

public interface ExelService {

    // export clients to exel
    void exportClientsToExcel(String filePath);

    // export companies to exel
    void exportCompaniesToExcel(String filePath);

    // update or add loyaltyLevel on clients
    void updateOrAddLoyaltyLevel(String filePath);
}
