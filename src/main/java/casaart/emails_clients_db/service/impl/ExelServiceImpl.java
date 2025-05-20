package casaart.emails_clients_db.service.impl;

import casaart.emails_clients_db.model.entity.Client;
import casaart.emails_clients_db.model.entity.Company;
import casaart.emails_clients_db.model.entity.CompanyManager;
import casaart.emails_clients_db.model.entity.ContactPerson;
import casaart.emails_clients_db.model.enums.IndustryType;
import casaart.emails_clients_db.model.enums.LoyaltyLevel;
import casaart.emails_clients_db.model.enums.Nationality;
import casaart.emails_clients_db.model.enums.SourceType;
import casaart.emails_clients_db.repository.*;
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
import java.util.stream.Collectors;

@Service
public class ExelServiceImpl implements ExelService {
    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final CompanyManagerRepository companyManagerRepository;
    private final ContactPersonRepository contactPersonRepository;

    private final ModelMapper mapper;

    private final UserHelperService userHelperService;

    public ExelServiceImpl(ClientRepository clientRepository, UserRepository userRepository, CompanyRepository companyRepository, CompanyManagerRepository companyManagerRepository, ContactPersonRepository contactPersonRepository, ModelMapper mapper, UserHelperService userHelperService) {
        this.clientRepository = clientRepository;
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.companyManagerRepository = companyManagerRepository;
        this.contactPersonRepository = contactPersonRepository;
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
            String[] headers = {"First Name", "Middle Name", "Last Name", "Phone Number", "Email", "Source Type", "Loyalty Level", "Counter stay", "Modify From", "Acc Date", "Nat", "First Call", "First Email", "Second Call", "Second Email", "Rating Food", "Rating Quality Price", "Rating Politeness", "Rating Clean Tidy", "User", "Comment"};

            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Попълване на данните
            int rowNum = 1;
            for (Client client : clients) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(client.getFirstName() != null ? client.getFirstName() : "");
                row.createCell(1).setCellValue(client.getMiddleName() != null ? client.getMiddleName() : "");
                row.createCell(2).setCellValue(client.getLastName() != null ? client.getLastName() : "");
                row.createCell(3).setCellValue(client.getPhoneNumber() != null ? client.getPhoneNumber() : "");
                row.createCell(4).setCellValue(client.getEmail() != null ? client.getEmail() : "");
                row.createCell(5).setCellValue(client.getSourceTypes() != null ? client.getSourceTypes().stream().map(SourceType::toString).collect(Collectors.joining(", ")) : "");
                row.createCell(6).setCellValue(client.getLoyaltyLevel() != null ? client.getLoyaltyLevel().toString() : "");
                row.createCell(7).setCellValue(client.getCounterStay() != null ? client.getCounterStay().toString() : "");
                row.createCell(8).setCellValue(client.getModifyFrom() != null ? client.getModifyFrom() : "");
                row.createCell(9).setCellValue(client.getAccommodationDate() != null ? client.getAccommodationDate().toString() : "");
                row.createCell(10).setCellValue(client.getNationality() != null ? client.getNationality().toString() : "");
                row.createCell(11).setCellValue(client.getFirstCall() != null ? client.getFirstCall().toString() : "");
                row.createCell(12).setCellValue(client.getFirstEmail() != null ? client.getFirstEmail().toString() : "");
                row.createCell(13).setCellValue(client.getSecondCall() != null ? client.getSecondCall().toString() : "");
                row.createCell(14).setCellValue(client.getSecondEmail() != null ? client.getSecondEmail().toString() : "");
                row.createCell(15).setCellValue(client.getRatingFood() != null ? client.getRatingFood().toString() : "");
                row.createCell(16).setCellValue(client.getRatingQualityPrice() != null ? client.getRatingQualityPrice().toString() : "");
                row.createCell(17).setCellValue(client.getRatingPoliteness() != null ? client.getRatingPoliteness().toString() : "");
                row.createCell(18).setCellValue(client.getRatingCleanTidy() != null ? client.getRatingCleanTidy().toString() : "");
                row.createCell(19).setCellValue(client.getUser() != null ? client.getUser().getUsername() : "");
                row.createCell(20).setCellValue(client.getComment() != null ? client.getComment() : "");
            }

