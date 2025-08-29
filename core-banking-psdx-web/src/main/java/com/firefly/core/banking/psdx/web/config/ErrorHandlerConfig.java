package com.firefly.core.banking.psdx.web.config;

import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for error handling.
 */
@Configuration
public class ErrorHandlerConfig {

    /**
     * Create a bean for web properties resources.
     *
     * @return The web properties resources
     */
    @Bean
    public WebProperties.Resources resources() {
        return new WebProperties.Resources();
    }
}
