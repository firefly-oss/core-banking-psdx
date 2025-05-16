package com.catalis.core.banking.psdx.core.mappers;

import com.catalis.core.banking.psdx.interfaces.dtos.PSDAccessLogDTO;
import com.catalis.core.banking.psdx.interfaces.dtos.PSDAccessLogRequestDTO;
import com.catalis.core.banking.psdx.interfaces.enums.AccessStatus;
import com.catalis.core.banking.psdx.interfaces.enums.AccessType;
import com.catalis.core.banking.psdx.interfaces.enums.ResourceType;
import com.catalis.core.banking.psdx.models.entities.AccessLog;
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
    @Mapping(target = "xRequestId", ignore = true)
    @Mapping(target = "tppRequestId", ignore = true)
    @Mapping(target = "psuId", ignore = true)
    @Mapping(target = "psuIdType", ignore = true)
    @Mapping(target = "psuCorporateId", ignore = true)
    @Mapping(target = "psuCorporateIdType", ignore = true)
    @Mapping(target = "tppRedirectUri", ignore = true)
    PSDAccessLogDTO toDto(AccessLog accessLog);

    /**
     * Convert an AccessLogDTO to an AccessLog entity.
     *
     * @param accessLogDTO The AccessLogDTO
     * @return The AccessLog entity
     */
    @Mapping(target = "accessType", expression = "java(com.catalis.core.banking.psdx.interfaces.enums.AccessType.valueOf(accessLogDTO.getAccessType()))")
    @Mapping(target = "resourceType", expression = "java(com.catalis.core.banking.psdx.interfaces.enums.ResourceType.valueOf(accessLogDTO.getResourceType()))")
    @Mapping(target = "status", expression = "java(com.catalis.core.banking.psdx.interfaces.enums.AccessStatus.valueOf(accessLogDTO.getStatus()))")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "createdAt", ignore = true)
    AccessLog toEntity(PSDAccessLogDTO accessLogDTO);

    /**
     * Convert an AccessLogRequestDTO to an AccessLog entity.
     *
     * @param request The AccessLogRequestDTO
     * @return The AccessLog entity
     */
    @Mapping(target = "accessType", expression = "java(com.catalis.core.banking.psdx.interfaces.enums.AccessType.valueOf(request.getAccessType()))")
    @Mapping(target = "resourceType", expression = "java(com.catalis.core.banking.psdx.interfaces.enums.ResourceType.valueOf(request.getResourceType()))")
    @Mapping(target = "status", expression = "java(com.catalis.core.banking.psdx.interfaces.enums.AccessStatus.valueOf(request.getStatus()))")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    AccessLog fromRequest(PSDAccessLogRequestDTO request);
}
