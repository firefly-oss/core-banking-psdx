package com.catalis.core.banking.psdx.core.services;

import com.catalis.core.banking.psdx.core.mappers.ThirdPartyProviderMapper;
import com.catalis.core.banking.psdx.interfaces.dtos.PSDThirdPartyProviderDTO;
import com.catalis.core.banking.psdx.interfaces.dtos.PSDThirdPartyProviderRegistrationDTO;
import com.catalis.core.banking.psdx.interfaces.enums.ProviderStatus;
import com.catalis.core.banking.psdx.interfaces.enums.ProviderType;
import com.catalis.core.banking.psdx.interfaces.services.ThirdPartyProviderService;
import com.catalis.core.banking.psdx.models.entities.ThirdPartyProvider;
import com.catalis.core.banking.psdx.models.repositories.ThirdPartyProviderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Implementation of the ThirdPartyProviderService interface.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ThirdPartyProviderServiceImpl implements ThirdPartyProviderService {

    private final ThirdPartyProviderRepository thirdPartyProviderRepository;
    private final ThirdPartyProviderMapper thirdPartyProviderMapper;

    @Override
    public Mono<PSDThirdPartyProviderDTO> registerProvider(PSDThirdPartyProviderRegistrationDTO registration) {
        log.debug("Registering new third party provider: {}", registration.getName());

        // Generate a unique API key
        String apiKey = UUID.randomUUID().toString();

        ThirdPartyProvider provider = ThirdPartyProvider.builder()
                .name(registration.getName())
                .registrationNumber(registration.getRegistrationNumber())
                .apiKey(apiKey)
                .redirectUri(registration.getRedirectUri())
                .status(ProviderStatus.ACTIVE)
                .providerType(ProviderType.valueOf(registration.getProviderType()))
                .build();

        return thirdPartyProviderRepository.save(provider)
                .map(thirdPartyProviderMapper::toDto)
                .doOnSuccess(dto -> log.info("Third party provider registered with ID: {}", dto.getId()));
    }

    @Override
    public Mono<PSDThirdPartyProviderDTO> getProvider(Long providerId) {
        log.debug("Getting third party provider with ID: {}", providerId);

        return thirdPartyProviderRepository.findById(providerId)
                .map(thirdPartyProviderMapper::toDto)
                .doOnSuccess(dto -> {
                    if (dto != null) {
                        log.debug("Found third party provider with ID: {}", providerId);
                    } else {
                        log.debug("Third party provider not found with ID: {}", providerId);
                    }
                });
    }

    @Override
    public Flux<PSDThirdPartyProviderDTO> getAllProviders() {
        log.debug("Getting all third party providers");

        return thirdPartyProviderRepository.findAll()
                .map(thirdPartyProviderMapper::toDto)
                .doOnComplete(() -> log.debug("Retrieved all third party providers"));
    }

    @Override
    public Mono<PSDThirdPartyProviderDTO> updateProvider(Long providerId, PSDThirdPartyProviderDTO providerUpdate) {
        log.debug("Updating third party provider with ID: {}", providerId);

        return thirdPartyProviderRepository.findById(providerId)
                .flatMap(provider -> {
                    provider.setName(providerUpdate.getName());
                    provider.setRedirectUri(providerUpdate.getRedirectUri());
                    provider.setUpdatedAt(LocalDateTime.now());
                    return thirdPartyProviderRepository.save(provider);
                })
                .map(thirdPartyProviderMapper::toDto)
                .doOnSuccess(dto -> log.info("Updated third party provider with ID: {}", providerId));
    }

    @Override
    public Mono<PSDThirdPartyProviderDTO> suspendProvider(Long providerId) {
        log.debug("Suspending third party provider with ID: {}", providerId);

        return thirdPartyProviderRepository.findById(providerId)
                .flatMap(provider -> {
                    provider.setStatus(ProviderStatus.SUSPENDED);
                    provider.setUpdatedAt(LocalDateTime.now());
                    return thirdPartyProviderRepository.save(provider);
                })
                .map(thirdPartyProviderMapper::toDto)
                .doOnSuccess(dto -> log.info("Suspended third party provider with ID: {}", providerId));
    }

    @Override
    public Mono<PSDThirdPartyProviderDTO> activateProvider(Long providerId) {
        log.debug("Activating third party provider with ID: {}", providerId);

        return thirdPartyProviderRepository.findById(providerId)
                .flatMap(provider -> {
                    provider.setStatus(ProviderStatus.ACTIVE);
                    provider.setUpdatedAt(LocalDateTime.now());
                    return thirdPartyProviderRepository.save(provider);
                })
                .map(thirdPartyProviderMapper::toDto)
                .doOnSuccess(dto -> log.info("Activated third party provider with ID: {}", providerId));
    }

    @Override
    public Mono<Boolean> revokeProvider(Long providerId) {
        log.debug("Revoking third party provider with ID: {}", providerId);

        return thirdPartyProviderRepository.findById(providerId)
                .flatMap(provider -> {
                    provider.setStatus(ProviderStatus.REVOKED);
                    provider.setUpdatedAt(LocalDateTime.now());
                    return thirdPartyProviderRepository.save(provider)
                            .thenReturn(true);
                })
                .defaultIfEmpty(false)
                .doOnSuccess(result -> {
                    if (result) {
                        log.info("Revoked third party provider with ID: {}", providerId);
                    } else {
                        log.warn("Failed to revoke third party provider with ID: {}", providerId);
                    }
                });
    }

    @Override
    public Mono<PSDThirdPartyProviderDTO> validateApiKey(String apiKey) {
        log.debug("Validating API key");

        return thirdPartyProviderRepository.findByApiKey(apiKey)
                .filter(provider -> provider.getStatus() == ProviderStatus.ACTIVE)
                .map(thirdPartyProviderMapper::toDto)
                .doOnSuccess(dto -> {
                    if (dto != null) {
                        log.debug("API key validated successfully");
                    } else {
                        log.debug("API key validation failed");
                    }
                });
    }
}
