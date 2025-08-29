package com.firefly.core.banking.psdx.core.config;

import com.firefly.core.banking.psdx.core.adapters.DefaultTransactionServiceAdapter;
import com.firefly.core.banking.psdx.core.ports.TransactionServicePort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Configuration for the Transaction service client.
 * This configuration provides a bean for the Transaction service port.
 * In a real implementation, this would be replaced with a client for the core-banking-ledger service.
 */
@Configuration
public class TransactionServiceClientConfig {

    @Value("${integration.ledger.enabled:false}")
    private boolean ledgerEnabled;

    /**
     * Create a bean for the Transaction service port.
     * If the Ledger service is enabled, this would return a client for that service.
     * Otherwise, it returns the default adapter that throws "not yet implemented" exceptions.
     *
     * @param defaultAdapter The default adapter
     * @return The Transaction service port
     */
    @Bean
    @Primary
    public TransactionServicePort transactionServicePort(DefaultTransactionServiceAdapter defaultAdapter) {
        // In a real implementation, this would return a client for the core-banking-ledger service
        // if ledgerEnabled is true. For now, we'll just return the default adapter.
        return defaultAdapter;
    }
}
