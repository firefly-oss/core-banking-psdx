package com.firefly.core.banking.psdx.models.repositories;

import com.firefly.core.banking.psdx.interfaces.enums.ProviderStatus;
import com.firefly.core.banking.psdx.interfaces.enums.ProviderType;
import com.firefly.core.banking.psdx.models.entities.ThirdPartyProvider;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Repository for managing ThirdPartyProvider entities.
 */
@Repository
public interface ThirdPartyProviderRepository extends ReactiveCrudRepository<ThirdPartyProvider, Long> {

    /**
     * Find a third party provider by its API key.
     *
     * @param apiKey The API key of the third party provider
     * @return A Mono of the third party provider
     */
    Mono<ThirdPartyProvider> findByApiKey(String apiKey);

    /**
     * Find a third party provider by its registration number.
     *
     * @param registrationNumber The registration number of the third party provider
     * @return A Mono of the third party provider
     */
    Mono<ThirdPartyProvider> findByRegistrationNumber(String registrationNumber);

    /**
     * Find all third party providers of a specific type.
     *
     * @param providerType The type of the third party provider
     * @return A Flux of third party providers
     */
    Flux<ThirdPartyProvider> findByProviderType(ProviderType providerType);

    /**
     * Find all active third party providers.
     *
     * @param status The status of the third party provider
     * @return A Flux of third party providers
     */
    Flux<ThirdPartyProvider> findByStatus(ProviderStatus status);
}
