package com.firefly.core.banking.psdx.core.services;

import com.firefly.core.banking.psdx.core.mappers.AccessLogMapper;
import com.firefly.core.banking.psdx.interfaces.dtos.PSDAccessLogDTO;
import com.firefly.core.banking.psdx.interfaces.enums.AccessStatus;
import com.firefly.core.banking.psdx.interfaces.enums.AccessType;
import com.firefly.core.banking.psdx.interfaces.enums.ResourceType;
import com.firefly.core.banking.psdx.interfaces.services.AccessLogServiceInterface;
import com.firefly.core.banking.psdx.models.entities.AccessLog;
import com.firefly.core.banking.psdx.models.repositories.AccessLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

/**
 * Service for managing access logs.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AccessLogService implements AccessLogServiceInterface {

    private final AccessLogRepository accessLogRepository;
    private final AccessLogMapper accessLogMapper;

    /**
     * Log an access to a resource.
     *
     * @param consentId The ID of the consent
     * @param partyId The ID of the party (customer)
     * @param thirdPartyId The ID of the third party provider
     * @param accessType The type of access
     * @param resourceType The type of resource being accessed
     * @param resourceId The ID of the resource being accessed
     * @param ipAddress The IP address of the client
     * @param userAgent The user agent of the client
     * @param status The status of the access
     * @param xRequestId The X-Request-ID header
     * @param tppRequestId The TPP-Request-ID header
     * @param psuId The PSU-ID header
     * @return A Mono of the created access log
     */
    @Override
    public Mono<PSDAccessLogDTO> logAccess(
            Long consentId,
            Long partyId,
            String thirdPartyId,
            AccessType accessType,
            ResourceType resourceType,
            String resourceId,
            String ipAddress,
            String userAgent,
            AccessStatus status,
            String xRequestId,
            String tppRequestId,
            String psuId) {
        
        log.debug("Logging access: consentId={}, partyId={}, thirdPartyId={}, accessType={}, resourceType={}, resourceId={}, status={}",
                consentId, partyId, thirdPartyId, accessType, resourceType, resourceId, status);
        
        AccessLog accessLog = AccessLog.builder()
                .consentId(consentId)
                .partyId(partyId)
                .thirdPartyId(thirdPartyId)
                .accessType(accessType)
                .resourceType(resourceType)
                .resourceId(resourceId)
                .ipAddress(ipAddress)
                .userAgent(userAgent)
                .status(status)
                .xRequestId(xRequestId)
                .tppRequestId(tppRequestId)
                .psuId(psuId)
                .timestamp(LocalDateTime.now())
                .build();
        
        return accessLogRepository.save(accessLog)
                .map(accessLogMapper::toDto)
                .doOnSuccess(dto -> log.info("Access logged with ID: {}", dto.getId()));
    }

    /**
     * Get access logs for a party.
     *
     * @param partyId The ID of the party (customer)
     * @return A Flux of access logs
     */
    @Override
    public Flux<PSDAccessLogDTO> getAccessLogsForParty(Long partyId) {
        log.debug("Getting access logs for party ID: {}", partyId);
        
        return accessLogRepository.findByPartyIdOrderByTimestampDesc(partyId)
                .map(accessLogMapper::toDto)
                .doOnComplete(() -> log.debug("Retrieved access logs for party ID: {}", partyId));
    }

    /**
     * Get access logs for a consent.
     *
     * @param consentId The ID of the consent
     * @return A Flux of access logs
     */
    @Override
    public Flux<PSDAccessLogDTO> getAccessLogsForConsent(Long consentId) {
        log.debug("Getting access logs for consent ID: {}", consentId);
        
        return accessLogRepository.findByConsentIdOrderByTimestampDesc(consentId)
                .map(accessLogMapper::toDto)
                .doOnComplete(() -> log.debug("Retrieved access logs for consent ID: {}", consentId));
    }

    /**
     * Get access logs for a third party provider.
     *
     * @param thirdPartyId The ID of the third party provider
     * @return A Flux of access logs
     */
    @Override
    public Flux<PSDAccessLogDTO> getAccessLogsForThirdParty(String thirdPartyId) {
        log.debug("Getting access logs for third party ID: {}", thirdPartyId);
        
        return accessLogRepository.findByThirdPartyIdOrderByTimestampDesc(thirdPartyId)
                .map(accessLogMapper::toDto)
                .doOnComplete(() -> log.debug("Retrieved access logs for third party ID: {}", thirdPartyId));
    }

    /**
     * Count access logs for a consent.
     *
     * @param consentId The ID of the consent
     * @return A Mono of the count
     */
    public Mono<Long> countAccessLogsForConsent(Long consentId) {
        log.debug("Counting access logs for consent ID: {}", consentId);
        
        return accessLogRepository.countByConsentId(consentId)
                .doOnSuccess(count -> log.debug("Counted {} access logs for consent ID: {}", count, consentId));
    }
}
