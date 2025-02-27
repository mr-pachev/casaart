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
        String filePathImport = "C:\\Users\\user\\Documents\\IP\\demo.xlsx"; // Път до локалния Excel файл
        String filePathExport = "C:\\Users\\user\\Documents\\IP\\demo1.xlsx"; // Път до локалния Excel файл

        editDataBaseService.removeDuplicateClients();
        editDataBaseService.updateAllClientNames();

        System.out.println("Export of clients from " + filePathExport);
        exelService.exportClientsToExcel(filePathExport);
        System.out.println("Export is finished!");

        System.out.println("Starts importing clients from " + filePathImport);
        exelService.importClientsFromExcel(filePathImport);
        System.out.println("Import is finished!");


    }
}
