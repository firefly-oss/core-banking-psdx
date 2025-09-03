package com.firefly.core.banking.psdx.web.interceptors;

import com.firefly.core.banking.psdx.core.services.ConsentValidationService;
import com.firefly.core.banking.psdx.interfaces.enums.ResourceType;
import com.firefly.core.banking.psdx.interfaces.exceptions.PSDConsentInvalidException;
import com.firefly.core.banking.psdx.interfaces.exceptions.PSDFormatException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Interceptor for validating consents in requests.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ConsentValidationInterceptor implements WebFilter {

    private final ConsentValidationService consentValidationService;

    private static final List<String> PUBLIC_PATHS = Arrays.asList(
            "/api/v1/auth/token",
            "/api/v1/auth/refresh",
            "/api/v1/providers/register",
            "/api/v1/providers/validate",
            "/v3/api-docs",
            "/swagger-ui",
            "/actuator/health",
            "/actuator/info"
    );

    /**
     * Filter method to validate consents.
     *
     * @param exchange The server web exchange
     * @param chain The web filter chain
     * @return A Mono of Void
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String path = exchange.getRequest().getPath().value();

        // Skip validation for public paths
        if (isPublicPath(path)) {
            return chain.filter(exchange);
        }

        // Skip validation for paths that don't require consent
        if (!requiresConsent(path)) {
            return chain.filter(exchange);
        }

        // Get consent ID from header
        String consentIdHeader = exchange.getRequest().getHeaders().getFirst("X-Consent-ID");
        if (consentIdHeader == null || consentIdHeader.isEmpty()) {
            log.warn("Missing X-Consent-ID header for path: {}", path);
            return Mono.error(new PSDFormatException("Missing X-Consent-ID header", "Please provide a valid consent ID in the X-Consent-ID header"));
        }

        UUID consentId;
        try {
            consentId = UUID.fromString(consentIdHeader);
        } catch (IllegalArgumentException e) {
            log.warn("Invalid X-Consent-ID header: {} for path: {}", consentIdHeader, path);
            return Mono.error(new PSDFormatException("Invalid X-Consent-ID header", "The consent ID must be a valid UUID"));
        }

        // Get party ID from request parameter or header
        UUID partyId = getPartyId(exchange.getRequest());
        if (partyId == null) {
            log.warn("Missing party ID for path: {}", path);
            return Mono.error(new PSDFormatException("Missing party ID", "Please provide a valid party ID in the request parameters or PSU-ID header"));
        }

        // Get third party ID from security context or header
        String thirdPartyId = getThirdPartyId(exchange.getRequest());
        if (thirdPartyId == null) {
            log.warn("Missing third party ID for path: {}", path);
            return Mono.error(new PSDFormatException("Missing third party ID", "Please provide a valid API key in the X-API-KEY header"));
        }

        // Determine resource type from path
        ResourceType resourceType = getResourceTypeFromPath(path);

        // Validate consent
        return consentValidationService.validateConsent(consentId, resourceType, partyId, thirdPartyId)
                .flatMap(isValid -> {
                    if (isValid) {
                        return chain.filter(exchange);
                    } else {
                        log.warn("Invalid consent ID: {} for resource type: {}, party ID: {}, third party ID: {}",
                                consentId, resourceType, partyId, thirdPartyId);
                        return Mono.error(new PSDConsentInvalidException(
                                "Invalid consent",
                                "The provided consent is invalid, expired, or does not grant access to the requested resource"));
                    }
                });
    }

    /**
     * Check if a path is a public path that doesn't require consent validation.
     *
     * @param path The path
     * @return True if the path is public, false otherwise
     */
    private boolean isPublicPath(String path) {
        return PUBLIC_PATHS.stream().anyMatch(path::startsWith);
    }

    /**
     * Check if a path requires consent validation.
     *
     * @param path The path
     * @return True if the path requires consent validation, false otherwise
     */
    private boolean requiresConsent(String path) {
        return path.startsWith("/api/v1/accounts") ||
               path.startsWith("/api/v1/card-accounts") ||
               path.startsWith("/api/v1/payments") ||
               path.startsWith("/api/v1/funds-confirmations");
    }

    /**
     * Get the party ID from the request.
     *
     * @param request The request
     * @return The party ID, or null if not found
     */
    private UUID getPartyId(ServerHttpRequest request) {
        // Try to get from query parameter
        String partyIdParam = request.getQueryParams().getFirst("partyId");
        if (partyIdParam != null && !partyIdParam.isEmpty()) {
            try {
                return UUID.fromString(partyIdParam);
            } catch (IllegalArgumentException e) {
                log.warn("Invalid party ID parameter: {}", partyIdParam);
                return null;
            }
        }

        // Try to get from PSU-ID header
        String psuId = request.getHeaders().getFirst("PSU-ID");
        if (psuId != null && !psuId.isEmpty()) {
            // In a real implementation, this would look up the party ID from the PSU ID
            // For now, we'll just return a dummy value
            return UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
        }

        return null;
    }

    /**
     * Get the third party ID from the request.
     *
     * @param request The request
     * @return The third party ID, or null if not found
     */
    private String getThirdPartyId(ServerHttpRequest request) {
        // Try to get from X-API-KEY header
        String apiKey = request.getHeaders().getFirst("X-API-KEY");
        if (apiKey != null && !apiKey.isEmpty()) {
            // In a real implementation, this would look up the third party ID from the API key
            // For now, we'll just return the API key
            return apiKey;
        }

        return null;
    }

    /**
     * Get the resource type from the path.
     *
     * @param path The path
     * @return The resource type
     */
    private ResourceType getResourceTypeFromPath(String path) {
        if (path.contains("/accounts") && !path.contains("/card-accounts")) {
            if (path.contains("/balances")) {
                return ResourceType.BALANCE;
            } else if (path.contains("/transactions")) {
                return ResourceType.TRANSACTION;
            } else {
                return ResourceType.ACCOUNT;
            }
        } else if (path.contains("/card-accounts")) {
            if (path.contains("/balances")) {
                return ResourceType.CARD_BALANCE;
            } else if (path.contains("/transactions")) {
                return ResourceType.CARD_TRANSACTION;
            } else {
                return ResourceType.CARD;
            }
        } else if (path.contains("/payments")) {
            return ResourceType.PAYMENT;
        } else if (path.contains("/funds-confirmations")) {
            return ResourceType.FUNDS_CONFIRMATION;
        } else {
            return ResourceType.CONSENT;
        }
    }
}
