package casaart.emails_clients_db.service;

import casaart.emails_clients_db.model.dto.AddClientDTO;
import casaart.emails_clients_db.model.dto.ClientDTO;

import java.util.List;

public interface ClientService {
    //get all clients
    List<ClientDTO> getAllClients();
    //get sorted clients
    List<ClientDTO> sortedClients(String sourceTypeName);
    //checking is exist client email
    boolean isExistClientEmail(String email);
    //add client
   void addClient(AddClientDTO addClientDTO);
   //find client by id
    ClientDTO findClientById(long id);
    //edit client
    void editClient(ClientDTO clientDTO);
    //delete client by id
    void deleteClient(long id);
}
