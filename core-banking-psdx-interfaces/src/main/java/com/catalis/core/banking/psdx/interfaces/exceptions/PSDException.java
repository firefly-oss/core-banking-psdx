package com.catalis.core.banking.psdx.interfaces.exceptions;

import lombok.Getter;

/**
 * Base exception for PSD2/PSD3 specific errors.
 */
@Getter
public class PSDException extends RuntimeException {

    private final String errorCode;
    private final String tppMessage;

    /**
     * Constructor for PSDException.
     *
     * @param errorCode The error code
     * @param message The error message
     */
    public PSDException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.tppMessage = null;
    }

    /**
     * Constructor for PSDException.
     *
     * @param errorCode The error code
     * @param message The error message
     * @param tppMessage The TPP message
     */
    public PSDException(String errorCode, String message, String tppMessage) {
        super(message);
        this.errorCode = errorCode;
        this.tppMessage = tppMessage;
    }

    /**
     * Constructor for PSDException.
     *
     * @param errorCode The error code
     * @param message The error message
     * @param cause The cause
     */
    public PSDException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.tppMessage = null;
    }

    /**
     * Constructor for PSDException.
     *
     * @param errorCode The error code
     * @param message The error message
     * @param tppMessage The TPP message
     * @param cause The cause
     */
    public PSDException(String errorCode, String message, String tppMessage, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.tppMessage = tppMessage;
    }
}
