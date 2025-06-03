package casaart.emails_clients_db.web;

import casaart.emails_clients_db.model.entity.Client;
import casaart.emails_clients_db.service.ExelService;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class ExcelExportController {
    private final ExelService exelService;

    public ExcelExportController(ExelService exelService) {
        this.exelService = exelService;
    }

    // export clients to exel
    @PostMapping("/export-clients")
    public void exportFilteredClients(@RequestParam("clientIds") List<Long> clientIds,
                                      HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        String fileName = "CLIENTS_" + LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) + ".xlsx";
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

        try (Workbook workbook = new XSSFWorkbook()) {
            exelService.exportClientsToExel(workbook, clientIds);
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

    // export suppliers
    @PostMapping("/export-suppliers")
    public void exportFilteredSuppliers(@RequestParam("suppliersIds") List<Long> suppliersIds,
                                      HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        String fileName = "SUPPLIERS_" + LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) + ".xlsx";
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

        try (Workbook workbook = new XSSFWorkbook()) {
            exelService.exportSuppliersToExel(workbook, suppliersIds);
            workbook.write(response.getOutputStream());
        }
    }
    // export partners
    @PostMapping("/export-partners")
    public void exportFilteredPartners(@RequestParam("partnersIds") List<Long> partnersIds,
                                        HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        String fileName = "PARTNERS_" + LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) + ".xlsx";
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

        try (Workbook workbook = new XSSFWorkbook()) {
            exelService.exportPartnersToExel(workbook, partnersIds);
            workbook.write(response.getOutputStream());
        }
    }
    // export companyManagers
    @PostMapping("/export-company-managers")
    public void exportFilteredCompanyManagers(@RequestParam("companyManagerIds") List<Long> companyManagerIds,
                                       HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        String fileName = "COMPANY_MANAGERS_" + LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) + ".xlsx";
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

        try (Workbook workbook = new XSSFWorkbook()) {
            exelService.exportCompanyManagersToExel(workbook, companyManagerIds);
            workbook.write(response.getOutputStream());
        }
    }
    // export contactPersons
    @PostMapping("/export-contact-persons")
    public void exportFilteredContactPersons(@RequestParam("contactPersonIds") List<Long> contactPersonIds,
                                              HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        String fileName = "CONTACT_PERSONS_" + LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) + ".xlsx";
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

        try (Workbook workbook = new XSSFWorkbook()) {
            exelService.exportContactPersonsToExel(workbook, contactPersonIds);
            workbook.write(response.getOutputStream());
        }
    }
    // import clients
    @PostMapping("/import-clients")
    public String importPartnersFromExcel(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            exelService.updateOrAddLoyaltyLevel(file);
            redirectAttributes.addFlashAttribute("successMessage", "Успешен импорт на партньори!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Грешка при импортиране: " + e.getMessage());
        }
        return "redirect:/clients";
    }

}