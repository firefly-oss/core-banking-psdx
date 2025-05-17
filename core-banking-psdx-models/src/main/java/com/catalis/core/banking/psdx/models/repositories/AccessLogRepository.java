package com.catalis.core.banking.psdx.models.repositories;

import com.catalis.core.banking.psdx.interfaces.enums.ResourceType;
import com.catalis.core.banking.psdx.models.entities.AccessLog;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

/**
 * Repository for managing AccessLog entities.
 */
@Repository
public interface AccessLogRepository extends ReactiveCrudRepository<AccessLog, Long> {

    /**
     * Find all access logs for a specific party.
     *
     * @param partyId The ID of the party
     * @return A Flux of access logs
     */
    Flux<AccessLog> findByPartyId(Long partyId);

    /**
     * Find all access logs for a specific consent.
     *
     * @param consentId The ID of the consent
     * @return A Flux of access logs
     */
    Flux<AccessLog> findByConsentId(Long consentId);

    /**
     * Find all access logs for a specific third party provider.
     *
     * @param thirdPartyId The ID of the third party provider
     * @return A Flux of access logs
     */
    Flux<AccessLog> findByThirdPartyId(String thirdPartyId);

    /**
     * Find all access logs for a specific party within a date range.
     *
     * @param partyId The ID of the party
     * @param startDate The start date of the range
     * @param endDate The end date of the range
     * @return A Flux of access logs
     */
    @Query("SELECT * FROM access_logs WHERE party_id = :partyId AND created_at BETWEEN :startDate AND :endDate ORDER BY created_at DESC")
    Flux<AccessLog> findByPartyIdAndDateRange(Long partyId, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Find all access logs for a specific resource type and resource ID.
     *
     * @param resourceType The type of resource
     * @param resourceId The ID of the resource
     * @return A Flux of access logs
     */
    Flux<AccessLog> findByResourceTypeAndResourceId(ResourceType resourceType, String resourceId);

    /**
     * Find all access logs for a specific party ordered by timestamp descending.
     *
     * @param partyId The ID of the party
     * @return A Flux of access logs
     */
    Flux<AccessLog> findByPartyIdOrderByTimestampDesc(Long partyId);

    /**
     * Find all access logs for a specific consent ordered by timestamp descending.
     *
     * @param consentId The ID of the consent
     * @return A Flux of access logs
     */
    Flux<AccessLog> findByConsentIdOrderByTimestampDesc(Long consentId);

    /**
     * Find all access logs for a specific third party provider ordered by timestamp descending.
     *
     * @param thirdPartyId The ID of the third party provider
     * @return A Flux of access logs
     */
    Flux<AccessLog> findByThirdPartyIdOrderByTimestampDesc(String thirdPartyId);

    /**
     * Count access logs for a specific consent.
     *
     * @param consentId The ID of the consent
     * @return A Mono of the count
     */
    Mono<Long> countByConsentId(Long consentId);
}
