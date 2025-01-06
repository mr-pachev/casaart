package casaart.emails_clients_db.web;

import casaart.emails_clients_db.service.ProductService;
import casaart.emails_clients_db.service.SerialNumberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class SellController {
    private final ProductService productService;
    private final SerialNumberService serialNumberService;

    public SellController(ProductService productService, SerialNumberService serialNumberService) {
        this.productService = productService;
        this.serialNumberService = serialNumberService;
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
                boolean isNotExist = !serialNumberService.isExistSn(serialInput);
                if (isNotExist){
                    model.addAttribute("isNotExist", true);
                    break;
                }

                // Проверка дали номера вече е въведен за продажба
                if (serialNumbers.contains(serialInput)){
                    model.addAttribute("isExistInList", true);
                    break;
                }

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
                // Проверка за несъществуващ номер в полето за въвеждане
                isNotExist = serialInput != null && !serialInput.isEmpty() && !serialNumberService.isExistSn(serialInput);

                // Проверка дали няма въведен номер и списъкът е празен
                if ((serialInput == null || serialInput.isEmpty()) && serialNumbers.isEmpty()) {
                    model.addAttribute("isEmpty", true);
                    break;
                }

                // Ако е въведен несъществуващ номер и в списъка има въведени номера
                if (!serialNumbers.isEmpty() && isNotExist) {
                    model.addAttribute("isNotExist", true);
                    break;
                }

                boolean isExistInList = serialNumbers.contains(serialInput);

                // Проверка дали номера вече е въведен за продажба
                if (!serialNumbers.isEmpty() && isExistInList){
                    model.addAttribute("isInsertedList", true);
                    break;
                }

                // Ако няма въведен номер, но списъкът съдържа елементи, премини към продажбата
                if (serialNumbers != null && !serialNumbers.isEmpty() && (serialInput == null || serialInput.isEmpty())) {
                    performSell(serialNumbers);
                    serialNumbers.clear(); // Изчистваме списъка след успешна продажба
                    model.addAttribute("isSelled", true); // Добавяме съобщение за успешна продажба
                    break;
                }

                // Ако е въведен само несъществуващ номер, без значение от състоянието на списъка
                if (isNotExist) {
                    model.addAttribute("isNotExist", true);
                    break;
                }
            default:
                // Непозната операция
                break;
        }

        // Обнови модела със списъка
        model.addAttribute("serialNumbers", serialNumbers);
        return "sell";
    }

    private void performSell(List<String> serialNumbers) {
        productService.sellingProducts(serialNumbers);
    }
}
