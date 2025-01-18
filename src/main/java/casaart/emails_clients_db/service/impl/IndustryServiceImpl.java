package casaart.emails_clients_db.service.impl;

import casaart.emails_clients_db.model.dto.IndustryDTO;
import casaart.emails_clients_db.model.entity.Industry;
import casaart.emails_clients_db.repository.IndustryRepository;
import casaart.emails_clients_db.service.IndustryService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class IndustryServiceImpl implements IndustryService {
    private final IndustryRepository industryRepository;
    private final ModelMapper mapper;

    public IndustryServiceImpl(IndustryRepository industryRepository, ModelMapper mapper) {
        this.industryRepository = industryRepository;
        this.mapper = mapper;
    }

    //get all industries
    @Override
    public List<IndustryDTO> getAllIndustries() {
        List<IndustryDTO> industryDTOS = new ArrayList<>();
        for (Industry industry : industryRepository.findAll()) {
            IndustryDTO industryDTO = mapper.map(industry, IndustryDTO.class);

            industryDTOS.add(industryDTO);
        }

        return industryDTOS;
    }
}
