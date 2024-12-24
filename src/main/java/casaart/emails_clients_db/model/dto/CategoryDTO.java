package casaart.emails_clients_db.model.dto;

import casaart.emails_clients_db.model.entity.Type;

import java.util.List;

public class CategoryDTO {
    long id;
    private String name;
    private String code;
    List<String> types;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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
