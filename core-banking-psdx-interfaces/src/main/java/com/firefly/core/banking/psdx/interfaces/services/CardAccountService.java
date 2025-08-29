package com.firefly.core.banking.psdx.interfaces.services;

import com.firefly.core.banking.psdx.interfaces.dtos.PSDBalanceDTO;
import com.firefly.core.banking.psdx.interfaces.dtos.PSDCardAccountDTO;
import com.firefly.core.banking.psdx.interfaces.dtos.PSDTransactionDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

/**
 * Service interface for Card Account Services according to PSD2/PSD3 regulations.
 */
public interface CardAccountService {

    /**
     * Get all card accounts for a customer that are accessible through a specific consent.
     *
     * @param consentId The ID of the consent
     * @param partyId The ID of the customer
     * @return A Flux of card accounts
     */
    Flux<PSDCardAccountDTO> getCardAccounts(Long consentId, Long partyId);

    /**
     * Get a specific card account for a customer.
     *
     * @param consentId The ID of the consent
     * @param cardId The ID of the card
     * @return A Mono of the card account
     */
    Mono<PSDCardAccountDTO> getCardAccount(Long consentId, Long cardId);

    /**
     * Get the balances for a specific card account.
     *
     * @param consentId The ID of the consent
     * @param cardId The ID of the card
     * @return A Flux of balances
     */
    Flux<PSDBalanceDTO> getCardBalances(Long consentId, Long cardId);

    /**
     * Get the transactions for a specific card account within a date range.
     *
     * @param consentId The ID of the consent
     * @param cardId The ID of the card
     * @param fromDate The start date of the range
     * @param toDate The end date of the range
     * @return A Flux of transactions
     */
    Flux<PSDTransactionDTO> getCardTransactions(Long consentId, Long cardId, LocalDate fromDate, LocalDate toDate);

    /**
     * Get a specific transaction for a card account.
     *
     * @param consentId The ID of the consent
     * @param cardId The ID of the card
     * @param transactionId The ID of the transaction
     * @return A Mono of the transaction
     */
    Mono<PSDTransactionDTO> getCardTransaction(Long consentId, Long cardId, Long transactionId);
}
