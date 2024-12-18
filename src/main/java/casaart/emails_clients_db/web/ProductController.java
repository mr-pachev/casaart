package casaart.emails_clients_db.web;

import casaart.emails_clients_db.model.entity.Product;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Value("${upload.path}")
    private String uploadPath;

    //Upload image
    @PostMapping("/add")
    public String addProduct(@ModelAttribute Product product,
                             @RequestParam("image") MultipartFile image,
                             Model model) {
        try {
            // Проверка дали има качен файл
            if (!image.isEmpty()) {
                String fileName = UUID.randomUUID().toString() + "_" + image.getOriginalFilename();
                Path filePath = Paths.get(uploadPath, fileName);

                // Съхраняване на файла
                Files.createDirectories(filePath.getParent());
                Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                // Запазване на пътя до файла в продукта
                product.setImagePath("/uploads/" + fileName);
            }

            // Тук добави логика за запазване на продукта в базата данни
            // Например: productRepository.save(product);

            model.addAttribute("successMessage", "Продуктът е добавен успешно!");
        } catch (IOException e) {
            model.addAttribute("errorMessage", "Грешка при качване на файла!");
            e.printStackTrace();
        }

        return "redirect:/products"; // Пренасочване към списъка с продукти
    }
}

