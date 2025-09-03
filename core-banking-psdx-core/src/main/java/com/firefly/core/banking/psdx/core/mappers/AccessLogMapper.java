package com.firefly.core.banking.psdx.core.mappers;

import com.firefly.core.banking.psdx.interfaces.dtos.PSDAccessLogDTO;
import com.firefly.core.banking.psdx.interfaces.dtos.PSDAccessLogRequestDTO;
import com.firefly.core.banking.psdx.models.entities.AccessLog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for converting between AccessLog entity and AccessLogDTO.
 */
@Mapper(componentModel = "spring")
public interface AccessLogMapper {

    /**
     * Convert an AccessLog entity to an AccessLogDTO.
     *
     * @param accessLog The AccessLog entity
     * @return The AccessLogDTO
     */
    @Mapping(target = "accessType", expression = "java(accessLog.getAccessType().name())")
    @Mapping(target = "resourceType", expression = "java(accessLog.getResourceType().name())")
    @Mapping(target = "status", expression = "java(accessLog.getStatus().name())")
    @Mapping(target = "xRequestId", source = "XRequestId")
    @Mapping(target = "tppRequestId", source = "tppRequestId")
    @Mapping(target = "psuId", source = "psuId")
    @Mapping(target = "psuIdType", source = "psuIdType")
    @Mapping(target = "psuCorporateId", source = "psuCorporateId")
    @Mapping(target = "psuCorporateIdType", source = "psuCorporateIdType")
    @Mapping(target = "tppRedirectUri", source = "tppRedirectUri")
    PSDAccessLogDTO toDto(AccessLog accessLog);

    /**
     * Convert an AccessLogDTO to an AccessLog entity.
     *
     * @param accessLogDTO The AccessLogDTO
     * @return The AccessLog entity
     */
    @Mapping(target = "accessType", expression = "java(com.firefly.core.banking.psdx.interfaces.enums.AccessType.valueOf(accessLogDTO.getAccessType()))")
    @Mapping(target = "resourceType", expression = "java(com.firefly.core.banking.psdx.interfaces.enums.ResourceType.valueOf(accessLogDTO.getResourceType()))")
    @Mapping(target = "status", expression = "java(com.firefly.core.banking.psdx.interfaces.enums.AccessStatus.valueOf(accessLogDTO.getStatus()))")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "timestamp", ignore = true)
    @Mapping(target = "xRequestId", source = "XRequestId")
    @Mapping(target = "tppRequestId", source = "tppRequestId")
    @Mapping(target = "psuId", source = "psuId")
    @Mapping(target = "psuIdType", source = "psuIdType")
    @Mapping(target = "psuCorporateId", source = "psuCorporateId")
    @Mapping(target = "psuCorporateIdType", source = "psuCorporateIdType")
    @Mapping(target = "tppRedirectUri", source = "tppRedirectUri")
    AccessLog toEntity(PSDAccessLogDTO accessLogDTO);

    /**
     * Convert an AccessLogRequestDTO to an AccessLog entity.
     *
     * @param request The AccessLogRequestDTO
     * @return The AccessLog entity
     */
    @Mapping(target = "accessType", expression = "java(com.firefly.core.banking.psdx.interfaces.enums.AccessType.valueOf(request.getAccessType()))")
    @Mapping(target = "resourceType", expression = "java(com.firefly.core.banking.psdx.interfaces.enums.ResourceType.valueOf(request.getResourceType()))")
    @Mapping(target = "status", expression = "java(com.firefly.core.banking.psdx.interfaces.enums.AccessStatus.valueOf(request.getStatus()))")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "timestamp", ignore = true)
    @Mapping(target = "xRequestId", source = "XRequestId")
    @Mapping(target = "tppRequestId", source = "tppRequestId")
    @Mapping(target = "psuId", source = "psuId")
    @Mapping(target = "psuIdType", source = "psuIdType")
    @Mapping(target = "psuCorporateId", source = "psuCorporateId")
    @Mapping(target = "psuCorporateIdType", source = "psuCorporateIdType")
    @Mapping(target = "tppRedirectUri", source = "tppRedirectUri")
    AccessLog fromRequest(PSDAccessLogRequestDTO request);
}
