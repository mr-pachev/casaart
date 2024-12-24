package casaart.emails_clients_db.service.impl;

import casaart.emails_clients_db.model.dto.AddTypeDTO;
import casaart.emails_clients_db.model.dto.CategoryDTO;
import casaart.emails_clients_db.model.dto.TypeDTO;
import casaart.emails_clients_db.model.entity.Category;
import casaart.emails_clients_db.model.entity.Type;
import casaart.emails_clients_db.repository.TypeRepository;
import casaart.emails_clients_db.service.TypeService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TypeServiceImpl implements TypeService {
    private final TypeRepository typeRepository;
    private final ModelMapper mapper;

    public TypeServiceImpl(TypeRepository typeRepository, ModelMapper mapper) {
        this.typeRepository = typeRepository;
        this.mapper = mapper;
    }

    @Override
    public List<TypeDTO> getAllTypes() {
        List<Type> types = typeRepository.findAll();
        List<TypeDTO> typeDTOS = new ArrayList<>();

        for (Type type : types) {
            TypeDTO typeDTO = mapper.map(type, TypeDTO.class);

            typeDTOS.add(typeDTO);
        }

        return typeDTOS;
    }

    //checking is exist type
    @Override
    public boolean isExistType(String name) {
        return typeRepository.findByName(name).isPresent();
    }

    //add type
    @Override
    public void addType(AddTypeDTO addTypeDTO) {


    }
}
