package com.catalis.core.banking.psdx.web.integration;

import com.catalis.core.banking.psdx.interfaces.services.ConsentService;
import com.catalis.core.banking.psdx.interfaces.dtos.PSDConsentDTO;
import com.catalis.core.banking.psdx.interfaces.dtos.PSDConsentRequestDTO;
import com.catalis.core.banking.psdx.interfaces.dtos.PSDConsentStatusDTO;
import com.catalis.core.banking.psdx.interfaces.enums.ConsentStatus;
import com.catalis.core.banking.psdx.interfaces.enums.ConsentType;
import com.catalis.core.banking.psdx.web.controllers.ConsentController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

/**
 * Integration tests for the Consent API.
 */
@ExtendWith(MockitoExtension.class)
public class ConsentIntegrationTest {

    private WebTestClient webTestClient;

    @Mock
    private ConsentService consentService;

    @InjectMocks
    private ConsentController consentController;

    private PSDConsentDTO consentDTO;
    private PSDConsentRequestDTO consentRequestDTO;

    @BeforeEach
    void setUp() {
        // Setup WebTestClient
        webTestClient = WebTestClient.bindToController(consentController).build();

        // Setup test data
        consentDTO = new PSDConsentDTO();
        consentDTO.setId(1L);
        consentDTO.setPartyId(123L);
        consentDTO.setConsentType(ConsentType.ACCOUNT_INFORMATION.name());
        consentDTO.setConsentStatus(ConsentStatus.VALID.name());
        consentDTO.setValidFrom(LocalDateTime.now().minusDays(1));
        consentDTO.setValidUntil(LocalDateTime.now().plusDays(90));
        consentDTO.setFrequencyPerDay(4);
        consentDTO.setRecurringIndicator(true);

        consentRequestDTO = new PSDConsentRequestDTO();
        consentRequestDTO.setPartyId(123L);
        consentRequestDTO.setConsentType(ConsentType.ACCOUNT_INFORMATION.name());
        consentRequestDTO.setValidFrom(LocalDateTime.now().minusDays(1));
        consentRequestDTO.setValidUntil(LocalDateTime.now().plusDays(90));
        consentRequestDTO.setFrequencyPerDay(4);
        consentRequestDTO.setRecurringIndicator(true);
        consentRequestDTO.setAccess(Arrays.asList(new PSDConsentRequestDTO.PSDAccessDTO()));
    }

    @Test
    void createConsent_shouldReturnCreatedConsent() {
        // Given
        when(consentService.createConsent(any(PSDConsentRequestDTO.class))).thenReturn(Mono.just(consentDTO));

        // When & Then
        webTestClient.post()
                .uri("/api/v1/consents")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(consentRequestDTO)
                .header("X-Request-ID", "test-request-id")
                .header("PSU-ID", "test-psu-id")
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.partyId").isEqualTo(123)
                .jsonPath("$.consentType").isEqualTo("ACCOUNT_INFORMATION")
                .jsonPath("$.consentStatus").isEqualTo("VALID")
                .jsonPath("$.recurringIndicator").isEqualTo(true);
    }

    @Test
    void getConsent_shouldReturnConsent() {
        // Given
        when(consentService.getConsent(1L)).thenReturn(Mono.just(consentDTO));

        // When & Then
        webTestClient.get()
                .uri("/api/v1/consents/1")
                .header("X-Request-ID", "test-request-id")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.partyId").isEqualTo(123)
                .jsonPath("$.consentType").isEqualTo("ACCOUNT_INFORMATION")
                .jsonPath("$.consentStatus").isEqualTo("VALID")
                .jsonPath("$.recurringIndicator").isEqualTo(true);
    }

    @Test
    void getConsents_shouldReturnConsents() {
        // Given
        when(consentService.getConsentsForCustomer(123L)).thenReturn(Flux.just(consentDTO));

        // When & Then
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/consents")
                        .queryParam("partyId", "123")
                        .build())
                .header("X-Request-ID", "test-request-id")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$").isArray()
                .jsonPath("$.length()").isEqualTo(1)
                .jsonPath("$[0].id").isEqualTo(1)
                .jsonPath("$[0].partyId").isEqualTo(123)
                .jsonPath("$[0].consentType").isEqualTo("ACCOUNT_INFORMATION")
                .jsonPath("$[0].consentStatus").isEqualTo("VALID");
    }

    @Test
    void updateConsentStatus_shouldReturnUpdatedConsent() {
        // Given
        PSDConsentStatusDTO statusDTO = new PSDConsentStatusDTO();
        statusDTO.setConsentStatus(ConsentStatus.VALID.name());

        // The controller returns a PSDConsentDTO, not a PSDConsentStatusDTO
        when(consentService.updateConsentStatus(eq(1L), any(PSDConsentStatusDTO.class))).thenReturn(Mono.just(consentDTO));

        // When & Then
        webTestClient.put()
                .uri("/api/v1/consents/1/status")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(statusDTO)
                .header("X-Request-ID", "test-request-id")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.partyId").isEqualTo(123)
                .jsonPath("$.consentType").isEqualTo("ACCOUNT_INFORMATION")
                .jsonPath("$.consentStatus").isEqualTo("VALID");
    }

    @Test
    void revokeConsent_shouldReturnNoContent() {
        // Given
        when(consentService.revokeConsent(1L)).thenReturn(Mono.empty());

        // When & Then
        webTestClient.delete()
                .uri("/api/v1/consents/1")
                .header("X-Request-ID", "test-request-id")
                .exchange()
                .expectStatus().isOk();
    }
}
