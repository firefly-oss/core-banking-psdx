package com.firefly.core.banking.psdx.interfaces.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO representing a request to initiate a payment according to PSD2/PSD3 standards.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "PSD Payment initiation request")
public class PSDPaymentInitiationRequestDTO {

    @NotBlank(message = "Payment type is required")
    @Schema(description = "Type of the payment", required = true, example = "sepa-credit-transfers")
    private String paymentType;

    @Schema(description = "End-to-end identifier", example = "E2E-ID-123")
    private String endToEndIdentification;

    @NotNull(message = "Debtor account is required")
    @Schema(description = "Debtor account", required = true)
    private PSDAccountReferenceDTO debtorAccount;

    @NotBlank(message = "Creditor name is required")
    @Schema(description = "Creditor name", required = true, example = "John Doe")
    private String creditorName;

    @NotNull(message = "Creditor account is required")
    @Schema(description = "Creditor account", required = true)
    private PSDAccountReferenceDTO creditorAccount;

    @Schema(description = "Creditor address")
    private PSDAddressDTO creditorAddress;

    @NotNull(message = "Amount is required")
    @Schema(description = "Payment amount information", required = true)
    private PSDAmountDTO instructedAmount;

    @Schema(description = "Remittance information unstructured", example = "Invoice 123")
    private String remittanceInformationUnstructured;

    @Schema(description = "Remittance information structured")
    private PSDRemittanceDTO remittanceInformationStructured;

    @Future(message = "Requested execution date must be in the future")
    @Schema(description = "Requested execution date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate requestedExecutionDate;

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
     * Inner class representing an address.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PSDAddressDTO {
        @Schema(description = "Street name", example = "Main Street")
        private String street;

        @Schema(description = "Building number", example = "123")
        private String buildingNumber;

        @Schema(description = "City", example = "Berlin")
        private String city;

        @Schema(description = "Postal code", example = "10115")
        private String postalCode;

        @Schema(description = "Country", example = "DE")
        private String country;
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
