package com.catalis.core.banking.psdx.core.ports;

import com.catalis.core.banking.psdx.interfaces.dtos.PSDAccountDTO;
import com.catalis.core.banking.psdx.interfaces.dtos.PSDBalanceDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Port for interacting with the account service.
 */
public interface AccountServicePort {

    /**
     * Get all accounts for a customer.
     *
     * @param partyId The ID of the customer
     * @return A Flux of accounts
     */
    Flux<PSDAccountDTO> getAccountsByPartyId(Long partyId);

    /**
     * Get a specific account.
     *
     * @param accountId The ID of the account
     * @return A Mono of the account
     */
    Mono<PSDAccountDTO> getAccountById(Long accountId);

    /**
     * Get balances for a specific account.
     *
     * @param accountId The ID of the account
     * @return A Flux of balances
     */
    Flux<PSDBalanceDTO> getBalancesByAccountId(Long accountId);
}
