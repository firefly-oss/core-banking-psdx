package com.catalis.core.banking.psdx.core.config;

import com.catalis.core.banking.psdx.core.adapters.DefaultPaymentServiceAdapter;
import com.catalis.core.banking.psdx.core.ports.PaymentServicePort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Configuration for the Payment service client.
 * This configuration provides a bean for the Payment service port.
 * In a real implementation, this would be replaced with a client for the core-banking-payment-hub service.
 */
@Configuration
public class PaymentServiceClientConfig {

    @Value("${integration.payment-hub.enabled:false}")
    private boolean paymentHubEnabled;

    /**
     * Create a bean for the Payment service port.
     * If the Payment Hub service is enabled, this would return a client for that service.
     * Otherwise, it returns the default adapter that throws "not yet implemented" exceptions.
     *
     * @param defaultAdapter The default adapter
     * @return The Payment service port
     */
    @Bean
    @Primary
    public PaymentServicePort paymentServicePort(DefaultPaymentServiceAdapter defaultAdapter) {
        // In a real implementation, this would return a client for the core-banking-payment-hub service
        // if paymentHubEnabled is true. For now, we'll just return the default adapter.
        return defaultAdapter;
    }
}
