package com.catalis.core.banking.psdx.interfaces.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO representing an account balance according to PSD2/PSD3 standards.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "PSD Balance information")
public class PSDBalanceDTO {

    @Schema(description = "Type of balance", example = "closingBooked")
    private String balanceType;

    @Schema(description = "Amount information")
    private PSDAmountDTO balanceAmount;

    @Schema(description = "Credit/Debit indicator", example = "CRDT")
    private String creditDebitIndicator;

    @Schema(description = "Date and time of the balance")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime referenceDate;

    @Schema(description = "Last committed transaction included in this balance")
    private String lastCommittedTransaction;

    /**
     * Inner class representing an amount with currency.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PSDAmountDTO {
        @Schema(description = "Currency of the amount", example = "EUR")
        private String currency;

        @Schema(description = "Amount value")
        private BigDecimal amount;
    }
}
