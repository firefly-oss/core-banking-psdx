package com.catalis.core.banking.psdx.web.interceptors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import com.catalis.core.banking.psdx.interfaces.exceptions.PSDFormatException;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

/**
 * Tests for the RequestHeadersInterceptor.
 */
@ExtendWith(MockitoExtension.class)
class RequestHeadersInterceptorTest {

    @Mock
    private WebFilterChain chain;

    private RequestHeadersInterceptor interceptor;

    @BeforeEach
    void setUp() {
        interceptor = new RequestHeadersInterceptor();
        lenient().when(chain.filter(any())).thenReturn(Mono.empty());
    }

    @Test
    void filter_shouldPassRequest_whenPathIsPublic() {
        // Given
        MockServerHttpRequest request = MockServerHttpRequest
                .get("/api/v1/auth/token")
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        // When
        Mono<Void> result = interceptor.filter(exchange, chain);

        // Then
        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    void filter_shouldReturnBadRequest_whenRequestIdHeaderIsMissing() {
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
                        throwable.getMessage().contains("Missing X-Request-ID header"))
                .verify();
    }

    @Test
    void filter_shouldReturnBadRequest_whenConsentIdHeaderIsMissingForAccountPath() {
        // Given
        MockServerHttpRequest request = MockServerHttpRequest
                .get("/api/v1/accounts")
                .header("X-Request-ID", "request-id-123")
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
    void filter_shouldReturnBadRequest_whenPsuIdHeaderIsMissingForAccountPath() {
        // Given
        MockServerHttpRequest request = MockServerHttpRequest
                .get("/api/v1/accounts")
                .header("X-Request-ID", "request-id-123")
                .header("X-Consent-ID", "consent-id-123")
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        // When
        Mono<Void> result = interceptor.filter(exchange, chain);

        // Then
        StepVerifier.create(result)
                .expectErrorMatches(throwable ->
                        throwable instanceof PSDFormatException &&
                        throwable.getMessage().contains("Missing PSU-ID header"))
                .verify();
    }

    @Test
    void filter_shouldPassRequest_whenAllRequiredHeadersArePresent() {
        // Given
        MockServerHttpRequest request = MockServerHttpRequest
                .get("/api/v1/accounts")
                .header("X-Request-ID", "request-id-123")
                .header("X-Consent-ID", "consent-id-123")
                .header("PSU-ID", "psu-id-123")
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        // When
        Mono<Void> result = interceptor.filter(exchange, chain);

        // Then
        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    void filter_shouldPassRequest_whenPathDoesNotRequireConsentHeader() {
        // Given
        MockServerHttpRequest request = MockServerHttpRequest
                .get("/api/v1/providers")
                .header("X-Request-ID", "request-id-123")
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        // When
        Mono<Void> result = interceptor.filter(exchange, chain);

        // Then
        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    void filter_shouldPassRequest_whenPathDoesNotRequirePsuHeaders() {
        // Given
        MockServerHttpRequest request = MockServerHttpRequest
                .get("/api/v1/consents")
                .header("X-Request-ID", "request-id-123")
                .header("X-Consent-ID", "consent-id-123")
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        // When
        Mono<Void> result = interceptor.filter(exchange, chain);

        // Then
        StepVerifier.create(result)
                .verifyComplete();
    }
}
