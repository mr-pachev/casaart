package casaart.emails_clients_db.service.impl;

import casaart.emails_clients_db.repository.PersonRepository;
import casaart.emails_clients_db.service.PersonService;
import org.springframework.stereotype.Service;

@Service
public class PersonServiceImpl implements PersonService {
    private final PersonRepository personRepository;

    public PersonServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    //checking is exist person
    @Override
    public boolean isExistPerson(String firstName, String lastName) {
        return personRepository.findByFirstNameAndLastName(firstName, lastName).isPresent();
    }
}
