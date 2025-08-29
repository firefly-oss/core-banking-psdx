package com.firefly.core.banking.psdx.interfaces.services;

import com.firefly.core.banking.psdx.interfaces.dtos.PSDThirdPartyProviderDTO;
import com.firefly.core.banking.psdx.interfaces.dtos.PSDThirdPartyProviderRegistrationDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service interface for managing Third Party Providers (TPPs) according to PSD2/PSD3 regulations.
 */
public interface ThirdPartyProviderService {

    /**
     * Register a new Third Party Provider.
     *
     * @param registration The registration details
     * @return A Mono of the registered TPP
     */
    Mono<PSDThirdPartyProviderDTO> registerProvider(PSDThirdPartyProviderRegistrationDTO registration);

    /**
     * Get a Third Party Provider by its ID.
     *
     * @param providerId The ID of the TPP
     * @return A Mono of the TPP
     */
    Mono<PSDThirdPartyProviderDTO> getProvider(Long providerId);

    /**
     * Get all Third Party Providers.
     *
     * @return A Flux of TPPs
     */
    Flux<PSDThirdPartyProviderDTO> getAllProviders();

    /**
     * Update a Third Party Provider.
     *
     * @param providerId The ID of the TPP
     * @param providerUpdate The updated details
     * @return A Mono of the updated TPP
     */
    Mono<PSDThirdPartyProviderDTO> updateProvider(Long providerId, PSDThirdPartyProviderDTO providerUpdate);

    /**
     * Suspend a Third Party Provider.
     *
     * @param providerId The ID of the TPP
     * @return A Mono of the suspended TPP
     */
    Mono<PSDThirdPartyProviderDTO> suspendProvider(Long providerId);

    /**
     * Activate a Third Party Provider.
     *
     * @param providerId The ID of the TPP
     * @return A Mono of the activated TPP
     */
    Mono<PSDThirdPartyProviderDTO> activateProvider(Long providerId);

    /**
     * Revoke a Third Party Provider.
     *
     * @param providerId The ID of the TPP
     * @return A Mono of Boolean indicating if the TPP was revoked
     */
    Mono<Boolean> revokeProvider(Long providerId);

    /**
     * Validate a Third Party Provider's API key.
     *
     * @param apiKey The API key of the TPP
     * @return A Mono of the TPP
     */
    Mono<PSDThirdPartyProviderDTO> validateApiKey(String apiKey);
}
