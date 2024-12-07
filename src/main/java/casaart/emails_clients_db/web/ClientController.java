package casaart.emails_clients_db.web;

import casaart.emails_clients_db.model.dto.AddClientDTO;
import casaart.emails_clients_db.model.dto.ClientDTO;
import casaart.emails_clients_db.model.enums.SourceType;
import casaart.emails_clients_db.service.ClientService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
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

        if (!model.containsAttribute("isExistEmail")) {
            model.addAttribute("isExistEmail", false);
        }

        return "add-client";
    }

    @PostMapping("/add-client")
    public String addClient(
            @Valid AddClientDTO addClientDTO,
            BindingResult bindingResult,
            RedirectAttributes rAtt, Model model) {

        if (bindingResult.hasErrors()) {
            rAtt.addFlashAttribute("addClientDTO", addClientDTO);
            rAtt.addFlashAttribute("org.springframework.validation.BindingResult.addClientDTO", bindingResult);

            return "redirect:/add-client";
        }

        if (clientService.isExistClientEmail(addClientDTO.getEmail())) {
            rAtt.addFlashAttribute("addClientDTO", addClientDTO);
            rAtt.addFlashAttribute("isExistEmail", true);
            rAtt.addFlashAttribute("org.springframework.validation.BindingResult.addClientDTO", bindingResult);

            return "redirect:/add-client";
        }

        clientService.addClient(addClientDTO);

        return "redirect:/clients";
    }
    //edit current client
    @PostMapping("/client-details/{id}")
    public String referenceToEdithClientForm(@PathVariable("id") Long id){

        return "redirect:/client-details/" + id;
    }

    @GetMapping("/client-details/{id}")
    public String fillEditClientForm(@PathVariable("id") Long id, Model model) {
        ClientDTO  clientDTO = clientService.findClientById(id);
        model.addAttribute(clientDTO);
        model.addAttribute("sourceType", SourceType.values());

        return "client-details";
    }

    @PostMapping("/client-details")
    public String edithClient(@RequestParam("id") Long id,
                              @Valid ClientDTO clientDTO,
                              BindingResult bindingResult,
                              RedirectAttributes rAtt,
                              Model model){

        clientDTO.setId(id);

        if(bindingResult.hasErrors()){
            rAtt.addFlashAttribute("clientDTO", clientDTO);
            rAtt.addFlashAttribute("org.springframework.validation.BindingResult.clientDTO", bindingResult);
            model.addAttribute("sourceType", SourceType.values());

            return "client-details";
        }

        boolean isChangedEmail = !clientDTO.getEmail().equals(clientService.findClientById(id).getEmail());

        if(isChangedEmail && clientService.isExistClientEmail(clientDTO.getEmail())){
            rAtt.addFlashAttribute("clientDTO", clientDTO);
            rAtt.addFlashAttribute("org.springframework.validation.BindingResult.clientDTO", bindingResult);
            model.addAttribute("sourceType", SourceType.values());
            model.addAttribute("isExistEmail", true);

            return "client-details";
        }

        clientService.editClient(clientDTO);
        return "redirect:/clients";
    }
    //delete client by id
    @PostMapping("/delete-client/{id}")
    public String removeClient(@PathVariable("id") Long id) {

        clientService.deleteClient(id);

        return "redirect:/clients";
    }
}
