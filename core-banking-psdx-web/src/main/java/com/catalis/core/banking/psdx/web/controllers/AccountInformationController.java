package com.catalis.core.banking.psdx.web.controllers;

import com.catalis.core.banking.psdx.interfaces.dtos.PSDAccountDTO;
import com.catalis.core.banking.psdx.interfaces.dtos.PSDBalanceDTO;
import com.catalis.core.banking.psdx.interfaces.dtos.PSDTransactionDTO;
import com.catalis.core.banking.psdx.interfaces.services.AccountInformationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;


/**
 * REST controller for account information services.
 */
@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Account Information Services", description = "APIs for account information services according to PSD2/PSD3 regulations")
public class AccountInformationController {

    private final AccountInformationService accountInformationService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get accounts", description = "Gets all accounts for a customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Accounts found",
                    content = @Content(schema = @Schema(implementation = PSDAccountDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Flux<PSDAccountDTO> getAccounts(
            @Parameter(description = "ID of the consent", required = true)
            @RequestHeader("X-Consent-ID") Long consentId,
            @Parameter(description = "ID of the customer", required = true)
            @RequestParam Long partyId) {
        log.debug("REST request to get accounts for party ID: {} with consent ID: {}", partyId, consentId);
        return accountInformationService.getAccounts(consentId, partyId);
    }

    @GetMapping(value = "/{accountId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get account", description = "Gets a specific account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account found",
                    content = @Content(schema = @Schema(implementation = PSDAccountDTO.class))),
            @ApiResponse(responseCode = "404", description = "Account not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Mono<PSDAccountDTO> getAccount(
            @Parameter(description = "ID of the consent", required = true)
            @RequestHeader("X-Consent-ID") Long consentId,
            @Parameter(description = "ID of the account", required = true)
            @PathVariable Long accountId) {
        log.debug("REST request to get account: {} with consent ID: {}", accountId, consentId);
        return accountInformationService.getAccount(consentId, accountId);
    }

    @GetMapping(value = "/{accountId}/balances", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get balances", description = "Gets balances for a specific account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Balances found",
                    content = @Content(schema = @Schema(implementation = PSDBalanceDTO.class))),
            @ApiResponse(responseCode = "404", description = "Account not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Flux<PSDBalanceDTO> getBalances(
            @Parameter(description = "ID of the consent", required = true)
            @RequestHeader("X-Consent-ID") Long consentId,
            @Parameter(description = "ID of the account", required = true)
            @PathVariable Long accountId) {
        log.debug("REST request to get balances for account: {} with consent ID: {}", accountId, consentId);
        return accountInformationService.getBalances(consentId, accountId);
    }

    @GetMapping(value = "/{accountId}/transactions", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get transactions", description = "Gets transactions for a specific account within a date range")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transactions found",
                    content = @Content(schema = @Schema(implementation = PSDTransactionDTO.class))),
            @ApiResponse(responseCode = "404", description = "Account not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Flux<PSDTransactionDTO> getTransactions(
            @Parameter(description = "ID of the consent", required = true)
            @RequestHeader("X-Consent-ID") Long consentId,
            @Parameter(description = "ID of the account", required = true)
            @PathVariable Long accountId,
            @Parameter(description = "Start date of the range", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @Parameter(description = "End date of the range", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        log.debug("REST request to get transactions for account: {} between {} and {} with consent ID: {}",
                accountId, fromDate, toDate, consentId);
        return accountInformationService.getTransactions(consentId, accountId, fromDate, toDate);
    }

    @GetMapping(value = "/{accountId}/transactions/{transactionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get transaction", description = "Gets a specific transaction for an account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction found",
                    content = @Content(schema = @Schema(implementation = PSDTransactionDTO.class))),
            @ApiResponse(responseCode = "404", description = "Transaction not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Mono<PSDTransactionDTO> getTransaction(
            @Parameter(description = "ID of the consent", required = true)
            @RequestHeader("X-Consent-ID") Long consentId,
            @Parameter(description = "ID of the account", required = true)
            @PathVariable Long accountId,
            @Parameter(description = "ID of the transaction", required = true)
            @PathVariable Long transactionId) {
        log.debug("REST request to get transaction: {} for account: {} with consent ID: {}",
                transactionId, accountId, consentId);
        return accountInformationService.getTransaction(consentId, accountId, transactionId);
    }
}
