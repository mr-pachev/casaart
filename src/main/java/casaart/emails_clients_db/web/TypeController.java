package casaart.emails_clients_db.web;

import casaart.emails_clients_db.model.dto.AddCategoryDTO;
import casaart.emails_clients_db.model.dto.AddTypeDTO;
import casaart.emails_clients_db.model.dto.CategoryDTO;
import casaart.emails_clients_db.model.dto.TypeDTO;
import casaart.emails_clients_db.service.TypeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Controller
public class TypeController {
    private final TypeService typeService;

    public TypeController(TypeService typeService) {
        this.typeService = typeService;
    }

    @ModelAttribute("addTypeDTO")
    public AddTypeDTO addTypeDTO() {
        return new AddTypeDTO();
    }

    @ModelAttribute("typeDTO")
    public TypeDTO typeDTO() {
        return new TypeDTO();
    }

    //view all types
    @GetMapping("/types")
    public String getAllTypes(Model model) {
        List<TypeDTO> typeDTOS = typeService.getAllTypes();

        model.addAttribute("allTypes", typeDTOS);

        return "types";
    }
}
