package com.catalis.core.banking.psdx.web.security;

import com.catalis.core.banking.psdx.interfaces.dtos.PSDThirdPartyProviderDTO;
import com.catalis.core.banking.psdx.interfaces.services.ThirdPartyProviderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Web filter for API key authentication.
 * This filter extracts the API key from the X-API-KEY header,
 * validates it against the ThirdPartyProviderService, and sets
 * the authentication in the security context.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ApiKeyAuthenticationWebFilter implements WebFilter {

    private final ThirdPartyProviderService thirdPartyProviderService;

    /**
     * Filter method to extract and validate API key.
     *
     * @param exchange The server web exchange
     * @param chain The web filter chain
     * @return A Mono of Void
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String apiKey = exchange.getRequest().getHeaders().getFirst("X-API-KEY");
        
        if (StringUtils.hasText(apiKey)) {
            return thirdPartyProviderService.validateApiKey(apiKey)
                    .flatMap(provider -> {
                        if (provider != null) {
                            log.debug("API key validated successfully for provider: {}", provider.getName());
                            return chain.filter(exchange)
                                    .contextWrite(ReactiveSecurityContextHolder.withAuthentication(
                                            createAuthentication(provider)));
                        } else {
                            log.debug("Invalid API key");
                            return chain.filter(exchange);
                        }
                    })
                    .onErrorResume(e -> {
                        log.error("Error validating API key", e);
                        return chain.filter(exchange);
                    });
        }
        
        return chain.filter(exchange);
    }

    /**
     * Create an authentication object from a provider.
     *
     * @param provider The provider
     * @return The authentication object
     */
    private UsernamePasswordAuthenticationToken createAuthentication(PSDThirdPartyProviderDTO provider) {
        List<SimpleGrantedAuthority> authorities = provider.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());
        
        return new UsernamePasswordAuthenticationToken(provider, null, authorities);
    }
}
