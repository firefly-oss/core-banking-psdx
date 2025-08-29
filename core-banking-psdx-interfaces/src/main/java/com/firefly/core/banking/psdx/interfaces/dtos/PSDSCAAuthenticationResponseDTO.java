package com.firefly.core.banking.psdx.interfaces.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO representing an SCA authentication response.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "PSD SCA Authentication response")
public class PSDSCAAuthenticationResponseDTO {

    @Schema(description = "ID of the SCA challenge", example = "sca-123456")
    private String challengeId;

    @Schema(description = "Authentication method used", example = "SMS")
    private String method;

    @Schema(description = "Masked target of the authentication", example = "+49 *** *** 789")
    private String maskedTarget;

    @Schema(description = "Expiry time of the challenge in seconds", example = "300")
    private Integer expiresIn;

    @Schema(description = "Additional information about the challenge", example = "An SMS has been sent to your registered mobile number")
    private String additionalInfo;
}
