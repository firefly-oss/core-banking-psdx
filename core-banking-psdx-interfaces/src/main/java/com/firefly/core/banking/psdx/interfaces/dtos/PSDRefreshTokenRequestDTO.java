package com.firefly.core.banking.psdx.interfaces.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO representing a refresh token request.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "PSD Refresh token request")
public class PSDRefreshTokenRequestDTO {

    @NotBlank(message = "Refresh token is required")
    @Size(min = 10, max = 2048, message = "Refresh token must be between 10 and 2048 characters")
    @Pattern(regexp = "^[A-Za-z0-9\\-._~+/]+=*$", message = "Refresh token must be a valid JWT or base64 encoded string")
    @Schema(description = "Refresh token", required = true, example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String refreshToken;
}
