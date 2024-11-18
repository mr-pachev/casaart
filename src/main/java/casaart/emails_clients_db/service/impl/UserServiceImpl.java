package casaart.emails_clients_db.service.impl;

import casaart.emails_clients_db.model.dto.AddUserDTO;
import casaart.emails_clients_db.model.entity.User;
import casaart.emails_clients_db.model.enums.RoleName;
import casaart.emails_clients_db.repository.RoleRepository;
import casaart.emails_clients_db.repository.UserRepository;
import casaart.emails_clients_db.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final ModelMapper mapper;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, ModelMapper mapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.mapper = mapper;
    }
    //get all users
    @Override
    public List<AddUserDTO> getAllUsers() {
        List<AddUserDTO> users = new ArrayList<>();

        for (User user : userRepository.findAll()) {
            users.add(mapToDTO(user));
        }
        return users;
    }

    //checking is exist user by username
    @Override
    public boolean isExistUser(String username) {
        return  getAllUsers()
                .stream()
                .anyMatch(userDTO -> userDTO.getUsername().equals(username));
    }
    //add new user
    @Override
    public void addUser(AddUserDTO addUserDTO) {
        User user = mapper.map(addUserDTO, User.class);

        if(userRepository.count() == 0){
            user.setRole(roleRepository.findByRoleName(RoleName.Admin));
        }else {
            user.setRole(roleRepository.findByRoleName(RoleName.User));
        }

        user.setPassword(passwordEncoder.encode(addUserDTO.getPassword()));

        userRepository.save(user);
    }

    AddUserDTO mapToDTO(User user){
        AddUserDTO addUserDTO = mapper.map(user, AddUserDTO.class);
//        addUserDTO.setRole(user.getRole().getRoleName().name());
        return addUserDTO;
    }
}
