package casaart.emails_clients_db.service;

import java.util.List;

public interface ExelService {

    // export clients to exel
    void exportClientsToExcel(String filePath);

    // export companies to exel
    void exportCompaniesToExcel(String filePath);

    // export companyManagers to exel
    void exportCompanyManagersToExcel(String filePath);

    // export contactPersons to exel
    void exportContactPersonsToExcel(String filePath);

    // update or add loyaltyLevel on clients
    void updateOrAddLoyaltyLevel(String filePath);

    //match emails from exel
    void matchEmailsFromExel(String inputFilePath, String outputFilePath);
}
