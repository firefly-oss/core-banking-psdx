package com.firefly.core.banking.psdx.core.ports;

import com.firefly.core.banking.psdx.interfaces.dtos.PSDTransactionDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Port for interacting with the transaction service.
 */
public interface TransactionServicePort {

    /**
     * Get transactions for a specific account within a date range.
     *
     * @param accountId The ID of the account
     * @param fromDate The start date of the range
     * @param toDate The end date of the range
     * @return A Flux of transactions
     */
    Flux<PSDTransactionDTO> getTransactionsByAccountId(UUID accountId, LocalDate fromDate, LocalDate toDate);

    /**
     * Get a specific transaction.
     *
     * @param transactionId The ID of the transaction
     * @return A Mono of the transaction
     */
    Mono<PSDTransactionDTO> getTransactionById(UUID transactionId);
}
