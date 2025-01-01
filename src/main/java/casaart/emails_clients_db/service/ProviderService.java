package casaart.emails_clients_db.service;

import casaart.emails_clients_db.model.dto.AddProviderDTO;
import casaart.emails_clients_db.model.dto.ProviderDTO;

import java.util.List;

public interface ProviderService {
    //get all providers
    List<ProviderDTO> allProviders();
    //checking is exist provider
    boolean isExistProvider(String name);
    //add provider
    void addProvider(AddProviderDTO addProviderDTO);
    //edit provider
    void editProvider(ProviderDTO providerDTO);
}
