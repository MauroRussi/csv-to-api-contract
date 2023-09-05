package co.com.mrsoft.utils.api.csv.model;

import lombok.Builder;
import lombok.ToString;

@Builder
@ToString
public class Record {
    private String jsonName;
    private DataTypeEnum dataType;
    private boolean choice;
    private Integer minOccurs;
    private Integer maxOccurs;
    private Integer minLength;
    private Integer maxLength;
    private String regexPattern;
    private String enumValues;
    private Integer fractionDigits;
    private Integer totalDigits;
    private Integer minInclusive;
}