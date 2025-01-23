package casaart.emails_clients_db.service.impl;

import casaart.emails_clients_db.repository.SerialNumberRepository;
import casaart.emails_clients_db.service.SerialNumberService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class SerialNumberServiceImpl implements SerialNumberService {
    private final SerialNumberRepository serialNumberRepository;

    private final ModelMapper mapper;

    public SerialNumberServiceImpl(SerialNumberRepository serialNumberRepository, ModelMapper mapper) {
        this.serialNumberRepository = serialNumberRepository;
        this.mapper = mapper;
    }

    //is exist serial number
    @Override
    public boolean isExistSn(String serialNumber) {
        return serialNumberRepository.findBySerialNumber(serialNumber).isPresent();
    }
}
