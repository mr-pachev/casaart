package casaart.emails_clients_db.model.dto;

import casaart.emails_clients_db.model.entity.Type;

import java.util.List;

public class CategoryDTO {
    private String name;
    private String code;
    List<String> types;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }
}
