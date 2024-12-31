package casaart.emails_clients_db.service;

import casaart.emails_clients_db.model.dto.AddTypeDTO;
import casaart.emails_clients_db.model.dto.TypeDTO;

import java.util.List;

public interface TypeService {
    //get all types
    List<TypeDTO> getAllTypes();
    //get all types by category
    List<TypeDTO> getAllTypesByCategory(String categoryNAme);
    //get type by id
    TypeDTO findTypeById(long id);
    //checking is exist type
    boolean isExistType(String name);
    //checking is exist type code
    boolean isExistTypeCode(String code);
    //add type
    void addType(AddTypeDTO addTypeDTO);
    //edit type
    void editType(TypeDTO typeDTO);
    //delete type
    void deleteType(long id);
}
