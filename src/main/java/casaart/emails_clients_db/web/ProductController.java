package casaart.emails_clients_db.web;

import casaart.emails_clients_db.model.dto.AddProductDTO;
import casaart.emails_clients_db.model.dto.ProductDTO;
import casaart.emails_clients_db.model.entity.Product;
import casaart.emails_clients_db.service.ProductService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Controller
//@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @ModelAttribute("addProductDTO")
    public AddProductDTO addProductDTO() {
        return new AddProductDTO();
    }

    @ModelAttribute("productDTO")
    public ProductDTO productDTO() {
        return new ProductDTO();
    }

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

    //view all products
    @GetMapping("/products")
    public String getAllProducts(Model model) {
        List<ProductDTO> productDTOS = productService.getAllProducts();

        model.addAttribute("allProducts", productDTOS);

        return "products";
    }

    //view all sorted products
    @PostMapping("/sort-products")
    public String sortProducts(@RequestParam("sourceType") String sourceType, Model model) {
        List<ProductDTO> sortedProducts = productService.sortedClients(sourceType);
        model.addAttribute("allProducts", sortedProducts);

        return "products"; // Връщаме същия шаблон с актуализиран списък
    }
}

