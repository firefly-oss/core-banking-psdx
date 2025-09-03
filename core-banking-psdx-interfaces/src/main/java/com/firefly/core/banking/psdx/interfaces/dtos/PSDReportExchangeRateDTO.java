package com.firefly.core.banking.psdx.interfaces.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.firefly.annotations.ValidCurrencyCode;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO representing an exchange rate according to PSD2/PSD3 standards.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "PSD Exchange Rate information")
public class PSDReportExchangeRateDTO {

    @NotNull(message = "Source currency is required")
    @ValidCurrencyCode(message = "Source currency must be a valid ISO 4217 currency code")
    @Schema(description = "Source currency", required = true, example = "EUR")
    private String sourceCurrency;

    @NotNull(message = "Exchange rate is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Exchange rate must be positive")
    @Schema(description = "Exchange rate", required = true, example = "1.1234")
    private BigDecimal exchangeRate;

    @ValidCurrencyCode(message = "Unit currency must be a valid ISO 4217 currency code")
    @Schema(description = "Unit currency", example = "EUR")
    private String unitCurrency;

    @NotNull(message = "Target currency is required")
    @ValidCurrencyCode(message = "Target currency must be a valid ISO 4217 currency code")
    @Schema(description = "Target currency", required = true, example = "USD")
    private String targetCurrency;

    @NotNull(message = "Quotation date is required")
    @Schema(description = "Quotation date", required = true)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime quotationDate;
}
