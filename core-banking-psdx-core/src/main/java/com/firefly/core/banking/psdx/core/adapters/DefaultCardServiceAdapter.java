package com.firefly.core.banking.psdx.core.adapters;

import com.firefly.core.banking.psdx.core.ports.CardServicePort;
import com.firefly.core.banking.psdx.interfaces.dtos.PSDBalanceDTO;
import com.firefly.core.banking.psdx.interfaces.dtos.PSDCardAccountDTO;
import com.firefly.core.banking.psdx.interfaces.dtos.PSDTransactionDTO;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Default implementation of the CardServicePort.
 * This implementation throws exceptions as "not yet implemented".
 */
@Component
public class DefaultCardServiceAdapter implements CardServicePort {

    @Override
    public Flux<PSDCardAccountDTO> getCardAccountsByPartyId(UUID partyId) {
        return Flux.error(new UnsupportedOperationException("Method getCardAccountsByPartyId not yet implemented"));
    }

    @Override
    public Mono<PSDCardAccountDTO> getCardAccountById(UUID cardId) {
        return Mono.error(new UnsupportedOperationException("Method getCardAccountById not yet implemented"));
    }

    @Override
    public Flux<PSDBalanceDTO> getBalancesByCardId(UUID cardId) {
        return Flux.error(new UnsupportedOperationException("Method getBalancesByCardId not yet implemented"));
    }

    @Override
    public Flux<PSDTransactionDTO> getTransactionsByCardId(UUID cardId, LocalDate fromDate, LocalDate toDate) {
        return Flux.error(new UnsupportedOperationException("Method getTransactionsByCardId not yet implemented"));
    }

    @Override
    public Mono<PSDTransactionDTO> getTransactionByCardIdAndTransactionId(UUID cardId, UUID transactionId) {
        return Mono.error(new UnsupportedOperationException("Method getTransactionByCardIdAndTransactionId not yet implemented"));
    }
}
