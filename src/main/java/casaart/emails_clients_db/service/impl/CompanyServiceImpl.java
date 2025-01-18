package casaart.emails_clients_db.service.impl;

import casaart.emails_clients_db.model.dto.AddCompanyDTO;
import casaart.emails_clients_db.model.entity.Company;
import casaart.emails_clients_db.model.entity.Industry;
import casaart.emails_clients_db.repository.CompanyRepository;
import casaart.emails_clients_db.repository.IndustryRepository;
import casaart.emails_clients_db.service.CompanyService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyServiceImpl implements CompanyService {
    private final CompanyRepository companyRepository;
    private final IndustryRepository industryRepository;
    private final ModelMapper mapper;

    public CompanyServiceImpl(CompanyRepository companyRepository, IndustryRepository industryRepository, ModelMapper mapper) {
        this.companyRepository = companyRepository;
        this.industryRepository = industryRepository;
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
        List<Industry> industries = industryRepository.findAllById(addCompanyDTO.getIndustries());

        company.setIndustries(industries);
        companyRepository.save(company);
    }
}
