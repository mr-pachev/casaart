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
import org.springframework.transaction.annotation.Transactional;

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
        List<CompanyManager> companyManagerList = companyManagerRepository.findAllByOrderByIdDesc();

        return companyManagersListMapToPersonDTOS(companyManagerList);
    }

    // sort company managers
    @Override
    public List<PersonDTO> sortedCompanyManagersByType(String type) {
        String[] inputArr = convertInputString(type);
        List<CompanyManager> companyManagerList = new ArrayList<>();

        if ("allCompanyManagers".equals(type)) {
            companyManagerList = companyManagerRepository.findAllByOrderByIdDesc();

        } else if ("allCompanyManagersByName".equals(type)) {
            companyManagerList = companyManagerRepository.findAllByOrderByFirstNameAscMiddleNameAscLastNameAsc();

        } else if ("allCompanyManagersByFirstCall".equals(type)) {
            companyManagerList = companyManagerRepository.findAllByOrderByFirstCallDesc();

        } else if ("allCompanyManagersBySendEmail".equals(type)) {
            companyManagerList = companyManagerRepository.findAllByOrderBySendEmailDesc();

        }  else if ("allCompanyManagersBySendLetter".equals(type)) {
            companyManagerList = companyManagerRepository.findAllByOrderBySendLetterDesc();

        } else if ("allCompanyManagersBySecondCall".equals(type)) {
            companyManagerList = companyManagerRepository.findAllByOrderBySecondCallDesc();

        } else if ("allCompanyManagersByPresence".equals(type)) {
            companyManagerList = companyManagerRepository.findAllByOrderByPresenceDesc();

        }else if(type.contains("@")){
            companyManagerList = companyManagerRepository.findByEmailStartingWithIgnoreCase(type);

        } else if(inputArr.length == 1){
            companyManagerList = companyManagerRepository.findAllByFirstName(inputArr[0]);

        }else if(inputArr.length == 2){
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
        CompanyManager companyManager = companyManagerRepository.findById(id);

        PersonDTO personDTO = mapper.map(companyManager, PersonDTO.class);
        personDTO.setCompany(companyManager.getCompany().getName());

        return personDTO;
    }

    // find company manager by company id
    @Override
    public PersonDTO findCompanyManagerByCompany(long id) {
        CompanyManager companyManager = companyManagerRepository.findByCompanyId(id);

        PersonDTO personDTO = mapper.map(companyManager, PersonDTO.class);
        personDTO.setCompany(companyManager.getCompany().getName());

        return personDTO;
    }

    // add company manager
    @Override
    @Transactional
    public void addCompanyManager(PersonDTO personDTO, long companyId) {
        Company company = companyRepository.findById(companyId).get();

        boolean isManagerInContactList = company.getContactPersons().stream()
                .anyMatch(person -> person.getFullName().equals(personDTO.getFullName()));

        if (isManagerInContactList) {
            company.getContactPersons().removeIf(person -> person.getFullName().equals(personDTO.getFullName()));
        }

        companyRepository.save(company);

        if (isManagerInContactList) {
            ContactPerson contactPerson = contactPersonRepository.findByFirstNameAndLastNameAndPhoneNumber(personDTO.getFirstName(),
                    personDTO.getLastName(),
                    personDTO.getPhoneNumber()).get();
            contactPerson.setCompany(null);
            contactPersonRepository.save(contactPerson);

            contactPersonRepository.deleteById(contactPerson.getId());
        }

        CompanyManager manager = mapper.map(personDTO, CompanyManager.class);

        manager.setId(null);
        manager.setCompany(company);
        companyManagerRepository.save(manager);

        company.setCompanyManager(manager);
        companyRepository.save(company);
    }

    // edit company manager
    @Override
    public void editCompanyManager(PersonDTO personDTO) {
        CompanyManager companyManager = personDTOMapToCompanyManager(personDTO);

        companyManagerRepository.save(companyManager);
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
    CompanyManager personDTOMapToCompanyManager(PersonDTO personDTO) {
        CompanyManager companyManager = companyManagerRepository.findById(personDTO.getId());

        companyManager.setFirstName(personDTO.getFirstName());

        if (personDTO.getMiddleName() != null) {
            companyManager.setMiddleName(personDTO.getMiddleName());
        }
        companyManager.setLastName(personDTO.getLastName());
        companyManager.setEmail(personDTO.getEmail());
        companyManager.setPhoneNumber(personDTO.getPhoneNumber());

        if(personDTO.getFirstCall() != null){
            companyManager.setFirstCall(personDTO.getFirstCall());
        }else {
            companyManager.setFirstCall(null);
        }

        if(personDTO.getSendEmail() != null){
            companyManager.setFirstCall(personDTO.getSendEmail());
        }else {
            companyManager.setSendEmail(null);
        }

        if(personDTO.getSendLetter() != null){
            companyManager.setSendLetter(personDTO.getSendLetter());
        }else {
            companyManager.setSendLetter(null);
        }

        if(personDTO.getSecondCall() != null){
            companyManager.setSecondCall(personDTO.getSecondCall());
        }else {
            companyManager.setSecondCall(null);
        }

        if(personDTO.getPresence() != null){
            companyManager.setPresence(personDTO.getPresence());
        }else {
            companyManager.setPresence(null);
        }

        return companyManager;
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
