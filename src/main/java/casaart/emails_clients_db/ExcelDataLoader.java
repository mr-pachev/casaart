package casaart.emails_clients_db;

import casaart.emails_clients_db.repository.ClientRepository;
import casaart.emails_clients_db.service.ExcelService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

//@Component
public class ExcelDataLoader implements CommandLineRunner {

    private final ExcelService excelService;
    private final ClientRepository clientRepository;

    public ExcelDataLoader(ExcelService excelService, ClientRepository clientRepository) {
        this.excelService = excelService;
        this.clientRepository = clientRepository;
    }

    @Override
    public void run(String... args) {
        String filePath = "D:\\demo.xlsx"; // Път до локалния Excel файл

//        clientRepository.removeDuplicateClients();
//        excelService.updateAllClientNames();

//        excelService.exportClientsToExcel(filePath);

        System.out.println("Започва импорт на клиенти от " + filePath);
        excelService.importClientsFromExcel(filePath);
        System.out.println("Импортът приключи!");
    }
}
