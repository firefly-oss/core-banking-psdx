package com.catalis.core.banking.psdx.interfaces.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO representing a funds confirmation request/response according to PSD2/PSD3 standards.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "PSD Funds Confirmation information")
public class PSDFundsConfirmationDTO {

    @Schema(description = "Unique identifier of the funds confirmation request")
    private Long fundsConfirmationId;

    @Schema(description = "ID of the consent used for the funds confirmation")
    private Long consentId;

    @Schema(description = "Account reference")
    @NotNull(message = "Account is required")
    private PSDAccountReferenceDTO account;

    @Schema(description = "Amount information")
    @NotNull(message = "Amount is required")
    private PSDAmountDTO instructedAmount;

    @Schema(description = "Creditor name", example = "John Doe")
    private String creditorName;

    @Schema(description = "Creditor account")
    private PSDAccountReferenceDTO creditorAccount;

    @Schema(description = "Card number", example = "5409050000000000")
    private String cardNumber;

    @Schema(description = "PSU name", example = "Jane Smith")
    private String psuName;

    @Schema(description = "Result of the funds confirmation", example = "true")
    private Boolean fundsAvailable;

    @Schema(description = "Date and time of the funds confirmation")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime confirmationDateTime;

    @Schema(description = "Links to related resources")
    private PSDLinksDTO _links;

    /**
     * Inner class representing an account reference.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PSDAccountReferenceDTO {
        @Schema(description = "IBAN of the account", example = "DE89370400440532013000")
        private String iban;

        @Schema(description = "BBAN of the account", example = "BARC12345612345678")
        private String bban;

        @Schema(description = "PAN of the card", example = "5409050000000000")
        private String pan;

        @Schema(description = "Masked PAN of the card", example = "540905******0000")
        private String maskedPan;

        @Schema(description = "MSISDN of the account", example = "+49 170 1234567")
        private String msisdn;

        @Schema(description = "Currency of the account", example = "EUR")
        private String currency;
    }

    /**
     * Inner class representing an amount with currency.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PSDAmountDTO {
        @NotBlank(message = "Currency is required")
        @Schema(description = "Currency of the amount", required = true, example = "EUR")
        private String currency;

        @NotNull(message = "Amount is required")
        @Positive(message = "Amount must be positive")
        @Schema(description = "Amount value", required = true)
        private BigDecimal amount;
    }
}
