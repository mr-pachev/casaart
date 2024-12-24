package casaart.emails_clients_db.service.impl;

import casaart.emails_clients_db.model.dto.AddTypeDTO;
import casaart.emails_clients_db.model.dto.TypeDTO;
import casaart.emails_clients_db.model.entity.Category;
import casaart.emails_clients_db.model.entity.Type;
import casaart.emails_clients_db.repository.CategoryRepository;
import casaart.emails_clients_db.repository.TypeRepository;
import casaart.emails_clients_db.service.TypeService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TypeServiceImpl implements TypeService {
    private final TypeRepository typeRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper mapper;

    public TypeServiceImpl(TypeRepository typeRepository, CategoryRepository categoryRepository, ModelMapper mapper) {
        this.typeRepository = typeRepository;
        this.categoryRepository = categoryRepository;
        this.mapper = mapper;
    }

    @Override
    public List<TypeDTO> getAllTypes() {
        List<Type> types = typeRepository.findAll();
        List<TypeDTO> typeDTOS = new ArrayList<>();

        for (Type type : types) {
            TypeDTO typeDTO = mapper.map(type, TypeDTO.class);

            typeDTO.setCategory(type.getCategory().getName());
            typeDTOS.add(typeDTO);
        }

        return typeDTOS;
    }

    //checking is exist type
    @Override
    public boolean isExistType(String name) {
        return typeRepository.findByName(name).isPresent();
    }

    //checking is exist type code
    @Override
    public boolean isExistTypeCode(String code) {
        return typeRepository.findByCode(code).isPresent();
    }

    //add type
    @Override
    public void addType(AddTypeDTO addTypeDTO) {
        Type type = mapper.map(addTypeDTO, Type.class);
        Category category = categoryRepository.findByName(addTypeDTO.getCategory()).get();

        type.setCategory(category);
        typeRepository.save(type);
    }
}
