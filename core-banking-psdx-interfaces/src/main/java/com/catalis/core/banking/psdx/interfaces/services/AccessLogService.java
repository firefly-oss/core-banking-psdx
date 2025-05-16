package com.catalis.core.banking.psdx.interfaces.services;

import com.catalis.core.banking.psdx.interfaces.dtos.PSDAccessLogDTO;
import com.catalis.core.banking.psdx.interfaces.dtos.PSDAccessLogRequestDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

/**
 * Service interface for managing access logs according to PSD2/PSD3 and FIDA regulations.
 */
public interface AccessLogService {

    /**
     * Log an access to customer data or an operation performed on their behalf.
     *
     * @param accessLogRequest The access log request
     * @return A Mono of the created access log
     */
    Mono<PSDAccessLogDTO> logAccess(PSDAccessLogRequestDTO accessLogRequest);

    /**
     * Get an access log by its ID.
     *
     * @param logId The ID of the access log
     * @return A Mono of the access log
     */
    Mono<PSDAccessLogDTO> getAccessLog(Long logId);

    /**
     * Get all access logs for a customer.
     *
     * @param partyId The ID of the customer
     * @return A Flux of access logs
     */
    Flux<PSDAccessLogDTO> getAccessLogsForCustomer(Long partyId);

    /**
     * Get all access logs for a customer within a date range.
     *
     * @param partyId The ID of the customer
     * @param fromDate The start date of the range
     * @param toDate The end date of the range
     * @return A Flux of access logs
     */
    Flux<PSDAccessLogDTO> getAccessLogsForCustomerInDateRange(Long partyId, LocalDateTime fromDate, LocalDateTime toDate);

    /**
     * Get all access logs for a specific consent.
     *
     * @param consentId The ID of the consent
     * @return A Flux of access logs
     */
    Flux<PSDAccessLogDTO> getAccessLogsForConsent(Long consentId);

    /**
     * Get all access logs for a specific Third Party Provider.
     *
     * @param thirdPartyId The ID of the Third Party Provider
     * @return A Flux of access logs
     */
    Flux<PSDAccessLogDTO> getAccessLogsForThirdParty(String thirdPartyId);
}
