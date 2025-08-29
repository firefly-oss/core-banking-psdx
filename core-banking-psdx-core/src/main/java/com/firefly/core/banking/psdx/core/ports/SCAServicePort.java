package com.firefly.core.banking.psdx.core.ports;

import com.firefly.core.banking.psdx.interfaces.dtos.PSDSCAAuthenticationRequestDTO;
import com.firefly.core.banking.psdx.interfaces.dtos.PSDSCAAuthenticationResponseDTO;
import com.firefly.core.banking.psdx.interfaces.dtos.PSDSCAValidationRequestDTO;
import com.firefly.core.banking.psdx.interfaces.dtos.PSDSCAValidationResponseDTO;
import reactor.core.publisher.Mono;

/**
 * Port for the SCA (Strong Customer Authentication) service.
 */
public interface SCAServicePort {

    /**
     * Initiate SCA for a customer.
     *
     * @param request The SCA authentication request
     * @return A Mono of PSDSCAAuthenticationResponseDTO
     */
    Mono<PSDSCAAuthenticationResponseDTO> initiateSCA(PSDSCAAuthenticationRequestDTO request);

    /**
     * Validate SCA for a customer.
     *
     * @param request The SCA validation request
     * @return A Mono of PSDSCAValidationResponseDTO
     */
    Mono<PSDSCAValidationResponseDTO> validateSCA(PSDSCAValidationRequestDTO request);

    /**
     * Check if SCA is required for a payment.
     *
     * @param amount The payment amount
     * @param currency The payment currency
     * @return A Mono of Boolean indicating if SCA is required
     */
    Mono<Boolean> isSCARequired(Double amount, String currency);
}
