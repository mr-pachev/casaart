package casaart.emails_clients_db.service;

import java.util.List;
import java.util.Set;

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

    // export duplicated emails to Excel
    void exportDuplicatedEmailsToExcel(Set<String> duplicatedEmails, String outputFilePath);

    // find duplicated emails from Excel
    void findDuplicatedEmailsFromExcel(String inputFilePath, String outputFilePath);
}
