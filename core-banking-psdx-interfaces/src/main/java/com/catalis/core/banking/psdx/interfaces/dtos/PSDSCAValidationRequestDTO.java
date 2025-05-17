package com.catalis.core.banking.psdx.interfaces.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO representing an SCA validation request.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "PSD SCA Validation request")
public class PSDSCAValidationRequestDTO {

    @NotBlank(message = "Challenge ID is required")
    @Schema(description = "ID of the SCA challenge", required = true, example = "sca-123456")
    private String challengeId;

    @NotBlank(message = "Authentication code is required")
    @Schema(description = "Authentication code provided by the customer", required = true, example = "123456")
    private String authenticationCode;
}
