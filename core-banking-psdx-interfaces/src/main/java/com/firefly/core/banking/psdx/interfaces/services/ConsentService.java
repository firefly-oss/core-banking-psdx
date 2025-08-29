package com.firefly.core.banking.psdx.interfaces.services;

import com.firefly.core.banking.psdx.interfaces.dtos.PSDConsentDTO;
import com.firefly.core.banking.psdx.interfaces.dtos.PSDConsentRequestDTO;
import com.firefly.core.banking.psdx.interfaces.dtos.PSDConsentStatusDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service interface for managing consents according to PSD2/PSD3 regulations.
 */
public interface ConsentService {

    /**
     * Create a new consent for a customer.
     *
     * @param consentRequest The consent request
     * @return A Mono of the created consent
     */
    Mono<PSDConsentDTO> createConsent(PSDConsentRequestDTO consentRequest);

    /**
     * Get a consent by its ID.
     *
     * @param consentId The ID of the consent
     * @return A Mono of the consent
     */
    Mono<PSDConsentDTO> getConsent(Long consentId);

    /**
     * Get all consents for a customer.
     *
     * @param partyId The ID of the customer
     * @return A Flux of consents
     */
    Flux<PSDConsentDTO> getConsentsForCustomer(Long partyId);

    /**
     * Update the status of a consent.
     *
     * @param consentId The ID of the consent
     * @param statusUpdate The new status
     * @return A Mono of the updated consent
     */
    Mono<PSDConsentDTO> updateConsentStatus(Long consentId, PSDConsentStatusDTO statusUpdate);

    /**
     * Revoke a consent.
     *
     * @param consentId The ID of the consent
     * @return A Mono of the revoked consent
     */
    Mono<PSDConsentDTO> revokeConsent(Long consentId);

    /**
     * Validate a consent for a specific operation.
     *
     * @param consentId The ID of the consent
     * @param resourceType The type of resource being accessed
     * @param accessType The type of access
     * @return A Mono of Boolean indicating if the consent is valid
     */
    Mono<Boolean> validateConsent(Long consentId, String resourceType, String accessType);

    /**
     * Get the status of a consent.
     *
     * @param consentId The ID of the consent
     * @return A Mono of the consent status
     */
    Mono<PSDConsentStatusDTO> getConsentStatus(Long consentId);
}
