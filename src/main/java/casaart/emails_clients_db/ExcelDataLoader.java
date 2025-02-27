package casaart.emails_clients_db;

import casaart.emails_clients_db.service.EditDataBaseService;
import casaart.emails_clients_db.service.ExcelService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ExcelDataLoader implements CommandLineRunner {

    private final ExcelService excelService;

    private final EditDataBaseService editDataBaseService;

    public ExcelDataLoader(ExcelService excelService, EditDataBaseService editDataBaseService) {
        this.excelService = excelService;
        this.editDataBaseService = editDataBaseService;
    }

    @Override
    public void run(String... args) {
        String filePath = "C:\\Users\\user\\Documents\\IP\\demo.xlsx"; // Път до локалния Excel файл

//        editDataBaseService.removeDuplicateClients();
//        editDataBaseService.updateAllClientNames();
//        editDataBaseService.exportClientsToExcel(filePath);

//        System.out.println("Започва импорт на клиенти от " + filePath);
        excelService.importClientsFromExcel(filePath);
//        System.out.println("Импортът приключи!");
    }
}
