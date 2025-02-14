package casaart.emails_clients_db.service.impl;

import casaart.emails_clients_db.model.entity.CompanyManager;
import casaart.emails_clients_db.repository.CompanyManagerRepository;
import casaart.emails_clients_db.service.MarketingService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class MarketingServiceImpl implements MarketingService {
    private final CompanyManagerRepository companyManagerRepository;

    public MarketingServiceImpl(CompanyManagerRepository companyManagerRepository) {
        this.companyManagerRepository = companyManagerRepository;
    }

    // register first email for manager
    @Override
    public void registerFirstEmailManager(long id) {
        CompanyManager companyManager = companyManagerRepository.findById(id);
        companyManager.setFirstEmail(LocalDate.now());

        companyManagerRepository.save(companyManager);
    }
}
