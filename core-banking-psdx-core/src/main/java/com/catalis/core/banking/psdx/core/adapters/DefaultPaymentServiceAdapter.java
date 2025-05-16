package com.catalis.core.banking.psdx.core.adapters;

import com.catalis.core.banking.psdx.core.ports.PaymentServicePort;
import com.catalis.core.banking.psdx.interfaces.dtos.PSDPaymentDTO;
import com.catalis.core.banking.psdx.interfaces.dtos.PSDPaymentInitiationRequestDTO;
import com.catalis.core.banking.psdx.interfaces.dtos.PSDPaymentStatusDTO;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * Default implementation of the PaymentServicePort.
 * This implementation throws exceptions as "not yet implemented".
 */
@Component
public class DefaultPaymentServiceAdapter implements PaymentServicePort {

    @Override
    public Mono<PSDPaymentDTO> initiatePayment(PSDPaymentInitiationRequestDTO paymentRequest) {
        return Mono.error(new UnsupportedOperationException("Method initiatePayment not yet implemented"));
    }

    @Override
    public Mono<PSDPaymentStatusDTO> getPaymentStatus(Long paymentId) {
        return Mono.error(new UnsupportedOperationException("Method getPaymentStatus not yet implemented"));
    }

    @Override
    public Mono<PSDPaymentDTO> getPayment(Long paymentId) {
        return Mono.error(new UnsupportedOperationException("Method getPayment not yet implemented"));
    }

    @Override
    public Mono<Boolean> cancelPayment(Long paymentId) {
        return Mono.error(new UnsupportedOperationException("Method cancelPayment not yet implemented"));
    }

    @Override
    public Mono<PSDPaymentDTO> authorizePayment(Long paymentId, String authorizationCode) {
        return Mono.error(new UnsupportedOperationException("Method authorizePayment not yet implemented"));
    }
}
