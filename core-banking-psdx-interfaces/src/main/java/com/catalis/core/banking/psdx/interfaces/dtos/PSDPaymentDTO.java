package com.catalis.core.banking.psdx.interfaces.dtos;

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
 * DTO representing a payment according to PSD2/PSD3 standards.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "PSD Payment information")
public class PSDPaymentDTO {

    @Schema(description = "Unique identifier of the payment")
    private Long paymentId;

    @Schema(description = "End-to-end identifier", example = "E2E-ID-123")
    private String endToEndIdentification;

    @Schema(description = "ID of the consent used for the payment")
    private Long consentId;

    @Schema(description = "Type of the payment", example = "sepa-credit-transfers")
    private String paymentType;

    @Schema(description = "Status of the payment", example = "ACCP")
    private String transactionStatus;

    @Schema(description = "Debtor account")
    private PSDAccountReferenceDTO debtorAccount;

    @Schema(description = "Creditor name", example = "John Doe")
    private String creditorName;

    @Schema(description = "Creditor account")
    private PSDAccountReferenceDTO creditorAccount;

    @Schema(description = "Creditor address")
    private PSDAddressDTO creditorAddress;

    @Schema(description = "Payment amount information")
    private PSDAmountDTO instructedAmount;

    @Schema(description = "Remittance information unstructured", example = "Invoice 123")
    private String remittanceInformationUnstructured;

    @Schema(description = "Remittance information structured")
    private PSDRemittanceDTO remittanceInformationStructured;

    @Schema(description = "Requested execution date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate requestedExecutionDate;

    @Schema(description = "Links to related resources")
    private PSDLinksDTO _links;

    @Schema(description = "Date and time when the payment was created")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @Schema(description = "Date and time when the payment was last updated")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;

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
