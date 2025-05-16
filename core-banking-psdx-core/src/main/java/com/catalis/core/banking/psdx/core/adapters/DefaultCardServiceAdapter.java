package com.catalis.core.banking.psdx.core.adapters;

import com.catalis.core.banking.psdx.core.ports.CardServicePort;
import com.catalis.core.banking.psdx.interfaces.dtos.PSDBalanceDTO;
import com.catalis.core.banking.psdx.interfaces.dtos.PSDCardAccountDTO;
import com.catalis.core.banking.psdx.interfaces.dtos.PSDTransactionDTO;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

/**
 * Default implementation of the CardServicePort.
 * This implementation throws exceptions as "not yet implemented".
 */
@Component
public class DefaultCardServiceAdapter implements CardServicePort {

    @Override
    public Flux<PSDCardAccountDTO> getCardAccountsByPartyId(Long partyId) {
        return Flux.error(new UnsupportedOperationException("Method getCardAccountsByPartyId not yet implemented"));
    }

    @Override
    public Mono<PSDCardAccountDTO> getCardAccountById(Long cardId) {
        return Mono.error(new UnsupportedOperationException("Method getCardAccountById not yet implemented"));
    }

    @Override
    public Flux<PSDBalanceDTO> getBalancesByCardId(Long cardId) {
        return Flux.error(new UnsupportedOperationException("Method getBalancesByCardId not yet implemented"));
    }

    @Override
    public Flux<PSDTransactionDTO> getTransactionsByCardId(Long cardId, LocalDate fromDate, LocalDate toDate) {
        return Flux.error(new UnsupportedOperationException("Method getTransactionsByCardId not yet implemented"));
    }

    @Override
    public Mono<PSDTransactionDTO> getTransactionByCardIdAndTransactionId(Long cardId, Long transactionId) {
        return Mono.error(new UnsupportedOperationException("Method getTransactionByCardIdAndTransactionId not yet implemented"));
    }
}
