package com.catalis.core.banking.psdx.core.security;

import com.catalis.core.banking.psdx.interfaces.security.EncryptionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Implementation of the EncryptionService.
 */
@Service
public class EncryptionServiceImpl extends EncryptionService {

    /**
     * Constructor for EncryptionServiceImpl.
     *
     * @param encryptionEnabled Whether encryption is enabled
     * @param algorithm The encryption algorithm
     * @param secretKey The secret key
     */
    public EncryptionServiceImpl(
            @Value("${psdx.security.encryption.enabled:false}") boolean encryptionEnabled,
            @Value("${psdx.security.encryption.algorithm:AES/GCM/NoPadding}") String algorithm,
            @Value("${psdx.security.encryption.secret-key:}") String secretKey) {
        super(encryptionEnabled, algorithm, secretKey);
    }
}
