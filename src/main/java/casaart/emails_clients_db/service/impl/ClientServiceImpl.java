package casaart.emails_clients_db.service.impl;

import casaart.emails_clients_db.model.dto.AddClientDTO;
import casaart.emails_clients_db.model.dto.ClientDTO;
import casaart.emails_clients_db.model.entity.Client;
import casaart.emails_clients_db.model.entity.User;
import casaart.emails_clients_db.model.enums.*;
import casaart.emails_clients_db.repository.ClientRepository;
import casaart.emails_clients_db.service.ClientService;
import casaart.emails_clients_db.service.UserHelperService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;
    private final UserHelperService userHelperService;
    private final ModelMapper mapper;

    public ClientServiceImpl(ClientRepository clientRepository, UserHelperService userHelperService, ModelMapper mapper) {
        this.clientRepository = clientRepository;
        this.userHelperService = userHelperService;
        this.mapper = mapper;
    }

    // get all clients
    @Override
    public List<ClientDTO> getAllClients() {
        List<Client> clients = clientRepository.findAllByOrderByIdDesc();

        return clientListMapToClientDTOS(clients);
    }

    // get sorted clients
    @Override
    public List<ClientDTO> sortedClients(String type) {
        String[] inputArr = convertInputString(type);
        List<Client> clientList = new ArrayList<>();

        if ("allClients".equals(type)) {
            clientList = clientRepository.findAllByOrderByIdDesc();

        } else if ("allClientsByName".equals(type)) {
            clientList = clientRepository.findAllByOrderByFirstNameAscMiddleNameAscLastNameAsc();

        } else if ("allClientsByFirstEmail".equals(type)) {
            clientList = clientRepository.findAllByOrderByFirstEmailDesc();

        } else if ("allClientsByFirstCall".equals(type)) {
            clientList = clientRepository.findAllByOrderByFirstCallDesc();

        } else if ("allClientsBySecondEmail".equals(type)) {
            clientList = clientRepository.findAllByOrderBySecondEmailDesc();

        } else if ("allClientsBySecondCall".equals(type)) {
            clientList = clientRepository.findAllByOrderBySecondCallDesc();

        } else if (type.contains("@")) {

            if (clientRepository.findByEmail(type).isPresent()) {
                clientList.add(clientRepository.findByEmail(type).get());
            }

        } else if (inputArr.length == 1) {
            clientList = clientRepository.findAllByFirstName(inputArr[0]);

        } else if (inputArr.length == 2) {
            clientList = clientRepository.findAllByFirstNameAndLastName(inputArr[0], inputArr[1]);

        }

        List<ClientDTO> clientDTOS = clientListMapToClientDTOS(clientList);

        return clientDTOS;
    }

    // get sorted clients by sourceType
    @Override
    public List<ClientDTO> sortedClientsBySourceType(String type) {
        SourceType sourceType = SourceType.valueOf(type);

        return clientRepository.findAllBySourceTypes(sourceType)
                .stream()
                .map(client -> mapper.map(client, ClientDTO.class)) // Преобразуване в DTO директно в ламбда израза
                .collect(Collectors.toList());
    }

    // get sorted clients by loyaltyLevel
    @Override
    public List<ClientDTO> sortedClientsByLoyaltyLevel(String type) {
        LoyaltyLevel loyaltyLevel = LoyaltyLevel.valueOf(type);

        return clientRepository.findAllByLoyaltyLevel(loyaltyLevel)
                .stream()
                .map(client -> mapper.map(client, ClientDTO.class)) // Преобразуване в DTO директно в ламбда израза
                .collect(Collectors.toList());
    }

    // get sorted clients by nationality
    @Override
    public List<ClientDTO> sortedClientsByNationality(String nationalityType) {
        Nationality nationality = Nationality.valueOf(nationalityType);

        return clientRepository.findAllByNationality(nationality)
                .stream()
                .map(client -> mapper.map(client, ClientDTO.class)) // Преобразуване в DTO директно в ламбда израза
                .collect(Collectors.toList());
    }

    // get sorted clients by rating
    @Override
    public List<ClientDTO> sortedClientsByRating(String ratingType, String ratingValue) {
        Rating rating = Rating.valueOf(ratingValue); // Стойността на рейтинга като enum

        List<Client> clients;

        // Извикваме съответния метод от репозитория в зависимост от типа на рейтинга
        switch (ratingType) {
            case "ratingFood":
                clients = clientRepository.findAllByRatingFood(rating);
                break;
            case "ratingQualityPrice":
                clients = clientRepository.findAllByRatingQualityPrice(rating);
                break;
            case "ratingPoliteness":
                clients = clientRepository.findAllByRatingPoliteness(rating);
                break;
            case "ratingCleanTidy":
                clients = clientRepository.findAllByRatingCleanTidy(rating);
                break;
            default:
                throw new IllegalArgumentException("Invalid rating type: " + ratingType);
        }

        return clients.stream()
                .map(this::mapToClientDTO) // Преобразуваме в DTO
                .collect(Collectors.toList());
    }


    // checking is exist client email
    @Override
    public boolean isExistClientEmail(String email) {

        return clientRepository.findByEmail(email).isPresent();
    }

    // add client
    @Override
    public void addClient(AddClientDTO addClientDTO) {
        Client client = mapper.map(addClientDTO, Client.class);
        List<SourceType> sourceTypes = new ArrayList<>();

        for (String source : addClientDTO.getSourceTypes()) {
            sourceTypes.add(SourceType.valueOf(source));
        }
        client.setSourceTypes(sourceTypes);

        // Има ли въведена дата настаняване
        if (addClientDTO.getAccommodationDate() != null) {
            client.setCounterStay(1);
            client.setAccommodationDate(addClientDTO.getAccommodationDate());

        } else {
            client.setLoyaltyLevel(null);
        }

        client.setUser(userHelperService.getUser());
        client.setModifyFrom(userHelperService.getUser().getUsername());

        clientRepository.save(client);
    }

    // find client by id
    @Override
    public ClientDTO findClientById(long id) {
        Client client = clientRepository.findById(id);

        return mapToClientDTO(client);
    }

    // edit client
    @Override
    public void editClient(ClientDTO clientDTO) {
        Client existClient = clientRepository.findById(clientDTO.getId());
        User user = existClient.getUser();

        Client editedClient = mapper.map(clientDTO, Client.class);

        List<SourceType> sourceTypes = new ArrayList<>();

        for (String source : clientDTO.getSourceTypes()) {
            sourceTypes.add(SourceType.valueOf(source));
        }
        editedClient.setSourceTypes(sourceTypes);

        // Настройка на дата настаняване и брояча за престой
        if (existClient.getAccommodationDate() == null && clientDTO.getAccommodationDate() != null) { // Няма дата настаняване и е въведена така
            editedClient.setCounterStay(1);

            editedClient.setAccommodationDate(clientDTO.getAccommodationDate());
        } else if (!Objects.equals(existClient.getAccommodationDate(), clientDTO.getAccommodationDate()) &&
                clientDTO.getAccommodationDate() != null) { // Има дата настаняване но е добавена нова
            editedClient.setCounterStay(existClient.getCounterStay() + 1);

            if (existClient.getCounterStay() >= 20) {
                editedClient.setLoyaltyLevel(LoyaltyLevel.LEVEL_3);
            } else if (existClient.getCounterStay() >= 10) {
                editedClient.setLoyaltyLevel(LoyaltyLevel.LEVEL_2);
            }

            editedClient.setAccommodationDate(clientDTO.getAccommodationDate());
        } else if (existClient.getAccommodationDate() != null &&
                Objects.equals(existClient.getAccommodationDate(), clientDTO.getAccommodationDate())) { // Има дата настаняване и съответства на въведената
            editedClient.setCounterStay(existClient.getCounterStay());

        } else if (clientDTO.getAccommodationDate() == null && clientDTO.getLoyaltyLevel() == null) { // Няма въведена дата настаняване и няма въвведено ниво лоялност
            editedClient.setCounterStay(null);
            editedClient.setAccommodationDate(null);

        } else { // Няма въведена дата настаняване но има въведено ниво лоялност
            editedClient.setLoyaltyLevel(null);
        }

        editedClient.setUser(user);
        editedClient.setModifyFrom(userHelperService.getUser().getUsername());

        clientRepository.save(editedClient);
    }

    // delete client by id
    @Override
    public void deleteClient(long id) {
        clientRepository.deleteById(id);
    }

    // ClientDTO map to Client
    ClientDTO mapToClientDTO(Client client) {
        if (client == null) {
            return null;
        }

        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setId(client.getId());
        clientDTO.setFirstName(client.getFirstName());
        clientDTO.setMiddleName(client.getMiddleName());
        clientDTO.setLastName(client.getLastName());
        clientDTO.setEmail(client.getEmail());
        clientDTO.setPhoneNumber(client.getPhoneNumber());
        clientDTO.setCounterStay(client.getCounterStay() != null ? client.getCounterStay() : 0);
        clientDTO.setModifyFrom(client.getModifyFrom());
        clientDTO.setAccommodationDate(client.getAccommodationDate());

        // Преобразуване на Enum списък към String списък
        if (client.getSourceTypes() != null) {
            List<String> sourceTypes = client.getSourceTypes().stream()
                    .map(Enum::name)
                    .collect(Collectors.toList());
            clientDTO.setSourceTypes(sourceTypes);
        }

        // Преобразуване на Enum LoyaltyLevel към String
        clientDTO.setLoyaltyLevel(client.getLoyaltyLevel() != null ? client.getLoyaltyLevel().name() : null);

        // Преобразуване на Enum Nationality към String
        clientDTO.setNationality(client.getNationality() != null ? client.getNationality().name() : null);

        // Задаване на потребителя, добавил клиента
        clientDTO.setAddedFrom(client.getUser() != null ? client.getUser().getUsername() : null);

        // Преобразуване на рейтингите от Enum към String
        clientDTO.setRatingFood(client.getRatingFood() != null ? client.getRatingFood().name() : null);
        clientDTO.setRatingQualityPrice(client.getRatingQualityPrice() != null ? client.getRatingQualityPrice().name() : null);
        clientDTO.setRatingPoliteness(client.getRatingPoliteness() != null ? client.getRatingPoliteness().name() : null);
        clientDTO.setRatingCleanTidy(client.getRatingCleanTidy() != null ? client.getRatingCleanTidy().name() : null);

        // Определяне на основния рейтинг (ако има такъв)
        String rating = "Няма рейтинг";
        if (client.getRatingFood() != null) {
            rating = client.getRatingFood().name();
        } else if (client.getRatingQualityPrice() != null) {
            rating = client.getRatingQualityPrice().name();
        } else if (client.getRatingPoliteness() != null) {
            rating = client.getRatingPoliteness().name();
        } else if (client.getRatingCleanTidy() != null) {
            rating = client.getRatingCleanTidy().name();
        }
        clientDTO.setRating(rating);

        return clientDTO;
    }

    // List<Client> map to List<ClientDTO>
    List<ClientDTO> clientListMapToClientDTOS(List<Client> clientList) {
        List<ClientDTO> allClientDTOS = new ArrayList<>();

        for (Client client : clientList) {
            ClientDTO clientDTO = mapToClientDTO(client);

            allClientDTOS.add(clientDTO);
        }

        return allClientDTOS;
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
