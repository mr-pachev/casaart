package casaart.emails_clients_db.service.impl;

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
    public List<String> allProviders() {
        List<Provider> providers = providerRepository.findAll();
        List<String> providersName = new ArrayList<>();

        for (Provider provider : providers) {
            providersName.add(provider.getName());
        }

        return providersName;
    }
}
