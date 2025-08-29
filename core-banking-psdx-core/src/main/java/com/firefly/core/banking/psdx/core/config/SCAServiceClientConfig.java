package com.firefly.core.banking.psdx.core.config;

import com.firefly.core.banking.psdx.core.adapters.DefaultSCAServiceAdapter;
import com.firefly.core.banking.psdx.core.ports.SCAServicePort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Configuration for the SCA service client.
 * This configuration provides a bean for the SCA service port.
 * In a real implementation, this would be replaced with a client for the common-platform-sca-mgmt service.
 */
@Configuration
public class SCAServiceClientConfig {

    @Value("${integration.sca-mgmt.enabled:false}")
    private boolean scaMgmtEnabled;

    /**
     * Create a bean for the SCA service port.
     * If the SCA Management service is enabled, this would return a client for that service.
     * Otherwise, it returns the default adapter that provides mock responses.
     *
     * @param defaultAdapter The default adapter
     * @return The SCA service port
     */
    @Bean
    @Primary
    public SCAServicePort scaServicePort(DefaultSCAServiceAdapter defaultAdapter) {
        // In a real implementation, this would return a client for the common-platform-sca-mgmt service
        // if scaMgmtEnabled is true. For now, we'll just return the default adapter.
        return defaultAdapter;
    }
}
