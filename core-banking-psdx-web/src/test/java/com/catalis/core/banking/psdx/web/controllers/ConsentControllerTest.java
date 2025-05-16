package com.catalis.core.banking.psdx.web.controllers;

import com.catalis.core.banking.psdx.interfaces.dtos.PSDConsentDTO;
import com.catalis.core.banking.psdx.interfaces.dtos.PSDConsentRequestDTO;
import com.catalis.core.banking.psdx.interfaces.dtos.PSDConsentStatusDTO;
import com.catalis.core.banking.psdx.interfaces.dtos.PSDLinksDTO;
import com.catalis.core.banking.psdx.interfaces.services.ConsentService;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.catalis.core.banking.psdx.web.utils.TestUtils;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConsentControllerTest {

    @Mock
    private ConsentService consentService;

    @InjectMocks
    private ConsentController consentController;

    private WebTestClient webTestClient;

    private final Long CONSENT_ID = 1L;
    private final Long PARTY_ID = 100L;

    private PSDConsentDTO consent1;
    private PSDConsentDTO consent2;
    private PSDConsentRequestDTO consentRequest;
    private PSDConsentStatusDTO statusUpdate;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToController(consentController).build();

        // Setup test data
        consent1 = new PSDConsentDTO();
        consent1.setId(CONSENT_ID);
        consent1.setPartyId(PARTY_ID);
        consent1.setConsentType("ACCOUNT_INFORMATION");
        consent1.setConsentStatus("VALID");
        consent1.setValidFrom(LocalDateTime.now().withNano(0));
        consent1.setValidUntil(LocalDateTime.now().plusDays(90).withNano(0));
        consent1.setFrequencyPerDay(4);
        consent1.setCreatedAt(LocalDateTime.now().withNano(0));

        consent2 = new PSDConsentDTO();
        consent2.setId(CONSENT_ID + 1);
        consent2.setPartyId(PARTY_ID);
        consent2.setConsentType("PAYMENT_INITIATION");
        consent2.setConsentStatus("VALID");
        consent2.setValidFrom(LocalDateTime.now().withNano(0));
        consent2.setValidUntil(LocalDateTime.now().plusDays(90).withNano(0));
        consent2.setFrequencyPerDay(4);
        consent2.setCreatedAt(LocalDateTime.now().withNano(0));

        consentRequest = new PSDConsentRequestDTO();
        consentRequest.setPartyId(PARTY_ID);
        consentRequest.setConsentType("ACCOUNT_INFORMATION");
        consentRequest.setValidUntil(LocalDateTime.now().plusDays(90).withNano(0));
        consentRequest.setValidFrom(LocalDateTime.now().withNano(0));
        consentRequest.setFrequencyPerDay(4);
        consentRequest.setRecurringIndicator(true);
        consentRequest.setCombinedServiceIndicator(false);

        // Create access permissions
        List<PSDConsentRequestDTO.PSDAccessDTO> accessList = new ArrayList<>();
        PSDConsentRequestDTO.PSDAccessDTO access = new PSDConsentRequestDTO.PSDAccessDTO();
        access.setType("account");

        // Create account references
        List<PSDConsentRequestDTO.PSDAccountReferenceDTO> accounts = new ArrayList<>();
        PSDConsentRequestDTO.PSDAccountReferenceDTO account = new PSDConsentRequestDTO.PSDAccountReferenceDTO();
        account.setIban("DE89370400440532013000");
        account.setCurrency("EUR");
        accounts.add(account);

        access.setAccounts(accounts);
        accessList.add(access);
        consentRequest.setAccess(accessList);

        statusUpdate = new PSDConsentStatusDTO();
        statusUpdate.setConsentStatus("REVOKED");
        statusUpdate.setStatus("REVOKED");
        statusUpdate.setStatusUpdateDateTime(LocalDateTime.now().withNano(0));
    }

    @Test
    void createConsent_shouldReturnCreatedConsent() {
        // Given
        when(consentService.createConsent(any(PSDConsentRequestDTO.class)))
                .thenReturn(Mono.just(consent1));

        // When & Then
        webTestClient.post()
                .uri("/api/consents")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(consentRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(PSDConsentDTO.class)
                .isEqualTo(consent1);
    }

    @Test
    void getConsent_shouldReturnConsent() {
        // Given
        when(consentService.getConsent(CONSENT_ID))
                .thenReturn(Mono.just(consent1));

        // When & Then
        webTestClient.get()
                .uri("/api/consents/{consentId}", CONSENT_ID)
                .exchange()
                .expectStatus().isOk()
                .expectBody(PSDConsentDTO.class)
                .isEqualTo(consent1);
    }

    @Test
    void getConsentsForCustomer_shouldReturnConsents() {
        // Given
        when(consentService.getConsentsForCustomer(PARTY_ID))
                .thenReturn(Flux.fromIterable(Arrays.asList(consent1, consent2)));

        // When & Then
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/consents")
                        .queryParam("partyId", PARTY_ID)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(PSDConsentDTO.class)
                .hasSize(2)
                .contains(consent1, consent2);
    }

    @Test
    void updateConsentStatus_shouldReturnUpdatedConsent() {
        // Given
        PSDConsentDTO updatedConsent = new PSDConsentDTO();
        updatedConsent.setId(CONSENT_ID);
        updatedConsent.setPartyId(PARTY_ID);
        updatedConsent.setConsentType("ACCOUNT_INFORMATION");
        updatedConsent.setConsentStatus("REVOKED");
        updatedConsent.setValidFrom(LocalDateTime.now().withNano(0));
        updatedConsent.setValidUntil(LocalDateTime.now().plusDays(90).withNano(0));
        updatedConsent.setFrequencyPerDay(4);
        updatedConsent.setCreatedAt(LocalDateTime.now().withNano(0));

        when(consentService.updateConsentStatus(eq(CONSENT_ID), any(PSDConsentStatusDTO.class)))
                .thenReturn(Mono.just(updatedConsent));

        // When & Then
        webTestClient.put()
                .uri("/api/consents/{consentId}/status", CONSENT_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(statusUpdate)
                .exchange()
                .expectStatus().isOk()
                .expectBody(PSDConsentDTO.class)
                .isEqualTo(updatedConsent);
    }

    @Test
    void revokeConsent_shouldReturnRevokedConsent() {
        // Given
        PSDConsentDTO revokedConsent = new PSDConsentDTO();
        revokedConsent.setId(CONSENT_ID);
        revokedConsent.setPartyId(PARTY_ID);
        revokedConsent.setConsentType("ACCOUNT_INFORMATION");
        revokedConsent.setConsentStatus("REVOKED");
        revokedConsent.setValidFrom(LocalDateTime.now().withNano(0));
        revokedConsent.setValidUntil(LocalDateTime.now().plusDays(90).withNano(0));
        revokedConsent.setFrequencyPerDay(4);
        revokedConsent.setCreatedAt(LocalDateTime.now().withNano(0));

        when(consentService.revokeConsent(CONSENT_ID))
                .thenReturn(Mono.just(revokedConsent));

        // When & Then
        webTestClient.delete()
                .uri("/api/consents/{consentId}", CONSENT_ID)
                .exchange()
                .expectStatus().isOk()
                .expectBody(PSDConsentDTO.class)
                .isEqualTo(revokedConsent);
    }
}
