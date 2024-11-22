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

    @ModelAttribute("clientDTO")
    public ClientDTO clientDTO() {
        return new ClientDTO();
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
    public String addClient(
            @Valid AddClientDTO addClientDTO,
            BindingResult bindingResult,
            RedirectAttributes rAtt) {

        if (bindingResult.hasErrors()) {
            rAtt.addFlashAttribute("addClientDTO", addClientDTO);
            rAtt.addFlashAttribute("org.springframework.validation.BindingResult.addClientDTO", bindingResult);

            return "redirect:/add-client";
        }
//
//        if (userService.isExistUser(addClientDTO.getUsername())) {
//            rAtt.addFlashAttribute("addClientDTO", addClientDTO);
//            rAtt.addFlashAttribute("noAddedClientr", true);
//            rAtt.addFlashAttribute("org.springframework.validation.BindingResult.addClientDTO", bindingResult);
//
//            return "redirect:/add-client";
//        }
//
        clientService.addClient(addClientDTO);
//
        return "redirect:/clients";
    }
}
