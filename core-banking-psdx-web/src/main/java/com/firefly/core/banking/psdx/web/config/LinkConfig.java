package com.firefly.core.banking.psdx.web.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for links.
 */
@Configuration
public class LinkConfig {

    @Value("${psdx.api.base-url:http://localhost:8080}")
    private String baseUrl;

    /**
     * Get the base URL for links.
     *
     * @return The base URL
     */
    @Bean
    public String baseUrl() {
        return baseUrl;
    }
}
