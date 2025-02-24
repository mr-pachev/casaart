package casaart.emails_clients_db.service;

import casaart.emails_clients_db.model.entity.Client;
import casaart.emails_clients_db.model.enums.SourceType;
import casaart.emails_clients_db.repository.ClientRepository;
import casaart.emails_clients_db.repository.UserRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
                String firstName = formatName(getCellValueAsString(firstNameCell));
                String middleName = formatName(getCellValueAsString(middleNameCell));
                String lastName = formatName(getCellValueAsString(lastNameCell));
                String email = getCellValueAsString(emailCell);
                String phoneNumber = getCellValueAsString(phoneCell);

                // Проверка за празни редове
                if(firstName.isEmpty()){
                    continue;
                }

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

                client.setPhoneNumber(formatPhoneNumber(phoneNumber));
                client.setCreatedAt(LocalDateTime.now());

                // Проверка за съществуващ клиент в базата
                boolean existsInDatabase = clientRepository
                        .findByFirstNameAndLastNameAndEmail(client.getFirstName(), client.getLastName(), client.getEmail())
                        .isPresent();

                // Проверка за съществуващ клиент в списъка clients
                boolean existsInList = clients.stream().anyMatch(c ->
                        c.getFirstName().equals(client.getFirstName()) &&
                                c.getLastName().equals(client.getLastName()) &&
                                Objects.equals(c.getEmail(), client.getEmail()));

                // Добавяне само ако клиентът не съществува нито в базата, нито в списъка
                if (!existsInDatabase && !existsInList && client.getEmail() != null) {
                    clients.add(client);
                }

            }
            clientRepository.saveAll(clients);
            System.out.println("Успешно записани " + clients.size() + " клиента в базата.");
        } catch (IOException e) {
            System.err.println("Грешка при четене на Excel файла: " + e.getMessage());
        }
    }

    public void exportClientsToExcel(String filePath) {
        List<Client> clients = clientRepository.findAll();
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Clients");

            // Заглавен ред
            Row headerRow = sheet.createRow(0);
            String[] headers = {"First Name", "Middle Name", "Last Name", "Company Name", "Email", "Phone Number", "Source Type", "Modify From", "First Call", "First Email", "Second Call", "Second Email"};
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
                row.createCell(7).setCellValue(client.getModifyFrom());
                row.createCell(8).setCellValue(client.getFirstCall() != null ? client.getFirstCall().toString() : "");
                row.createCell(9).setCellValue(client.getFirstEmail() != null ? client.getFirstEmail().toString() : "");
                row.createCell(10).setCellValue(client.getSecondCall() != null ? client.getSecondCall().toString() : "");
                row.createCell(11).setCellValue(client.getSecondEmail() != null ? client.getSecondEmail().toString() : "");
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

    // Обработка на телефонния номер на клиент
    private String formatPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            return null;
        }

        // Премахване на всички интервали
        phoneNumber = phoneNumber.replaceAll("\\s+", "");

        // Замяна на +359 с 0
        if (phoneNumber.startsWith("+359")) {
            phoneNumber = "0" + phoneNumber.substring(4);
        }
        // Добавяне на 0 отпред, ако номерът започва с 8
        else if (phoneNumber.startsWith("8")) {
            phoneNumber = "0" + phoneNumber;
        }

        return phoneNumber;
    }

    // Форматиране на имената от базата данни
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
}
