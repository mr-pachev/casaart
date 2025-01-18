package casaart.emails_clients_db.service;

import casaart.emails_clients_db.model.dto.IndustryDTO;
import casaart.emails_clients_db.model.entity.Industry;

import java.util.List;

public interface IndustryService {
    //get all industries
    List<Industry> getAllIndustries();
}
