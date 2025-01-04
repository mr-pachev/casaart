package casaart.emails_clients_db.web;

import casaart.emails_clients_db.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class SellController {
    private final List<String> serialNumbers = new ArrayList<>();
    private final ProductService productService;

    public SellController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/sell")
    public String showSellPage() {
        return "sell"; // Зарежда страницата с формата
    }

    @PostMapping("/sell")
    public String handleAction(@RequestParam String action,
                               @RequestParam(required = false) String serialInput,
                               @RequestParam(required = false) List<String> serialNumbers,
                               Model model) {

        if ("add".equals(action) && serialInput != null && !serialInput.trim().isEmpty()) {
            this.serialNumbers.add(serialInput.trim());
        } else if ("sell".equals(action)) {
            // Обработка на списъка с добавени сериен номера
            productService.sellingProducts(this.serialNumbers);
            this.serialNumbers.clear(); // Изчистване на списъка след продажбата
        }

        model.addAttribute("serialNumbers", this.serialNumbers);
        return "sell"; // Връщаме същата страница
    }
}
