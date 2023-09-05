package co.com.mrsoft.utils.api.raml.model;

import lombok.Data;
import lombok.ToString;

import java.util.Map;

@Data
@ToString
public class BooleanScalarNode implements Node {
    private String name;
    private String description;
    private boolean required;

    @Override
    public Map<String, Object> toMap() {
        return null;
    }
}
