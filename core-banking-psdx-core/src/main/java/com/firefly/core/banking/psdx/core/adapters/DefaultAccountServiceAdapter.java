package com.firefly.core.banking.psdx.core.adapters;

import com.firefly.core.banking.psdx.core.ports.AccountServicePort;
import com.firefly.core.banking.psdx.interfaces.dtos.PSDAccountDTO;
import com.firefly.core.banking.psdx.interfaces.dtos.PSDBalanceDTO;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Default implementation of the AccountServicePort.
 * This implementation throws exceptions as "not yet implemented".
 */
@Component
public class DefaultAccountServiceAdapter implements AccountServicePort {

    @Override
    public Flux<PSDAccountDTO> getAccountsByPartyId(Long partyId) {
        return Flux.error(new UnsupportedOperationException("Method getAccountsByPartyId not yet implemented"));
    }

    @Override
    public Mono<PSDAccountDTO> getAccountById(Long accountId) {
        return Mono.error(new UnsupportedOperationException("Method getAccountById not yet implemented"));
    }

    @Override
    public Flux<PSDBalanceDTO> getBalancesByAccountId(Long accountId) {
        return Flux.error(new UnsupportedOperationException("Method getBalancesByAccountId not yet implemented"));
    }
}
