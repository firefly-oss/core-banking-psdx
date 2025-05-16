package com.catalis.core.banking.psdx.core.services;

import com.catalis.core.banking.psdx.core.ports.AccountServicePort;
import com.catalis.core.banking.psdx.core.ports.TransactionServicePort;
import com.catalis.core.banking.psdx.interfaces.dtos.PSDAccountDTO;
import com.catalis.core.banking.psdx.interfaces.dtos.PSDBalanceDTO;
import com.catalis.core.banking.psdx.interfaces.dtos.PSDTransactionDTO;
import com.catalis.core.banking.psdx.interfaces.services.AccountInformationService;
import com.catalis.core.banking.psdx.interfaces.services.ConsentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

/**
 * Implementation of the AccountInformationService interface.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AccountInformationServiceImpl implements AccountInformationService {

    private final AccountServicePort accountServicePort;
    private final TransactionServicePort transactionServicePort;
    private final ConsentService consentService;

    @Override
    public Flux<PSDAccountDTO> getAccounts(Long consentId, Long partyId) {
        log.debug("Getting accounts for party ID: {} with consent ID: {}", partyId, consentId);

        return consentService.validateConsent(consentId, "ACCOUNT", "READ")
                .flatMapMany(isValid -> {
                    if (!isValid) {
                        return Flux.error(new IllegalArgumentException("Invalid or expired consent"));
                    }

                    return accountServicePort.getAccountsByPartyId(partyId)
                            .doOnComplete(() -> log.debug("Retrieved accounts for party ID: {}", partyId));
                });
    }

    @Override
    public Mono<PSDAccountDTO> getAccount(Long consentId, Long accountId) {
        log.debug("Getting account with ID: {} using consent ID: {}", accountId, consentId);

        return consentService.validateConsent(consentId, "ACCOUNT", "READ")
                .flatMap(isValid -> {
                    if (!isValid) {
                        return Mono.error(new IllegalArgumentException("Invalid or expired consent"));
                    }

                    return accountServicePort.getAccountById(accountId)
                            .doOnSuccess(account -> log.debug("Retrieved account with ID: {}", accountId));
                });
    }

    @Override
    public Flux<PSDBalanceDTO> getBalances(Long consentId, Long accountId) {
        log.debug("Getting balances for account ID: {} using consent ID: {}", accountId, consentId);

        return consentService.validateConsent(consentId, "BALANCE", "READ")
                .flatMapMany(isValid -> {
                    if (!isValid) {
                        return Flux.error(new IllegalArgumentException("Invalid or expired consent"));
                    }

                    return accountServicePort.getBalancesByAccountId(accountId)
                            .doOnComplete(() -> log.debug("Retrieved balances for account ID: {}", accountId));
                });
    }

    @Override
    public Flux<PSDTransactionDTO> getTransactions(Long consentId, Long accountId, LocalDate fromDate, LocalDate toDate) {
        log.debug("Getting transactions for account ID: {} between {} and {} using consent ID: {}",
                accountId, fromDate, toDate, consentId);

        return consentService.validateConsent(consentId, "TRANSACTION", "READ")
                .flatMapMany(isValid -> {
                    if (!isValid) {
                        return Flux.error(new IllegalArgumentException("Invalid or expired consent"));
                    }

                    return transactionServicePort.getTransactionsByAccountId(accountId, fromDate, toDate)
                            .doOnComplete(() -> log.debug("Retrieved transactions for account ID: {} in date range", accountId));
                });
    }

    @Override
    public Mono<PSDTransactionDTO> getTransaction(Long consentId, Long accountId, Long transactionId) {
        log.debug("Getting transaction with ID: {} for account ID: {} using consent ID: {}",
                transactionId, accountId, consentId);

        return consentService.validateConsent(consentId, "TRANSACTION", "READ")
                .flatMap(isValid -> {
                    if (!isValid) {
                        return Mono.error(new IllegalArgumentException("Invalid or expired consent"));
                    }

                    return transactionServicePort.getTransactionById(transactionId)
                            .doOnSuccess(transaction -> log.debug("Retrieved transaction with ID: {}", transactionId));
                });
    }
}
