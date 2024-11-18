package casaart.emails_clients_db.service;

import casaart.emails_clients_db.model.dto.AddUserDTO;

import java.util.List;

public interface UserService {
    //get all users
    List<AddUserDTO> getAllUsers();
    //checking is exist user by username
    boolean isExistUser(String username);
}
