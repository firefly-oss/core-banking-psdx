package com.firefly.core.banking.psdx.interfaces.dtos;

import com.firefly.annotations.ValidPasswordStrength;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @Email(message = "Username must be a valid email address")
    @Size(max = 100, message = "Username must not exceed 100 characters")
    @Schema(description = "Username", required = true, example = "user@example.com")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 128, message = "Password must be between 8 and 128 characters")
    @ValidPasswordStrength(message = "Password must meet security requirements")
    @Schema(description = "Password", required = true, example = "password")
    private String password;
}
