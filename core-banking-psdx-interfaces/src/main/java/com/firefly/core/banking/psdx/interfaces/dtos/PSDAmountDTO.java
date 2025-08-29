package com.firefly.core.banking.psdx.interfaces.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "Currency of the amount", example = "EUR")
    private String currency;

    @Schema(description = "Amount value", example = "123.45")
    private BigDecimal amount;
}
