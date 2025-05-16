package com.catalis.core.banking.psdx.core.mappers;

import com.catalis.core.banking.psdx.interfaces.dtos.PSDThirdPartyProviderDTO;
import com.catalis.core.banking.psdx.interfaces.enums.ProviderStatus;
import com.catalis.core.banking.psdx.interfaces.enums.ProviderType;
import com.catalis.core.banking.psdx.models.entities.ThirdPartyProvider;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for converting between ThirdPartyProvider entity and ThirdPartyProviderDTO.
 */
@Mapper(componentModel = "spring")
public interface ThirdPartyProviderMapper {

    /**
     * Convert a ThirdPartyProvider entity to a ThirdPartyProviderDTO.
     *
     * @param provider The ThirdPartyProvider entity
     * @return The ThirdPartyProviderDTO
     */
    @Mapping(target = "status", expression = "java(provider.getStatus().name())")
    @Mapping(target = "providerType", expression = "java(provider.getProviderType().name())")
    @Mapping(target = "nationalCompetentAuthority", ignore = true)
    @Mapping(target = "nationalCompetentAuthorityCountry", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "certificate", ignore = true)
    @Mapping(target = "_links", ignore = true)
    PSDThirdPartyProviderDTO toDto(ThirdPartyProvider provider);

    /**
     * Convert a ThirdPartyProviderDTO to a ThirdPartyProvider entity.
     *
     * @param providerDTO The ThirdPartyProviderDTO
     * @return The ThirdPartyProvider entity
     */
    @Mapping(target = "status", expression = "java(com.catalis.core.banking.psdx.interfaces.enums.ProviderStatus.valueOf(providerDTO.getStatus()))")
    @Mapping(target = "providerType", expression = "java(com.catalis.core.banking.psdx.interfaces.enums.ProviderType.valueOf(providerDTO.getProviderType()))")
    @Mapping(target = "apiKey", ignore = true)
    ThirdPartyProvider toEntity(PSDThirdPartyProviderDTO providerDTO);
}