package com.catalis.core.banking.psdx.core.ports;

import com.catalis.core.banking.psdx.interfaces.dtos.PSDPaymentDTO;
import com.catalis.core.banking.psdx.interfaces.dtos.PSDPaymentInitiationRequestDTO;
import com.catalis.core.banking.psdx.interfaces.dtos.PSDPaymentStatusDTO;
import reactor.core.publisher.Mono;

/**
 * Port for interacting with the payment service.
 */
public interface PaymentServicePort {

    /**
     * Initiate a new payment.
     *
     * @param paymentRequest The payment initiation request
     * @return A Mono of the initiated payment
     */
    Mono<PSDPaymentDTO> initiatePayment(PSDPaymentInitiationRequestDTO paymentRequest);

    /**
     * Get the status of a payment.
     *
     * @param paymentId The ID of the payment
     * @return A Mono of the payment status
     */
    Mono<PSDPaymentStatusDTO> getPaymentStatus(Long paymentId);

    /**
     * Get the details of a payment.
     *
     * @param paymentId The ID of the payment
     * @return A Mono of the payment
     */
    Mono<PSDPaymentDTO> getPayment(Long paymentId);

    /**
     * Cancel a payment.
     *
     * @param paymentId The ID of the payment
     * @return A Mono of Boolean indicating if the payment was cancelled
     */
    Mono<Boolean> cancelPayment(Long paymentId);

    /**
     * Authorize a payment.
     *
     * @param paymentId The ID of the payment
     * @param authorizationCode The authorization code
     * @return A Mono of the payment
     */
    Mono<PSDPaymentDTO> authorizePayment(Long paymentId, String authorizationCode);
}
