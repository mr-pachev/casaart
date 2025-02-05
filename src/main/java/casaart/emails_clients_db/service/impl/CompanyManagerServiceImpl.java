package casaart.emails_clients_db.service.impl;

import casaart.emails_clients_db.model.dto.PersonDTO;
import casaart.emails_clients_db.model.entity.CompanyManager;
import casaart.emails_clients_db.repository.CompanyManagerRepository;
import casaart.emails_clients_db.service.CompanyManagerService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class CompanyManagerServiceImpl implements CompanyManagerService {
    private final CompanyManagerRepository companyManagerRepository;
    private final ModelMapper mapper;

    public CompanyManagerServiceImpl(CompanyManagerRepository companyManagerRepository, ModelMapper mapper) {
        this.companyManagerRepository = companyManagerRepository;
        this.mapper = mapper;
    }

    // check is exist company manager
    @Override
    public boolean isExistCompanyManager(PersonDTO personDTO) {
        return companyManagerRepository.findByFirstNameAndLastNameAndPhoneNumber(personDTO.getFirstName(),
                personDTO.getLastName(),
                personDTO.getPhoneNumber()).isPresent();
    }

    //find company manager by id
    @Override
    public PersonDTO findCompanyManagerById(long id) {
        CompanyManager companyManager = companyManagerRepository.findById(id);

        PersonDTO personDTO = mapper.map(companyManager, PersonDTO.class);

        return personDTO;
    }
}
