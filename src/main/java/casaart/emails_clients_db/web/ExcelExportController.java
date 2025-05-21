package casaart.emails_clients_db.web;

import casaart.emails_clients_db.service.ExelService;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Controller
public class ExcelExportController {
    private final ExelService exelService;

    public ExcelExportController(ExelService exelService) {
        this.exelService = exelService;
    }

    @GetMapping("/export-clients")
    public void exportClients(HttpServletResponse response) throws IOException {
        // Подготовка на отговора за Excel файл
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        String fileName = "CLIENTS_" + LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) + ".xlsx";
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

        // Извикване на метода ти, който пише директно в OutputStream
        try (Workbook workbook = new XSSFWorkbook()) {
            exelService.populateWorkbookWithClients(workbook); // нов метод, който НЕ пише на диск
            workbook.write(response.getOutputStream());
        }
    }

    // never use
    @GetMapping("/export-all-companies")
    public void exportCompanies(HttpServletResponse response) throws IOException {
        // Подготовка на отговора за Excel файл
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        String fileName = "COMPANIES_" + LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) + ".xlsx";
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

        // Извикване на метода ти, който пише директно в OutputStream
        try (Workbook workbook = new XSSFWorkbook()) {
            exelService.populateWorkbookWithCompanies(workbook); // нов метод, който НЕ пише на диск
            workbook.write(response.getOutputStream());
        }
    }

    @GetMapping("/export-suppliers")
    public void exportSuppliers(HttpServletResponse response) throws IOException {
        // Подготовка на отговора за Excel файл
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        String fileName = "SUPPLIERS_" + LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) + ".xlsx";
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

        // Извикване на метода ти, който пише директно в OutputStream
        try (Workbook workbook = new XSSFWorkbook()) {
            exelService.populateWorkbookWithSuppliers(workbook);// нов метод, който НЕ пише на диск
            workbook.write(response.getOutputStream());
        }
    }

    @GetMapping("/export-partners")
    public void exportPartners(HttpServletResponse response) throws IOException {
        // Подготовка на отговора за Excel файл
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        String fileName = "PARTNERS_" + LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) + ".xlsx";
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

        // Извикване на метода ти, който пише директно в OutputStream
        try (Workbook workbook = new XSSFWorkbook()) {
            exelService.populateWorkbookWithPartners(workbook);// нов метод, който НЕ пише на диск
            workbook.write(response.getOutputStream());
        }
    }

    @GetMapping("/export-company-managers")
    public void exportCompanyManagers(HttpServletResponse response) throws IOException {
        // Подготовка на отговора за Excel файл
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        String fileName = "COMPANY_MANAGERS_" + LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) + ".xlsx";
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

        // Извикване на метода ти, който пише директно в OutputStream
        try (Workbook workbook = new XSSFWorkbook()) {
            exelService.populateWorkbookWithCompanyManagers(workbook); // нов метод, който НЕ пише на диск
            workbook.write(response.getOutputStream());
        }
    }

    @GetMapping("/export-contact-persons")
    public void exportContactPersons(HttpServletResponse response) throws IOException {
        // Подготовка на отговора за Excel файл
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            String fileName = "CONTACT_PERSONS_" + LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) + ".xlsx";
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

        // Извикване на метода ти, който пише директно в OutputStream
        try (Workbook workbook = new XSSFWorkbook()) {
            exelService.populateWorkbookWithCompanyManagers(workbook); // нов метод, който НЕ пише на диск
            workbook.write(response.getOutputStream());
        }
    }
}