package casaart.emails_clients_db.service.impl;

import casaart.emails_clients_db.model.dto.AddClientDTO;
import casaart.emails_clients_db.model.dto.ClientDTO;
import casaart.emails_clients_db.model.entity.Client;
import casaart.emails_clients_db.model.entity.User;
import casaart.emails_clients_db.model.enums.LoyaltyLevel;
import casaart.emails_clients_db.model.enums.SourceType;
import casaart.emails_clients_db.repository.ClientRepository;
import casaart.emails_clients_db.service.ClientService;
import casaart.emails_clients_db.service.UserHelperService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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

        return clienListMapToClientDTOS(clients);
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

        } else if (inputArr.length == 1) {
            clientList = clientRepository.findAllByFirstName(inputArr[0].toLowerCase());

        } else if (inputArr.length == 2) {
            clientList = clientRepository.findAllByFirstNameAndLastName(inputArr[0].toLowerCase(), inputArr[1].toLowerCase());

        }

        List<ClientDTO> clientDTOS = clienListMapToClientDTOS(clientList);

        return clientDTOS;
    }

    // get sorted clients by sourceType
    @Override
    public List<ClientDTO> sortedClientsBySourceType(String type) {
        SourceType sourceType = SourceType.valueOf(type);

        return clientRepository.findAllBySourceType(sourceType)
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

    // checking is exist client email
    @Override
    public boolean isExistClientEmail(String email) {

        return clientRepository.findByEmail(email).isPresent();
    }

    // add client
    @Override
    public void addClient(AddClientDTO addClientDTO) {
        Client client = mapper.map(addClientDTO, Client.class);
        client.setUser(userHelperService.getUser());

        if(addClientDTO.getSourceType().equals("SHOWROOM") || addClientDTO.getSourceType().equals("HOTEL")){
            client.setLoyaltyLevel(LoyaltyLevel.LEVEL_1);
        }

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
        Client client = clientRepository.findById(clientDTO.getId());
        User user = client.getUser();

        client = mapper.map(clientDTO, Client.class);
        client.setUser(user);
        client.setModifyFrom(userHelperService.getUser().getUsername());

        clientRepository.save(client);
    }

    // delete client by id
    @Override
    public void deleteClient(long id) {
        clientRepository.deleteById(id);
    }

    // ClientDTO map to Client
    ClientDTO mapToClientDTO(Client client) {
        ClientDTO clientDTO = mapper.map(client, ClientDTO.class);
        clientDTO.setAddedFrom(client.getUser().getUsername());

        return clientDTO;
    }

    // List<Client> map to List<ClientDTO>
    List<ClientDTO> clienListMapToClientDTOS(List<Client> clientList) {
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
