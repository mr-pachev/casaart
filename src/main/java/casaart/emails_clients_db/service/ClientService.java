package casaart.emails_clients_db.service;

import casaart.emails_clients_db.model.dto.AddClientDTO;
import casaart.emails_clients_db.model.dto.ClientDTO;
import casaart.emails_clients_db.model.dto.CompanyDTO;

import java.util.List;

public interface ClientService {
    //get all clients
    List<ClientDTO> getAllClients();
    //get sorted clients
    List<ClientDTO> sortedClients(String type);
    // get sorted clients by sourceType
    List<ClientDTO> sortedClientsBySourceType(String sourceType);
    // get sorted clients by loyaltyLevel
    List<ClientDTO> sortedClientsByLoyaltyLevel(String loyaltyLevel);
    // get sorted clients by nationality
    List<ClientDTO> sortedClientsByNationality(String nationality);
    // get sorted clients by rating
    List<ClientDTO> sortedClientsByRating(String ratingType, String ratingValue);
    // checking is exist client email
    boolean isExistClientEmail(String email);
    // add client
   void addClient(AddClientDTO addClientDTO);
   // find client by id
    ClientDTO findClientById(long id);
    // edit client
    void editClient(ClientDTO clientDTO);
    // delete client by id
    void deleteClient(long id);
}
