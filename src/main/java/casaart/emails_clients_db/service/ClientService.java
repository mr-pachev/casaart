package casaart.emails_clients_db.service;

import casaart.emails_clients_db.model.dto.AddClientDTO;
import casaart.emails_clients_db.model.dto.ClientDTO;

import java.util.List;

public interface ClientService {
    //get all clients
    List<ClientDTO> getAllClients();
}
