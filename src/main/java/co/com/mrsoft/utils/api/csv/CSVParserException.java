package co.com.mrsoft.utils.api.csv;

public class CSVParserException extends Exception {
    private static final String HEADER_FORMAT = "Line %s - %s";

    public CSVParserException(String message) {
        super(message);
    }

    public CSVParserException(String message, Throwable cause) {
        super(message, cause);
    }

    public CSVParserException(int rowNumber, String message) {
        this(String.format(HEADER_FORMAT, rowNumber, message));
    }
}
