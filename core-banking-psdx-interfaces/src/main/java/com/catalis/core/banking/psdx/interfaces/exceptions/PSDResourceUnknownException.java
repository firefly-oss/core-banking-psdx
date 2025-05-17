package com.catalis.core.banking.psdx.interfaces.exceptions;

/**
 * Exception for unknown resources.
 */
public class PSDResourceUnknownException extends PSDException {

    private static final String ERROR_CODE = "RESOURCE_UNKNOWN";

    /**
     * Constructor for PSDResourceUnknownException.
     *
     * @param message The error message
     */
    public PSDResourceUnknownException(String message) {
        super(ERROR_CODE, message);
    }

    /**
     * Constructor for PSDResourceUnknownException.
     *
     * @param message The error message
     * @param tppMessage The TPP message
     */
    public PSDResourceUnknownException(String message, String tppMessage) {
        super(ERROR_CODE, message, tppMessage);
    }

    /**
     * Constructor for PSDResourceUnknownException.
     *
     * @param message The error message
     * @param cause The cause
     */
    public PSDResourceUnknownException(String message, Throwable cause) {
        super(ERROR_CODE, message, cause);
    }

    /**
     * Constructor for PSDResourceUnknownException.
     *
     * @param message The error message
     * @param tppMessage The TPP message
     * @param cause The cause
     */
    public PSDResourceUnknownException(String message, String tppMessage, Throwable cause) {
        super(ERROR_CODE, message, tppMessage, cause);
    }
}
