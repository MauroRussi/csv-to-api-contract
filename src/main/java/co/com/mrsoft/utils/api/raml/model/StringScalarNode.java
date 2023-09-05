package co.com.mrsoft.utils.api.raml.model;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Data
@ToString
public class StringScalarNode implements Node {
    private String name;
    private String description;
    private String example;
    private boolean required;
    private Integer minLength;
    private Integer maxLength;
    private String pattern;
    private String enumValues;

    @Override
    public Map<String, Object> toMap() {
        return null;
    }
}
