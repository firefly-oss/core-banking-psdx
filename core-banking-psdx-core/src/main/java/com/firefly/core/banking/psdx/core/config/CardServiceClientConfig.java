package com.firefly.core.banking.psdx.core.config;

import com.firefly.core.banking.psdx.core.adapters.DefaultCardServiceAdapter;
import com.firefly.core.banking.psdx.core.ports.CardServicePort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Configuration for the Card service client.
 * This configuration provides a bean for the Card service port.
 * In a real implementation, this would be replaced with a client for the core-banking-cards service.
 */
@Configuration
public class CardServiceClientConfig {

    @Value("${integration.cards.enabled:false}")
    private boolean cardsEnabled;

    /**
     * Create a bean for the Card service port.
     * If the Cards service is enabled, this would return a client for that service.
     * Otherwise, it returns the default adapter that throws "not yet implemented" exceptions.
     *
     * @param defaultAdapter The default adapter
     * @return The Card service port
     */
    @Bean
    @Primary
    public CardServicePort cardServicePort(DefaultCardServiceAdapter defaultAdapter) {
        // In a real implementation, this would return a client for the core-banking-cards service
        // if cardsEnabled is true. For now, we'll just return the default adapter.
        return defaultAdapter;
    }
}
