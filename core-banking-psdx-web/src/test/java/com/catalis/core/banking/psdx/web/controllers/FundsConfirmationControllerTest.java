package com.catalis.core.banking.psdx.web.controllers;

import com.catalis.core.banking.psdx.interfaces.dtos.PSDFundsConfirmationDTO;
import com.catalis.core.banking.psdx.interfaces.services.FundsConfirmationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.catalis.core.banking.psdx.web.utils.TestUtils;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FundsConfirmationControllerTest {

    @Mock
    private FundsConfirmationService fundsConfirmationService;

    @InjectMocks
    private FundsConfirmationController fundsConfirmationController;

    private WebTestClient webTestClient;

    private final Long CONSENT_ID = 1L;
    private final Long FUNDS_CONFIRMATION_ID = 1000L;

    private PSDFundsConfirmationDTO fundsConfirmation;
    private PSDFundsConfirmationDTO fundsConfirmationRequest;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToController(fundsConfirmationController).build();

        // Setup test data
        fundsConfirmation = new PSDFundsConfirmationDTO();
        fundsConfirmation.setFundsConfirmationId(FUNDS_CONFIRMATION_ID);
        fundsConfirmation.setConsentId(CONSENT_ID);

        // Create account reference
        PSDFundsConfirmationDTO.PSDAccountReferenceDTO account = new PSDFundsConfirmationDTO.PSDAccountReferenceDTO();
        account.setIban("DE89370400440532013000");
        account.setCurrency("EUR");
        fundsConfirmation.setAccount(account);

        // Create instructed amount
        PSDFundsConfirmationDTO.PSDAmountDTO instructedAmount = new PSDFundsConfirmationDTO.PSDAmountDTO();
        instructedAmount.setCurrency("EUR");
        instructedAmount.setAmount(BigDecimal.valueOf(100.00));
        fundsConfirmation.setInstructedAmount(instructedAmount);

        fundsConfirmation.setCreditorName("John Doe");

        // Create creditor account
        PSDFundsConfirmationDTO.PSDAccountReferenceDTO creditorAccount = new PSDFundsConfirmationDTO.PSDAccountReferenceDTO();
        creditorAccount.setIban("FR7630006000011234567890189");
        creditorAccount.setCurrency("EUR");
        fundsConfirmation.setCreditorAccount(creditorAccount);

        fundsConfirmation.setFundsAvailable(true);
        fundsConfirmation.setConfirmationDateTime(LocalDateTime.now().withNano(0));

        fundsConfirmationRequest = new PSDFundsConfirmationDTO();

        // Create account reference for request
        PSDFundsConfirmationDTO.PSDAccountReferenceDTO accountRequest = new PSDFundsConfirmationDTO.PSDAccountReferenceDTO();
        accountRequest.setIban("DE89370400440532013000");
        accountRequest.setCurrency("EUR");
        fundsConfirmationRequest.setAccount(accountRequest);

        // Create instructed amount for request
        PSDFundsConfirmationDTO.PSDAmountDTO instructedAmountRequest = new PSDFundsConfirmationDTO.PSDAmountDTO();
        instructedAmountRequest.setCurrency("EUR");
        instructedAmountRequest.setAmount(BigDecimal.valueOf(100.00));
        fundsConfirmationRequest.setInstructedAmount(instructedAmountRequest);

        fundsConfirmationRequest.setCreditorName("John Doe");

        // Create creditor account for request
        PSDFundsConfirmationDTO.PSDAccountReferenceDTO creditorAccountRequest = new PSDFundsConfirmationDTO.PSDAccountReferenceDTO();
        creditorAccountRequest.setIban("FR7630006000011234567890189");
        creditorAccountRequest.setCurrency("EUR");
        fundsConfirmationRequest.setCreditorAccount(creditorAccountRequest);
    }

    @Test
    void confirmFunds_shouldReturnFundsConfirmation() {
        // Given
        when(fundsConfirmationService.confirmFunds(eq(CONSENT_ID), any(PSDFundsConfirmationDTO.class)))
                .thenReturn(Mono.just(fundsConfirmation));

        // When & Then
        webTestClient.post()
                .uri("/api/funds-confirmations")
                .header("X-Consent-ID", CONSENT_ID.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(fundsConfirmationRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(PSDFundsConfirmationDTO.class)
                .isEqualTo(fundsConfirmation);
    }

    @Test
    void getFundsConfirmation_shouldReturnFundsConfirmation() {
        // Given
        when(fundsConfirmationService.getFundsConfirmation(CONSENT_ID, FUNDS_CONFIRMATION_ID))
                .thenReturn(Mono.just(fundsConfirmation));

        // When & Then
        webTestClient.get()
                .uri("/api/funds-confirmations/{fundsConfirmationId}", FUNDS_CONFIRMATION_ID)
                .header("X-Consent-ID", CONSENT_ID.toString())
                .exchange()
                .expectStatus().isOk()
                .expectBody(PSDFundsConfirmationDTO.class)
                .isEqualTo(fundsConfirmation);
    }
}
