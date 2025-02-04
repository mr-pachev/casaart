package casaart.emails_clients_db.service.impl;

import casaart.emails_clients_db.model.dto.AddUserDTO;
import casaart.emails_clients_db.model.dto.UserDTO;
import casaart.emails_clients_db.model.entity.User;
import casaart.emails_clients_db.model.enums.RoleName;
import casaart.emails_clients_db.repository.RoleRepository;
import casaart.emails_clients_db.repository.UserRepository;
import casaart.emails_clients_db.service.UserHelperService;
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
    private final UserHelperService userHelperService;

    private final PasswordEncoder passwordEncoder;

    private final ModelMapper mapper;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, UserHelperService userHelperService, PasswordEncoder passwordEncoder, ModelMapper mapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userHelperService = userHelperService;
        this.passwordEncoder = passwordEncoder;
        this.mapper = mapper;
    }

    //get all users
    @Override
    public List<UserDTO> getAllUsers() {
        List<UserDTO> users = new ArrayList<>();

        for (User user : userRepository.findAll()) {
            users.add(mapToDTO(user));
        }
        return users;
    }

    //checking is exist user by username
    @Override
    public boolean isExistUser(String username) {
        return getAllUsers()
                .stream()
                .anyMatch(userDTO -> userDTO.getUsername().equals(username));
    }

    //find user by id
    @Override
    public UserDTO findUserById(long id) {
        User user = userRepository.findById(id);
        UserDTO userDTO = mapper.map(user, UserDTO.class);
        userDTO.setRole(user.getRole().getRoleName().name());

        return userDTO;
    }

    //add new user
    @Override
    public void addUser(AddUserDTO addUserDTO) {
        User user = mapper.map(addUserDTO, User.class);

        if (userRepository.count() == 0) {
            user.setRole(roleRepository.findByRoleName(RoleName.Admin));
        } else {
            user.setRole(roleRepository.findByRoleName(RoleName.User));
        }

        user.setPassword(passwordEncoder.encode(addUserDTO.getPassword()));

        userRepository.save(user);
    }

    //edit user
    @Override
    public void editUser(UserDTO userDTO) {
        User existingUser = userRepository.findById(userDTO.getId());

        User updatedUser = mapper.map(userDTO, User.class);

        // проверка дали има попълнено поле с парола
        if(userDTO.getPassword().isEmpty() || userDTO.getConfirmPassword().isEmpty()){
            updatedUser.setPassword(existingUser.getPassword());
        }else {
            updatedUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }
        updatedUser.setRole(roleRepository.findByRoleName(RoleName.valueOf(userDTO.getRole())));
        updatedUser.setClients(existingUser.getClients());

        userRepository.save(updatedUser);
    }

    UserDTO mapToDTO(User user) {
        UserDTO userDTO = mapper.map(user, UserDTO.class);
        userDTO.setRole(user.getRole().getRoleName().name());
        return userDTO;
    }
}
