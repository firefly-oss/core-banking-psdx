package com.catalis.core.banking.psdx.interfaces.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO representing an authentication request.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "PSD Authentication request")
public class PSDAuthenticationRequestDTO {

    @NotBlank(message = "Username is required")
    @Schema(description = "Username", required = true, example = "user@example.com")
    private String username;

    @NotBlank(message = "Password is required")
    @Schema(description = "Password", required = true, example = "password")
    private String password;
}
