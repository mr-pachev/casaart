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

        // добавяне на текущата дата към filePathExportCompanies
        String basePathCompanies = "D:\\BACKUP_COMPANIES.xlsx";
        String formattedDateCompanies = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyy"));

        String filePathImportClients = "D:\\IMPORT_CLIENTS_TEMPLATE.xlsx"; // Път до локалния Excel файл demo
        String filePathImportEmailsFromMailChimp = "D:\\IMPORT_EMAILS_FROM_MAILCHIMP.xlsx"; // Път до локалния Excel IMPORT_EMAILS_FROM_MAILCHIMP
        String filePathExportCompanies = basePathCompanies + "_" + formattedDateCompanies + ".xlsx"; // Път до локалния Excel файл BACKUP_COMPANIES
        String filePathExportNoMatchedEmails = "D:\\NEW_EMAILS.xlsx"; // Път до локалния Excel файл NEW_EMAILS

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

//        EXPORT COMPANIES
//        System.out.println("EXPORT OF companies TO " + filePathExportCompanies);
//        exelService.exportCompaniesToExcel(filePathExportCompanies);
//        System.out.println("EXPORT is FINISHED!");

//        exelService.findDuplicatedEmailsFromExcel(filePathImportEmailsFromMailChimp, filePathExportNoMatchedEmails);

//        editDataBaseService.initializeCounterStayIfNull();
    }
}
