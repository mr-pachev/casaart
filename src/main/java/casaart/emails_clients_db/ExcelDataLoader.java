package casaart.emails_clients_db;

import casaart.emails_clients_db.service.EditDataBaseService;
import casaart.emails_clients_db.service.ExelService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
        // добавяне на текущата дата към filePathExportClients
        String basePath = "C:\\Users\\user\\Documents\\IP\\BACKUP_CLIENTS";
        String formattedDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyy"));

        // добавяне на текущата дата към filePathExportCompanyManager
        String basePathCompanyManager = "C:\\Users\\user\\Documents\\IP\\BACKUP_COMPANY_MANAGERS";
        String formattedDateCompanyManager = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyy"));

        // добавяне на текущата дата към filePathExportContactPerson
        String basePathContactPersons = "C:\\Users\\user\\Documents\\IP\\BACKUP_CONTACT_PERSONS.xlsx";
        String formattedDateContactPersons = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyy"));

        // добавяне на текущата дата към filePathExportCompanies
        String basePathCompanies = "C:\\Users\\user\\Documents\\IP\\BACKUP_COMPANIES.xlsx";
        String formattedDateCompanies = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyy"));

        String filePathImportClients = "C:\\Users\\user\\Documents\\IP\\demo.xlsx"; // Път до локалния Excel файл demo
        String filePathImportEmailsFromMailChimp = "C:\\Users\\user\\Documents\\IP\\IMPORT_EMAILS_FROM_MAILCHIMP.xlsx"; // Път до локалния Excel IMPORT_EMAILS_FROM_MAILCHIMP
        String filePathExportClients = basePath + "_" + formattedDate + ".xlsx"; // Път до локалния Excel файл BACKUP_CLIENTS
        String filePathExportCompanies = basePathCompanies + "_" + formattedDateCompanies + ".xlsx"; // Път до локалния Excel файл BACKUP_COMPANIES
        String filePathExportCompanyManager = basePathCompanyManager + "_" + formattedDateCompanyManager + ".xlsx"; // Път до локалния Excel файл BACKUP_COMPANY_MANAGERS
        String filePathExportContactPerson = basePathContactPersons + "_" + formattedDateContactPersons + ".xlsx"; // Път до локалния Excel файл BACKUP_CONTACT_PERSONS
        String filePathExportNoMatchedEmails = "C:\\Users\\user\\Documents\\IP\\NEW_EMAILS.xlsx"; // Път до локалния Excel файл NEW_EMAILS

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
//        exelService.exportCompanyManagersToExcel(filePathExportCompanyManager);
//        System.out.println("EXPORT is FINISHED!");

//        EXPORT CONTACT PERSONS
//        System.out.println("EXPORT OF contactPersons TO " + filePathExportContactPerson);
//        exelService.exportContactPersonsToExcel(filePathExportContactPerson);
//        System.out.println("EXPORT is FINISHED!");

//        exelService.findDuplicatedEmailsFromExcel(filePathImportEmailsFromMailChimp, filePathExportNoMatchedEmails);
    }
}
