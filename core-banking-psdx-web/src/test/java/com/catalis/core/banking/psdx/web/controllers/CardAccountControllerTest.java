package com.catalis.core.banking.psdx.web.controllers;

import com.catalis.core.banking.psdx.interfaces.dtos.PSDBalanceDTO;
import com.catalis.core.banking.psdx.interfaces.dtos.PSDCardAccountDTO;
import com.catalis.core.banking.psdx.interfaces.dtos.PSDTransactionDTO;
import com.catalis.core.banking.psdx.interfaces.services.CardAccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

import com.catalis.core.banking.psdx.web.utils.TestUtils;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.anyLong;

@ExtendWith(MockitoExtension.class)
class CardAccountControllerTest {

    @Mock
    private CardAccountService cardAccountService;

    @InjectMocks
    private CardAccountController cardAccountController;

    private WebTestClient webTestClient;

    private final Long CONSENT_ID = 1L;
    private final Long PARTY_ID = 100L;
    private final Long CARD_ACCOUNT_ID = 1000L;
    private final Long TRANSACTION_ID = 10000L;

    private PSDCardAccountDTO cardAccount1;
    private PSDCardAccountDTO cardAccount2;
    private PSDBalanceDTO balance1;
    private PSDBalanceDTO balance2;
    private PSDTransactionDTO transaction1;
    private PSDTransactionDTO transaction2;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToController(cardAccountController).build();

        // Setup test data
        cardAccount1 = new PSDCardAccountDTO();
        cardAccount1.setResourceId(CARD_ACCOUNT_ID);
        cardAccount1.setMaskedPan("123456******7890");
        cardAccount1.setOwnerPartyId(PARTY_ID);
        cardAccount1.setCurrency("EUR");
        cardAccount1.setName("Credit Card");
        cardAccount1.setProduct("Gold Card");
        cardAccount1.setStatus("enabled");

        cardAccount2 = new PSDCardAccountDTO();
        cardAccount2.setResourceId(CARD_ACCOUNT_ID + 1);
        cardAccount2.setMaskedPan("987654******3210");
        cardAccount2.setOwnerPartyId(PARTY_ID);
        cardAccount2.setCurrency("USD");
        cardAccount2.setName("Debit Card");
        cardAccount2.setProduct("Standard Card");
        cardAccount2.setStatus("enabled");

        balance1 = new PSDBalanceDTO();
        balance1.setBalanceType("closingBooked");
        balance1.setBalanceAmount(new PSDBalanceDTO.PSDAmountDTO("EUR", BigDecimal.valueOf(1000.00)));

        balance2 = new PSDBalanceDTO();
        balance2.setBalanceType("expected");
        balance2.setBalanceAmount(new PSDBalanceDTO.PSDAmountDTO("EUR", BigDecimal.valueOf(1100.00)));

        transaction1 = new PSDTransactionDTO();
        transaction1.setTransactionId(TRANSACTION_ID);
        transaction1.setTransactionStatus("booked");
        transaction1.setBookingDate(LocalDate.now().minusDays(1));
        transaction1.setValueDate(LocalDate.now());
        transaction1.setTransactionAmount(new PSDTransactionDTO.PSDAmountDTO("EUR", BigDecimal.valueOf(50.00)));

