package casaart.emails_clients_db.web;

import casaart.emails_clients_db.model.dto.AddUserDTO;
import casaart.emails_clients_db.model.dto.ClientDTO;
import casaart.emails_clients_db.model.dto.LoginUserDTO;
import casaart.emails_clients_db.model.dto.UserDTO;
import casaart.emails_clients_db.model.enums.RoleName;
import casaart.emails_clients_db.model.enums.SourceType;
import casaart.emails_clients_db.service.UserHelperService;
import casaart.emails_clients_db.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class UserController {
    private final UserService userService;
    private final UserHelperService userHelperService;

    public UserController(UserService userService, UserHelperService userHelperService) {
        this.userService = userService;
        this.userHelperService = userHelperService;
    }

    @ModelAttribute("addUserDTO")
    public AddUserDTO addUserDTO() {
        return new AddUserDTO();
    }

    @ModelAttribute("userDTO")
    public UserDTO userDTO() {
        return new UserDTO();
    }

    @ModelAttribute("loginUserDTO")
    public LoginUserDTO loginUserDTO() {
        return new LoginUserDTO();
    }

    //view all users
    @GetMapping("/users")
    public String getAllUsers(Model model) {
        List<UserDTO> userDTOS = userService.getAllUsers();

        model.addAttribute("allUsers", userDTOS);

        return "users";
    }

    //create new user
    @GetMapping("/registration")
    public String viewAddUserForm(Model model) {
        model.addAttribute("sourceType", SourceType.values());

        if (!model.containsAttribute("unConfirmedPass")) {
            model.addAttribute("unConfirmedPass", false);
        }

        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(
            @Valid AddUserDTO addUserDTO,
            BindingResult bindingResult,
            RedirectAttributes rAtt) {

        if (bindingResult.hasErrors()) {
            rAtt.addFlashAttribute("addUserDTO", addUserDTO);
            rAtt.addFlashAttribute("org.springframework.validation.BindingResult.addUserDTO", bindingResult);

            return "redirect:/registration";
        }

        if (userService.isExistUser(addUserDTO.getUsername())) {
            rAtt.addFlashAttribute("addUserDTO", addUserDTO);
            rAtt.addFlashAttribute("noAddedUser", true);
            rAtt.addFlashAttribute("org.springframework.validation.BindingResult.addUserDTO", bindingResult);

            return "redirect:/registration";
        }

        boolean confirmPassword = addUserDTO.getPassword().equals(addUserDTO.getConfirmPassword());

        if (!confirmPassword) {
            rAtt.addFlashAttribute("addUserDTO", addUserDTO);
            rAtt.addFlashAttribute("unConfirmedPass", true);
            rAtt.addFlashAttribute("org.springframework.validation.BindingResult.addUserDTO", bindingResult);

            return "redirect:/registration";
        }

        userService.addUser(addUserDTO);

        return "redirect:/login";
    }

    //edit user
    @PostMapping("/user-details/{id}")
    public String referenceToEdithUserForm(@PathVariable("id") Long id) {

        return "redirect:/user-details/" + id;
    }

    @GetMapping("/user-details/{id}")
    public String fillEditUserForm(@PathVariable("id") Long id, Model model) {
        UserDTO userDTO = userService.findUserById(id);
        model.addAttribute(userDTO);
        model.addAttribute("sourceType", SourceType.values());
        model.addAttribute("roles", RoleName.values());

        return "user-details";
    }

    @PostMapping("/user-details")
    public String editUser(@RequestParam("id") Long id,
                              @Valid UserDTO userDTO,
                              BindingResult bindingResult,
                              RedirectAttributes rAtt,
                              Model model) {

        userDTO.setUserId(id);

        if (bindingResult.hasErrors()) {
            rAtt.addFlashAttribute("userDTO", userDTO);
            rAtt.addFlashAttribute("org.springframework.validation.BindingResult.userDTO", bindingResult);
            model.addAttribute("sourceType", SourceType.values());
            model.addAttribute("roles", RoleName.values());

            return "user-details";
        }

//        boolean isChangedEmail = !clientDTO.getEmail().equals(clientService.findClientById(id).getEmail());
//
//        if (isChangedEmail && clientService.isExistClientEmail(clientDTO.getEmail())) {
//            rAtt.addFlashAttribute("clientDTO", clientDTO);
//            rAtt.addFlashAttribute("org.springframework.validation.BindingResult.clientDTO", bindingResult);
//            model.addAttribute("sourceType", SourceType.values());
//              model.addAttribute("roles", RoleName.values());
//            model.addAttribute("isExistEmail", true);
//
//            return "client-details";
//        }
//
//        clientService.editClient(clientDTO);
        return "redirect:/users";
    }

    //login
    @GetMapping("/login")
    public String viewLogin() {

        return "login";
    }

    @GetMapping("/login-error")
    public String viewLoginError(Model model) {
        model.addAttribute("showErrorMessage", true);

        return "login";
    }
}
