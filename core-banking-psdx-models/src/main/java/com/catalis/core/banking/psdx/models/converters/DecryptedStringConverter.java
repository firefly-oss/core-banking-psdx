package com.catalis.core.banking.psdx.models.converters;

import com.catalis.core.banking.psdx.interfaces.security.EncryptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

/**
 * Converter for decrypting strings when reading from the database.
 */
@ReadingConverter
@RequiredArgsConstructor
public class DecryptedStringConverter implements Converter<String, String> {

    private final EncryptionService encryptionService;

    /**
     * Convert an encrypted string to a decrypted string.
     *
     * @param source The encrypted string
     * @return The decrypted string
     */
    @Override
    public String convert(String source) {
        return encryptionService.decrypt(source);
    }
}
