package casaart.emails_clients_db.service.impl;

import casaart.emails_clients_db.model.dto.AddProviderDTO;
import casaart.emails_clients_db.model.dto.ProviderDTO;
import casaart.emails_clients_db.model.entity.Provider;
import casaart.emails_clients_db.repository.ProviderRepository;
import casaart.emails_clients_db.service.ProviderService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProviderServiceImpl implements ProviderService {
    private final ProviderRepository providerRepository;
    private final ModelMapper mapper;

    public ProviderServiceImpl(ProviderRepository providerRepository, ModelMapper mapper) {
        this.providerRepository = providerRepository;
        this.mapper = mapper;
    }

    //get all providers
    @Override
    public List<ProviderDTO> allProviders() {
        List<Provider> providers = providerRepository.findAll();

        return providerListToProviderDTOList(providers);
    }

    //checking is exist provider
    @Override
    public boolean isExistProvider(String name) {
        return providerRepository.findByName(name).isPresent();
    }

    //add provider
    @Override
    public void addProvider(AddProviderDTO addProviderDTO) {
        Provider provider = mapper.map(addProviderDTO, Provider.class);

        providerRepository.save(provider);
    }

    List<ProviderDTO> providerListToProviderDTOList(List<Provider> providers) {
        List<ProviderDTO> providerDTOS = new ArrayList<>();

        for (Provider provider : providers) {
            ProviderDTO providerDTO = mapper.map(provider, ProviderDTO.class);

            providerDTOS.add(providerDTO);
        }
        return providerDTOS;
    }
}
