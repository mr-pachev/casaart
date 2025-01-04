package casaart.emails_clients_db.service.impl;

import casaart.emails_clients_db.model.dto.AddTypeDTO;
import casaart.emails_clients_db.model.dto.ProductDTO;
import casaart.emails_clients_db.model.dto.TypeDTO;
import casaart.emails_clients_db.model.entity.Category;
import casaart.emails_clients_db.model.entity.Product;
import casaart.emails_clients_db.model.entity.Type;
import casaart.emails_clients_db.repository.CategoryRepository;
import casaart.emails_clients_db.repository.ProductRepository;
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
    private final ProductRepository productRepository;
    private final ModelMapper mapper;

    public TypeServiceImpl(TypeRepository typeRepository, CategoryRepository categoryRepository, ProductRepository productRepository, ModelMapper mapper) {
        this.typeRepository = typeRepository;
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.mapper = mapper;
    }

    @Override
    public List<TypeDTO> getAllTypes() {
        List<Type> types = typeRepository.findAll();

        return typeListToTypeDTOList(types);
    }

    //get all types by category
    @Override
    public List<TypeDTO> getAllTypesByCategory(String categoryNAme) {
        return null;
    }

    //get type by id
    @Override
    public TypeDTO findTypeById(long id) {
        Type type = typeRepository.findById(id).get();
        TypeDTO typeDTO = mapper.map(type, TypeDTO.class);
        List<ProductDTO> productDTOS = new ArrayList<>();

        typeDTO.setCategory(type.getCategory().getName());
        for (Product product : type.getProducts()) {
            ProductDTO productDTO = mapper.map(product, ProductDTO.class);
            productDTO.setPcs(product.getSerialNumbers().size());

            productDTOS.add(productDTO);
        }
        typeDTO.setProducts(productDTOS);

        return typeDTO;
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

    //edit type
    @Override
    public void editType(TypeDTO typeDTO) {
        Type type = typeRepository.findById(typeDTO.getId()).get();
        type.setName(typeDTO.getName());
        type.setCode(typeDTO.getCode());
        type.setCategory(categoryRepository.findByName(typeDTO.getCategory()).get());

        typeRepository.save(type);

        List<Product> products = productRepository.findAllByTypeName(typeDTO.getName());

        for (Product product : products) {
            product.updateSerialNumbersOnTypeChange();

            productRepository.save(product);
        }
    }

    //delete type
    @Override
    public void deleteType(long id) {
        typeRepository.deleteById(id);
    }

    List<TypeDTO> typeListToTypeDTOList(List<Type> types) {
        List<TypeDTO> typeDTOS = new ArrayList<>();

        for (Type type : types) {
            TypeDTO typeDTO = mapper.map(type, TypeDTO.class);

            typeDTO.setCategory(type.getCategory().getName());
            typeDTOS.add(typeDTO);
        }
        return typeDTOS;
    }

}
