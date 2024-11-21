package casaart.emails_clients_db.service.impl;

import casaart.emails_clients_db.model.dto.AddClientDTO;
import casaart.emails_clients_db.model.entity.Client;
import casaart.emails_clients_db.repository.ClientRepository;
import casaart.emails_clients_db.service.ClientService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;
    private final ModelMapper mapper;

    public ClientServiceImpl(ClientRepository clientRepository, ModelMapper mapper) {
        this.clientRepository = clientRepository;
        this.mapper = mapper;
    }

    @Override
    public List<AddClientDTO> getAllClients() {
        List<AddClientDTO> addClientDTOS = new ArrayList<>();
        List<Client> clients = clientRepository.findAll();

        for (Client client : clients) {
            AddClientDTO addClientDTO = mapToAddClientDTO(client);

            addClientDTOS.add(addClientDTO);
        }


        return addClientDTOS;
    }

    AddClientDTO mapToAddClientDTO(Client client){
        AddClientDTO addClientDTO = mapper.map(client, AddClientDTO.class);

        return addClientDTO;
    }
}
