package co.com.mrsoft.utils.api.csv.model;

public enum DataTypeEnum {
    STRING("string"),
    DATE("date"),
    DATETIME("dateTime"),
    DECIMAL("decimal"),
    BOOLEAN("boolean");

    private final String dataType;

    DataTypeEnum(String dataType) {
        this.dataType = dataType;
    }
    public String getDataType() {
        return this.dataType;
    }
}
