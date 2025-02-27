package casaart.emails_clients_db.service;

import casaart.emails_clients_db.model.entity.Client;
import casaart.emails_clients_db.model.enums.LoyaltyLevel;
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
                Cell loyaltyLevelCell = row.getCell(3, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);  // D
                Cell firstNameCell = row.getCell(4, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);  // E
                Cell middleNameCell = row.getCell(5, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK); // F
                Cell lastNameCell = row.getCell(6, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);   // G
                Cell emailCell = row.getCell(7, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);      // H
                Cell phoneCell = row.getCell(8, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);      // I

                String sourceType = getCellValueAsString(sourceTypeCell);
                String loyaltyLevel = getCellValueAsString(loyaltyLevelCell);
                String firstName = formatName(getCellValueAsString(firstNameCell));
                String middleName = formatName(getCellValueAsString(middleNameCell));
                String lastName = formatName(getCellValueAsString(lastNameCell));
                String email = getCellValueAsString(emailCell);
                String phoneNumber = getCellValueAsString(phoneCell);

                // Проверка за празни редове
                if (firstName.isEmpty()) {
                    continue;
                }

                // Проверка за празни редове
                if (firstName.isEmpty() && lastName.isEmpty() && email.isEmpty()) {
                    continue;
                }

                // Внасяне на нови клиенти
//                Client client = new Client();
//                client.setUser(userRepository.findById(1));
//                client.setSourceType(SourceType.valueOf(sourceType));
//
//                // Разделяне на firstName, ако съдържа две думи
//                if (firstName != null && firstName.trim().contains(" ")) {
//                    String[] nameParts = firstName.trim().split("\\s+", 2); // Разделяме по първия интервал
//                    client.setFirstName(nameParts[0]); // Първата дума -> firstName
//                    client.setLastName(nameParts[1]); // Втората дума -> lastName
//                } else {
//                    client.setFirstName(firstName);
//                    client.setLastName(lastName);
//                }
//
//                client.setMiddleName(middleName.isEmpty() ? null : middleName);
//                client.setEmail(email.isEmpty() ? null : email);
//
//                client.setPhoneNumber(formatPhoneNumber(phoneNumber));
//                client.setCreatedAt(LocalDateTime.now());
//
//                // Проверка за съществуващ клиент в базата
//                boolean existsInDatabase = clientRepository
//                        .findByFirstNameAndLastNameAndEmail(client.getFirstName(), client.getLastName(), client.getEmail())
//                        .isPresent();
//
//                // Проверка за съществуващ клиент в списъка clients
//                boolean existsInList = clients.stream().anyMatch(c ->
//                        c.getFirstName().equals(client.getFirstName()) &&
//                                c.getLastName().equals(client.getLastName()) &&
//                                Objects.equals(c.getEmail(), client.getEmail()));
//
//                // Добавяне само ако клиентът не съществува нито в базата, нито в списъка
//                if (!existsInDatabase && !existsInList && client.getEmail() != null) {
//                    clients.add(client);
//                }


                // Обновяване на loyaltyLevel/добавяне на клиент с loyaltyLevel
                Client existingClient = clientRepository
                        .findByFirstNameAndLastNameAndEmail(firstName, lastName, email)
                        .orElse(null);

                if (existingClient != null) {
                    existingClient.setLoyaltyLevel(LoyaltyLevel.valueOf(loyaltyLevel));

                    if (existingClient.getPhoneNumber() == null) {
                        existingClient.setPhoneNumber(phoneNumber);
                    }

                    clientRepository.save(existingClient);
                } else {
                    Client newClient = new Client();
                    newClient.setUser(userRepository.findById(1));
                    newClient.setSourceType(SourceType.valueOf(sourceType));

                    // Разделяне на firstName, ако съдържа две думи
                    if (firstName != null && firstName.trim().contains(" ")) {
                        String[] nameParts = firstName.trim().split("\\s+", 2); // Разделяме по първия интервал
                        newClient.setFirstName(nameParts[0]); // Първата дума -> firstName
                        newClient.setLastName(nameParts[1]); // Втората дума -> lastName
                    } else {
                        newClient.setFirstName(firstName);
                        newClient.setLastName(lastName);
                    }

                    newClient.setMiddleName(middleName.isEmpty() ? null : middleName);
                    newClient.setEmail(email);
                    newClient.setPhoneNumber(formatPhoneNumber(phoneNumber));
                    newClient.setCreatedAt(LocalDateTime.now());
                    newClient.setLoyaltyLevel(LoyaltyLevel.valueOf(loyaltyLevel));

                    // Проверка за съществуващ клиент в списъка clients
                    boolean existsInList = clients.stream().anyMatch(c ->
                            c.getFirstName().equals(newClient.getFirstName()) &&
                                    c.getLastName().equals(newClient.getLastName()) &&
                                    Objects.equals(c.getEmail(), newClient.getEmail()));

                    if (!existsInList) {
                        clients.add(newClient);
                    }
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
    String formatPhoneNumber(String phoneNumber) {
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
}
