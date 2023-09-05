package co.com.mrsoft.utils.api.raml.model;

import lombok.Data;
import lombok.ToString;

import java.util.Map;

@Data
@ToString
public class DateScalarNode implements Node {
    public enum DateTypeEnum {DATE_ONLY, TIME_ONLY, DATETIME_ONLY, DATETIME}
    private String name;
    private String description;
    private boolean required;
    private DateTypeEnum dateType;
    private String example;

    @Override
    public Map<String, Object> toMap() {
        return null;
    }
}
