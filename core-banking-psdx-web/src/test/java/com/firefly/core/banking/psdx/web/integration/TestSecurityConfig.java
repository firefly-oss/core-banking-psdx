package com.firefly.core.banking.psdx.web.integration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * Security configuration for integration tests.
 */
@Configuration
@EnableWebFluxSecurity
public class TestSecurityConfig {

    /**
     * Configure security for integration tests.
     * This configuration disables CSRF and allows all requests for testing purposes.
     *
     * @param http The ServerHttpSecurity
     * @return The SecurityWebFilterChain
     */
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf().disable()
                .authorizeExchange()
                .anyExchange().permitAll()
                .and()
                .build();
    }
}
