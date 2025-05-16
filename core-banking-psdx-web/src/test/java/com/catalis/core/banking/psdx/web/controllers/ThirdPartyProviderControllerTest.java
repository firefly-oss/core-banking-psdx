package com.catalis.core.banking.psdx.web.controllers;

import com.catalis.core.banking.psdx.interfaces.dtos.PSDThirdPartyProviderDTO;
import com.catalis.core.banking.psdx.interfaces.dtos.PSDThirdPartyProviderRegistrationDTO;
import com.catalis.core.banking.psdx.interfaces.services.ThirdPartyProviderService;
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

import com.catalis.core.banking.psdx.web.utils.TestUtils;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ThirdPartyProviderControllerTest {

    @Mock
    private ThirdPartyProviderService thirdPartyProviderService;

    @InjectMocks
    private ThirdPartyProviderController thirdPartyProviderController;

    private WebTestClient webTestClient;

    private final Long PROVIDER_ID = 1L;

    private PSDThirdPartyProviderDTO provider1;
    private PSDThirdPartyProviderDTO provider2;
    private PSDThirdPartyProviderRegistrationDTO registrationDTO;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToController(thirdPartyProviderController).build();

        // Setup test data
        provider1 = new PSDThirdPartyProviderDTO();
        provider1.setId(PROVIDER_ID);
        provider1.setName("Test Provider 1");
        provider1.setStatus("ACTIVE");
        provider1.setProviderType("AISP");
        provider1.setRegistrationNumber("TPP123456");
        provider1.setCreatedAt(LocalDateTime.now().withNano(0));

        provider2 = new PSDThirdPartyProviderDTO();
        provider2.setId(PROVIDER_ID + 1);
        provider2.setName("Test Provider 2");
        provider2.setStatus("ACTIVE");
        provider2.setProviderType("PISP");
        provider2.setRegistrationNumber("TPP789012");
        provider2.setCreatedAt(LocalDateTime.now().withNano(0));

        registrationDTO = new PSDThirdPartyProviderRegistrationDTO();
        registrationDTO.setName("New Provider");
        registrationDTO.setRegistrationNumber("TPP345678");
        registrationDTO.setRedirectUri("https://newprovider.com/callback");
        registrationDTO.setNationalCompetentAuthority("DE-BAFIN");
        registrationDTO.setNationalCompetentAuthorityCountry("DE");
        registrationDTO.setProviderType("AISP");
        registrationDTO.setRoles(Arrays.asList("AISP", "PISP"));

        PSDThirdPartyProviderRegistrationDTO.PSDCertificateDTO certificate =
                new PSDThirdPartyProviderRegistrationDTO.PSDCertificateDTO();
        certificate.setSerialNumber("12345678");
        certificate.setSubject("CN=New Provider,O=New Provider Ltd,C=DE");
        certificate.setIssuer("CN=PSD2 CA,O=European Banking Authority,C=EU");
        certificate.setContent("MIIEpAIBAAKCAQEA...");
        registrationDTO.setCertificate(certificate);
    }

    @Test
    void registerProvider_shouldReturnCreatedProvider() {
        // Given
        when(thirdPartyProviderService.registerProvider(any(PSDThirdPartyProviderRegistrationDTO.class)))
                .thenReturn(Mono.just(provider1));

        // When & Then
        webTestClient.post()
                .uri("/api/providers")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(registrationDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(PSDThirdPartyProviderDTO.class)
                .isEqualTo(provider1);
    }

    @Test
    void getProvider_shouldReturnProvider() {
        // Given
        when(thirdPartyProviderService.getProvider(PROVIDER_ID))
                .thenReturn(Mono.just(provider1));

        // When & Then
        webTestClient.get()
                .uri("/api/providers/{providerId}", PROVIDER_ID)
                .exchange()
                .expectStatus().isOk()
                .expectBody(PSDThirdPartyProviderDTO.class)
                .isEqualTo(provider1);
    }

    @Test
    void getAllProviders_shouldReturnProviders() {
        // Given
        when(thirdPartyProviderService.getAllProviders())
                .thenReturn(Flux.fromIterable(Arrays.asList(provider1, provider2)));

        // When & Then
        webTestClient.get()
                .uri("/api/providers")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(PSDThirdPartyProviderDTO.class)
                .hasSize(2)
                .contains(provider1, provider2);
    }

    @Test
    void updateProvider_shouldReturnUpdatedProvider() {
        // Given
        when(thirdPartyProviderService.updateProvider(eq(PROVIDER_ID), any(PSDThirdPartyProviderDTO.class)))
                .thenReturn(Mono.just(provider1));

        // When & Then
        webTestClient.put()
                .uri("/api/providers/{providerId}", PROVIDER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(provider1)
                .exchange()
                .expectStatus().isOk()
                .expectBody(PSDThirdPartyProviderDTO.class)
                .isEqualTo(provider1);
    }

    @Test
    void suspendProvider_shouldReturnSuspendedProvider() {
        // Given
        PSDThirdPartyProviderDTO suspendedProvider = new PSDThirdPartyProviderDTO();
        suspendedProvider.setId(PROVIDER_ID);
        suspendedProvider.setName("Test Provider 1");
        suspendedProvider.setStatus("SUSPENDED");

        when(thirdPartyProviderService.suspendProvider(PROVIDER_ID))
                .thenReturn(Mono.just(suspendedProvider));

        // When & Then
        webTestClient.post()
                .uri("/api/providers/{providerId}/suspend", PROVIDER_ID)
                .exchange()
                .expectStatus().isOk()
                .expectBody(PSDThirdPartyProviderDTO.class)
                .isEqualTo(suspendedProvider);
    }

    @Test
    void activateProvider_shouldReturnActivatedProvider() {
        // Given
        when(thirdPartyProviderService.activateProvider(PROVIDER_ID))
                .thenReturn(Mono.just(provider1));

        // When & Then
        webTestClient.post()
                .uri("/api/providers/{providerId}/activate", PROVIDER_ID)
                .exchange()
                .expectStatus().isOk()
                .expectBody(PSDThirdPartyProviderDTO.class)
                .isEqualTo(provider1);
    }

    @Test
    void validateApiKey_shouldReturnProvider() {
        // Given
        String apiKey = "test-api-key";
        when(thirdPartyProviderService.validateApiKey(apiKey))
                .thenReturn(Mono.just(provider1));

        // When & Then
        webTestClient.get()
                .uri("/api/providers/validate")
                .header("X-API-KEY", apiKey)
                .exchange()
                .expectStatus().isOk()
                .expectBody(PSDThirdPartyProviderDTO.class)
                .isEqualTo(provider1);
    }
}
