package com.firefly.core.banking.psdx.core.mappers;

import com.firefly.core.banking.psdx.interfaces.dtos.PSDConsentDTO;
import com.firefly.core.banking.psdx.models.entities.Consent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for converting between Consent entity and ConsentDTO.
 */
@Mapper(componentModel = "spring")
public interface ConsentMapper {

    /**
     * Convert a Consent entity to a ConsentDTO.
     *
     * @param consent The Consent entity
     * @return The ConsentDTO
     */
    @Mapping(target = "consentType", expression = "java(consent.getConsentType().name())")
    @Mapping(target = "consentStatus", expression = "java(consent.getStatus().name())")
    @Mapping(target = "accessFrequency", source = "accessFrequency")
    @Mapping(target = "frequencyPerDay", source = "accessFrequency")
    @Mapping(target = "accessScope", source = "accessScope")
    @Mapping(target = "lastActionDate", ignore = true)
    @Mapping(target = "access", ignore = true)
    @Mapping(target = "combinedServiceIndicator", ignore = true)
    @Mapping(target = "recurringIndicator", ignore = true)
    @Mapping(target = "_links", ignore = true)
    PSDConsentDTO toDto(Consent consent);

    /**
     * Convert a ConsentDTO to a Consent entity.
     *
     * @param consentDTO The ConsentDTO
     * @return The Consent entity
     */
    @Mapping(target = "consentType", expression = "java(com.firefly.core.banking.psdx.interfaces.enums.ConsentType.valueOf(consentDTO.getConsentType()))")
    @Mapping(target = "status", expression = "java(com.firefly.core.banking.psdx.interfaces.enums.ConsentStatus.valueOf(consentDTO.getConsentStatus()))")
    @Mapping(target = "accessFrequency", source = "accessFrequency")
    @Mapping(target = "accessScope", source = "accessScope")
    @Mapping(target = "access", ignore = true)
    Consent toEntity(PSDConsentDTO consentDTO);

    /**
     * Convert a list of PSDAccessDTO to a string.
     *
     * @param accessList The list of PSDAccessDTO
     * @return The string representation
     */
    default String map(java.util.List<PSDConsentDTO.PSDAccessDTO> accessList) {
        if (accessList == null || accessList.isEmpty()) {
            return null;
        }
        return accessList.stream()
                .map(access -> access.getResourceId())
                .filter(java.util.Objects::nonNull)
                .collect(java.util.stream.Collectors.joining(","));
    }
}