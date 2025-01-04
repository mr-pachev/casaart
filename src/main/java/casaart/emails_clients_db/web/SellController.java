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
    public String showSellPage(Model model) {
        // Инициализиране на списъка със серийни номера, ако все още не е наличен
        if (!model.containsAttribute("serialNumbers")) {
            model.addAttribute("serialNumbers", new ArrayList<String>());
        }
        return "sell";
    }

    @PostMapping("/sell")
    public String handleSellActions(
            @RequestParam(name = "action", required = false) String action,
            @RequestParam(name = "serialInput", required = false) String serialInput,
            @RequestParam(name = "serialNumbers", required = false) List<String> serialNumbers,
            @RequestParam(name = "remove", required = false) Integer removeIndex,
            Model model) {

        // Ако списъкът със серийни номера е null, инициализирай го
        if (serialNumbers == null) {
            serialNumbers = new ArrayList<>();
        }

        switch (action) {
            case "add":
                if (serialInput != null && !serialInput.isBlank()) {
                    serialNumbers.add(serialInput);
                }
                break;
            case "remove":
                if (removeIndex != null && removeIndex >= 0 && removeIndex < serialNumbers.size()) {
                    serialNumbers.remove(removeIndex.intValue());
                }
                break;
            case "sell":
                // Логика за обработка на продажба
                performSell(serialNumbers);
                serialNumbers.clear(); // Изчисти списъка след успешна продажба
                break;
            default:
                // Непозната операция
                break;
        }

        // Обнови модела със списъка
        model.addAttribute("serialNumbers", serialNumbers);
        return "sell";
    }

    private void performSell(List<String> serialNumbers) {
        // Логика за продажба, например запис в база данни или друга обработка
        productService.sellingProducts(serialNumbers);
    }
}
