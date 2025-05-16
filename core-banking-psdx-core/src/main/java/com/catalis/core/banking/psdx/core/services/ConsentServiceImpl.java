package com.catalis.core.banking.psdx.core.services;

import com.catalis.core.banking.psdx.core.mappers.ConsentMapper;
import com.catalis.core.banking.psdx.interfaces.dtos.PSDConsentDTO;
import com.catalis.core.banking.psdx.interfaces.dtos.PSDConsentRequestDTO;
import com.catalis.core.banking.psdx.interfaces.dtos.PSDConsentStatusDTO;
import com.catalis.core.banking.psdx.interfaces.enums.ConsentStatus;
import com.catalis.core.banking.psdx.interfaces.enums.ConsentType;
import com.catalis.core.banking.psdx.interfaces.services.ConsentService;
import com.catalis.core.banking.psdx.models.entities.Consent;
import com.catalis.core.banking.psdx.models.repositories.ConsentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

/**
 * Implementation of the ConsentService interface.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ConsentServiceImpl implements ConsentService {

    private final ConsentRepository consentRepository;
    private final ConsentMapper consentMapper;

    @Override
    public Mono<PSDConsentDTO> createConsent(PSDConsentRequestDTO consentRequest) {
        log.debug("Creating consent for party ID: {}", consentRequest.getPartyId());

        Consent consent = Consent.builder()
                .partyId(consentRequest.getPartyId())
                .consentType(ConsentType.valueOf(consentRequest.getConsentType()))
                .status(ConsentStatus.RECEIVED)
                .validFrom(consentRequest.getValidFrom() != null ? consentRequest.getValidFrom() : LocalDateTime.now())
                .validUntil(consentRequest.getValidUntil())
                .accessFrequency(consentRequest.getAccessFrequency())
                .accessScope(consentRequest.getAccessScope())
                .build();

        return consentRepository.save(consent)
                .map(consentMapper::toDto)
                .doOnSuccess(dto -> log.info("Consent created with ID: {}", dto.getId()));
    }

    @Override
    public Mono<PSDConsentDTO> getConsent(Long consentId) {
        log.debug("Getting consent with ID: {}", consentId);

        return consentRepository.findById(consentId)
                .map(consentMapper::toDto)
                .doOnSuccess(dto -> {
                    if (dto != null) {
                        log.debug("Found consent with ID: {}", consentId);
                    } else {
                        log.debug("Consent not found with ID: {}", consentId);
                    }
                });
    }

    @Override
    public Flux<PSDConsentDTO> getConsentsForCustomer(Long partyId) {
        log.debug("Getting consents for party ID: {}", partyId);

        return consentRepository.findByPartyId(partyId)
                .map(consentMapper::toDto)
                .doOnComplete(() -> log.debug("Retrieved consents for party ID: {}", partyId));
    }

    @Override
    public Mono<PSDConsentDTO> updateConsentStatus(Long consentId, PSDConsentStatusDTO statusUpdate) {
        log.debug("Updating consent status for ID: {} to {}", consentId, statusUpdate.getStatus());

        return consentRepository.findById(consentId)
                .flatMap(consent -> {
                    consent.setStatus(ConsentStatus.valueOf(statusUpdate.getStatus()));
                    consent.setUpdatedAt(LocalDateTime.now());
                    return consentRepository.save(consent);
                })
                .map(consentMapper::toDto)
                .doOnSuccess(dto -> log.info("Updated consent status for ID: {} to {}", consentId, statusUpdate.getStatus()));
    }

    @Override
    public Mono<PSDConsentDTO> revokeConsent(Long consentId) {
        log.debug("Revoking consent with ID: {}", consentId);

        return consentRepository.findById(consentId)
                .flatMap(consent -> {
                    consent.setStatus(ConsentStatus.REVOKED);
                    consent.setUpdatedAt(LocalDateTime.now());
                    return consentRepository.save(consent);
                })
                .map(consentMapper::toDto)
                .doOnSuccess(dto -> log.info("Revoked consent with ID: {}", consentId));
    }

    @Override
    public Mono<Boolean> validateConsent(Long consentId, String resourceType, String accessType) {
        log.debug("Validating consent with ID: {} for resource type: {} and access type: {}",
                consentId, resourceType, accessType);

        return consentRepository.findById(consentId)
                .map(consent -> {
                    // Check if consent is valid
                    boolean isValid = consent.getStatus() == ConsentStatus.VALID &&
                            consent.getValidFrom().isBefore(LocalDateTime.now()) &&
                            consent.getValidUntil().isAfter(LocalDateTime.now()) &&
                            consent.getAccessScope().contains(resourceType.toLowerCase());

                    log.debug("Consent validation result for ID {}: {}", consentId, isValid);
                    return isValid;
                })
                .defaultIfEmpty(false);
    }
}