            // Автоматично нагласяне на ширината на колоните
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Запис в файл
            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
            }
            System.out.println("SUCCESSFULLY EXPORTED --< " + clients.size() + " >-- clients IN " + filePath);

        } catch (IOException e) {
            System.err.println("ERROR WRITING Excel file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // export companies to exel
    @Override
    public void exportCompaniesToExcel(String filePath) {
        List<Company> companies = companyRepository.findAll();

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Companies");

            // Заглавен ред
            Row headerRow = sheet.createRow(0);
            String[] headers = {"Name", "Address", "Phone Number", "Email", "URL", "Location Type", "Company Type",
                    "Industry Types", "Unit Types", "Partner Types", "Company Manager", "Contact Persons"};

            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Попълване на данните
            int rowNum = 1;
            for (Company company : companies) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(company.getName() != null ? company.getName() : "");
                row.createCell(1).setCellValue(company.getAddress() != null ? company.getAddress() : "");
                row.createCell(2).setCellValue(company.getPhoneNumber() != null ? company.getPhoneNumber() : "");
                row.createCell(3).setCellValue(company.getEmail() != null ? company.getEmail() : "");
                row.createCell(4).setCellValue(company.getUrl() != null ? company.getUrl() : "");
                row.createCell(5).setCellValue(company.getLocationType() != null ? company.getLocationType().toString() : "");
                row.createCell(6).setCellValue(company.getCompanyType() != null ? company.getCompanyType().toString() : "");
                row.createCell(7).setCellValue(company.getIndustryTypes() != null ? company.getIndustryTypes().stream().map(Enum::toString).collect(Collectors.joining(", ")) : "");
                row.createCell(8).setCellValue(company.getUnitTypes() != null ? company.getUnitTypes().stream().map(Enum::toString).collect(Collectors.joining(", ")) : "");
                row.createCell(9).setCellValue(company.getPartnerTypes() != null ? company.getPartnerTypes().stream().map(Enum::toString).collect(Collectors.joining(", ")) : "");
                row.createCell(10).setCellValue(company.getCompanyManager() != null ? company.getCompanyManager().getFullName() : "");
                row.createCell(11).setCellValue(company.getContactPersons() != null ? company.getContactPersons().stream().map(ContactPerson::getFullName).collect(Collectors.joining(", ")) : "");
            }

            // Автоматично нагласяне на ширината на колоните
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Запис в файл
            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
            }
            System.out.println("SUCCESSFULLY EXPORTED --< " + companies.size() + " >-- companies IN " + filePath);
        } catch (IOException e) {
            System.err.println("ERROR WRITING Excel file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // export companyManagers to exel
    @Override
    public void exportCompanyManagersToExcel(String filePath) {
        List<CompanyManager> managers = companyManagerRepository.findAll();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Company Managers");

            // Заглавен ред
            String[] headers = {"First Name", "Middle Name", "Last Name", "Email", "Phone Number", "Company", "First Call", "Send Email", "Send Letter", "Second Call", "Presence"};
            Row headerRow = sheet.createRow(0);
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Попълване на данните
            int rowNum = 1;
            for (CompanyManager manager : managers) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(manager.getFirstName() != null ? manager.getFirstName() : "");
                row.createCell(1).setCellValue(manager.getMiddleName() != null ? manager.getMiddleName() : "");
                row.createCell(2).setCellValue(manager.getLastName() != null ? manager.getLastName() : "");
                row.createCell(3).setCellValue(manager.getEmail() != null ? manager.getEmail() : "");
                row.createCell(4).setCellValue(manager.getPhoneNumber() != null ? manager.getPhoneNumber() : "");
                row.createCell(5).setCellValue(manager.getCompany() != null ? manager.getCompany().getName() : "");
                row.createCell(6).setCellValue(manager.getFirstCall() != null ? manager.getFirstCall().format(formatter) : "");
                row.createCell(7).setCellValue(manager.getSendEmail() != null ? manager.getSendEmail().format(formatter) : "");
                row.createCell(8).setCellValue(manager.getSendLetter() != null ? manager.getSendLetter().format(formatter) : "");
                row.createCell(9).setCellValue(manager.getSecondCall() != null ? manager.getSecondCall().format(formatter) : "");
                row.createCell(10).setCellValue(manager.getPresence() != null ? manager.getPresence().format(formatter) : "");
            }

            // Автоматично нагласяне на ширината на колоните
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Запис в файл
            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
            }

            System.out.println("SUCCESSFULLY EXPORTED --< " + managers.size() + " >-- company managers IN " + filePath);

        } catch (IOException e) {
            System.err.println("ERROR WRITING Excel file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // export contactPersons to exel
    @Override
    public void exportContactPersonsToExcel(String filePath) {
        List<ContactPerson> contactPersons = contactPersonRepository.findAll();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Contact Persons");

            // Заглавен ред
            Row headerRow = sheet.createRow(0);
            String[] headers = {"First Name", "Middle Name", "Last Name", "Email", "Phone Number", "Company",
                    "First Call", "Send Email", "Send Letter", "Second Call", "Presence"};

            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Попълване на данните
            int rowNum = 1;
            for (ContactPerson person : contactPersons) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(person.getFirstName() != null ? person.getFirstName() : "");
                row.createCell(1).setCellValue(person.getMiddleName() != null ? person.getMiddleName() : "");
                row.createCell(2).setCellValue(person.getLastName() != null ? person.getLastName() : "");
                row.createCell(3).setCellValue(person.getEmail() != null ? person.getEmail() : "");
                row.createCell(4).setCellValue(person.getPhoneNumber() != null ? person.getPhoneNumber() : "");
                row.createCell(5).setCellValue(person.getCompany() != null ? person.getCompany().getName() : "");
                row.createCell(6).setCellValue(person.getFirstCall() != null ? person.getFirstCall().format(dateFormatter) : "");
                row.createCell(7).setCellValue(person.getSendEmail() != null ? person.getSendEmail().format(dateFormatter) : "");
                row.createCell(8).setCellValue(person.getSendLetter() != null ? person.getSendLetter().format(dateFormatter) : "");
                row.createCell(9).setCellValue(person.getSecondCall() != null ? person.getSecondCall().format(dateFormatter) : "");
                row.createCell(10).setCellValue(person.getPresence() != null ? person.getPresence().format(dateFormatter) : "");
            }

            // Автоматично нагласяне на ширината на колоните
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Запис в файл
            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
            }
            System.out.println("SUCCESSFULLY EXPORTED --< " + contactPersons.size() + " >-- contact persons IN " + filePath);
        } catch (IOException e) {
            System.err.println("ERROR WRITING Excel file: " + e.getMessage());
            e.printStackTrace();
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

                // Филтър за пропускане на редове
                if (firstName.isEmpty() || email.isEmpty()) continue;

                // Обработка на датата за настаняване
                LocalDate accommodationDate = accommodationDateStr.isEmpty() ? null : mapper.map(accommodationDateStr, LocalDate.class);

                // Обработка на колона националност
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

                // Проверка дали съществува въведения клиент в базата данни
                Client existingClient = clientRepository
                        .findByFirstNameAndLastNameAndEmail(firstName, lastName, email)
                        .orElse(null);

                // Проверка дали ще се обновява съществуващ клиент или ще се създава нов
                if (existingClient != null) {
                    counterUpdated++;

                    // Настройка на дата настаняване
                    if (accommodationDate != null) {

                        // Проверка има ли въведена дата на настаняване
                        if (existingClient.getAccommodationDate() == null) {
                            existingClient.setCounterStay(1);

                        } else if (!existingClient.getAccommodationDate().equals(accommodationDate)) {
                            existingClient.setCounterStay(existingClient.getCounterStay() + 1);

                            // Автоматична настройка ниво лоялност
                            if (existingClient.getCounterStay() >= 20) {
                                existingClient.setLoyaltyLevel(LoyaltyLevel.LEVEL_3);
                            } else if (existingClient.getCounterStay() >= 10) {
                                existingClient.setLoyaltyLevel(LoyaltyLevel.LEVEL_2);
                            }
                        }

                        existingClient.setAccommodationDate(accommodationDate);
                    }

                    // Настройка на лоялност
                    if (existingClient.getLoyaltyLevel() == null && !loyaltyLevel.isEmpty()) {
                        existingClient.setLoyaltyLevel(LoyaltyLevel.valueOf(loyaltyLevel));
                    }

                    // Настройка телефонен номер
                    if (existingClient.getPhoneNumber() == null && !phoneNumber.isEmpty()) {
                        existingClient.setPhoneNumber(phoneNumber);
                    }

                    // Настройка националност
                    if (nationality != null) {
                        existingClient.setNationality(nationality);

                    }else if(existingClient.getNationality() == null){
                        existingClient.setNationality(Nationality.BG);
                    }

                    // Настройка имена
                    existingClient.setFirstName(firstName);
                    existingClient.setMiddleName(!middleName.isEmpty() ? middleName : null);
                    existingClient.setLastName(lastName);

                    // Настройка на изтобник клиент
                    SourceType importSourceType = SourceType.valueOf(sourceType);
                    if (!existingClient.getSourceTypes().contains(importSourceType)){
                        List<SourceType> updatedSourceTypes = existingClient.getSourceTypes();

                        updatedSourceTypes.add(importSourceType);

                        existingClient.setSourceTypes(updatedSourceTypes);
                    }

                    clientRepository.save(existingClient);

                } else {
                    // Създаване на нов клиент
                    Client newClient = new Client();
                    newClient.setUser(userRepository.findById(1));
                    newClient.setSourceTypes(
                            sourceType.isEmpty() ? Collections.emptyList() :
                                    Arrays.stream(sourceType.split(","))
                                            .map(String::trim) // Премахване на излишни интервали
                                            .map(SourceType::valueOf) // Преобразуване в SourceType
                                            .collect(Collectors.toList())
                    );
                    newClient.setLoyaltyLevel(loyaltyLevel.isEmpty() ? null : LoyaltyLevel.valueOf(loyaltyLevel));
                    newClient.setNationality(nationality);
                    newClient.setEmail(email);
                    newClient.setPhoneNumber(formatPhoneNumber(phoneNumber));
                    newClient.setCreatedAt(LocalDateTime.now());
                    newClient.setAccommodationDate(accommodationDate);

                    if (accommodationDate != null) {
                        newClient.setCounterStay(1);
                    }

                    newClient.setFirstName(firstName);
                    newClient.setMiddleName(!middleName.isEmpty() ? middleName : null);
                    newClient.setLastName(lastName);

                    // Проверка дали клиента съществува няколко пъти в екселската таблица
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
            System.err.println("ERROR READING EXEL FILE: " + e.getMessage());
        }
    }

    /* matching emails from mailchimp - 1/2 */
    // export duplicated emails to Excel
    @Override
    public void exportDuplicatedEmailsToExcel(Set<String> duplicatedEmails, String outputFilePath) {
        try (Workbook workbook = new XSSFWorkbook(); FileOutputStream fos = new FileOutputStream(outputFilePath)) {
            Sheet sheet = workbook.createSheet("Duplicated Emails");

            int rowNum = 0;
            for (String email : duplicatedEmails) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(email);
            }

            workbook.write(fos);
            System.out.println("SUCCESSFULLY EXPORTED ---< " + duplicatedEmails.size() + " >-- DUPLICATED EMAILS.");
        } catch (IOException e) {
            System.err.println("Error writing Excel file: " + e.getMessage());
        }
    }

    /* matching emails from mailchimp - 2/2 */
    // find duplicated emails from Excel
    @Override
    public void findDuplicatedEmailsFromExcel(String inputFilePath, String outputFilePath) {
        Map<String, Integer> emailCountMap = new HashMap<>();

        try (FileInputStream fis = new FileInputStream(new File(inputFilePath));
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Пропускаме заглавния ред

                Cell emailCell = row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                String email = emailCell.toString().trim();

                if (!email.isEmpty()) {
                    emailCountMap.put(email, emailCountMap.getOrDefault(email, 0) + 1);
                }
            }

            // Филтрираме само имейлите, които се повтарят поне два пъти
            Set<String> duplicatedEmails = emailCountMap.entrySet().stream()
                    .filter(entry -> entry.getValue() >= 2)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toSet());

            exportDuplicatedEmailsToExcel(duplicatedEmails, outputFilePath);
        } catch (IOException e) {
            System.err.println("ERROR READING EXCEL FILE: " + e.getMessage());
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

    // Обработка на имената на ниво String
    private String formatName(String name) {
        if (name == null || name.isEmpty()) {
            return "";
        }
        name = name.toLowerCase().trim();

        // Премахване на " - loyalty 1" независимо от разстоянията
        name = name.replaceAll("\\s*-\\s*loyalty\\s*1\\s*", "");

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
