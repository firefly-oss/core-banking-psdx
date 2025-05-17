package com.catalis.core.banking.psdx.interfaces.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO representing an SCA validation response.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "PSD SCA Validation response")
public class PSDSCAValidationResponseDTO {

    @Schema(description = "Whether the validation was successful", example = "true")
    private Boolean success;

    @Schema(description = "Authentication token for the validated session", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String authenticationToken;

    @Schema(description = "Expiry time of the token in seconds", example = "3600")
    private Integer expiresIn;

    @Schema(description = "Error message if validation failed", example = "Invalid authentication code")
    private String errorMessage;
}
