package casaart.emails_clients_db.web;

import casaart.emails_clients_db.model.dto.AddClientDTO;
import casaart.emails_clients_db.model.dto.AddUserDTO;
import casaart.emails_clients_db.model.dto.ClientDTO;
import casaart.emails_clients_db.model.enums.SourceType;
import casaart.emails_clients_db.service.ClientService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class ClientController {
    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @ModelAttribute("addClientDTO")
    public AddClientDTO addClientDTO() {
        return new AddClientDTO();
    }

    //view all clients
    @GetMapping("/clients")
    public String getAllClients(Model model) {
        List<ClientDTO> clientDTOS = clientService.getAllClients();

        model.addAttribute("allClients", clientDTOS);

        return "clients";
    }

    //create new client
    @GetMapping("/add-client")
    public String viewAddClientForm(Model model) {
        model.addAttribute("sourceType", SourceType.values());
        return "add-client";
    }

    @PostMapping("/add-client")
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

            return "redirect:/add-client";
        }

        boolean confirmPassword = addUserDTO.getPassword().equals(addUserDTO.getConfirmPassword());

        if (!confirmPassword) {
            rAtt.addFlashAttribute("addUserDTO", addUserDTO);
//            rAtt.addFlashAttribute("unconfirmed", true);
            rAtt.addFlashAttribute("org.springframework.validation.BindingResult.addUserDTO", bindingResult);

            return "redirect:/add-client";
        }

        userService.addUser(addUserDTO);

        return "redirect:/clients";
    }
}
