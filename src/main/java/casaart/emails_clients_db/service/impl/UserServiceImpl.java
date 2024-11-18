package casaart.emails_clients_db.service.impl;

import casaart.emails_clients_db.model.dto.AddUserDTO;
import casaart.emails_clients_db.model.entity.User;
import casaart.emails_clients_db.repository.UserRepository;
import casaart.emails_clients_db.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper mapper;

    public UserServiceImpl(UserRepository userRepository, ModelMapper mapper) {
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Override
    public List<AddUserDTO> getAllUsers() {
        List<AddUserDTO> users = new ArrayList<>();

        for (User user : userRepository.findAll()) {
            users.add(mapToDTO(user));
        }
        return users;
    }

    @Override
    public boolean isExistUser(String username) {
        return  getAllUsers()
                .stream()
                .anyMatch(userDTO -> userDTO.getUsername().equals(username));
    }
    AddUserDTO mapToDTO(User user){
        AddUserDTO addUserDTO = mapper.map(user, AddUserDTO.class);
//        addUserDTO.setRole(user.getRole().getRoleName().name());
        return addUserDTO;
    }
}
