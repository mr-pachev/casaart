package casaart.emails_clients_db.web;

import casaart.emails_clients_db.model.dto.ClientDTO;
import casaart.emails_clients_db.model.dto.OrderDTO;
import casaart.emails_clients_db.service.ClientService;
import casaart.emails_clients_db.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
public class OrderController {
    private final ClientService clientService;
    private final OrderService orderService;

    public OrderController(ClientService clientService, OrderService orderService) {
        this.clientService = clientService;
        this.orderService = orderService;
    }

    @ModelAttribute("orderDTO")
    public OrderDTO orderDTO() {
        return new OrderDTO();
    }

    // view current order by client id
    @PostMapping("/current-orders/{id}")
    public String referenceToViewCurrentOrdersForm(@PathVariable("id") Long id) {

        return "redirect:/current-orders/" + id;
    }

    @GetMapping("/current-orders/{id}")
    public String fillEditCurrentOrdersForm(@PathVariable("id") Long id, Model model) {
        List<OrderDTO> orderDTOS = orderService.currentOrdersByClientId(id);
        ClientDTO clientDTO = clientService.findClientById(id);

        model.addAttribute("orderDTOS", orderDTOS);
        model.addAttribute("clientDTO", clientDTO);

        return "current-orders";
    }

    // add order
    @PostMapping("/add-order-with-client-id/{id}")
    public String referenceToAddOrderForm(@PathVariable("id") Long id) {

        return "redirect:/add-order/" + id;
    }

    @GetMapping("/add-order/{id}")
    public String showAddOrderForm(@PathVariable("id") Long id, Model model) {
        ClientDTO clientDTO = clientService.findClientById(id);
        model.addAttribute("clientDTO", clientDTO);

        return "add-order";
    }

    @PostMapping("/add-order")
    public String addContactPerson(@RequestParam("id") Long id,
                                   @Valid OrderDTO orderDTO,
                                   BindingResult bindingResult,
                                   RedirectAttributes rAtt,
                                   Model model) {

        ClientDTO clientDTO = clientService.findClientById(id);

        if (bindingResult.hasErrors()) {
            model.addAttribute("clientDTO", clientDTO);
            rAtt.addFlashAttribute("orderDTO", orderDTO);
            rAtt.addFlashAttribute("org.springframework.validation.BindingResult.orderDTO", bindingResult);

            return "add-order";
        }

        boolean isExistOrder = orderService.isExistOrder(orderDTO);

        if (isExistOrder) {
            model.addAttribute("clientDTO", clientDTO);
            model.addAttribute("orderDTO", orderDTO);
            model.addAttribute("isExist", true);
            model.addAttribute("org.springframework.validation.BindingResult.orderDTO", bindingResult);

            return "add-order";
        }

        orderDTO.setClientId(id);
        orderService.addOrder(orderDTO, id);

        return "redirect:/current-orders/" + id;
    }




    // edit order
    @PostMapping("/order-details/{id}")
    public String referenceToEditOrderForm(@PathVariable("id") Long id) {

        return "redirect:/order-details/" + id;
    }

    @GetMapping("/order-details/{id}")
    public String fillEditOrderForm(@PathVariable("id") Long id, Model model) {
        OrderDTO orderDTO = orderService.getOrderById(id);
        ClientDTO clientDTO = clientService.findClientById(orderDTO.getClientId());

        model.addAttribute("orderDTO", orderDTO);
        model.addAttribute("clientDTO", clientDTO);

        return "order-details";
    }

    @PostMapping("/order-details")
    public String editOrder(@Valid OrderDTO orderDTO,
                                    BindingResult bindingResult,
                                    RedirectAttributes rAtt,
                                    Model model) {

        ClientDTO clientDTO = clientService.findClientById(orderDTO.getClientId());

        if (bindingResult.hasErrors()) {
            model.addAttribute("clientDTO", clientDTO); // връща се, като модел за да може да си изпише името на клиента в шаблона
            rAtt.addFlashAttribute("orderDTO", orderDTO);
            rAtt.addFlashAttribute("org.springframework.validation.BindingResult.orderDTO", bindingResult);

            return "order-details";
        }

        boolean isChangedNumber = !orderDTO.getNumber().equals(orderService.getOrderById(orderDTO.getId()).getNumber());

        if (isChangedNumber && orderService.isExistOrder(orderDTO)) {
            model.addAttribute("clientDTO", clientDTO);
            model.addAttribute("orderDTO", orderDTO);
            model.addAttribute("isExist", true);
            model.addAttribute("org.springframework.validation.BindingResult.orderDTO", bindingResult);

            return "order-details";
        }

        orderService.editOrder(orderDTO);

        return "redirect:/current-orders/" + clientDTO.getId();
    }








    // delete order
    @PostMapping("/delete-order/{id}")
    public String removeOrder(@PathVariable("id") Long id) {
        OrderDTO orderDTO = orderService.getOrderById(id);
        ClientDTO clientDTO = clientService.findClientById(orderDTO.getClientId());

        orderService.removeOrder(id);

        return "redirect:/current-orders/" + clientDTO.getId();
    }

    // open folder path
    @GetMapping("/open-order-folder/{id}")
    public String openOrderFolder(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        OrderDTO orderDTO = orderService.getOrderById(id);

        String folderPath = orderDTO.getFolderPath();

        if (folderPath != null) {
            try {
                Desktop.getDesktop().open(new File(folderPath));
            } catch (IOException e) {
                redirectAttributes.addFlashAttribute("error", "Неуспешно отваряне на папката.");
            }
        }

        return "redirect:/current-orders/" + orderDTO.getClientId();
    }

}
