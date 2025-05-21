package casaart.emails_clients_db.service;

import org.apache.poi.ss.usermodel.Workbook;

import java.util.Set;

public interface ExelService {

    // export clients to exel to browser
    void populateWorkbookWithClients(Workbook workbook);

    // export all companies to exel to HDD
    void exportCompaniesToExcel(String filePath);

    // export all companies to exel to browser
    void populateWorkbookWithCompanies(Workbook workbook);

    // export all suppliers to exel to browser
    void populateWorkbookWithSuppliers(Workbook workbook);

    // export all partners to exel to browser
    void populateWorkbookWithPartners(Workbook workbook);

    // export companyManagers to exel to browser
    void populateWorkbookWithCompanyManagers(Workbook workbook);

    // export contactPersons to exel to browser
    void populateWorkbookWithContactPersons(Workbook workbook);

    // update or add loyaltyLevel on clients
    void updateOrAddLoyaltyLevel(String filePath);

    // export duplicated emails to Excel
    void exportDuplicatedEmailsToExcel(Set<String> duplicatedEmails, String outputFilePath);

    // find duplicated emails from Excel
    void findDuplicatedEmailsFromExcel(String inputFilePath, String outputFilePath);
}
