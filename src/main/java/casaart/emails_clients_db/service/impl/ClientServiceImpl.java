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
    public List<ClientDTO> sortedClients(String sourceTypeName) {
        if(sourceTypeName.equals("ALL CLIENTS")){
           return getAllClients();
        }
        List<Client> clientsBySourceType = clientRepository.findAllBySourceType(SourceType.valueOf(sourceTypeName));

        return mapToClientDTOList(clientsBySourceType);
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

    List<ClientDTO> mapToClientDTOList(List<Client> clientList){
        List<ClientDTO> allClientDTOS = new ArrayList<>();

        for (Client client : clientList) {
            ClientDTO clientDTO = mapToClientDTO(client);

            allClientDTOS.add(clientDTO);
        }

        return allClientDTOS;
    }
}
