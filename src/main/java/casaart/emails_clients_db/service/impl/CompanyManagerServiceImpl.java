package casaart.emails_clients_db.service.impl;

import casaart.emails_clients_db.model.dto.PersonDTO;
import casaart.emails_clients_db.model.entity.Company;
import casaart.emails_clients_db.model.entity.CompanyManager;
import casaart.emails_clients_db.model.entity.ContactPerson;
import casaart.emails_clients_db.repository.CompanyManagerRepository;
import casaart.emails_clients_db.repository.CompanyRepository;
import casaart.emails_clients_db.repository.ContactPersonRepository;
import casaart.emails_clients_db.service.CompanyManagerService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CompanyManagerServiceImpl implements CompanyManagerService {
    private final CompanyManagerRepository companyManagerRepository;
    private final CompanyRepository companyRepository;
    private final ContactPersonRepository contactPersonRepository;
    private final ModelMapper mapper;

    public CompanyManagerServiceImpl(CompanyManagerRepository companyManagerRepository, CompanyRepository companyRepository, ContactPersonRepository contactPersonRepository, ModelMapper mapper) {
        this.companyManagerRepository = companyManagerRepository;
        this.companyRepository = companyRepository;
        this.contactPersonRepository = contactPersonRepository;
        this.mapper = mapper;
    }

    // get all company managers
    @Override
    public List<PersonDTO> allCompanyManagers() {
        List<CompanyManager> companyManagerList = companyManagerRepository.findAllCompanyManagersForPartnersOrderedByIdDesc();

        return companyManagersListMapToPersonDTOS(companyManagerList);
    }

    // sort company managers
    @Override
    public List<PersonDTO> sortedCompanyManagersByType(String type) {
        String[] inputArr = convertInputString(type);
        List<CompanyManager> companyManagerList = new ArrayList<>();

        if ("allCompanyManagers".equals(type)) {
            companyManagerList = companyManagerRepository.findAllCompanyManagersForPartnersOrderedByIdDesc();

        } else if ("allCompanyManagersByName".equals(type)) {
            companyManagerList = companyManagerRepository.findAllPartnersOrderByFirstNameAscMiddleNameAscLastNameAsc();

        } else if ("allCompanyManagersByFirstCall".equals(type)) {
            companyManagerList = companyManagerRepository.findAllPartnersOrderByFirstCallDesc();

        } else if ("allCompanyManagersBySendEmail".equals(type)) {
            companyManagerList = companyManagerRepository.findAllPartnersOrderBySendEmailDesc();

        } else if ("allCompanyManagersBySendLetter".equals(type)) {
            companyManagerList = companyManagerRepository.findAllPartnersOrderBySendLetterDesc();

        } else if ("allCompanyManagersBySecondCall".equals(type)) {
            companyManagerList = companyManagerRepository.findAllPartnersOrderBySecondCallDesc();

        } else if ("allCompanyManagersByPresence".equals(type)) {
            companyManagerList = companyManagerRepository.findAllPartnersOrderByPresenceDesc();

        } else if (type.contains("@")) {
            companyManagerList = companyManagerRepository.findByEmailStartingWithIgnoreCase(type);

        } else if (inputArr.length == 1) {
            companyManagerList = companyManagerRepository.findAllByFirstName(inputArr[0]);

        } else if (inputArr.length == 2) {
            companyManagerList = companyManagerRepository.findAllByFirstNameAndLastName(inputArr[0], inputArr[1]);

        }

        List<PersonDTO> companyManagerDTOS = companyManagersListMapToPersonDTOS(companyManagerList);

        return companyManagerDTOS;
    }

    // check is exist company manager
    @Override
    public boolean isExistCompanyManager(PersonDTO personDTO) {
        return companyManagerRepository.findByFirstNameAndLastNameAndPhoneNumber(personDTO.getFirstName(),
                personDTO.getLastName(),
                personDTO.getPhoneNumber()).isPresent();
    }

    // find company manager by id
    @Override
    public PersonDTO findCompanyManagerById(long id) {
        CompanyManager companyManager = companyManagerRepository.findById(id).get();

        return companyManagerMapToPersonDTO(companyManager);
    }

    // find company manager by company id
    @Override
    public PersonDTO findCompanyManagerByCompany(long id) {
        CompanyManager companyManager = companyManagerRepository.findByCompanyId(id);

        return companyManagerMapToPersonDTO(companyManager);
    }

    // add company manager
    @Override
    public void addCompanyManager(PersonDTO personDTO, long companyId) {
        // Намери компанията
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new EntityNotFoundException("Company with id " + companyId + " not found"));

        // Проверка дали ContactPerson съществува в списъка с контактни лица на компанията
        boolean isManagerInContactList = company.getContactPersons().stream()
                .anyMatch(person -> person.getFullName().equals(personDTO.getFullName()));

        ContactPerson contactPerson = null;

        if (isManagerInContactList) {
            // Намери ContactPerson
            contactPerson = contactPersonRepository.findByFirstNameAndLastNameAndPhoneNumber(
                    personDTO.getFirstName(),
                    personDTO.getLastName(),
                    personDTO.getPhoneNumber()).orElse(null);

            // Ако ContactPerson съществува, премахваме го от списъка с контактни лица на компанията
            if (contactPerson != null) {
                company.getContactPersons().remove(contactPerson);
            }

            companyRepository.save(company);
        }

        // Създаване на новия CompanyManager
        CompanyManager manager = personDTOMapToCompanyManager(personDTO);

        // Задаване на нов управител за компанията
        company.setCompanyManager(manager);
        companyRepository.save(company);

        // Ако ContactPerson съществува, премахни връзката с компанията и го изтрий
        if (contactPerson != null) {
            contactPerson.setCompany(null);
            contactPersonRepository.save(contactPerson); // Запази преди изтриване, за да избегнем грешки
            contactPersonRepository.deleteById(contactPerson.getId());
        }
    }

    // edit company manager
    @Override
    public void editCompanyManager(PersonDTO personDTO) {
        CompanyManager companyManager = personDTOMapToCompanyManager(personDTO);
    }

    // delete company manager
    @Override
    public void removeCompanyManager(long id) {
        Company company = companyRepository.findByCompanyManagerId(id)
                .orElseThrow(() -> new EntityNotFoundException("Компания с id " + id + " не е намерена."));

        // Ако company има CompanyManager, запазете временно неговото id
        Long companyManagerId = null;
        if (company.getCompanyManager() != null) {
            companyManagerId = company.getCompanyManager().getId();
        }

        // Откачете CompanyManager от компанията
        company.setCompanyManager(null);
        companyRepository.save(company);

        // Изтрийте CompanyManager, ако съществува
        if (companyManagerId != null) {
            companyManagerRepository.deleteById(companyManagerId);
        }
    }

    // PersonDTO map to CompanyManager
    public CompanyManager personDTOMapToCompanyManager(PersonDTO personDTO) {
        CompanyManager companyManager = new CompanyManager();

        // Изчистване името на фирмата от запетайки
        personDTO.getCompany().trim().replaceAll("[,./\\s]+", " ");

        Company company = companyRepository.findByName(personDTO.getCompany()).get();

        // Проверка дали ще се променя управителя или ще се създава нов
        if (companyManagerRepository.findById(personDTO.getId()).isPresent()) {
            companyManager = companyManagerRepository.findById(personDTO.getId()).get();
        }

        companyManager.setId(null);
        companyManager.setFirstName(personDTO.getFirstName());
        companyManager.setMiddleName(personDTO.getMiddleName());
        companyManager.setLastName(personDTO.getLastName());
        companyManager.setEmail(personDTO.getEmail());
        companyManager.setPhoneNumber(personDTO.getPhoneNumber());

        companyManager.setCompany(company);
        company.setCompanyManager(companyManager); // Връщаме връзката обратно

        companyManager.setFirstCall(personDTO.getFirstCall());
        companyManager.setSendEmail(personDTO.getSendEmail());
        companyManager.setSendLetter(personDTO.getSendLetter());
        companyManager.setSecondCall(personDTO.getSecondCall());
        companyManager.setPresence(personDTO.getPresence());

        companyManager = companyManagerRepository.save(companyManager);
        companyRepository.save(company); // Запазваме и компанията

        return companyManager;
    }

    // CompanyManager map to PersonDTO
    PersonDTO companyManagerMapToPersonDTO(CompanyManager companyManager){
        PersonDTO personDTO = new PersonDTO();

        personDTO.setId(companyManager.getId());
        personDTO.setFirstName(companyManager.getFirstName());
        personDTO.setMiddleName(companyManager.getMiddleName());
        personDTO.setLastName(companyManager.getLastName());
        personDTO.setEmail(companyManager.getEmail());
        personDTO.setPhoneNumber(companyManager.getPhoneNumber());
        personDTO.setCompany(companyManager.getCompany().getName());
        personDTO.setFirstCall(companyManager.getFirstCall());
        personDTO.setSendEmail(companyManager.getSendEmail());
        personDTO.setSendLetter(companyManager.getSendLetter());
        personDTO.setSecondCall(companyManager.getSecondCall());
        personDTO.setPresence(companyManager.getPresence());

        return personDTO;
    }

    // List<CompanyManager> map to List<PersonDTO>
    List<PersonDTO> companyManagersListMapToPersonDTOS(List<CompanyManager> companyManagerList) {
        List<PersonDTO> allCompanyManagersDTOS = new ArrayList<>();

        for (CompanyManager manager : companyManagerList) {
            PersonDTO personDTO = mapper.map(manager, PersonDTO.class);
            personDTO.setCompany(manager.getCompany().getName());

            allCompanyManagersDTOS.add(personDTO);
        }

        return allCompanyManagersDTOS;
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
