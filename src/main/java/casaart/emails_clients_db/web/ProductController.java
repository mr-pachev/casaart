package casaart.emails_clients_db.web;

import casaart.emails_clients_db.model.dto.AddProductDTO;
import casaart.emails_clients_db.model.dto.ProductDTO;
import casaart.emails_clients_db.model.entity.Product;
import casaart.emails_clients_db.service.CategoryService;
import casaart.emails_clients_db.service.ProductService;
import casaart.emails_clients_db.service.ProviderService;
import casaart.emails_clients_db.service.TypeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Controller
public class ProductController {
    private final ProductService productService;
    private final ProviderService providerService;
    private final CategoryService categoryService;
    private final TypeService typeService;

    public ProductController(ProductService productService, ProviderService providerService, CategoryService categoryService, TypeService typeService) {
        this.productService = productService;
        this.providerService = providerService;
        this.categoryService = categoryService;
        this.typeService = typeService;
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

    //view all products
    @GetMapping("/products")
    public String getAllProducts(Model model) {
        List<ProductDTO> productDTOS = productService.getAllProducts();

        model.addAttribute("allProducts", productDTOS);

        return "products";
    }

    //create new product
    @GetMapping("/add-product")
    public String viewAddProductForm(Model model) {
        model.addAttribute("allProviders", providerService.allProviders());
        model.addAttribute("allCategories", categoryService.getAllCategory());
        model.addAttribute("allTypes", typeService.getAllTypes());

        if (!model.containsAttribute("isExistName")) {
            model.addAttribute("isExistName", false);
        }

        if (!model.containsAttribute("isExistCode")) {
            model.addAttribute("isExistCode", false);
        }

        return "add-product";
    }

    @PostMapping("/add-product")
    public String addProduct(
            @Valid AddProductDTO addProductDTO,
            BindingResult bindingResult,
            @RequestParam("image") MultipartFile image,
            RedirectAttributes rAtt) {

        if (bindingResult.hasErrors()) {
            rAtt.addFlashAttribute("addProductDTO", addProductDTO);
            rAtt.addFlashAttribute("org.springframework.validation.BindingResult.addProductDTO", bindingResult);

            return "redirect:/add-product";
        }

        if (image.isEmpty()) {
            rAtt.addFlashAttribute("addProductDTO", addProductDTO);
            rAtt.addFlashAttribute("imageError", "Качването на снимка е задължително!");

            return "redirect:/add-product";
        }

        // Записване на файла
        try {
            Path uploadDir = Paths.get(uploadPath);
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            String fileName = image.getOriginalFilename();
            Path filePath = uploadDir.resolve(fileName);
            image.transferTo(filePath.toFile());

            // Свързване на каченото изображение с DTO
            addProductDTO.setImagePath("/uploads/" + fileName);
        } catch (IOException e) {
            rAtt.addFlashAttribute("addProductDTO", addProductDTO);
            rAtt.addFlashAttribute("imageError", "Грешка при качване на снимката!");

            return "redirect:/add-product";
        }

        if (productService.isExistProductName(addProductDTO.getName())) {
            rAtt.addFlashAttribute("addProductDTO", addProductDTO);
            rAtt.addFlashAttribute("isExistName", true);
            rAtt.addFlashAttribute("org.springframework.validation.BindingResult.addProductDTO", bindingResult);

            return "redirect:/add-product";
        }

        if (productService.isExistProductCode(addProductDTO.getProductCode())) {
            rAtt.addFlashAttribute("addProductDTO", addProductDTO);
            rAtt.addFlashAttribute("isExistCode", true);
            rAtt.addFlashAttribute("org.springframework.validation.BindingResult.addProductDTO", bindingResult);

            return "redirect:/add-product";
        }

        productService.addProduct(addProductDTO);

        return "redirect:/products";
    }
}

