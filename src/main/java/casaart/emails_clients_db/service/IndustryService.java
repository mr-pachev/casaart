package casaart.emails_clients_db.service;

import casaart.emails_clients_db.model.dto.IndustryDTO;

import java.util.List;

public interface IndustryService {
    //get all industries
    List<IndustryDTO> getAllIndustries();
}
