package com.firefly.core.banking.psdx.interfaces.exceptions;

/**
 * Exception for invalid consents.
 */
public class PSDConsentInvalidException extends PSDException {

    private static final String ERROR_CODE = "CONSENT_INVALID";

    /**
     * Constructor for PSDConsentInvalidException.
     *
     * @param message The error message
     */
    public PSDConsentInvalidException(String message) {
        super(ERROR_CODE, message);
    }

    /**
     * Constructor for PSDConsentInvalidException.
     *
     * @param message The error message
     * @param tppMessage The TPP message
     */
    public PSDConsentInvalidException(String message, String tppMessage) {
        super(ERROR_CODE, message, tppMessage);
    }

    /**
     * Constructor for PSDConsentInvalidException.
     *
     * @param message The error message
     * @param cause The cause
     */
    public PSDConsentInvalidException(String message, Throwable cause) {
        super(ERROR_CODE, message, cause);
    }

    /**
     * Constructor for PSDConsentInvalidException.
     *
     * @param message The error message
     * @param tppMessage The TPP message
     * @param cause The cause
     */
    public PSDConsentInvalidException(String message, String tppMessage, Throwable cause) {
        super(ERROR_CODE, message, tppMessage, cause);
    }
}
