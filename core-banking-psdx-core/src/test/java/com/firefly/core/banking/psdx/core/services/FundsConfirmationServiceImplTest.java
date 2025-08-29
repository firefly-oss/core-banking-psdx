package com.firefly.core.banking.psdx.core.services;

import com.firefly.core.banking.psdx.core.ports.AccountServicePort;
import com.firefly.core.banking.psdx.interfaces.dtos.PSDAccountReferenceDTO;
import com.firefly.core.banking.psdx.interfaces.dtos.PSDAmountDTO;
import com.firefly.core.banking.psdx.interfaces.dtos.PSDFundsConfirmationDTO;
import com.firefly.core.banking.psdx.interfaces.services.ConsentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FundsConfirmationServiceImplTest {

    @Mock
    private AccountServicePort accountServicePort;

    @Mock
    private ConsentService consentService;

    @InjectMocks
    private FundsConfirmationServiceImpl fundsConfirmationService;

    private final Long CONSENT_ID = 1L;
    private final Long FUNDS_CONFIRMATION_ID = 1000L;

    private PSDFundsConfirmationDTO fundsConfirmationRequest;

    @BeforeEach
    void setUp() {
        // Setup test data
        PSDAccountReferenceDTO account = new PSDAccountReferenceDTO();
        account.setIban("DE89370400440532013000");

        PSDAmountDTO amount = new PSDAmountDTO();
        amount.setCurrency("EUR");
        amount.setAmount(BigDecimal.valueOf(100.00));

        fundsConfirmationRequest = new PSDFundsConfirmationDTO();
        fundsConfirmationRequest.setAccount(account);
        fundsConfirmationRequest.setInstructedAmount(amount);
    }

    @Test
    void confirmFunds_shouldReturnConfirmation_whenConsentIsValid() {
        // Given
        when(consentService.validateConsent(CONSENT_ID, "FUNDS_CONFIRMATION", "READ")).thenReturn(Mono.just(true));

        // When & Then
        StepVerifier.create(fundsConfirmationService.confirmFunds(CONSENT_ID, fundsConfirmationRequest))
                .expectNextMatches(confirmation -> {
                    return confirmation.getFundsConfirmationId() != null &&
                           confirmation.getConsentId().equals(CONSENT_ID) &&
                           confirmation.getFundsAvailable() != null &&
                           confirmation.getConfirmationDateTime() != null;
                })
                .verifyComplete();
    }

    @Test
    void confirmFunds_shouldReturnError_whenConsentIsInvalid() {
        // Given
        when(consentService.validateConsent(CONSENT_ID, "FUNDS_CONFIRMATION", "READ")).thenReturn(Mono.just(false));

        // When & Then
        StepVerifier.create(fundsConfirmationService.confirmFunds(CONSENT_ID, fundsConfirmationRequest))
                .expectError(IllegalArgumentException.class)
                .verify();
    }

    @Test
    void getFundsConfirmation_shouldReturnConfirmation_whenConsentIsValid() {
        // Given
        when(consentService.validateConsent(CONSENT_ID, "FUNDS_CONFIRMATION", "READ")).thenReturn(Mono.just(true));

        // When & Then
        StepVerifier.create(fundsConfirmationService.getFundsConfirmation(CONSENT_ID, FUNDS_CONFIRMATION_ID))
                .expectNextMatches(confirmation -> {
                    return confirmation.getFundsConfirmationId().equals(FUNDS_CONFIRMATION_ID) &&
                           confirmation.getConsentId().equals(CONSENT_ID) &&
                           confirmation.getFundsAvailable() != null &&
                           confirmation.getConfirmationDateTime() != null;
                })
                .verifyComplete();
    }

    @Test
    void getFundsConfirmation_shouldReturnError_whenConsentIsInvalid() {
        // Given
        when(consentService.validateConsent(CONSENT_ID, "FUNDS_CONFIRMATION", "READ")).thenReturn(Mono.just(false));

        // When & Then
        StepVerifier.create(fundsConfirmationService.getFundsConfirmation(CONSENT_ID, FUNDS_CONFIRMATION_ID))
                .expectError(IllegalArgumentException.class)
                .verify();
    }
}
