package com.firefly.core.banking.psdx.interfaces.services;

import com.firefly.core.banking.psdx.interfaces.dtos.PSDAccountDTO;
import com.firefly.core.banking.psdx.interfaces.dtos.PSDBalanceDTO;
import com.firefly.core.banking.psdx.interfaces.dtos.PSDTransactionDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

/**
 * Service interface for Account Information Services (AIS) according to PSD2/PSD3 regulations.
 */
public interface AccountInformationService {

    /**
     * Get all accounts for a customer that are accessible through a specific consent.
     *
     * @param consentId The ID of the consent
     * @param partyId The ID of the customer
     * @return A Flux of accounts
     */
    Flux<PSDAccountDTO> getAccounts(Long consentId, Long partyId);

    /**
     * Get a specific account for a customer.
     *
     * @param consentId The ID of the consent
     * @param accountId The ID of the account
     * @return A Mono of the account
     */
    Mono<PSDAccountDTO> getAccount(Long consentId, Long accountId);

    /**
     * Get the balances for a specific account.
     *
     * @param consentId The ID of the consent
     * @param accountId The ID of the account
     * @return A Flux of balances
     */
    Flux<PSDBalanceDTO> getBalances(Long consentId, Long accountId);

    /**
     * Get the transactions for a specific account within a date range.
     *
     * @param consentId The ID of the consent
     * @param accountId The ID of the account
     * @param fromDate The start date of the range
     * @param toDate The end date of the range
     * @return A Flux of transactions
     */
    Flux<PSDTransactionDTO> getTransactions(Long consentId, Long accountId, LocalDate fromDate, LocalDate toDate);

    /**
     * Get a specific transaction for an account.
     *
     * @param consentId The ID of the consent
     * @param accountId The ID of the account
     * @param transactionId The ID of the transaction
     * @return A Mono of the transaction
     */
    Mono<PSDTransactionDTO> getTransaction(Long consentId, Long accountId, Long transactionId);
}
