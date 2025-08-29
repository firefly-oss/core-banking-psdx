package com.firefly.core.banking.psdx.interfaces.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO representing an account transaction according to PSD2/PSD3 standards.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "PSD Transaction information")
public class PSDTransactionDTO {

    @Schema(description = "Unique identifier of the transaction")
    private Long transactionId;

    @Schema(description = "End-to-end identifier", example = "E2E-ID-123")
    private String endToEndId;

    @Schema(description = "Mandate identifier", example = "MANDATE-2023-10-01")
    private String mandateId;

    @Schema(description = "Creditor reference", example = "RF18539007547034")
    private String creditorReference;

    @Schema(description = "Status of the transaction", example = "booked")
    private String transactionStatus;

    @Schema(description = "Booking date of the transaction")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate bookingDate;

    @Schema(description = "Value date of the transaction")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate valueDate;

    @Schema(description = "Transaction amount information")
    private PSDAmountDTO transactionAmount;

    @Schema(description = "Exchange rate if applicable")
    private String exchangeRate;

    @Schema(description = "Creditor name", example = "John Doe")
    private String creditorName;

    @Schema(description = "Creditor account")
    private PSDAccountReferenceDTO creditorAccount;

    @Schema(description = "Creditor agent (BIC)", example = "DEUTDEFF")
    private String creditorAgent;

    @Schema(description = "Debtor name", example = "Jane Smith")
    private String debtorName;

    @Schema(description = "Debtor account")
    private PSDAccountReferenceDTO debtorAccount;

    @Schema(description = "Debtor agent (BIC)", example = "SOGEFRPP")
    private String debtorAgent;

    @Schema(description = "Remittance information unstructured", example = "Invoice 123")
    private String remittanceInformationUnstructured;

    @Schema(description = "Remittance information structured")
    private PSDRemittanceDTO remittanceInformationStructured;

    @Schema(description = "Purpose code", example = "OTHR")
    private String purposeCode;

    @Schema(description = "Bank transaction code", example = "PMNT-RCDT-ESCT")
    private String bankTransactionCode;

    @Schema(description = "Proprietary bank transaction code", example = "ABCD+EF-123")
    private String proprietaryBankTransactionCode;

    @Schema(description = "Links to related resources")
    private PSDLinksDTO _links;

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
     * Inner class representing structured remittance information.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PSDRemittanceDTO {
        @Schema(description = "Reference type", example = "SCOR")
        private String reference;

        @Schema(description = "Reference issuer", example = "CUR")
        private String referenceIssuer;

        @Schema(description = "Reference date", example = "2023-10-01")
        private LocalDate referenceDate;
    }
}
