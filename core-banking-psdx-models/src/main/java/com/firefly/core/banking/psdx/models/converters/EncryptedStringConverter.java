package com.firefly.core.banking.psdx.models.converters;

import com.firefly.core.banking.psdx.interfaces.security.EncryptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.r2dbc.core.Parameter;

/**
 * Converter for encrypting strings when writing to the database.
 */
@WritingConverter
@RequiredArgsConstructor
public class EncryptedStringConverter implements Converter<String, Parameter> {

    private final EncryptionService encryptionService;

    /**
     * Convert a string to an encrypted string.
     *
     * @param source The source string
     * @return The encrypted string
     */
    @Override
    public Parameter convert(String source) {
        return Parameter.from(encryptionService.encrypt(source));
    }
}
