package casaart.emails_clients_db.service.impl;

import casaart.emails_clients_db.model.dto.PersonDTO;
import casaart.emails_clients_db.model.entity.Company;
import casaart.emails_clients_db.model.entity.ContactPerson;
import casaart.emails_clients_db.repository.CompanyRepository;
import casaart.emails_clients_db.repository.ContactPersonRepository;
import casaart.emails_clients_db.service.ContactPersonService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ContactPersonServiceImpl implements ContactPersonService {
    private final ContactPersonRepository contactPersonRepository;
    private final CompanyRepository companyRepository;

    private final ModelMapper mapper;

    public ContactPersonServiceImpl(ContactPersonRepository contactPersonRepository, CompanyRepository companyRepository, ModelMapper mapper) {
        this.contactPersonRepository = contactPersonRepository;
        this.companyRepository = companyRepository;
        this.mapper = mapper;
    }

    // all contact persons
    @Override
    public List<PersonDTO> allContactPersons() {
        List<ContactPerson> contactPersonList = contactPersonRepository.findAll();
        List<PersonDTO> personDTOS = new ArrayList<>();

        for (ContactPerson person : contactPersonList) {
            PersonDTO personDTO = contactPersonMapToPersonDTO(person);

            personDTOS.add(personDTO);
        }

        return personDTOS;
    }

    // sort contact persons
    @Override
    public List<PersonDTO> sortedContactPersonsByType(String type) {
        List<ContactPerson> contactPersonList = new ArrayList<>();

        switch (type) {
            case "allContactPersons":
                contactPersonList = contactPersonRepository.findAllByOrderByIdDesc();
                break;
            case "allContactPersonsByName":
                contactPersonList = contactPersonRepository.findAllByOrderByFirstNameAscMiddleNameAscLastNameAsc();
                break;
            case "allContactPersonsByFirstEmail":
                contactPersonList = contactPersonRepository.findAllByOrderByFirstEmailDesc();
                break;
            case "allContactPersonsByFirstCall":
                contactPersonList = contactPersonRepository.findAllByOrderByFirstCallDesc();
                break;
            case "allContactPersonsBySecondEmail":
                contactPersonList = contactPersonRepository.findAllByOrderBySecondEmailDesc();
                break;
            case "allContactPersonsBySecondCall":
                contactPersonList = contactPersonRepository.findAllByOrderBySecondCallDesc();
                break;
        }

        List<PersonDTO> contactPersonsDTOS = contactPersonsListMapToPersonDTOS(contactPersonList);

        return contactPersonsDTOS;
    }

    // all contact persons by company id
    @Override
    public List<PersonDTO> currentContactPersonsByCompanyId(long id) {
        List<ContactPerson> contactPersonList = contactPersonRepository.findAllByCompanyId(id);
        List<PersonDTO> contactPersonsDTOS = new ArrayList<>();

        for (ContactPerson person : contactPersonList) {
            contactPersonsDTOS.add(contactPersonMapToPersonDTO(person));
        }

        return contactPersonsDTOS;
    }

    // find contact person by id
    @Override
    public PersonDTO getContactPersonById(long id) {
        ContactPerson contactPerson = contactPersonRepository.findById(id).get();

        PersonDTO personDTO = mapper.map(contactPerson, PersonDTO.class);
        personDTO.setCompany(contactPerson.getCompany().getName());

        return personDTO;
    }

    // check is exist contact person
    @Override
    public boolean isExistContactPerson(PersonDTO personDTO) {
        return contactPersonRepository.findByFirstNameAndLastNameAndPhoneNumber(personDTO.getFirstName(),
                personDTO.getLastName(),
                personDTO.getPhoneNumber()).isPresent();
    }

    // add contact person
    @Override
    public void addContactPerson(PersonDTO personDTO, long companyId) {
        Company company = companyRepository.findById(companyId).get();

        ContactPerson contactPerson = personDTOMapToContactPerson(personDTO);
        contactPerson.setCompany(company);

        contactPersonRepository.save(contactPerson);
    }

    // edit contact person
    @Override
    public void editContactPerson(PersonDTO personDTO) {
        ContactPerson contactPerson = contactPersonRepository.findById(personDTO.getId()).get();

        contactPerson.setFirstName(personDTO.getFirstName());
        if (personDTO.getMiddleName() != null) {
            contactPerson.setMiddleName(personDTO.getMiddleName());
        }
        contactPerson.setLastName(personDTO.getLastName());
        contactPerson.setEmail(personDTO.getEmail());
        contactPerson.setPhoneNumber(personDTO.getPhoneNumber());

        contactPersonRepository.save(contactPerson);
    }

    // delete contact person by id
    @Override
    @Transactional
    public void removeContactPerson(long id) {
        ContactPerson contactPerson = contactPersonRepository.findById(id).get();
        Company company = companyRepository.findByName(contactPerson.getCompany().getName()).get();

        company.getContactPersons().remove(contactPerson);

        companyRepository.save(company);

        contactPersonRepository.delete(contactPerson);
    }

    // PersonDTO map to ContactPerson
    ContactPerson personDTOMapToContactPerson(PersonDTO personDTO) {
        ContactPerson contactPerson = new ContactPerson();

        contactPerson.setId(null);
        contactPerson.setFirstName(personDTO.getFirstName());
        if (personDTO.getMiddleName() != null) {
            contactPerson.setMiddleName(personDTO.getMiddleName());
        }
        contactPerson.setLastName(personDTO.getLastName());
        contactPerson.setEmail(personDTO.getEmail());
        contactPerson.setPhoneNumber(personDTO.getPhoneNumber());

        return contactPerson;
    }

    // ContactPerson map to PersonDTO
    PersonDTO contactPersonMapToPersonDTO(ContactPerson contactPerson) {
        PersonDTO contactPersonDTO = mapper.map(contactPerson, PersonDTO.class);
        contactPersonDTO.setCompany(contactPerson.getCompany().getName());

        return contactPersonDTO;
    }

    // List<ContactPerson> map to List<PersonDTO>
    List<PersonDTO> contactPersonsListMapToPersonDTOS(List<ContactPerson> contactPersonListList) {
        List<PersonDTO> allContactPersonsDTOS = new ArrayList<>();

        for (ContactPerson person : contactPersonListList) {
            PersonDTO personDTO = mapper.map(person, PersonDTO.class);
            personDTO.setCompany(person.getCompany().getName());

            allContactPersonsDTOS.add(personDTO);
        }

        return allContactPersonsDTOS;
    }
}
