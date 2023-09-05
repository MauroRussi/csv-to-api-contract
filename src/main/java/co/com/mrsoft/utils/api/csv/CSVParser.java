package co.com.mrsoft.utils.api.csv;

import co.com.mrsoft.utils.api.csv.model.DataTypeEnum;
import co.com.mrsoft.utils.api.csv.model.Record;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.*;

public class CSVParser {
    private static final int NUM_FIELDS = 12;
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

    private Record buildRecord(int lineNumber, String line) throws CSVParserException {
        String[] items = line.split(FIELD_SEPARATOR, -1);

        if(items.length != NUM_FIELDS) {
            throw new CSVParserException(lineNumber, "Invalid length of fields: " + items.length);
        }

        return Record.builder()
                //Remove start double quotes and send to parse
                .jsonName(parseField("jsonName", items[0].replace("\"", ""), false, lineNumber, String.class))
                .dataType(parseField("dataType", items[1], false, lineNumber, DataTypeEnum.class))
                .choice(parseField("choice", items[2], true, lineNumber, Boolean.class))
                .minOccurs(parseField("minOccurs", items[3], true, lineNumber, Integer.class))
                .maxOccurs(parseField("maxOccurs", items[4], true, lineNumber, Integer.class))
                .minLength(parseField("minLength", items[5], true, lineNumber, Integer.class))
                .maxLength(parseField("maxLength", items[6], true, lineNumber, Integer.class))
                .regexPattern(parseField("pattern", items[7], true, lineNumber, String.class))
                .enumValues(parseField("enum values", items[8], true, lineNumber, String.class))
                .fractionDigits(parseField("fractionDigits", items[9], true, lineNumber, Integer.class))
                .totalDigits(parseField("totalDigits", items[10], true, lineNumber, Integer.class))
                //Remove end double quotes and send to parse
                .minInclusive(parseField("minInclusive", items[11].replace("\"", ""), true, lineNumber, Integer.class))
                .build();
    }

    @SuppressWarnings(value = "unchecked")
    private <T> T parseField(String fieldName, String fieldValue, boolean nullable,
                             int lineNumber, Class<T> fieldClass) throws CSVParserException {
        T tValue = null;

        boolean fieldValueBlank = StringUtils.isBlank(fieldValue);

        if(!nullable && fieldValueBlank) {
            throw new CSVParserException(lineNumber, "Field " + fieldName + " cannot be null");
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
                        throw new CSVParserException(lineNumber, "Invalid data type");
                    }
                } else {
                    tValue = (T) tFieldValue;
                }
            } catch (NumberFormatException nEx) {
                throw new CSVParserException(lineNumber, "Field " + fieldName + " invalid number value: " + tFieldValue);
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
