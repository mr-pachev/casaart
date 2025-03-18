package casaart.emails_clients_db;

import casaart.emails_clients_db.service.EditDataBaseService;
import casaart.emails_clients_db.service.ExelService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

//@Component
public class ExcelDataLoader implements CommandLineRunner {

    final ExelService exelService;
    final EditDataBaseService editDataBaseService;

    public ExcelDataLoader(ExelService exelService, EditDataBaseService editDataBaseService) {
        this.exelService = exelService;
        this.editDataBaseService = editDataBaseService;
    }

    @Override
    public void run(String... args) {
        String filePathImportClients = "C:\\Users\\user\\Documents\\IP\\demo.xlsx"; // Път до локалния Excel файл
        String filePathImportEmails = "C:\\Users\\user\\Documents\\IP\\IMPORT_EMAILS.xlsx"; // Път до локалния Excel файл
        String filePathExportClients = "C:\\Users\\user\\Documents\\IP\\BACKUP_CLIENTS.xlsx"; // Път до локалния Excel файл
        String filePathExportCompanies = "C:\\Users\\user\\Documents\\IP\\BACKUP_COMPANIES.xlsx"; // Път до локалния Excel файл
        String filePathExportCompanyManager = "C:\\Users\\user\\Documents\\IP\\BACKUP_COMPANY_MANAGERS.xlsx"; // Път до локалния Excel файл
        String filePathExportContactPerson = "C:\\Users\\user\\Documents\\IP\\BACKUP_CONTACT_PERSONS.xlsx"; // Път до локалния Excel файл
        String filePathExportEmails = "C:\\Users\\user\\Documents\\IP\\NEW_EMAILS.xlsx"; // Път до локалния Excel файл

//        EDIT CLIENTS
//        editDataBaseService.removeDuplicateClients();
//        editDataBaseService.removeDuplicateClientsSameEmail();
//        editDataBaseService.removedClientsWithFalseEmail();
//        editDataBaseService.normalizeEmailsForClients();
//        editDataBaseService.normalizeEmailsForCompanies();
//        editDataBaseService.updateAllClientNames();

//        IMPORT CLIENTS
//        System.out.println("START UPDATING/IMPORT clients FROM " + filePathImportClients);
//        exelService.updateOrAddLoyaltyLevel(filePathImportClients);
//        System.out.println("UPDATING/IMPORT IS FINISHED!");

//        EXPORT CLIENTS
//        System.out.println("EXPORT OF clients TO " + filePathExportClients);
//        exelService.exportClientsToExcel(filePathExportClients);
//        System.out.println("EXPORT is FINISHED!");

//        EXPORT COMPANIES
//        System.out.println("EXPORT OF companies TO " + filePathExportCompanies);
//        exelService.exportCompaniesToExcel(filePathExportCompanies);
//        System.out.println("EXPORT is FINISHED!");

//        EXPORT COMPANY MANAGERS
//        System.out.println("EXPORT OF companyManagers TO " + filePathExportCompanyManager);
//        exelService.exportCompanyManagerToExcel(filePathExportCompanyManager);
//        System.out.println("EXPORT is FINISHED!");

//        EXPORT CONTACT PERSONS
//        System.out.println("EXPORT OF contactPersons TO " + filePathExportContactPerson);
//        exelService.exportContactPersonsToExcel(filePathExportContactPerson);
//        System.out.println("EXPORT is FINISHED!");

//        exelService.matchEmailsFromExel(filePathImportEmails, filePathExportEmails);
    }
}
