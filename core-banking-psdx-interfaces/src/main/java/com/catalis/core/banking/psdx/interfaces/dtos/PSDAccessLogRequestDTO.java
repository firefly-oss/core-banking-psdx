package com.catalis.core.banking.psdx.interfaces.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO representing a request to log an access according to PSD2/PSD3 standards.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "PSD Access log request")
public class PSDAccessLogRequestDTO {

    @NotNull(message = "Consent ID is required")
    @Schema(description = "ID of the consent used for the access", required = true)
    private Long consentId;

    @NotNull(message = "Party ID is required")
    @Schema(description = "ID of the customer", required = true)
    private Long partyId;

    @NotBlank(message = "Third party ID is required")
    @Schema(description = "ID of the third party provider", required = true)
    private String thirdPartyId;

    @NotBlank(message = "Access type is required")
    @Schema(description = "Type of access", required = true, example = "READ")
    private String accessType;

    @NotBlank(message = "Resource type is required")
    @Schema(description = "Type of resource being accessed", required = true, example = "ACCOUNT")
    private String resourceType;

    @NotBlank(message = "Resource ID is required")
    @Schema(description = "ID of the resource being accessed", required = true, example = "ACC123456")
    private String resourceId;

    @Schema(description = "IP address of the requester", example = "192.168.1.1")
    private String ipAddress;

    @Schema(description = "User agent of the requester", example = "Mozilla/5.0")
    private String userAgent;

    @NotBlank(message = "Status is required")
    @Schema(description = "Status of the access", required = true, example = "SUCCESS")
    private String status;

    @Schema(description = "Error message if the access failed", example = "Unauthorized access")
    private String errorMessage;

    @Schema(description = "X-Request-ID header value", example = "99391c7e-ad88-49ec-a2ad-99ddcb1f7721")
    private String xRequestId;

    @Schema(description = "TPP-Request-ID header value", example = "TPP-123456")
    private String tppRequestId;

    @Schema(description = "PSU-ID header value", example = "PSU-123456")
    private String psuId;

    @Schema(description = "PSU-ID-Type header value", example = "email")
    private String psuIdType;

    @Schema(description = "PSU-Corporate-ID header value", example = "CORP-123456")
    private String psuCorporateId;

    @Schema(description = "PSU-Corporate-ID-Type header value", example = "email")
    private String psuCorporateIdType;

    @Schema(description = "TPP-Redirect-URI header value", example = "https://tpp.com/callback")
    private String tppRedirectUri;
}
