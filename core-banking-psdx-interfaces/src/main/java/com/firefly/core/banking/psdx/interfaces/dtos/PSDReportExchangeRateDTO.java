package com.firefly.core.banking.psdx.interfaces.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "Source currency", example = "EUR")
    private String sourceCurrency;

    @Schema(description = "Exchange rate", example = "1.1234")
    private BigDecimal exchangeRate;

    @Schema(description = "Unit currency", example = "EUR")
    private String unitCurrency;

    @Schema(description = "Target currency", example = "USD")
    private String targetCurrency;

    @Schema(description = "Quotation date")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime quotationDate;
}
