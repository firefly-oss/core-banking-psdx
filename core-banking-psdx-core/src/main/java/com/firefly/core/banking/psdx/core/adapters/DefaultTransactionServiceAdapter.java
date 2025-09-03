package com.firefly.core.banking.psdx.core.adapters;

import com.firefly.core.banking.psdx.core.ports.TransactionServicePort;
import com.firefly.core.banking.psdx.interfaces.dtos.PSDTransactionDTO;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Default implementation of the TransactionServicePort.
 * This implementation throws exceptions as "not yet implemented".
 */
@Component
public class DefaultTransactionServiceAdapter implements TransactionServicePort {

    @Override
    public Flux<PSDTransactionDTO> getTransactionsByAccountId(UUID accountId, LocalDate fromDate, LocalDate toDate) {
        return Flux.error(new UnsupportedOperationException("Method getTransactionsByAccountId not yet implemented"));
    }

    @Override
    public Mono<PSDTransactionDTO> getTransactionById(UUID transactionId) {
        return Mono.error(new UnsupportedOperationException("Method getTransactionById not yet implemented"));
    }
}
