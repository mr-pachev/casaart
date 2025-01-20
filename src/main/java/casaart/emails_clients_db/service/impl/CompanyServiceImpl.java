package casaart.emails_clients_db.service.impl;

import casaart.emails_clients_db.model.dto.AddCompanyDTO;
import casaart.emails_clients_db.model.dto.CompanyDTO;
import casaart.emails_clients_db.model.dto.IndustryDTO;
import casaart.emails_clients_db.model.dto.PersonDTO;
import casaart.emails_clients_db.model.entity.Company;
import casaart.emails_clients_db.model.entity.ContactPerson;
import casaart.emails_clients_db.model.entity.Industry;
import casaart.emails_clients_db.repository.CompanyRepository;
import casaart.emails_clients_db.repository.IndustryRepository;
import casaart.emails_clients_db.service.CompanyService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    //get all companies
    @Override
    public List<CompanyDTO> getAllCompanies() {
        List<Company> allCompanies = companyRepository.findAllByOrderByCreatedAtDesc();
        List<CompanyDTO> companyDTOS = new ArrayList<>();

        for (Company company : allCompanies) {
            CompanyDTO companyDTO = mapCompanyToCompanyDTO(company);
            companyDTOS.add(companyDTO);
        }


        return companyDTOS;
    }

    //checking is exist company
    @Override
    public boolean isExistCompany(String name) {
        return companyRepository.findByName(name).isPresent();
    }

    //add company
    @Override
    @Transactional
    public void addCompany(AddCompanyDTO addCompanyDTO) {
        // Създаване на обект Company от DTO
        Company company = mapper.map(addCompanyDTO, Company.class);

        // Запазване на Company, за да се генерира ID
        company = companyRepository.save(company);

        // Намиране на индустриите по ID
        List<Industry> industries = industryRepository.findAllById(addCompanyDTO.getIndustries());

        // Настройка на връзката между Industry и Company
        for (Industry industry : industries) {
            industry.setCompany(company); // Свързване с компанията
            industryRepository.save(industry); // Запазване на Industry
        }

        // Свързване на индустриите към Company (ако е нужно за приложението)
        company.setIndustries(industries);

        // Може да актуализирате Company отново, ако се налага
        companyRepository.save(company);
    }

    //delete company by id
    @Override
    public void removeCompany(long id) {
        companyRepository.deleteById(id);
    }

    //mapCompanyToCompanyDTO
    CompanyDTO mapCompanyToCompanyDTO(Company company){
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setId(company.getId());
        companyDTO.setName(company.getName());
        companyDTO.setAddress(company.getAddress());
        companyDTO.setPhoneNumber(company.getPhoneNumber());
        companyDTO.setEmail(company.getEmail());
        companyDTO.setLocationType(company.getLocationType().name());

        List<PersonDTO> personDTOS = new ArrayList<>();
        for (ContactPerson contactPerson : company.getContactPerson()) {
            PersonDTO personDTO = mapper.map(contactPerson, PersonDTO.class);

            personDTOS.add(personDTO);
        }
        companyDTO.setContactPerson(personDTOS);

        List<IndustryDTO> industryDTOS = new ArrayList<>();
        for (Industry industry : company.getIndustries()) {
            IndustryDTO industryDTO = new IndustryDTO();
            industryDTO.setId(industry.getId());
            industryDTO.setName(industry.getName());

            industryDTOS.add(industryDTO);
        }
        companyDTO.setIndustries(industryDTOS);

        return companyDTO;
    }
}
