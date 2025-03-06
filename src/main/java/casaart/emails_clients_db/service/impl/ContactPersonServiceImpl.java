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
        String[] inputArr = convertInputString(type);
        List<ContactPerson> contactPersonList = new ArrayList<>();

        if ("allContactPersons".equals(type)) {
            contactPersonList = contactPersonRepository.findAllByOrderByIdDesc();

        } else if ("allContactPersonsByName".equals(type)) {
            contactPersonList = contactPersonRepository.findAllByOrderByFirstNameAscMiddleNameAscLastNameAsc();

        } else if ("allContactPersonsByFirstEmail".equals(type)) {
            contactPersonList = contactPersonRepository.findAllByOrderByFirstEmailDesc();

        } else if ("allContactPersonsByFirstCall".equals(type)) {
            contactPersonList = contactPersonRepository.findAllByOrderByFirstCallDesc();

        } else if ("allContactPersonsBySecondEmail".equals(type)) {
            contactPersonList = contactPersonRepository.findAllByOrderBySecondEmailDesc();

        } else if ("allContactPersonsBySecondCall".equals(type)) {
            contactPersonList = contactPersonRepository.findAllByOrderBySecondCallDesc();

        } else if(inputArr.length == 1){
            contactPersonList = contactPersonRepository.findAllByFirstName(inputArr[0]);

        } else if(inputArr.length == 2){
            contactPersonList = contactPersonRepository.findAllByFirstNameAndLastName(inputArr[0], inputArr[1]);

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
        ContactPerson contactPerson = personDTOMapToContactPerson(personDTO);

        contactPersonRepository.save(contactPerson);
    }

    // delete contact person by id
    @Override
    @Transactional
    public void removeContactPerson(long id) {
        ContactPerson contactPerson = contactPersonRepository.findById(id).get();
        Company company = companyRepository.findByNameIgnoreCase(contactPerson.getCompany().getName()).get();

        company.getContactPersons().remove(contactPerson);

        companyRepository.save(company);

        contactPersonRepository.delete(contactPerson);
    }

    // PersonDTO map to ContactPerson
    ContactPerson personDTOMapToContactPerson(PersonDTO personDTO) {
        ContactPerson contactPerson = new ContactPerson();

        if(contactPersonRepository.findById(personDTO.getId()).isPresent()){
            contactPerson = contactPersonRepository.findById(personDTO.getId()).get();
        }

        contactPerson.setFirstName(personDTO.getFirstName());

        if (personDTO.getMiddleName() != null) {
            contactPerson.setMiddleName(personDTO.getMiddleName());
        }
        contactPerson.setLastName(personDTO.getLastName());
        contactPerson.setEmail(personDTO.getEmail());
        contactPerson.setPhoneNumber(personDTO.getPhoneNumber());

        if (personDTO.getFirstEmail() != null) {
            contactPerson.setFirstEmail(personDTO.getFirstEmail());
        }
        if (personDTO.getFirstCall() != null) {
            contactPerson.setFirstCall(personDTO.getFirstCall());
        }
        if (personDTO.getSecondEmail() != null) {
            contactPerson.setSecondEmail(personDTO.getSecondEmail());
        }
        if (personDTO.getSecondCall() != null) {
            contactPerson.setSecondCall(personDTO.getSecondCall());
        }

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

    // convert input string
    String[] convertInputString(String input) {
        if (input == null || input.trim().isEmpty()) {
            return new String[0]; // Връщане на празен масив за нула или празен вход
        }

        // 1. Trim: Премахване на празните пространства в началото и края
        String trimmedString = input.trim();

        // 2. Преобразуване на всички символи в малки букви
        String lowerCaseString = trimmedString.toLowerCase();

        // 3. Премахване на препинателните знаци
        String cleanedString = lowerCaseString.replaceAll("[^a-zA-Zа-яА-Я\\s]", "");

        // 4. Разделяне на низа на отделни думи (по интервали)
        String[] words = cleanedString.split("\\s+");

        return words;
    }
}
