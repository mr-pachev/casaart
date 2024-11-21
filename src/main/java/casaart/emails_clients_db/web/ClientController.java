package casaart.emails_clients_db.web;

import casaart.emails_clients_db.model.dto.AddClientDTO;
import casaart.emails_clients_db.model.dto.AddUserDTO;
import casaart.emails_clients_db.service.ClientService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

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
        List<AddClientDTO> addClientDTOS = clientService.getAllClients();

        model.addAttribute("allClients", addClientDTOS);

        return "clients";
    }
}
