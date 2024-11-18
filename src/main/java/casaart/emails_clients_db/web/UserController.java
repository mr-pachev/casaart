package casaart.emails_clients_db.web;

import casaart.emails_clients_db.model.dto.AddUserDTO;
import casaart.emails_clients_db.model.dto.LoginUserDTO;
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


    //login
    @GetMapping("/login")
    public String viewLogin(){

        return "login";
    }

    @GetMapping("/login-error")
    public String viewLoginError(Model model) {
        model.addAttribute("showErrorMessage", true);

        return "login";
    }
}
