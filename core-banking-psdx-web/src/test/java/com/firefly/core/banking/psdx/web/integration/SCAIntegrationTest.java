package com.firefly.core.banking.psdx.web.integration;

import com.firefly.core.banking.psdx.core.ports.SCAServicePort;
import com.firefly.core.banking.psdx.interfaces.dtos.PSDSCAAuthenticationRequestDTO;
import com.firefly.core.banking.psdx.interfaces.dtos.PSDSCAAuthenticationResponseDTO;
import com.firefly.core.banking.psdx.interfaces.dtos.PSDSCAValidationRequestDTO;
import com.firefly.core.banking.psdx.interfaces.dtos.PSDSCAValidationResponseDTO;
import com.firefly.core.banking.psdx.web.controllers.SCAController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Integration tests for the SCA API.
 */
@ExtendWith(MockitoExtension.class)
public class SCAIntegrationTest {

    private WebTestClient webTestClient;

    @Mock
    private SCAServicePort scaServicePort;

    @InjectMocks
    private SCAController scaController;

    private PSDSCAAuthenticationRequestDTO authRequest;
    private PSDSCAAuthenticationResponseDTO authResponse;
    private PSDSCAValidationRequestDTO validationRequest;
    private PSDSCAValidationResponseDTO validationResponse;

    @BeforeEach
    void setUp() {
        // Setup WebTestClient
        webTestClient = WebTestClient.bindToController(scaController).build();

        // Setup authentication request
        authRequest = new PSDSCAAuthenticationRequestDTO();
        authRequest.setPartyId(123L);
        authRequest.setResourceId("payment-123456");
        authRequest.setResourceType("PAYMENT");
        authRequest.setAmount(100.00);
        authRequest.setCurrency("EUR");
        authRequest.setPreferredMethod("SMS");

        // Setup authentication response
        authResponse = new PSDSCAAuthenticationResponseDTO();
        authResponse.setChallengeId("sca-123456");
        authResponse.setMethod("SMS");
        authResponse.setMaskedTarget("+49 *** *** 789");
        authResponse.setExpiresIn(300);
        authResponse.setAdditionalInfo("An SMS has been sent to your registered mobile number");

        // Setup validation request
        validationRequest = new PSDSCAValidationRequestDTO();
        validationRequest.setChallengeId("sca-123456");
        validationRequest.setAuthenticationCode("123456");

        // Setup validation response
        validationResponse = new PSDSCAValidationResponseDTO();
        validationResponse.setSuccess(true);
        validationResponse.setAuthenticationToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...");
        validationResponse.setExpiresIn(3600);
    }

    @Test
    void initiateSCA_shouldReturnAuthenticationResponse() {
        // Given
        when(scaServicePort.initiateSCA(any(PSDSCAAuthenticationRequestDTO.class))).thenReturn(Mono.just(authResponse));

        // When & Then
        webTestClient.post()
                .uri("/api/v1/sca/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(authRequest)
                .header("X-Request-ID", "test-request-id")
                .exchange()
                .expectStatus().isOk()
                .expectBody(PSDSCAAuthenticationResponseDTO.class)
                .isEqualTo(authResponse);
    }

    @Test
    void validateSCA_shouldReturnValidationResponse() {
        // Given
        when(scaServicePort.validateSCA(any(PSDSCAValidationRequestDTO.class))).thenReturn(Mono.just(validationResponse));

        // When & Then
        webTestClient.post()
                .uri("/api/v1/sca/validate")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(validationRequest)
                .header("X-Request-ID", "test-request-id")
                .exchange()
                .expectStatus().isOk()
                .expectBody(PSDSCAValidationResponseDTO.class)
                .isEqualTo(validationResponse);
    }

    @Test
    void isSCARequired_shouldReturnBoolean() {
        // Given
        when(scaServicePort.isSCARequired(100.0, "EUR")).thenReturn(Mono.just(true));

        // When & Then
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/sca/required")
                        .queryParam("amount", "100.0")
                        .queryParam("currency", "EUR")
                        .build())
                .header("X-Request-ID", "test-request-id")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Boolean.class)
                .isEqualTo(true);
    }
}
