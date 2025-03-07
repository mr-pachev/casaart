package casaart.emails_clients_db.service.impl;

import casaart.emails_clients_db.model.entity.Client;
import casaart.emails_clients_db.model.enums.LoyaltyLevel;
import casaart.emails_clients_db.model.enums.Nationality;
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
import java.util.*;

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
            String[] headers = {"First Name", "Middle Name", "Last Name", "Phone Number", "Email", "Source Type", "Loyalty Level", "Modify From", "Acc Date", "Nat", "First Call", "First Email", "Second Call", "Second Email"};
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
                row.createCell(9).setCellValue(client.getNationality() != null ? client.getNationality().toString() : "");
                row.createCell(10).setCellValue(client.getFirstCall() != null ? client.getFirstCall().toString() : "");
                row.createCell(12).setCellValue(client.getFirstEmail() != null ? client.getFirstEmail().toString() : "");
                row.createCell(13).setCellValue(client.getSecondCall() != null ? client.getSecondCall().toString() : "");
                row.createCell(14).setCellValue(client.getSecondEmail() != null ? client.getSecondEmail().toString() : "");
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
        List<Client> newClients = new ArrayList<>();
        int counterUpdated = 0;

        try (FileInputStream fis = new FileInputStream(new File(filePath));
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;

                // Apache POI използва индекси от 0, затова взимаме колоните коректно:
                Cell accommodationDateCell = row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);    // A
                Cell sourceTypeCell = row.getCell(1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);           // B
                Cell loyaltyLevelCell = row.getCell(2, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);         // C
                Cell firstNameCell = row.getCell(3, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);            // D
                Cell middleNameCell = row.getCell(4, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);           // E
                Cell lastNameCell = row.getCell(5, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);             // F
                Cell phoneCell = row.getCell(6, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);                // G
                Cell emailCell = row.getCell(7, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);                // H
                Cell nationalityCell = row.getCell(8, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);          // I

                // Преобразуване на стойностите в string
                String accommodationDateStr = getCellValueAsString(accommodationDateCell);
                String sourceType = getCellValueAsString(sourceTypeCell);
                String loyaltyLevel = getCellValueAsString(loyaltyLevelCell);
                String firstName = formatName(getCellValueAsString(firstNameCell));
                String middleName = formatName(getCellValueAsString(middleNameCell));
                String lastName = formatName(getCellValueAsString(lastNameCell));
                String phoneNumber = getCellValueAsString(phoneCell);
                String email = getCellValueAsString(emailCell);
                String nationalityStr = getCellValueAsString(nationalityCell);

                if (firstName.isEmpty() || email.isEmpty()) continue;

                // Обработка на датата за настаняване
                LocalDate accommodationDate = accommodationDateStr.isEmpty() ? null : mapper.map(accommodationDateStr, LocalDate.class);
                Nationality nationality = null;
                if (!nationalityStr.isEmpty()) {
                    try {
                        nationality = Nationality.valueOf(nationalityStr);
                    } catch (IllegalArgumentException e) {
                        System.err.println("INVALID NATIONALITY: " + nationalityStr);
                    }
                }

                // Обработка на имената, когато са въведени в колона firstName
                if (firstName.split("\\s+").length > 1) {
                    if (firstName.trim().isEmpty()) {
                        return;
                    }

                    String[] nameParts = firstName.trim().split("\\s+");

                    if (nameParts.length == 1) {
                        firstName = (nameParts[0]);
                    } else if (nameParts.length == 2) {
                        firstName = (nameParts[0]);
                        lastName = (nameParts[1]);
                    } else {
                        firstName = (nameParts[0]);
                        lastName = (nameParts[nameParts.length - 1]);
                        middleName = (String.join(" ", Arrays.copyOfRange(nameParts, 1, nameParts.length - 1)));
                    }
                }

                Client existingClient = clientRepository
                        .findByFirstNameAndLastNameAndEmail(firstName, lastName, email)
                        .orElse(null);

                // Проверка дали ще се обновява съществуващ клиент или ще се създава нов
                if (existingClient != null) {
                    counterUpdated++;
                    // Обновяване само на липсващи данни
                    if (accommodationDate != null) {
                        if (existingClient.getAccommodationDate() == null) {
                            existingClient.setAccommodationDate(accommodationDate);
                        } else if (!existingClient.getAccommodationDate().equals(accommodationDate)) {
                            existingClient.setCounterStay(existingClient.getCounterStay() + 1);
                            if (existingClient.getCounterStay() >= 20) {
                                existingClient.setLoyaltyLevel(LoyaltyLevel.LEVEL_3);
                            } else if (existingClient.getCounterStay() >= 10) {
                                existingClient.setLoyaltyLevel(LoyaltyLevel.LEVEL_2);
                            }
                        }
                    }

                    if (existingClient.getLoyaltyLevel() == null && !loyaltyLevel.isEmpty()) {
                        existingClient.setLoyaltyLevel(LoyaltyLevel.valueOf(loyaltyLevel));
                    }

                    if (existingClient.getPhoneNumber() == null && !phoneNumber.isEmpty()) {
                        existingClient.setPhoneNumber(phoneNumber);
                    }

                    if (existingClient.getNationality() == null && nationality != null) {
                        existingClient.setNationality(nationality);
                    }

                    if (firstName.split("\\s+").length > 1) {
                        splitName(existingClient, firstName);
                    } else {
                        existingClient.setFirstName(firstName);
                        existingClient.setMiddleName(!middleName.isEmpty() ? middleName : null);
                    }
                    clientRepository.save(existingClient);

                } else {
                    // Създаване на нов клиент
                    Client newClient = new Client();
                    newClient.setUser(userRepository.findById(1));
                    newClient.setSourceType(sourceType.isEmpty() ? null : SourceType.valueOf(sourceType));
                    newClient.setLoyaltyLevel(loyaltyLevel.isEmpty() ? null : LoyaltyLevel.valueOf(loyaltyLevel));
                    newClient.setNationality(nationality);
                    newClient.setEmail(email);
                    newClient.setPhoneNumber(formatPhoneNumber(phoneNumber));
                    newClient.setCreatedAt(LocalDateTime.now());
                    newClient.setAccommodationDate(accommodationDate);
                    newClient.setCounterStay(loyaltyLevel.equals("LEVEL_1") ? 1 : 0);

                    if (firstName.split("\\s+").length > 1) {
                        splitName(newClient, firstName);
                    } else {
                        newClient.setFirstName(firstName);
                        newClient.setMiddleName(!middleName.isEmpty() ? middleName : null);
                        newClient.setLastName(lastName);
                    }

                    boolean existsInList = newClients.stream().anyMatch(c ->
                            Objects.equals(c.getFirstName(), newClient.getFirstName()) &&
                                    Objects.equals(c.getLastName(), newClient.getLastName()) ||
                                    Objects.equals(c.getEmail(), newClient.getEmail()));


                    // Проверка дали новосъздадения клиент вече съществува в базата
                    if (!existsInList &&
                            !email.endsWith("@guest.booking.com") &&
                            !email.endsWith("@m.expediapartnercentral.com") &&
                            clientRepository.findByEmail(email).isEmpty()) {
                        newClients.add(newClient);
                    }
                }
            }

            clientRepository.saveAll(newClients);
            System.out.println("SUCCESSFULLY ADDED --< " + newClients.size() + " >-- new clients in the database.");
            System.out.println("SUCCESSFULLY UPDATED --< " + counterUpdated + " >-- clients from the database.");
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
        name = name.toLowerCase().trim();

        // Премахване на " - Loyalty 1", ако се съдържа
        name = name.replace(" - loyalty 1", "");

        // Премахване на всичко след " / "
        if (name.contains(" / ")) {
            name = name.substring(0, name.indexOf(" / ")).trim();
        }

        // Премахване на излишни интервали около тирета
        name = name.replaceAll("\\s*-\\s*", "-");

        String[] words = name.split("\\s+");
        StringBuilder formattedName = new StringBuilder();

        for (String word : words) {
            if (!word.isEmpty()) {
                // Запазваме тирето в думата
                String[] hyphenParts = word.split("-");
                StringBuilder formattedWord = new StringBuilder();

                for (int i = 0; i < hyphenParts.length; i++) {
                    if (!hyphenParts[i].isEmpty()) {
                        formattedWord.append(Character.toUpperCase(hyphenParts[i].charAt(0)))
                                .append(hyphenParts[i].substring(1));
                    }
                    if (i < hyphenParts.length - 1) {
                        formattedWord.append("-"); // Запазваме тирето
                    }
                }

                formattedName.append(formattedWord).append(" ");
            }
        }

        return formattedName.toString().trim();
    }




    // Разделяне на името
    private void splitName(Client client, String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            return;
        }

        // Премахване на излишни интервали около тирета
        fullName = fullName.replaceAll("\\s*-\\s*", "-").trim();

        String[] nameParts = fullName.split("\\s+");

        if (nameParts.length == 1) {
            client.setFirstName(nameParts[0]);
            client.setMiddleName(null);
            client.setLastName(null);
        } else if (nameParts.length == 2) {
            client.setFirstName(nameParts[0]);
            client.setMiddleName(null);
            client.setLastName(nameParts[1]);
        } else {
            client.setFirstName(nameParts[0]);
            client.setLastName(nameParts[nameParts.length - 1]);
            client.setMiddleName(String.join(" ", Arrays.copyOfRange(nameParts, 1, nameParts.length - 1)));
        }
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
