package casaart.emails_clients_db;

import casaart.emails_clients_db.service.EditDataBaseService;
import casaart.emails_clients_db.service.ExelService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
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
        String filePathExportClients = "C:\\Users\\user\\Documents\\IP\\BACKUP_CLIENTS.xlsx"; // Път до локалния Excel файл
        String filePathExportCompanies = "C:\\Users\\user\\Documents\\IP\\BACKUP_COMPANIES.xlsx"; // Път до локалния Excel файл
        String filePathExportCompanyManager = "C:\\Users\\user\\Documents\\IP\\BACKUP_COMPANY_MANAGER.xlsx"; // Път до локалния Excel файл

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

//        EXPORT COMPANY MANAGER
        System.out.println("EXPORT OF companyManager TO " + filePathExportCompanyManager);
        exelService.exportCompanyManagerToExcel(filePathExportCompanyManager);
        System.out.println("EXPORT is FINISHED!");
    }
}
