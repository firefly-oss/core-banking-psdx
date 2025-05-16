package com.catalis.core.banking.psdx.web.controllers;

import com.catalis.core.banking.psdx.interfaces.dtos.PSDBalanceDTO;
import com.catalis.core.banking.psdx.interfaces.dtos.PSDCardAccountDTO;
import com.catalis.core.banking.psdx.interfaces.dtos.PSDTransactionDTO;
import com.catalis.core.banking.psdx.interfaces.services.CardAccountService;
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
 * REST controller for card account information.
 */
@RestController
@RequestMapping("/api/card-accounts")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Card Account Information", description = "APIs for card account information according to PSD2/PSD3 regulations")
public class CardAccountController {

    private final CardAccountService cardAccountService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get card accounts", description = "Gets all card accounts for a customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Card accounts found",
                    content = @Content(schema = @Schema(implementation = PSDCardAccountDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Flux<PSDCardAccountDTO> getCardAccounts(
            @Parameter(description = "ID of the consent", required = true)
            @RequestHeader("X-Consent-ID") Long consentId,
            @Parameter(description = "ID of the customer", required = true)
            @RequestParam Long partyId) {
        log.debug("REST request to get card accounts for party ID: {} with consent ID: {}", partyId, consentId);
        return cardAccountService.getCardAccounts(consentId, partyId);
    }

    @GetMapping(value = "/{cardId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get card account", description = "Gets a card account by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Card account found",
                    content = @Content(schema = @Schema(implementation = PSDCardAccountDTO.class))),
            @ApiResponse(responseCode = "404", description = "Card account not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Mono<PSDCardAccountDTO> getCardAccount(
            @Parameter(description = "ID of the consent", required = true)
            @RequestHeader("X-Consent-ID") Long consentId,
            @Parameter(description = "ID of the card", required = true)
            @PathVariable Long cardId) {
        log.debug("REST request to get card account: {} with consent ID: {}", cardId, consentId);
        return cardAccountService.getCardAccount(consentId, cardId);
    }

    @GetMapping(value = "/{cardId}/balances", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get card balances", description = "Gets balances for a card account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Card balances found",
                    content = @Content(schema = @Schema(implementation = PSDBalanceDTO.class))),
            @ApiResponse(responseCode = "404", description = "Card account not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Flux<PSDBalanceDTO> getCardBalances(
            @Parameter(description = "ID of the consent", required = true)
            @RequestHeader("X-Consent-ID") Long consentId,
            @Parameter(description = "ID of the card", required = true)
            @PathVariable Long cardId) {
        log.debug("REST request to get balances for card ID: {} with consent ID: {}", cardId, consentId);
        return cardAccountService.getCardBalances(consentId, cardId);
    }

    @GetMapping(value = "/{cardId}/transactions", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get card transactions", description = "Gets transactions for a card account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Card transactions found",
                    content = @Content(schema = @Schema(implementation = PSDTransactionDTO.class))),
            @ApiResponse(responseCode = "404", description = "Card account not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Flux<PSDTransactionDTO> getCardTransactions(
            @Parameter(description = "ID of the consent", required = true)
            @RequestHeader("X-Consent-ID") Long consentId,
            @Parameter(description = "ID of the card", required = true)
            @PathVariable Long cardId,
            @Parameter(description = "Start date of the range", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @Parameter(description = "End date of the range", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        log.debug("REST request to get transactions for card ID: {} between {} and {} with consent ID: {}",
                cardId, fromDate, toDate, consentId);
        return cardAccountService.getCardTransactions(consentId, cardId, fromDate, toDate);
    }

    @GetMapping(value = "/{cardId}/transactions/{transactionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get card transaction", description = "Gets a transaction for a card account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Card transaction found",
                    content = @Content(schema = @Schema(implementation = PSDTransactionDTO.class))),
            @ApiResponse(responseCode = "404", description = "Card transaction not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Mono<PSDTransactionDTO> getCardTransaction(
            @Parameter(description = "ID of the consent", required = true)
            @RequestHeader("X-Consent-ID") Long consentId,
            @Parameter(description = "ID of the card", required = true)
            @PathVariable Long cardId,
            @Parameter(description = "ID of the transaction", required = true)
            @PathVariable Long transactionId) {
        log.debug("REST request to get transaction: {} for card ID: {} with consent ID: {}",
                transactionId, cardId, consentId);
        return cardAccountService.getCardTransaction(consentId, cardId, transactionId);
    }
}
