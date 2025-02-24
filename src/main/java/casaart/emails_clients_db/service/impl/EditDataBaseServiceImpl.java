package casaart.emails_clients_db.service.impl;

import casaart.emails_clients_db.model.entity.Client;
import casaart.emails_clients_db.model.enums.LoyaltyLevel;
import casaart.emails_clients_db.model.enums.SourceType;
import casaart.emails_clients_db.repository.ClientRepository;
import casaart.emails_clients_db.service.EditDataBaseService;
import jakarta.transaction.Transactional;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EditDataBaseServiceImpl implements EditDataBaseService {
    private final ClientRepository clientRepository;

    public EditDataBaseServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    @Transactional
    public void removeDuplicateClients() {

        List<Client> allClients = clientRepository.findAll();
        allClients.stream()
                .collect(Collectors.groupingBy(c -> c.getFirstName() + "|" + c.getLastName() + "|" + c.getEmail()))
                .values()
                .forEach(duplicates -> {
                    if (duplicates.size() > 1) {
                        duplicates.stream().skip(1).forEach(clientRepository::delete);
                    }
                });
    }

    // update all client names
    @Override
    public void updateAllClientNames() {
        List<Client> allClients = clientRepository.findAll(); // Извличаме всички клиенти

        for (Client client : allClients) {
            client.setFirstName(formatName(client.getFirstName()));
            client.setMiddleName(formatName(client.getMiddleName()));
            client.setLastName(formatName(client.getLastName()));
        }

        clientRepository.saveAll(allClients); // Запазваме обновените клиенти
        System.out.println("Обновени са " + allClients.size() + " клиента.");
    }

    // export clients to exel
    @Override
    public void exportClientsToExcel(String filePath) {
        List<Client> clients = clientRepository.findAll();
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Clients");

            // Заглавен ред
            Row headerRow = sheet.createRow(0);
            String[] headers = {"First Name", "Middle Name", "Last Name", "Company Name", "Email", "Phone Number", "Source Type", "Loyalty Level", "Modify From", "First Call", "First Email", "Second Call", "Second Email"};
            for (int i = 0; i < headers.length; i++) {
                headerRow.createCell(i).setCellValue(headers[i]);
            }

            // Попълване на данните
            int rowNum = 1;
            for (Client client : clients) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(client.getFirstName());
                row.createCell(1).setCellValue(client.getMiddleName());
                row.createCell(2).setCellValue(client.getLastName());
                row.createCell(3).setCellValue(client.getCompanyName());
                row.createCell(4).setCellValue(client.getEmail());
                row.createCell(5).setCellValue(client.getPhoneNumber());
                row.createCell(6).setCellValue(client.getSourceType().toString());
                row.createCell(7).setCellValue(client.getLoyaltyLevel().toString());
                row.createCell(8).setCellValue(client.getModifyFrom());
                row.createCell(9).setCellValue(client.getFirstCall() != null ? client.getFirstCall().toString() : "");
                row.createCell(10).setCellValue(client.getFirstEmail() != null ? client.getFirstEmail().toString() : "");
                row.createCell(11).setCellValue(client.getSecondCall() != null ? client.getSecondCall().toString() : "");
                row.createCell(12).setCellValue(client.getSecondEmail() != null ? client.getSecondEmail().toString() : "");
            }

            // Запис в файл
            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
            }
            System.out.println("Успешно експортирани " + clients.size() + " клиента в " + filePath);
        } catch (IOException e) {
            System.err.println("Грешка при запис в Excel файл: " + e.getMessage());
        }
    }

    // set loyalty level_1 for all clients
    @Override
    public void setLoyaltyLevel_1ForAll() {
        List<Client> allClients = clientRepository.findAll(); // Извличаме всички клиенти

        for (Client client : allClients) {
            if (client.getSourceType().equals(SourceType.HOTEL) ||
                    client.getSourceType().equals(SourceType.SHOWROOM)) {
                client.setLoyaltyLevel(LoyaltyLevel.LEVEL_1);
            }
        }
        clientRepository.saveAll(allClients); // Запазваме обновените клиенти
        System.out.println("Обновени са " + allClients.size() + " клиента.");
    }

    // Обработка на имената
    private String formatName(String name) {
        if (name == null || name.isEmpty()) {
            return "";
        }
        name = name.toLowerCase();
        String[] words = name.split("\\s+");
        StringBuilder formattedName = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) {
                formattedName.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1)).append(" ");
            }
        }
        return formattedName.toString().trim();
    }
}

