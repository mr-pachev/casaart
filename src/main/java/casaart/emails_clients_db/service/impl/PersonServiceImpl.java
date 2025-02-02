package casaart.emails_clients_db.service.impl;

import casaart.emails_clients_db.repository.CompanyManagerRepository;
import casaart.emails_clients_db.service.PersonService;
import org.springframework.stereotype.Service;

@Service
public class PersonServiceImpl implements PersonService {
    private final CompanyManagerRepository companyManagerRepository;

    public PersonServiceImpl(CompanyManagerRepository companyManagerRepository) {
        this.companyManagerRepository = companyManagerRepository;
    }

    //checking is exist person
    @Override
    public boolean isExistPerson(String firstName, String lastName) {
        return companyManagerRepository.findByFirstNameAndLastName(firstName, lastName).isPresent();
    }
}
