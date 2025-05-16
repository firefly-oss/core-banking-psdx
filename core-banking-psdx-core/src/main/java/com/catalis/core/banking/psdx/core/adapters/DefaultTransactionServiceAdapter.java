package com.catalis.core.banking.psdx.core.adapters;

import com.catalis.core.banking.psdx.core.ports.TransactionServicePort;
import com.catalis.core.banking.psdx.interfaces.dtos.PSDTransactionDTO;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

/**
 * Default implementation of the TransactionServicePort.
 * This implementation throws exceptions as "not yet implemented".
 */
@Component
public class DefaultTransactionServiceAdapter implements TransactionServicePort {

    @Override
    public Flux<PSDTransactionDTO> getTransactionsByAccountId(Long accountId, LocalDate fromDate, LocalDate toDate) {
        return Flux.error(new UnsupportedOperationException("Method getTransactionsByAccountId not yet implemented"));
    }

    @Override
    public Mono<PSDTransactionDTO> getTransactionById(Long transactionId) {
        return Mono.error(new UnsupportedOperationException("Method getTransactionById not yet implemented"));
    }
}
