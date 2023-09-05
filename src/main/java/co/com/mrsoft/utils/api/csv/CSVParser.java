package co.com.mrsoft.utils.api.csv;

import co.com.mrsoft.utils.api.csv.model.DataTypeEnum;
import co.com.mrsoft.utils.api.csv.model.Record;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.*;

public class CSVParser {
    private static final int NUM_FIELDS = 11;
    private static final String FIELD_SEPARATOR = "\"\\|\"";
    private final Map<String, DataTypeEnum> dataTypesCache = new HashMap<>();
    private final String filename;

    public CSVParser(String filename) {
        this.filename = filename;

        //Load the data types in a map for quick access
        DataTypeEnum[] dataTypeValues = DataTypeEnum.values();

        for (DataTypeEnum dataTypeValue : dataTypeValues) {
            dataTypesCache.put(dataTypeValue.getDataType(), dataTypeValue);
        }
    }

    public Collection<Record> parse() throws CSVParserException {
        List<Record> records = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(this.filename))) {
            String line;

            for(int numLine = 1; (line = reader.readLine()) != null; numLine++) {
                records.add(this.buildRecord(numLine, line));
            }
        } catch (IOException e) {
            throw new CSVParserException("Error parsing file " + this.filename, e);
        }

        return records;
    }

    private Record buildRecord(int rowNumber, String line) throws CSVParserException {
        String[] items = line.split(FIELD_SEPARATOR, -1);

        if(items.length != NUM_FIELDS) {
            throw new CSVParserException(rowNumber, "Invalid length of fields: " + items.length);
        }

        return Record.builder()
                .rowNumber(rowNumber)
                //Remove start double quotes and send to parse
                .jsonName(parseField("jsonName", items[0].replace("\"", ""), false, rowNumber, String.class))
                .description(parseField("description", items[1], false, rowNumber, String.class))
                .dataType(parseField("dataType", items[2], false, rowNumber, DataTypeEnum.class))
                .required(parseField("required", items[3], true, rowNumber, Boolean.class))
                .minLength(parseField("minLength", items[4], true, rowNumber, Integer.class))
                .maxLength(parseField("maxLength", items[5], true, rowNumber, Integer.class))
                .regexPattern(parseField("pattern", items[6], true, rowNumber, String.class))
                .enumValues(parseField("enum values", items[7], true, rowNumber, String.class))
                .fractionDigits(parseField("fractionDigits", items[8], true, rowNumber, Integer.class))
                .totalDigits(parseField("totalDigits", items[9], true, rowNumber, Integer.class))
                //Remove end double quotes and send to parse
                .minInclusive(parseField("minInclusive", items[10].replace("\"", ""), true, rowNumber, Integer.class))
                .build();
    }

    @SuppressWarnings(value = "unchecked")
    private <T> T parseField(String fieldName, String fieldValue, boolean nullable,
                             int rowNumber, Class<T> fieldClass) throws CSVParserException {
        T tValue = null;

        boolean fieldValueBlank = StringUtils.isBlank(fieldValue);

        if(!nullable && fieldValueBlank) {
            throw new CSVParserException(rowNumber, "Field " + fieldName + " cannot be null");
        }

        String tFieldValue = fieldValue.trim();

        if(!fieldValueBlank) {
            try {
                if (Integer.class.equals(fieldClass)) {
                    tValue = (T) Integer.valueOf(tFieldValue);
                } else if (Boolean.class.equals(fieldClass)) {
                    tValue = (T) Boolean.valueOf(tFieldValue);
                } else if (DataTypeEnum.class.equals(fieldClass)) {
                    DataTypeEnum dataType = this.dataTypesCache.get(tFieldValue);

                    if (dataType == null) {
                        throw new CSVParserException(rowNumber, "Invalid data type");
                    }

                    tValue = (T) dataType;
                } else {
                    tValue = (T) tFieldValue;
                }
            } catch (NumberFormatException nEx) {
                throw new CSVParserException(rowNumber, "Field " + fieldName + " invalid number value: " + tFieldValue);
            }
        }
        else {
            if(Boolean.class.equals(fieldClass)) {
                tValue = (T) Boolean.FALSE;
            }
        }

        return tValue;
    }
}
