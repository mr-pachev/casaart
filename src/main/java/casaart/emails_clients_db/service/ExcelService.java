package casaart.emails_clients_db.service;

import casaart.emails_clients_db.model.entity.Client;
import casaart.emails_clients_db.model.enums.SourceType;
import casaart.emails_clients_db.repository.ClientRepository;
import casaart.emails_clients_db.repository.UserRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExcelService {
    private final ClientRepository clientRepository;
    private final UserRepository userRepository;

    public ExcelService(ClientRepository clientRepository, UserRepository userRepository) {
        this.clientRepository = clientRepository;
        this.userRepository = userRepository;
    }

    public void importClientsFromExcel(String filePath) {
        List<Client> clients = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(new File(filePath));
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0); // Взимаме първия лист
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Прескачаме заглавния ред

                // Важно! Apache POI използва индексите от 0, затова:
                Cell sourceTypeCell = row.getCell(2, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);  // C
                Cell firstNameCell = row.getCell(3, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);  // D
                Cell middleNameCell = row.getCell(4, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK); // E
                Cell lastNameCell = row.getCell(5, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);   // F
                Cell emailCell = row.getCell(6, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);      // G
                Cell phoneCell = row.getCell(7, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);      // H

                String sourceType = getCellValueAsString(sourceTypeCell);
                String firstName = getCellValueAsString(firstNameCell);
                String middleName = getCellValueAsString(middleNameCell);
                String lastName = getCellValueAsString(lastNameCell);
                String email = getCellValueAsString(emailCell);
                String phoneNumber = getCellValueAsString(phoneCell);

                // Проверка за празни редове
                if(firstName.isEmpty() && lastName.isEmpty() && email.isEmpty()){
                    continue;
                }

                // Създаваме нов клиент
                Client client = new Client();
                client.setUser(userRepository.findById(1));
                client.setSourceType(SourceType.valueOf(sourceType));

                // Разделяне на firstName, ако съдържа две думи
                if (firstName != null && firstName.trim().contains(" ")) {
                    String[] nameParts = firstName.trim().split("\\s+", 2); // Разделяме по първия интервал
                    client.setFirstName(nameParts[0]); // Първата дума -> firstName
                    client.setLastName(nameParts[1]); // Втората дума -> lastName
                } else {
                    client.setFirstName(firstName);
                    client.setLastName(lastName);
                }

                client.setMiddleName(middleName.isEmpty() ? null : middleName);
                client.setEmail(email.isEmpty() ? null : email);
                client.setPhoneNumber(phoneNumber.isEmpty() ? null : phoneNumber);
                client.setCreatedAt(LocalDateTime.now());

                // Проверка за съществувващ клиент
                if(clientRepository.findByFirstNameAndLastNameAndEmail(client.getFirstName(), client.getLastName(), client.getEmail()).isEmpty()){
                    clients.add(client);
                }
            }

            clientRepository.saveAll(clients);
            System.out.println("Успешно записани " + clients.size() + " клиента в базата.");
        } catch (IOException e) {
            System.err.println("Грешка при четене на Excel файла: " + e.getMessage());
        }
    }


     // Метод за извличане на стойност от клетка като String
    private String getCellValueAsString(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString(); // Преобразува датите в текст
                } else {
                    return String.valueOf((long) cell.getNumericCellValue()).trim(); // Принудително конвертира числата
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue()).trim();
            case FORMULA:
                try {
                    return cell.getStringCellValue().trim(); // Опит за вземане на резултата като текст
                } catch (IllegalStateException e) {
                    return String.valueOf(cell.getNumericCellValue()).trim(); // Ако не е текст, взима числовата стойност
                }
            default:
                return "";
        }
    }

}
