package co.com.mrsoft.utils.api.raml.model;

import lombok.Data;
import lombok.ToString;

import java.util.Map;

@Data
@ToString
public class NumberScalarNode implements Node {
    private String name;
    private String description;
    private String example;
    private boolean required;
    private int minimum;
    private int maximum;

    @Override
    public Map<String, Object> toMap() {
        return null;
    }
}
