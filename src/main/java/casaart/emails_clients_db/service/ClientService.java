package casaart.emails_clients_db.service;

import casaart.emails_clients_db.model.dto.AddClientDTO;

import java.util.List;

public interface ClientService {
    //get all clients
    List<AddClientDTO> getAllClients();
}
