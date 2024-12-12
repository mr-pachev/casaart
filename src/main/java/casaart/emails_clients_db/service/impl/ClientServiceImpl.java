package casaart.emails_clients_db.service.impl;

import casaart.emails_clients_db.model.dto.AddClientDTO;
import casaart.emails_clients_db.model.dto.ClientDTO;
import casaart.emails_clients_db.model.entity.Client;
import casaart.emails_clients_db.model.entity.User;
import casaart.emails_clients_db.model.enums.SourceType;
import casaart.emails_clients_db.repository.ClientRepository;
import casaart.emails_clients_db.service.ClientService;
import casaart.emails_clients_db.service.UserHelperService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

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

    //get all clients
    @Override
    public List<ClientDTO> getAllClients() {
        List<Client> clients = clientRepository.findAll();

        return mapToClientDTOList(clients);
    }

    //get sorted clients
    @Override
    public List<ClientDTO> sortedClients(String sortRule) {
        List<Client> sortedClientList = new ArrayList<>();
        sortRule = sortRule.trim();
        String[] words = sortRule.split("\\s+");
        String oneName = "^[А-Я][а-я]*$";

        if ("creatDate".equals(words[0])) {
            sortedClientList = clientRepository.findAllByOrderByCreatDateDesc();
        } else if ("modifyDate".equals(words[0])) {
            sortedClientList = clientRepository.findAllByOrderByModifyDateDesc();
        } else if ("addedFrom".equals(words[0])) {
            sortedClientList = clientRepository.findAllByOrderByUserUsernameAsc();
        } else if ("firstAndLastName".equals(words[0])) {
            sortedClientList = clientRepository.findAllByOrderByFirstNameAscLastNameAsc();
        } else if ("allClients".equals(words[0])) {
            return getAllClients();
        } else if ("clientFullName".equals(words[0])) {
            return getAllClients();
        } else if (words.length == 2) {
            String firstName = words[0];
            String lastName = words[1];

            sortedClientList = clientRepository.findByFirstNameAndLastName(firstName, lastName);
        } else if (words.length == 3) {
            String firstName = words[0];
            String middleName = words[1];
            String lastName = words[2];

            sortedClientList = clientRepository.findByFirstNameAndMiddleNameAndLastName(firstName, middleName, lastName);
        } else if (Pattern.matches(oneName, words[0])) {
            sortedClientList = clientRepository.findByFirstName(words[0]);
        } else if (!words[0].equals("REGISTRATION") &&
                !"ORDER".equals(words[0]) &&
                !"HOTEL".equals(words[0]) &&
                !"SHOW_ROOM".equals(words[0]) &&
                !"ONLINE_SHOP".equals(words[0])) {
            return getAllClients();
        } else {
            sortedClientList = clientRepository.findAllBySourceType(SourceType.valueOf(words[0]));
        }

        return mapToClientDTOList(sortedClientList);
    }

    //checking is exist client email
    @Override
    public boolean isExistClientEmail(String email) {

        return clientRepository.findByEmail(email).isPresent();
    }

    //add client
    @Override
    public void addClient(AddClientDTO addClientDTO) {
        Client client = mapper.map(addClientDTO, Client.class);
        client.setUser(userHelperService.getUser());
        client.setModifyFrom(userHelperService.getUser().getUsername());
        client.setCreatDate(LocalDate.now());
        client.setModifyDate(LocalDate.now());

        clientRepository.save(client);
    }

    //find client by id
    @Override
    public ClientDTO findClientById(long id) {
        Client client = clientRepository.findById(id);

        ClientDTO clientDTO = mapper.map(client, ClientDTO.class);
        clientDTO.setAddedFrom(client.getSourceType().name());

        return clientDTO;
    }

    //edit client
    @Override
    public void editClient(ClientDTO clientDTO) {
        Client client = clientRepository.findById(clientDTO.getId());
        User user = client.getUser();
        LocalDate createdDate = client.getCreatDate();

        client = mapper.map(clientDTO, Client.class);
        client.setModifyDate(LocalDate.now());
        client.setCreatDate(createdDate);
        client.setUser(user);
        client.setModifyFrom(userHelperService.getUser().getUsername());

        clientRepository.save(client);
    }

    //delete client by id
    @Override
    public void deleteClient(long id) {
        clientRepository.deleteById(id);
    }

    ClientDTO mapToClientDTO(Client client) {
        ClientDTO clientDTO = mapper.map(client, ClientDTO.class);
        clientDTO.setAddedFrom(client.getUser().getUsername());

        return clientDTO;
    }

    List<ClientDTO> mapToClientDTOList(List<Client> clientList) {
        List<ClientDTO> allClientDTOS = new ArrayList<>();

        for (Client client : clientList) {
            ClientDTO clientDTO = mapToClientDTO(client);

            allClientDTOS.add(clientDTO);
        }

        return allClientDTOS;
    }
}
