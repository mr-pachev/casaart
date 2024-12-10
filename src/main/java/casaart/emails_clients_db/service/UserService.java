package casaart.emails_clients_db.service;

import casaart.emails_clients_db.model.dto.AddUserDTO;
import casaart.emails_clients_db.model.dto.UserDTO;

import java.util.List;

public interface UserService {
    //get all users
    List<UserDTO> getAllUsers();
    //checking is exist user by username
    boolean isExistUser(String username);
    //find user by id
    UserDTO findUserById(long id);
    //add new user
    void addUser(AddUserDTO addUserDTO);
    //edit usera
    void editUser(UserDTO userDTO);
}
