package com.firefly.core.banking.psdx.interfaces.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * DTO representing an SCA authentication request.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "PSD SCA Authentication request")
public class PSDSCAAuthenticationRequestDTO {

    @NotNull(message = "Party ID is required")
    @Schema(description = "ID of the customer", required = true, example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID partyId;

    @NotBlank(message = "Resource ID is required")
    @Size(max = 100, message = "Resource ID must not exceed 100 characters")
    @Pattern(regexp = "^[A-Za-z0-9\\-_]+$", message = "Resource ID must contain only alphanumeric characters, hyphens, and underscores")
    @Schema(description = "ID of the resource being accessed", required = true, example = "payment-123456")
    private String resourceId;

    @NotBlank(message = "Resource type is required")
    @Pattern(regexp = "^(PAYMENT|ACCOUNT|CONSENT|FUNDS_CONFIRMATION)$", message = "Resource type must be one of: PAYMENT, ACCOUNT, CONSENT, FUNDS_CONFIRMATION")
    @Schema(description = "Type of the resource being accessed", required = true, example = "PAYMENT")
    private String resourceType;

    @Schema(description = "Amount of the transaction", example = "100.00")
    private Double amount;

    @Schema(description = "Currency of the transaction", example = "EUR")
    private String currency;

    @Schema(description = "Preferred authentication method", example = "SMS")
    private String preferredMethod;
}
