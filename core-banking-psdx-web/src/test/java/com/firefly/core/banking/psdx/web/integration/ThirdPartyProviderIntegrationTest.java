package com.firefly.core.banking.psdx.web.integration;

import com.firefly.core.banking.psdx.interfaces.dtos.PSDThirdPartyProviderDTO;
import com.firefly.core.banking.psdx.interfaces.dtos.PSDThirdPartyProviderRegistrationDTO;
import com.firefly.core.banking.psdx.interfaces.services.ThirdPartyProviderService;
import com.firefly.core.banking.psdx.web.controllers.ThirdPartyProviderController;
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

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

/**
 * Integration tests for the ThirdPartyProvider API.
 */
@ExtendWith(MockitoExtension.class)
public class ThirdPartyProviderIntegrationTest {

    private WebTestClient webTestClient;

    @Mock
    private ThirdPartyProviderService thirdPartyProviderService;

    @InjectMocks
    private ThirdPartyProviderController thirdPartyProviderController;

    private PSDThirdPartyProviderDTO providerDTO;
    private PSDThirdPartyProviderRegistrationDTO registrationDTO;

    @BeforeEach
    void setUp() {
        // Setup WebTestClient
        webTestClient = WebTestClient.bindToController(thirdPartyProviderController).build();

        // Setup provider DTO
        providerDTO = new PSDThirdPartyProviderDTO();
        providerDTO.setId(1L);
        providerDTO.setName("Test TPP");
        providerDTO.setRegistrationNumber("TPP123456");
        providerDTO.setRedirectUri("https://test-tpp.com/callback");
        providerDTO.setStatus("ACTIVE");
        providerDTO.setProviderType("AISP");
        providerDTO.setRoles(Arrays.asList("AISP"));
        providerDTO.setNationalCompetentAuthority("DE-BAFIN");
        providerDTO.setNationalCompetentAuthorityCountry("DE");

        // Setup registration DTO
        registrationDTO = new PSDThirdPartyProviderRegistrationDTO();
        registrationDTO.setName("Test TPP");
        registrationDTO.setRegistrationNumber("TPP123456");
        registrationDTO.setRedirectUri("https://test-tpp.com/callback");
        registrationDTO.setProviderType("AISP");
        registrationDTO.setRoles(Arrays.asList("AISP"));
        registrationDTO.setNationalCompetentAuthority("DE-BAFIN");
        registrationDTO.setNationalCompetentAuthorityCountry("DE");
    }

    @Test
    void registerProvider_shouldReturnRegisteredProvider() {
        // Given
        when(thirdPartyProviderService.registerProvider(any(PSDThirdPartyProviderRegistrationDTO.class)))
                .thenReturn(Mono.just(providerDTO));

        // When & Then
        webTestClient.post()
                .uri("/api/v1/providers")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(registrationDTO)
                .header("X-Request-ID", "test-request-id")
                .exchange()
                .expectStatus().isCreated()
                .expectBody(PSDThirdPartyProviderDTO.class)
                .isEqualTo(providerDTO);
    }

    @Test
    void getProvider_shouldReturnProvider() {
        // Given
        when(thirdPartyProviderService.getProvider(1L)).thenReturn(Mono.just(providerDTO));

        // When & Then
        webTestClient.get()
                .uri("/api/v1/providers/1")
                .header("X-Request-ID", "test-request-id")
                .exchange()
                .expectStatus().isOk()
                .expectBody(PSDThirdPartyProviderDTO.class)
                .isEqualTo(providerDTO);
    }

    @Test
    void getAllProviders_shouldReturnProviders() {
        // Given
        when(thirdPartyProviderService.getAllProviders()).thenReturn(Flux.just(providerDTO));

        // When & Then
        webTestClient.get()
                .uri("/api/v1/providers")
                .header("X-Request-ID", "test-request-id")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(PSDThirdPartyProviderDTO.class)
                .hasSize(1)
                .contains(providerDTO);
    }

    @Test
    void updateProvider_shouldReturnUpdatedProvider() {
        // Given
        when(thirdPartyProviderService.updateProvider(eq(1L), any(PSDThirdPartyProviderDTO.class)))
                .thenReturn(Mono.just(providerDTO));

        // When & Then
        webTestClient.put()
                .uri("/api/v1/providers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(providerDTO)
                .header("X-Request-ID", "test-request-id")
                .exchange()
                .expectStatus().isOk()
                .expectBody(PSDThirdPartyProviderDTO.class)
                .isEqualTo(providerDTO);
    }

    @Test
    void suspendProvider_shouldReturnSuspendedProvider() {
        // Given
        PSDThirdPartyProviderDTO suspendedProvider = new PSDThirdPartyProviderDTO();
        suspendedProvider.setId(1L);
        suspendedProvider.setStatus("SUSPENDED");

        when(thirdPartyProviderService.suspendProvider(1L)).thenReturn(Mono.just(suspendedProvider));

        // When & Then
        webTestClient.post()
                .uri("/api/v1/providers/1/suspend")
                .header("X-Request-ID", "test-request-id")
                .exchange()
                .expectStatus().isOk()
                .expectBody(PSDThirdPartyProviderDTO.class)
                .isEqualTo(suspendedProvider);
    }

    @Test
    void activateProvider_shouldReturnActivatedProvider() {
        // Given
        when(thirdPartyProviderService.activateProvider(1L)).thenReturn(Mono.just(providerDTO));

        // When & Then
        webTestClient.post()
                .uri("/api/v1/providers/1/activate")
                .header("X-Request-ID", "test-request-id")
                .exchange()
                .expectStatus().isOk()
                .expectBody(PSDThirdPartyProviderDTO.class)
                .isEqualTo(providerDTO);
    }

    @Test
    void revokeProvider_shouldReturnOk() {
        // Given
        when(thirdPartyProviderService.revokeProvider(1L)).thenReturn(Mono.just(true));

        // When & Then
        webTestClient.delete()
                .uri("/api/v1/providers/1")
                .header("X-Request-ID", "test-request-id")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Boolean.class)
                .isEqualTo(true);
    }

    @Test
    void validateApiKey_shouldReturnProvider() {
        // Given
        when(thirdPartyProviderService.validateApiKey("test-api-key")).thenReturn(Mono.just(providerDTO));

        // When & Then
        webTestClient.get()
                .uri("/api/v1/providers/validate")
                .header("X-API-KEY", "test-api-key")
                .header("X-Request-ID", "test-request-id")
                .exchange()
                .expectStatus().isOk()
                .expectBody(PSDThirdPartyProviderDTO.class)
                .isEqualTo(providerDTO);
    }
}