        transaction2 = new PSDTransactionDTO();
        transaction2.setTransactionId(TRANSACTION_ID + 1);
        transaction2.setTransactionStatus("booked");
        transaction2.setBookingDate(LocalDate.now().minusDays(2));
        transaction2.setValueDate(LocalDate.now());
        transaction2.setTransactionAmount(new PSDTransactionDTO.PSDAmountDTO("EUR", BigDecimal.valueOf(75.00)));
    }

    @Test
    void getCardAccounts_shouldReturnCardAccounts() {
        // Given
        when(cardAccountService.getCardAccounts(CONSENT_ID, PARTY_ID))
                .thenReturn(Flux.fromIterable(Arrays.asList(cardAccount1, cardAccount2)));

        // When & Then
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/v1/card-accounts")
                        .queryParam("partyId", PARTY_ID)
                        .build())
                .header("X-Consent-ID", CONSENT_ID.toString())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(PSDCardAccountDTO.class)
                .hasSize(2)
                .contains(cardAccount1, cardAccount2);
    }

    @Test
    void getCardAccount_shouldReturnCardAccount() {
        // Given
        when(cardAccountService.getCardAccount(CONSENT_ID, CARD_ACCOUNT_ID))
                .thenReturn(Mono.just(cardAccount1));

        // When & Then
        webTestClient.get()
                .uri("/api/v1/card-accounts/{cardAccountId}", CARD_ACCOUNT_ID)
                .header("X-Consent-ID", CONSENT_ID.toString())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(PSDCardAccountDTO.class)
                .isEqualTo(cardAccount1);
    }

    @Test
    void getCardAccountBalances_shouldReturnBalances() {
        // Given
        when(cardAccountService.getCardBalances(CONSENT_ID, CARD_ACCOUNT_ID))
                .thenReturn(Flux.fromIterable(Arrays.asList(balance1, balance2)));

        // When & Then
        webTestClient.get()
                .uri("/api/v1/card-accounts/{cardAccountId}/balances", CARD_ACCOUNT_ID)
                .header("X-Consent-ID", CONSENT_ID.toString())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(PSDBalanceDTO.class)
                .hasSize(2)
                .contains(balance1, balance2);
    }

    @Test
    void getCardAccountTransactions_shouldReturnTransactions() {
        // Given
        LocalDate fromDate = LocalDate.now().minusDays(7);
        LocalDate toDate = LocalDate.now();

        when(cardAccountService.getCardTransactions(eq(CONSENT_ID), eq(CARD_ACCOUNT_ID), eq(fromDate), eq(toDate)))
                .thenReturn(Flux.fromIterable(Arrays.asList(transaction1, transaction2)));

        // When & Then
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/v1/card-accounts/{cardAccountId}/transactions")
                        .queryParam("fromDate", fromDate.toString())
                        .queryParam("toDate", toDate.toString())
                        .build(CARD_ACCOUNT_ID))
                .header("X-Consent-ID", CONSENT_ID.toString())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(PSDTransactionDTO.class)
                .hasSize(2)
                .contains(transaction1, transaction2);
    }

    @Test
    void getCardAccountTransactionsWithDateRange_shouldReturnTransactions() {
        // Given
        LocalDate fromDate = LocalDate.now().minusDays(7);
        LocalDate toDate = LocalDate.now();

        when(cardAccountService.getCardTransactions(eq(CONSENT_ID), eq(CARD_ACCOUNT_ID), eq(fromDate), eq(toDate)))
                .thenReturn(Flux.fromIterable(Arrays.asList(transaction1, transaction2)));

        // When & Then
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/v1/card-accounts/{cardAccountId}/transactions")
                        .queryParam("fromDate", fromDate.toString())
                        .queryParam("toDate", toDate.toString())
                        .build(CARD_ACCOUNT_ID))
                .header("X-Consent-ID", CONSENT_ID.toString())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(PSDTransactionDTO.class)
                .hasSize(2)
                .contains(transaction1, transaction2);
    }

    @Test
    void getCardAccountTransaction_shouldReturnTransaction() {
        // Given
        when(cardAccountService.getCardTransaction(CONSENT_ID, CARD_ACCOUNT_ID, TRANSACTION_ID))
                .thenReturn(Mono.just(transaction1));

        // When & Then
        webTestClient.get()
                .uri("/api/v1/card-accounts/{cardAccountId}/transactions/{transactionId}", CARD_ACCOUNT_ID, TRANSACTION_ID)
                .header("X-Consent-ID", CONSENT_ID.toString())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(PSDTransactionDTO.class)
                .isEqualTo(transaction1);
    }
}
