package com.catalis.core.banking.psdx.interfaces.services;

import com.catalis.core.banking.psdx.interfaces.dtos.PSDFundsConfirmationDTO;
import reactor.core.publisher.Mono;

/**
 * Service interface for Funds Confirmation Services (FCS) according to PSD2/PSD3 regulations.
 */
public interface FundsConfirmationService {

    /**
     * Confirm the availability of funds for a specific amount.
     *
     * @param consentId The ID of the consent
     * @param fundsConfirmationRequest The funds confirmation request
     * @return A Mono of the funds confirmation response
     */
    Mono<PSDFundsConfirmationDTO> confirmFunds(Long consentId, PSDFundsConfirmationDTO fundsConfirmationRequest);

    /**
     * Get a funds confirmation by its ID.
     *
     * @param consentId The ID of the consent
     * @param fundsConfirmationId The ID of the funds confirmation
     * @return A Mono of the funds confirmation
     */
    Mono<PSDFundsConfirmationDTO> getFundsConfirmation(Long consentId, Long fundsConfirmationId);
}
