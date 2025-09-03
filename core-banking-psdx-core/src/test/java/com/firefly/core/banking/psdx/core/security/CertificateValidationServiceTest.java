package com.firefly.core.banking.psdx.core.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the CertificateValidationService.
 */
class CertificateValidationServiceTest {

    private CertificateValidationService certificateValidationService;

    @BeforeEach
    void setUp() {
        certificateValidationService = new CertificateValidationService(true);
    }

    @Test
    void validateCertificate_withNullCertificate_shouldReturnTrue() {
        // When
        boolean result = certificateValidationService.validateCertificate((String) null);

        // Then
        assertTrue(result);
    }

    @Test
    void validateCertificate_withEmptyCertificate_shouldReturnTrue() {
        // When
        boolean result = certificateValidationService.validateCertificate("");

        // Then
        assertTrue(result);
    }

    @Test
    void validateCertificate_withInvalidCertificate_shouldReturnFalse() {
        // Given
        String invalidCertificate = "invalid certificate content";

        // When
        boolean result = certificateValidationService.validateCertificate(invalidCertificate);

        // Then
        assertFalse(result);
    }

    @Test
    void extractCertificateInfo_withNullCertificate_shouldReturnNull() {
        // When
        CertificateValidationService.CertificateInfo result = certificateValidationService.extractCertificateInfo(null);

        // Then
        assertNull(result);
    }

    @Test
    void extractCertificateInfo_withEmptyCertificate_shouldReturnNull() {
        // When
        CertificateValidationService.CertificateInfo result = certificateValidationService.extractCertificateInfo("");

        // Then
        assertNull(result);
    }

    @Test
    void extractCertificateInfo_withInvalidCertificate_shouldReturnNull() {
        // Given
        String invalidCertificate = "invalid certificate content";

        // When
        CertificateValidationService.CertificateInfo result = certificateValidationService.extractCertificateInfo(invalidCertificate);

        // Then
        assertNull(result);
    }

    // Additional tests would be added for valid certificates, but that would require
    // generating test certificates, which is beyond the scope of this example.
}
