package com.catalis.core.banking.psdx.core.services;

import com.catalis.core.banking.psdx.core.ports.PaymentServicePort;
import com.catalis.core.banking.psdx.interfaces.dtos.PSDPaymentDTO;
import com.catalis.core.banking.psdx.interfaces.dtos.PSDPaymentInitiationRequestDTO;
import com.catalis.core.banking.psdx.interfaces.dtos.PSDPaymentStatusDTO;
import com.catalis.core.banking.psdx.interfaces.services.ConsentService;
import com.catalis.core.banking.psdx.interfaces.services.PaymentInitiationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Implementation of the PaymentInitiationService interface.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentInitiationServiceImpl implements PaymentInitiationService {

    private final PaymentServicePort paymentServicePort;
    private final ConsentService consentService;

    @Override
    public Mono<PSDPaymentDTO> initiatePayment(Long consentId, PSDPaymentInitiationRequestDTO paymentRequest) {
        log.debug("Initiating payment using consent ID: {}", consentId);

        return consentService.validateConsent(consentId, "PAYMENT", "WRITE")
                .flatMap(isValid -> {
                    if (!isValid) {
                        return Mono.error(new IllegalArgumentException("Invalid or expired consent"));
                    }

                    return paymentServicePort.initiatePayment(paymentRequest)
                            .doOnSuccess(payment -> log.info("Payment initiated with ID: {}", payment.getPaymentId()));
                });
    }

    @Override
    public Mono<PSDPaymentStatusDTO> getPaymentStatus(Long consentId, Long paymentId) {
        log.debug("Getting status for payment ID: {} using consent ID: {}", paymentId, consentId);

        return consentService.validateConsent(consentId, "PAYMENT", "READ")
                .flatMap(isValid -> {
                    if (!isValid) {
                        return Mono.error(new IllegalArgumentException("Invalid or expired consent"));
                    }

                    return paymentServicePort.getPaymentStatus(paymentId)
                            .doOnSuccess(status -> log.debug("Retrieved status for payment ID: {}: {}",
                                    paymentId, status.getStatus()));
                });
    }

    @Override
    public Mono<PSDPaymentDTO> getPayment(Long consentId, Long paymentId) {
        log.debug("Getting payment with ID: {} using consent ID: {}", paymentId, consentId);

        return consentService.validateConsent(consentId, "PAYMENT", "READ")
                .flatMap(isValid -> {
                    if (!isValid) {
                        return Mono.error(new IllegalArgumentException("Invalid or expired consent"));
                    }

                    return paymentServicePort.getPayment(paymentId)
                            .doOnSuccess(payment -> log.debug("Retrieved payment with ID: {}", paymentId));
                });
    }

    @Override
    public Mono<PSDPaymentDTO> cancelPayment(Long consentId, Long paymentId) {
        log.debug("Cancelling payment with ID: {} using consent ID: {}", paymentId, consentId);

        return consentService.validateConsent(consentId, "PAYMENT", "WRITE")
                .flatMap(isValid -> {
                    if (!isValid) {
                        return Mono.error(new IllegalArgumentException("Invalid or expired consent"));
                    }

                    return paymentServicePort.cancelPayment(paymentId)
                            .flatMap(result -> {
                                if (result) {
                                    log.info("Payment with ID: {} cancelled successfully", paymentId);
                                    // Get the updated payment after cancellation
                                    return paymentServicePort.getPayment(paymentId)
                                            .map(payment -> {
                                                // Update the status to CANC
                                                payment.setTransactionStatus("CANC");
                                                return payment;
                                            });
                                } else {
                                    log.warn("Failed to cancel payment with ID: {}", paymentId);
                                    return Mono.error(new IllegalStateException("Failed to cancel payment"));
                                }
                            });
                });
    }

    @Override
    public Mono<PSDPaymentDTO> authorizePayment(Long consentId, Long paymentId, String authorizationCode) {
        log.debug("Authorizing payment with ID: {} using consent ID: {}", paymentId, consentId);

        return consentService.validateConsent(consentId, "PAYMENT", "WRITE")
                .flatMap(isValid -> {
                    if (!isValid) {
                        return Mono.error(new IllegalArgumentException("Invalid or expired consent"));
                    }

                    return paymentServicePort.authorizePayment(paymentId, authorizationCode)
                            .doOnSuccess(payment -> log.info("Payment with ID: {} authorized successfully", paymentId));
                });
    }
}
