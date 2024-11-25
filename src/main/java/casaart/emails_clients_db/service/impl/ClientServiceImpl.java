package casaart.emails_clients_db.service.impl;

import casaart.emails_clients_db.model.dto.AddClientDTO;
import casaart.emails_clients_db.model.dto.ClientDTO;
import casaart.emails_clients_db.model.entity.Client;
import casaart.emails_clients_db.repository.ClientRepository;
import casaart.emails_clients_db.service.ClientService;
import casaart.emails_clients_db.service.UserHelperService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    @Override
    public List<ClientDTO> getAllClients() {
        List<ClientDTO> addClientDTOS = new ArrayList<>();
        List<Client> clients = clientRepository.findAll();

        for (Client client : clients) {
            ClientDTO clientDTO = mapToClientDTO(client);

            addClientDTOS.add(clientDTO);
        }


        return addClientDTOS;
    }

    @Override
    public boolean isExistClient(String fullName) {
        List<String> nameParts = Arrays.asList(fullName.split(" "));

        Client client = switch (nameParts.size()) {
            case 1 -> clientRepository.findByFirstName(nameParts.get(0)).orElseThrow();
            case 2 -> clientRepository.findByFirstNameAndLastName(nameParts.get(0), nameParts.get(1)).orElseThrow();
            case 3 -> clientRepository.findByFirstNameAndMiddleNameAndLastName(nameParts.get(0), nameParts.get(1), nameParts.get(2)).orElseThrow();
            default -> throw new IllegalStateException("Unexpected value: " + nameParts.size());
        };

        return true;
    }

    //add client
    @Override
    public void addClient(AddClientDTO addClientDTO) {
        Client client = mapper.map(addClientDTO, Client.class);
        client.setUser(userHelperService.getUser());
        client.setCreatDate(LocalDate.now());
        client.setModifyDate(LocalDate.now());

        clientRepository.save(client);
    }

    ClientDTO mapToClientDTO(Client client) {
        ClientDTO clientDTO = mapper.map(client, ClientDTO.class);
        clientDTO.setAddedFrom(client.getUser().getUsername());

        return clientDTO;
    }
}