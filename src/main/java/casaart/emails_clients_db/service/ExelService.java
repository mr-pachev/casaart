package casaart.emails_clients_db.service;

import casaart.emails_clients_db.model.entity.Client;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

public interface ExelService {

    // export clients to exel
   void exportClientsToExel(Workbook workbook, List<Long> clientIds);

    // export all companies to exel to HDD
    void exportCompaniesToExcel(String filePath);

    // export all companies to exel to browser
    void populateWorkbookWithCompanies(Workbook workbook);

    // export suppliers to exel
    void exportSuppliersToExel(Workbook workbook, List<Long> suppliersIds);

    // export partners to exel
    void exportPartnersToExel(Workbook workbook, List<Long> partnersIds);

    // export companyManagers to exel
    void exportCompanyManagersToExel(Workbook workbook, List<Long> companyManagerIds);

    // export contactPersons to exel
    void exportContactPersonsToExel(Workbook workbook, List<Long> contactPersonIds);

    // import clients
    void updateOrAddLoyaltyLevel(MultipartFile file);
}
