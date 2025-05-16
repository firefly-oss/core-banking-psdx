package com.catalis.core.banking.psdx.web.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for tests
 */
public class TestUtils {

    /**
     * Converts a LocalDateTime to a string with seconds precision
     * @param dateTime The LocalDateTime to convert
     * @return A string representation with seconds precision
     */
    public static String toSecondsPrecision(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                .replaceAll("\\.\\d+", "");
    }
}
