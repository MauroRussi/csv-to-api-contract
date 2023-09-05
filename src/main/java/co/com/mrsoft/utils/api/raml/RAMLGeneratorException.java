package co.com.mrsoft.utils.api.raml;

public class RAMLGeneratorException extends Exception {
    private static final String HEADER_FORMAT = "Line %s - %s";

    public RAMLGeneratorException(String message) {
        super(message);
    }

    public RAMLGeneratorException(String message, Throwable cause) {
        super(message, cause);
    }

    public RAMLGeneratorException(int rowNumber, String message) {
        this(String.format(HEADER_FORMAT, rowNumber, message));
    }
}
