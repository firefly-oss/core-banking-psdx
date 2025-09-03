package com.firefly.core.banking.psdx.models.repositories;

import com.firefly.core.banking.psdx.models.entities.FundsConfirmation;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Repository for managing FundsConfirmation entities.
 */
@Repository
public interface FundsConfirmationRepository extends ReactiveCrudRepository<FundsConfirmation, UUID> {

    /**
     * Find all funds confirmations for a specific consent.
     *
     * @param consentId The ID of the consent
     * @return A Flux of funds confirmations
     */
    Flux<FundsConfirmation> findByConsentId(UUID consentId);

    /**
     * Find all funds confirmations for a specific account reference.
     *
     * @param accountReference The account reference
     * @return A Flux of funds confirmations
     */
    Flux<FundsConfirmation> findByAccountReference(String accountReference);

    /**
     * Find all funds confirmations for a specific card number.
     *
     * @param cardNumber The card number
     * @return A Flux of funds confirmations
     */
    Flux<FundsConfirmation> findByCardNumber(String cardNumber);

    /**
     * Find all funds confirmations created within a date range.
     *
     * @param startDate The start date
     * @param endDate The end date
     * @return A Flux of funds confirmations
     */
    @Query("SELECT * FROM funds_confirmations WHERE created_at BETWEEN :startDate AND :endDate")
    Flux<FundsConfirmation> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Find the most recent funds confirmation for a specific account reference.
     *
     * @param accountReference The account reference
     * @return A Mono of the funds confirmation
     */
    @Query("SELECT * FROM funds_confirmations WHERE account_reference = :accountReference ORDER BY created_at DESC LIMIT 1")
    Mono<FundsConfirmation> findMostRecentByAccountReference(String accountReference);

    /**
     * Find the most recent funds confirmation for a specific card number.
     *
     * @param cardNumber The card number
     * @return A Mono of the funds confirmation
     */
    @Query("SELECT * FROM funds_confirmations WHERE card_number = :cardNumber ORDER BY created_at DESC LIMIT 1")
    Mono<FundsConfirmation> findMostRecentByCardNumber(String cardNumber);
}