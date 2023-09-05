package co.com.mrsoft.utils.api.csv.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class Record {
    private int rowNumber;
    private String jsonName;
    private DataTypeEnum dataType;
    private String description;
    private boolean required;
    private Integer minLength;
    private Integer maxLength;
    private String regexPattern;
    private String enumValues;
    private Integer fractionDigits;
    private Integer totalDigits;
    private Integer minInclusive;
    private String example;
}