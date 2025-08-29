package com.firefly.core.banking.psdx.interfaces.exceptions;

/**
 * Exception for format errors.
 */
public class PSDFormatException extends PSDException {

    private static final String ERROR_CODE = "FORMAT_ERROR";

    /**
     * Constructor for PSDFormatException.
     *
     * @param message The error message
     */
    public PSDFormatException(String message) {
        super(ERROR_CODE, message);
    }

    /**
     * Constructor for PSDFormatException.
     *
     * @param message The error message
     * @param tppMessage The TPP message
     */
    public PSDFormatException(String message, String tppMessage) {
        super(ERROR_CODE, message, tppMessage);
    }

    /**
     * Constructor for PSDFormatException.
     *
     * @param message The error message
     * @param cause The cause
     */
    public PSDFormatException(String message, Throwable cause) {
        super(ERROR_CODE, message, cause);
    }

    /**
     * Constructor for PSDFormatException.
     *
     * @param message The error message
     * @param tppMessage The TPP message
     * @param cause The cause
     */
    public PSDFormatException(String message, String tppMessage, Throwable cause) {
        super(ERROR_CODE, message, tppMessage, cause);
    }
}
