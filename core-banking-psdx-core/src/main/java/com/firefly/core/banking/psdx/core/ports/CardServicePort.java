package com.firefly.core.banking.psdx.core.ports;

import com.firefly.core.banking.psdx.interfaces.dtos.PSDBalanceDTO;
import com.firefly.core.banking.psdx.interfaces.dtos.PSDCardAccountDTO;
import com.firefly.core.banking.psdx.interfaces.dtos.PSDTransactionDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Port for interacting with the card service.
 */
public interface CardServicePort {

    /**
     * Get all card accounts for a customer.
     *
     * @param partyId The ID of the customer
     * @return A Flux of card accounts
     */
    Flux<PSDCardAccountDTO> getCardAccountsByPartyId(UUID partyId);

    /**
     * Get a specific card account.
     *
     * @param cardId The ID of the card
     * @return A Mono of the card account
     */
    Mono<PSDCardAccountDTO> getCardAccountById(UUID cardId);

    /**
     * Get balances for a specific card account.
     *
     * @param cardId The ID of the card
     * @return A Flux of balances
     */
    Flux<PSDBalanceDTO> getBalancesByCardId(UUID cardId);

    /**
     * Get transactions for a specific card account within a date range.
     *
     * @param cardId The ID of the card
     * @param fromDate The start date of the range
     * @param toDate The end date of the range
     * @return A Flux of transactions
     */
    Flux<PSDTransactionDTO> getTransactionsByCardId(UUID cardId, LocalDate fromDate, LocalDate toDate);

    /**
     * Get a specific transaction for a card account.
     *
     * @param cardId The ID of the card
     * @param transactionId The ID of the transaction
     * @return A Mono of the transaction
     */
    Mono<PSDTransactionDTO> getTransactionByCardIdAndTransactionId(UUID cardId, UUID transactionId);
}
