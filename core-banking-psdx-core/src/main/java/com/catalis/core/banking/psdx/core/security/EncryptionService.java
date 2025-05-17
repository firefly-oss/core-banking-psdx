package com.catalis.core.banking.psdx.core.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Service for encrypting and decrypting sensitive data.
 */
@Service
@Slf4j
public class EncryptionService {

    private static final int GCM_IV_LENGTH = 12;
    private static final int GCM_TAG_LENGTH = 16;
    
    private final boolean encryptionEnabled;
    private final String algorithm;
    private final SecretKey secretKey;
    private final SecureRandom secureRandom;

    /**
     * Constructor for EncryptionService.
     *
     * @param encryptionEnabled Whether encryption is enabled
     * @param algorithm The encryption algorithm
     * @param secretKeyString The secret key
     */
    public EncryptionService(
            @Value("${psdx.security.encryption.enabled:false}") boolean encryptionEnabled,
            @Value("${psdx.security.encryption.algorithm:AES/GCM/NoPadding}") String algorithm,
            @Value("${psdx.security.encryption.secret-key:}") String secretKeyString) {
        
        this.encryptionEnabled = encryptionEnabled;
        this.algorithm = algorithm;
        this.secretKey = new SecretKeySpec(
                secretKeyString.getBytes(StandardCharsets.UTF_8), "AES");
        this.secureRandom = new SecureRandom();
        
        log.info("Encryption service initialized with algorithm: {}, enabled: {}", algorithm, encryptionEnabled);
    }

    /**
     * Encrypt a string.
     *
     * @param plaintext The plaintext to encrypt
     * @return The encrypted string, or the original string if encryption is disabled
     */
    public String encrypt(String plaintext) {
        if (!encryptionEnabled || plaintext == null) {
            return plaintext;
        }
        
        try {
            // Generate a random IV
            byte[] iv = new byte[GCM_IV_LENGTH];
            secureRandom.nextBytes(iv);
            
            // Initialize the cipher
            Cipher cipher = Cipher.getInstance(algorithm);
            GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec);
            
            // Encrypt the plaintext
            byte[] ciphertext = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
            
            // Combine IV and ciphertext
            ByteBuffer byteBuffer = ByteBuffer.allocate(iv.length + ciphertext.length);
            byteBuffer.put(iv);
            byteBuffer.put(ciphertext);
            
            // Encode as Base64
            return Base64.getEncoder().encodeToString(byteBuffer.array());
        } catch (Exception e) {
            log.error("Error encrypting data", e);
            return plaintext;
        }
    }

    /**
     * Decrypt a string.
     *
     * @param ciphertext The ciphertext to decrypt
     * @return The decrypted string, or the original string if encryption is disabled
     */
    public String decrypt(String ciphertext) {
        if (!encryptionEnabled || ciphertext == null) {
            return ciphertext;
        }
        
        try {
            // Decode from Base64
            byte[] ciphertextBytes = Base64.getDecoder().decode(ciphertext);
            
            // Extract IV and ciphertext
            ByteBuffer byteBuffer = ByteBuffer.wrap(ciphertextBytes);
            byte[] iv = new byte[GCM_IV_LENGTH];
            byteBuffer.get(iv);
            byte[] encryptedBytes = new byte[byteBuffer.remaining()];
            byteBuffer.get(encryptedBytes);
            
            // Initialize the cipher
            Cipher cipher = Cipher.getInstance(algorithm);
            GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, parameterSpec);
            
            // Decrypt the ciphertext
            byte[] plaintextBytes = cipher.doFinal(encryptedBytes);
            
            return new String(plaintextBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("Error decrypting data", e);
            return ciphertext;
        }
    }
}
