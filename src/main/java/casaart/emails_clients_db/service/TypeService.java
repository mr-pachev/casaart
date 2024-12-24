package casaart.emails_clients_db.service;

import casaart.emails_clients_db.model.dto.AddCategoryDTO;
import casaart.emails_clients_db.model.dto.AddTypeDTO;
import casaart.emails_clients_db.model.dto.CategoryDTO;
import casaart.emails_clients_db.model.dto.TypeDTO;

import java.util.List;

public interface TypeService {
    //get all types
    List<TypeDTO> getAllTypes();
    //checking is exist type
    boolean isExistType(String name);
    //add type
    void addType(AddTypeDTO addTypeDTO);
}
