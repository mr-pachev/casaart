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
        String filePathImport = "D:\\demo.xlsx"; // Път до локалния Excel файл
        String filePathExport = "D:\\demo1.xlsx"; // Път до локалния Excel файл

//        editDataBaseService.removeDuplicateClients();
//        editDataBaseService.updateAllClientNames();
//
//        System.out.println("EXPORT OF clients TO " + filePathExport);
//        exelService.exportClientsToExcel(filePathExport);
//        System.out.println("EXPORT is FINISHED!");
//
//        System.out.println("START IMPORTING clients FROM " + filePathImport);
//        exelService.importClientsFromExcel(filePathImport);
//        System.out.println("IMPORT is FINISHED!");

        System.out.println("START UPDATING clients FROM " + filePathImport);
        exelService.updateOrAddLoyaltyLevel(filePathImport);
        System.out.println("UPDATING IS FINISHED!");
    }
}
