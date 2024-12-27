package casaart.emails_clients_db.web;

import casaart.emails_clients_db.model.dto.AddProductDTO;
import casaart.emails_clients_db.model.dto.ProductDTO;
import casaart.emails_clients_db.service.CategoryService;
import casaart.emails_clients_db.service.ProductService;
import casaart.emails_clients_db.service.ProviderService;
import casaart.emails_clients_db.service.TypeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
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

    //edit product
    @PostMapping("/product-details/{id}")
    public String referenceToEditProductForm(@PathVariable("id") Long id) {

        return "redirect:/product-details/" + id;
    }

    @GetMapping("/product-details/{id}")
    public String fillEditProductForm(@PathVariable("id") Long id, Model model) {
        ProductDTO productDTO = productService.findProductById(id);

        model.addAttribute(productDTO);
        model.addAttribute("allProviders", providerService.allProviders());
        model.addAttribute("allCategories", categoryService.getAllCategory());
        model.addAttribute("allTypes", typeService.getAllTypes());

        return "product-details";
    }

    @PostMapping("/product-details")
    public String editProduct(@RequestParam("id") Long id,
                              @Valid ProductDTO productDTO,
                              BindingResult bindingResult,
                              @RequestParam(value = "image", required = false) MultipartFile image,
                              RedirectAttributes rAtt,
                              Model model) {

        productDTO.setId(id); // Уверяваме се, че ID е зададено.

        if (bindingResult.hasErrors()) {
            rAtt.addFlashAttribute("productDTO", productDTO);
            rAtt.addFlashAttribute("org.springframework.validation.BindingResult.productDTO", bindingResult);
            model.addAttribute("allProviders", providerService.allProviders());
            model.addAttribute("allCategories", categoryService.getAllCategory());
            model.addAttribute("allTypes", typeService.getAllTypes());
            return "product-details";
        }

        // Ако е качено ново изображение
        if (image != null && !image.isEmpty()) {
            try {
                Path uploadDir = Paths.get(uploadPath);
                if (!Files.exists(uploadDir)) {
                    Files.createDirectories(uploadDir);
                }

                String originalFileName = image.getOriginalFilename();
                String fileExtension = originalFileName.substring(originalFileName.lastIndexOf(".") + 1).toLowerCase();
                List<String> allowedExtensions = Arrays.asList("jpg", "jpeg", "png", "gif");

                if (!allowedExtensions.contains(fileExtension)) {
                    rAtt.addFlashAttribute("imageError", "Невалиден формат на изображението!");
                    return "redirect:/product-details/" + id;
                }

                String uniqueFileName = UUID.randomUUID().toString() + "." + fileExtension;
                Path filePath = uploadDir.resolve(uniqueFileName);
                image.transferTo(filePath.toFile());

                // Изтриване на старата снимка
                String oldImagePath = productService.findProductById(id).getImagePath();
                if (oldImagePath != null && !oldImagePath.isEmpty()) {
                    Path oldFilePath = Paths.get(uploadPath, oldImagePath.replace("/uploads/", ""));
                    Files.deleteIfExists(oldFilePath);
                }

                // Задаване на пътя към новата снимка
                productDTO.setImagePath("/uploads/" + uniqueFileName);
            } catch (IOException e) {
                rAtt.addFlashAttribute("imageError", "Грешка при качване на снимката!");
                return "redirect:/product-details/" + id;
            }
        } else {
            // Ако няма ново изображение, запазваме старото
            productDTO.setImagePath(productService.findProductById(id).getImagePath());
        }

        boolean isChangedProductName = !productDTO.getName().equals(productService.findProductById(id).getName());
        boolean isChangedProductCode = !productDTO.getProductCode().equals(productService.findProductById(id).getProductCode());

        if (isChangedProductName && productService.isExistProductName(productDTO.getName())) {
            rAtt.addFlashAttribute("productDTO", productDTO);
            rAtt.addFlashAttribute("isExistName", true);
            return "redirect:/product-details/" + id;
        }

        if (isChangedProductCode && productService.isExistProductCode(productDTO.getProductCode())) {
            rAtt.addFlashAttribute("productDTO", productDTO);
            rAtt.addFlashAttribute("isExistCode", true);
            return "redirect:/product-details/" + id;
        }

        productService.editProduct(productDTO);
        return "redirect:/products";
    }

    //delete product by id
    @PostMapping("/delete-product/{id}")
    public String removeProduct(@PathVariable("id") Long id) {

        productService.deleteProduct(id);

        return "redirect:/products";
    }

    //delete serial number by id
    @PostMapping("/delete-sn/{id}/{prodId}")
    public String removeSerialNumber(@PathVariable("id") Long id, @PathVariable("prodId") Long prodId) {

        productService.deleteSerialNumber(id, prodId);

        return "redirect:/product-details/" + prodId;
    }
}

