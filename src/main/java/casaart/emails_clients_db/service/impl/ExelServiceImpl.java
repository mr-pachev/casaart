package casaart.emails_clients_db.service.impl;

import casaart.emails_clients_db.model.entity.Client;
import casaart.emails_clients_db.model.enums.LoyaltyLevel;
import casaart.emails_clients_db.model.enums.SourceType;
import casaart.emails_clients_db.repository.ClientRepository;
import casaart.emails_clients_db.repository.UserRepository;
import casaart.emails_clients_db.service.ExelService;
import casaart.emails_clients_db.service.UserHelperService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Service
public class ExelServiceImpl implements ExelService {
    private final ClientRepository clientRepository;
    private final UserRepository userRepository;

    private final ModelMapper mapper;

    private final UserHelperService userHelperService;

    public ExelServiceImpl(ClientRepository clientRepository, UserRepository userRepository, ModelMapper mapper, UserHelperService userHelperService) {
        this.clientRepository = clientRepository;
        this.userRepository = userRepository;
        this.mapper = mapper;
        this.userHelperService = userHelperService;
    }

    // export clients to exel
    @Override
    public void exportClientsToExcel(String filePath) {
        List<Client> clients = clientRepository.findAll();
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Clients");

            // Заглавен ред
            Row headerRow = sheet.createRow(0);
            String[] headers = {"First Name", "Middle Name", "Last Name", "Phone Number", "Email", "Source Type", "Loyalty Level", "Modify From", "Acc Date","First Call", "First Email", "Second Call", "Second Email"};
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
                row.createCell(3).setCellValue(client.getPhoneNumber());
                row.createCell(4).setCellValue(client.getEmail());
                row.createCell(5).setCellValue(client.getSourceType().toString());
                row.createCell(6).setCellValue(client.getLoyaltyLevel() != null ? client.getLoyaltyLevel().toString() : "");
                row.createCell(7).setCellValue(client.getModifyFrom());
                row.createCell(8).setCellValue(client.getAccommodationDate() != null ? client.getAccommodationDate().toString() : "");
                row.createCell(9).setCellValue(client.getFirstCall() != null ? client.getFirstCall().toString() : "");
                row.createCell(10).setCellValue(client.getFirstEmail() != null ? client.getFirstEmail().toString() : "");
                row.createCell(11).setCellValue(client.getSecondCall() != null ? client.getSecondCall().toString() : "");
                row.createCell(12).setCellValue(client.getSecondEmail() != null ? client.getSecondEmail().toString() : "");
            }

            // Запис в файл
            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
            }
            System.out.println("SUCCESSFULLY EXPORTED --< " + clients.size() + " >-- clients IN " + filePath);
        } catch (IOException e) {
            System.err.println("ERROR READING Excel file: " + e.getMessage());
        }
    }

    // update or add loyaltyLevel on clients
    @Override
    public void updateOrAddLoyaltyLevel(String filePath) {
        List<Client> clients = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(new File(filePath));
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0); // Взимаме първия лист
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Прескачаме заглавния ред

                // Важно! Apache POI използва индексите от 0, затова:
                Cell accommodationDateCell = row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);    // A
                Cell sourceTypeCell = row.getCell(1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);           // B
                Cell loyaltyLevelCell = row.getCell(2, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);         // C
                Cell firstNameCell = row.getCell(3, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);            // D
                Cell middleNameCell = row.getCell(4, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);           // E
                Cell lastNameCell = row.getCell(5, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);             // F
                Cell phoneCell = row.getCell(6, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);                // G
                Cell emailCell = row.getCell(7, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);                // H

                String accommodationDate = getCellValueAsString(accommodationDateCell);
                String sourceType = getCellValueAsString(sourceTypeCell);
                String loyaltyLevel = getCellValueAsString(loyaltyLevelCell);
                String firstName = formatName(getCellValueAsString(firstNameCell));
                String middleName = formatName(getCellValueAsString(middleNameCell));
                String lastName = formatName(getCellValueAsString(lastNameCell));
                String phoneNumber = getCellValueAsString(phoneCell);
                String email = getCellValueAsString(emailCell);

                // Проверка за празни редове
                if (firstName.isEmpty() || email.isEmpty()) {
                    continue;
                }

                // Проверка дали клиента от таблицата съществува в базата
                Client existingClient = clientRepository
                        .findByFirstNameAndLastNameAndEmail(firstName, lastName, email)
                        .orElse(null);

                // Ако съществува клиента в базата и има попълнен loyaltyLevel в таблицата за този клиент
                if (existingClient != null && !loyaltyLevel.isEmpty()) {

                    existingClient.setLoyaltyLevel(LoyaltyLevel.valueOf(loyaltyLevel));

                    if (existingClient.getPhoneNumber() == null) {
                        existingClient.setPhoneNumber(phoneNumber);
                    }

                    clientRepository.save(existingClient);

                } else {
                    Client newClient = new Client();
                    newClient.setUser(userHelperService.getUser());
                    newClient.setSourceType(SourceType.valueOf(sourceType));

                    // Разделяне на firstName, ако съдържа две думи
                    if (firstName != null && firstName.trim().contains(" ")) {
                        String[] nameParts = firstName.trim().split("\\s+", 2); // Разделяме по първия интервал
                        newClient.setFirstName(nameParts[0]);   // Първата дума -> firstName
                        newClient.setLastName(nameParts[1]);    // Втората дума -> lastName
                    } else {
                        newClient.setFirstName(firstName);
                        newClient.setLastName(lastName);
                    }

                    newClient.setMiddleName(middleName.isEmpty() ? null : middleName);

                    newClient.setEmail(email);
                    newClient.setPhoneNumber(formatPhoneNumber(phoneNumber));
                    newClient.setCreatedAt(LocalDateTime.now());
                    newClient.setLoyaltyLevel(loyaltyLevel.isEmpty() ? null :  LoyaltyLevel.valueOf(loyaltyLevel));

                    if(!accommodationDate.isEmpty()){
                        LocalDate date = mapper.map(accommodationDate, LocalDate.class);
                        newClient.setAccommodationDate(date);
                    }

                    newClient.setAccommodationDate(null);

                    // Проверка за съществуващ клиент в списъка clients
                    boolean existsInList = clients.stream().anyMatch(c ->
                            c.getFirstName().equals(newClient.getFirstName()) &&
                                    c.getLastName().equals(newClient.getLastName()) &&
                                    Objects.equals(c.getEmail(), newClient.getEmail()));

                    if (!existsInList &&
                            newClient.getEmail() != null &&
                            clientRepository.findByEmail(email).isEmpty() &&
                            !newClient.getEmail().endsWith("@guest.booking.com") ||
                            !newClient.getEmail().endsWith("@m.expediapartnercentral.com")) {
                        clients.add(newClient);
                    }
                }

            }
            clientRepository.saveAll(clients);
            System.out.println("SUCCESSFULLY ADDED --< " + clients.size() + " >-- new clients in the database.");
        } catch (IOException e) {
            System.err.println("ERROR READING Excel file: " + e.getMessage());
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
