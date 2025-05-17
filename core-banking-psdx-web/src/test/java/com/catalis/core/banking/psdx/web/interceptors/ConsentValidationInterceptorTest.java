package com.catalis.core.banking.psdx.web.interceptors;

import com.catalis.core.banking.psdx.core.services.ConsentValidationService;
import com.catalis.core.banking.psdx.interfaces.enums.ResourceType;
import com.catalis.core.banking.psdx.interfaces.exceptions.PSDFormatException;
import com.catalis.core.banking.psdx.interfaces.exceptions.PSDConsentInvalidException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.net.URI;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

/**
 * Tests for the ConsentValidationInterceptor.
 */
@ExtendWith(MockitoExtension.class)
class ConsentValidationInterceptorTest {

    @Mock
    private ConsentValidationService consentValidationService;

    @Mock
    private WebFilterChain chain;

    private ConsentValidationInterceptor interceptor;

    @BeforeEach
    void setUp() {
        interceptor = new ConsentValidationInterceptor(consentValidationService);
        lenient().when(chain.filter(any())).thenReturn(Mono.empty());
    }

    @Test
    void filter_shouldPassRequest_whenPathIsPublic() {
        // Given
        MockServerHttpRequest request = MockServerHttpRequest
                .get("/api/auth/token")
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        // When
        Mono<Void> result = interceptor.filter(exchange, chain);

        // Then
        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    void filter_shouldPassRequest_whenPathDoesNotRequireConsent() {
        // Given
        MockServerHttpRequest request = MockServerHttpRequest
                .get("/api/providers/register")
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        // When
        Mono<Void> result = interceptor.filter(exchange, chain);

        // Then
        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    void filter_shouldReturnBadRequest_whenConsentIdHeaderIsMissing() {
        // Given
        MockServerHttpRequest request = MockServerHttpRequest
                .get("/api/v1/accounts")
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        // When
        Mono<Void> result = interceptor.filter(exchange, chain);

        // Then
        StepVerifier.create(result)
                .expectErrorMatches(throwable ->
                        throwable instanceof PSDFormatException &&
                        throwable.getMessage().contains("Missing X-Consent-ID header"))
                .verify();
    }

    @Test
    void filter_shouldReturnBadRequest_whenConsentIdHeaderIsInvalid() {
        // Given
        MockServerHttpRequest request = MockServerHttpRequest
                .get("/api/v1/accounts")
                .header("X-Consent-ID", "invalid")
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        // When
        Mono<Void> result = interceptor.filter(exchange, chain);

        // Then
        StepVerifier.create(result)
                .expectErrorMatches(throwable ->
                        throwable instanceof PSDFormatException &&
                        throwable.getMessage().contains("Invalid X-Consent-ID header"))
                .verify();
    }

    @Test
    void filter_shouldReturnBadRequest_whenPartyIdIsMissing() {
        // Given
        MockServerHttpRequest request = MockServerHttpRequest
                .get("/api/v1/accounts")
                .header("X-Consent-ID", "123")
                .header("X-API-KEY", "api-key-123")
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        // When
        Mono<Void> result = interceptor.filter(exchange, chain);

        // Then
        StepVerifier.create(result)
                .expectErrorMatches(throwable ->
                        throwable instanceof PSDFormatException &&
                        throwable.getMessage().contains("Missing party ID"))
                .verify();
    }

    @Test
    void filter_shouldReturnBadRequest_whenThirdPartyIdIsMissing() {
        // Given
        MockServerHttpRequest request = MockServerHttpRequest
                .get("/api/v1/accounts")
                .header("X-Consent-ID", "123")
                .queryParam("partyId", "456")
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        // When
        Mono<Void> result = interceptor.filter(exchange, chain);

        // Then
        StepVerifier.create(result)
                .expectErrorMatches(throwable ->
                        throwable instanceof PSDFormatException &&
                        throwable.getMessage().contains("Missing third party ID"))
                .verify();
    }

    @Test
    void filter_shouldPassRequest_whenConsentIsValid() {
        // Given
        MockServerHttpRequest request = MockServerHttpRequest
                .get("/api/v1/accounts")
                .header("X-Consent-ID", "123")
                .header("X-API-KEY", "api-key-123")
                .queryParam("partyId", "456")
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        when(consentValidationService.validateConsent(eq(123L), any(ResourceType.class), eq(456L), eq("api-key-123")))
                .thenReturn(Mono.just(true));

        // When
        Mono<Void> result = interceptor.filter(exchange, chain);

        // Then
        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    void filter_shouldReturnForbidden_whenConsentIsInvalid() {
        // Given
        MockServerHttpRequest request = MockServerHttpRequest
                .get("/api/v1/accounts")
                .header("X-Consent-ID", "123")
                .header("X-API-KEY", "api-key-123")
                .queryParam("partyId", "456")
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        when(consentValidationService.validateConsent(eq(123L), any(ResourceType.class), eq(456L), eq("api-key-123")))
                .thenReturn(Mono.just(false));

        // When
        Mono<Void> result = interceptor.filter(exchange, chain);

        // Then
        StepVerifier.create(result)
                .expectErrorMatches(throwable ->
                        throwable instanceof PSDConsentInvalidException &&
                        throwable.getMessage().contains("Invalid consent"))
                .verify();
    }
}
