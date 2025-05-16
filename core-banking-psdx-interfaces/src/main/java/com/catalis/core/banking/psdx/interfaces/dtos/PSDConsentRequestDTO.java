package com.catalis.core.banking.psdx.interfaces.dtos;

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
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO representing a request to create a consent according to PSD2/PSD3 standards.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "PSD Consent request")
public class PSDConsentRequestDTO {

    @NotNull(message = "Party ID is required")
    @Schema(description = "ID of the customer who gives the consent", required = true)
    private Long partyId;

    @NotBlank(message = "Consent type is required")
    @Schema(description = "Type of consent", required = true, example = "account")
    private String consentType;

    @Schema(description = "Date and time from which the consent is valid")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime validFrom;

    @NotNull(message = "Valid until date is required")
    @Future(message = "Valid until date must be in the future")
    @Schema(description = "Date and time until which the consent is valid", required = true)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime validUntil;

    @Positive(message = "Frequency per day must be positive")
    @Schema(description = "Frequency of access per day", example = "4")
    private Integer frequencyPerDay;

    @Schema(description = "Access frequency (alias for frequencyPerDay)", example = "4")
    private Integer accessFrequency;

    @Schema(description = "Access scope", example = "all-accounts")
    private String accessScope;

    @NotNull(message = "Access permissions are required")
    @Schema(description = "Access permissions", required = true)
    private List<PSDAccessDTO> access;

    @Schema(description = "Flag indicating if combined service is allowed", example = "false")
    private Boolean combinedServiceIndicator;

    @Schema(description = "Flag indicating if recurring indicator", example = "true")
    private Boolean recurringIndicator;

    /**
     * Inner class representing access permissions.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PSDAccessDTO {
        @NotBlank(message = "Access type is required")
        @Schema(description = "Type of access", required = true, example = "account")
        private String type;

        @Schema(description = "Accounts that can be accessed")
        private List<PSDAccountReferenceDTO> accounts;

        @Schema(description = "Balances that can be accessed")
        private List<PSDAccountReferenceDTO> balances;

        @Schema(description = "Transactions that can be accessed")
        private List<PSDAccountReferenceDTO> transactions;

        @Schema(description = "Available accounts consent", example = "allAccounts")
        private String availableAccounts;

        @Schema(description = "Available accounts with balances consent", example = "allAccounts")
        private String availableAccountsWithBalances;

        @Schema(description = "All PSD2 consent", example = "allAccounts")
        private String allPsd2;
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
}
