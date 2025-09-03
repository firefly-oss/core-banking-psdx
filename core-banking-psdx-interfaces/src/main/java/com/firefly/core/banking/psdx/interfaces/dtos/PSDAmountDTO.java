package com.firefly.core.banking.psdx.interfaces.dtos;

import com.firefly.annotations.ValidAmount;
import com.firefly.annotations.ValidCurrencyCode;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO representing an amount with currency according to PSD2/PSD3 standards.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "PSD Amount information")
public class PSDAmountDTO {

    @NotNull(message = "Currency is required")
    @ValidCurrencyCode(message = "Currency must be a valid ISO 4217 currency code")
    @Schema(description = "Currency of the amount", example = "EUR", required = true)
    private String currency;

    @NotNull(message = "Amount is required")
    @ValidAmount(message = "Amount must be a valid monetary amount")
    @Schema(description = "Amount value", example = "123.45", required = true)
    private BigDecimal amount;
}
