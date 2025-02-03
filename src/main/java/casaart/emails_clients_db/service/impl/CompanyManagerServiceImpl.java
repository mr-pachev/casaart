package casaart.emails_clients_db.service.impl;

import casaart.emails_clients_db.model.dto.PersonDTO;
import casaart.emails_clients_db.repository.CompanyManagerRepository;
import casaart.emails_clients_db.service.CompanyManagerService;
import org.springframework.stereotype.Service;

@Service
public class CompanyManagerServiceImpl implements CompanyManagerService {
    private final CompanyManagerRepository companyManagerRepository;

    public CompanyManagerServiceImpl(CompanyManagerRepository companyManagerRepository) {
        this.companyManagerRepository = companyManagerRepository;
    }

    // check is exist company manager
    @Override
    public boolean isExistCompanyManager(PersonDTO personDTO) {
        return companyManagerRepository.findByFirstNameAndLastNameAndPhoneNumber(personDTO.getFirstName(),
                personDTO.getLastName(),
                personDTO.getPhoneNumber()).isPresent();
    }
}
