package casaart.emails_clients_db.service.impl;

import casaart.emails_clients_db.model.dto.AddCompanyDTO;
import casaart.emails_clients_db.model.entity.Company;
import casaart.emails_clients_db.repository.CompanyRepository;
import casaart.emails_clients_db.service.CompanyService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class CompanyServiceImpl implements CompanyService {
    private final CompanyRepository companyRepository;
    private final ModelMapper mapper;

    public CompanyServiceImpl(CompanyRepository companyRepository, ModelMapper mapper) {
        this.companyRepository = companyRepository;
        this.mapper = mapper;
    }

    //checking is exist company
    @Override
    public boolean isExistCompany(String name) {
        return companyRepository.findByName(name).isPresent();
    }

    //add company
    @Override
    public void addCompany(AddCompanyDTO addCompanyDTO) {
        Company company = mapper.map(addCompanyDTO, Company.class);

        companyRepository.save(company);
    }
}
