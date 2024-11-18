package casaart.emails_clients_db.web;

import casaart.emails_clients_db.model.dto.AddUserDTO;
import casaart.emails_clients_db.model.dto.LoginUserDTO;
import casaart.emails_clients_db.model.enums.SourceType;
import casaart.emails_clients_db.service.UserHelperService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {

    @ModelAttribute("addUserDTO")
    public AddUserDTO addUserDTO() {
        return new AddUserDTO();
    }

    @ModelAttribute("loginUserDTO")
    public LoginUserDTO loginUserDTO() {
        return new LoginUserDTO();
    }

    //create new user
    @GetMapping("/registration")
    public String viewAddUserForm(Model model) {
        model.addAttribute("sourceType", SourceType.values());
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

//        if (userService.isExistUser(addUserDTO.getUsername()) ||
//                !employeeService.isExistEmployeeByIN(addUserDTO.getIdentificationNumber())) {
//            rAtt.addFlashAttribute("addUserDTO", addUserDTO);
//            rAtt.addFlashAttribute("noAddedUser", true);
//            rAtt.addFlashAttribute("org.springframework.validation.BindingResult.addUserDTO", bindingResult);
//
//            return "redirect:/registration";
//        }

        boolean confirmPassword = addUserDTO.getPassword().equals(addUserDTO.getConfirmPassword());

        if (!confirmPassword) {
            rAtt.addFlashAttribute("addUserDTO", addUserDTO);
            rAtt.addFlashAttribute("unconfirmed", true);
            rAtt.addFlashAttribute("org.springframework.validation.BindingResult.addUserDTO", bindingResult);

            return "redirect:/registration";
        }

//        userService.addUser(addUserDTO);

        return "redirect:/login";
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
