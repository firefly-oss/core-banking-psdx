package com.catalis.core.banking.psdx.interfaces.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

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
    @Schema(description = "ID of the customer", required = true, example = "123456")
    private Long partyId;

    @NotBlank(message = "Resource ID is required")
    @Schema(description = "ID of the resource being accessed", required = true, example = "payment-123456")
    private String resourceId;

    @NotBlank(message = "Resource type is required")
    @Schema(description = "Type of the resource being accessed", required = true, example = "PAYMENT")
    private String resourceType;

    @Schema(description = "Amount of the transaction", example = "100.00")
    private Double amount;

    @Schema(description = "Currency of the transaction", example = "EUR")
    private String currency;

    @Schema(description = "Preferred authentication method", example = "SMS")
    private String preferredMethod;
}
