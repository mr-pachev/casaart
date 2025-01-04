package casaart.emails_clients_db.service.impl;

import casaart.emails_clients_db.repository.SerialNumberRepository;
import casaart.emails_clients_db.service.SerialNumberService;
import org.springframework.stereotype.Service;

@Service
public class SerialNumberServiceImpl implements SerialNumberService {
    private final SerialNumberRepository serialNumberRepository;

    public SerialNumberServiceImpl(SerialNumberRepository serialNumberRepository) {
        this.serialNumberRepository = serialNumberRepository;
    }

    //is exist serial number
    @Override
    public boolean isExistSn(String serialNumber) {
        return serialNumberRepository.findBySerialNumber(serialNumber).isPresent();
    }
}
