package com.firefly.core.banking.psdx.core.services;

import com.firefly.core.banking.psdx.interfaces.enums.ConsentStatus;
import com.firefly.core.banking.psdx.interfaces.enums.ConsentType;
import com.firefly.core.banking.psdx.interfaces.enums.ResourceType;
import com.firefly.core.banking.psdx.models.entities.Consent;
import com.firefly.core.banking.psdx.models.repositories.ConsentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests for the ConsentValidationService.
 */
@ExtendWith(MockitoExtension.class)
class ConsentValidationServiceTest {

    @Mock
    private ConsentRepository consentRepository;

    @Mock
    private com.firefly.core.banking.psdx.interfaces.services.AccessLogService accessLogService;

    private ConsentValidationService consentValidationService;

    private Consent validConsent;
    private Consent expiredConsent;
    private Consent invalidStatusConsent;
    private Consent limitedFrequencyConsent;

    @BeforeEach
    void setUp() {
        consentValidationService = new ConsentValidationService(consentRepository, accessLogService);

        LocalDateTime now = LocalDateTime.now();

        validConsent = new Consent();
        validConsent.setId(1L);
        validConsent.setPartyId(123L);
        validConsent.setConsentType(ConsentType.ACCOUNT_INFORMATION);
        validConsent.setStatus(ConsentStatus.VALID);
        validConsent.setValidFrom(now.minusDays(1));
        validConsent.setValidUntil(now.plusDays(1));

        expiredConsent = new Consent();
        expiredConsent.setId(2L);
        expiredConsent.setPartyId(123L);
        expiredConsent.setConsentType(ConsentType.ACCOUNT_INFORMATION);
        expiredConsent.setStatus(ConsentStatus.VALID);
        expiredConsent.setValidFrom(now.minusDays(2));
        expiredConsent.setValidUntil(now.minusDays(1));

        invalidStatusConsent = new Consent();
        invalidStatusConsent.setId(3L);
        invalidStatusConsent.setPartyId(123L);
        invalidStatusConsent.setConsentType(ConsentType.ACCOUNT_INFORMATION);
        invalidStatusConsent.setStatus(ConsentStatus.REVOKED);
        invalidStatusConsent.setValidFrom(now.minusDays(1));
        invalidStatusConsent.setValidUntil(now.plusDays(1));

        limitedFrequencyConsent = new Consent();
        limitedFrequencyConsent.setId(4L);
        limitedFrequencyConsent.setPartyId(123L);
        limitedFrequencyConsent.setConsentType(ConsentType.ACCOUNT_INFORMATION);
        limitedFrequencyConsent.setStatus(ConsentStatus.VALID);
        limitedFrequencyConsent.setValidFrom(now.minusDays(1));
        limitedFrequencyConsent.setValidUntil(now.plusDays(1));
        limitedFrequencyConsent.setAccessFrequency(5);
    }

    @Test
    void validateConsent_withValidConsent_shouldReturnTrue() {
        // Given
        when(consentRepository.findById(1L)).thenReturn(Mono.just(validConsent));
        when(consentRepository.save(any(Consent.class))).thenReturn(Mono.just(validConsent));

        // When
        Mono<Boolean> result = consentValidationService.validateConsent(1L, ResourceType.ACCOUNT, 123L, "tpp1");

        // Then
        StepVerifier.create(result)
                .expectNext(true)
                .verifyComplete();

        ArgumentCaptor<Consent> consentCaptor = ArgumentCaptor.forClass(Consent.class);
        verify(consentRepository).save(consentCaptor.capture());
        assertNotNull(consentCaptor.getValue().getLastActionDate());
    }

    @Test
    void validateConsent_withExpiredConsent_shouldReturnFalse() {
        // Given
        when(consentRepository.findById(2L)).thenReturn(Mono.just(expiredConsent));

        // When
        Mono<Boolean> result = consentValidationService.validateConsent(2L, ResourceType.ACCOUNT, 123L, "tpp1");

        // Then
        StepVerifier.create(result)
                .expectNext(false)
                .verifyComplete();

        verify(consentRepository, never()).save(any(Consent.class));
    }

    @Test
    void validateConsent_withInvalidStatusConsent_shouldReturnFalse() {
        // Given
        when(consentRepository.findById(3L)).thenReturn(Mono.just(invalidStatusConsent));

        // When
        Mono<Boolean> result = consentValidationService.validateConsent(3L, ResourceType.ACCOUNT, 123L, "tpp1");

        // Then
        StepVerifier.create(result)
                .expectNext(false)
                .verifyComplete();

        verify(consentRepository, never()).save(any(Consent.class));
    }

    @Test
    void validateConsent_withWrongPartyId_shouldReturnFalse() {
        // Given
        when(consentRepository.findById(1L)).thenReturn(Mono.just(validConsent));

        // When
        Mono<Boolean> result = consentValidationService.validateConsent(1L, ResourceType.ACCOUNT, 456L, "tpp1");

        // Then
        StepVerifier.create(result)
                .expectNext(false)
                .verifyComplete();

        verify(consentRepository, never()).save(any(Consent.class));
    }

    @Test
    void validateConsent_withWrongResourceType_shouldReturnFalse() {
        // Given
        when(consentRepository.findById(1L)).thenReturn(Mono.just(validConsent));

        // When
        Mono<Boolean> result = consentValidationService.validateConsent(1L, ResourceType.PAYMENT, 123L, "tpp1");

        // Then
        StepVerifier.create(result)
                .expectNext(false)
                .verifyComplete();

        verify(consentRepository, never()).save(any(Consent.class));
    }

    @Test
    void validateConsent_withLimitedFrequencyNotExceeded_shouldReturnTrue() {
        // Given
        when(consentRepository.findById(4L)).thenReturn(Mono.just(limitedFrequencyConsent));
        when(accessLogService.countAccessLogsForConsent(4L)).thenReturn(Mono.just(3L));
        when(consentRepository.save(any(Consent.class))).thenReturn(Mono.just(limitedFrequencyConsent));

        // When
        Mono<Boolean> result = consentValidationService.validateConsent(4L, ResourceType.ACCOUNT, 123L, "tpp1");

        // Then
        StepVerifier.create(result)
                .expectNext(true)
                .verifyComplete();

        ArgumentCaptor<Consent> consentCaptor = ArgumentCaptor.forClass(Consent.class);
        verify(consentRepository).save(consentCaptor.capture());
        assertNotNull(consentCaptor.getValue().getLastActionDate());
    }

    @Test
    void validateConsent_withLimitedFrequencyExceeded_shouldReturnFalse() {
        // Given
        when(consentRepository.findById(4L)).thenReturn(Mono.just(limitedFrequencyConsent));
        when(accessLogService.countAccessLogsForConsent(4L)).thenReturn(Mono.just(5L));

        // When
        Mono<Boolean> result = consentValidationService.validateConsent(4L, ResourceType.ACCOUNT, 123L, "tpp1");

        // Then
        StepVerifier.create(result)
                .expectNext(false)
                .verifyComplete();

        verify(consentRepository, never()).save(any(Consent.class));
    }
}
