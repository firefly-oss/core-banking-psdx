package com.catalis.core.banking.psdx.interfaces.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO representing a card account according to PSD2/PSD3 standards.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "PSD Card Account information")
public class PSDCardAccountDTO {

    @Schema(description = "Unique identifier of the card account (resource ID)")
    private Long resourceId;

    @Schema(description = "Masked PAN of the card", example = "540905******0000")
    private String maskedPan;

    @Schema(description = "Currency of the card account", example = "EUR")
    private String currency;

    @Schema(description = "Name of the card account as defined by the PSU", example = "Main Card")
    private String name;

    @Schema(description = "Product name of the bank for this card account", example = "Premium Card")
    private String product;

    @Schema(description = "Status of the card account", example = "enabled")
    private String status;

    @Schema(description = "Credit limit of the card", example = "5000.00")
    private String creditLimit;

    @Schema(description = "ID of the card holder (PSU)")
    private Long ownerPartyId;

    @Schema(description = "Card type", example = "debit")
    private String cardType;

    @Schema(description = "Card network", example = "visa")
    private String cardNetwork;

    @Schema(description = "Card issuer name", example = "Example Bank")
    private String cardIssuerName;

    @Schema(description = "Card issuer ID", example = "EXAMPLEBANK")
    private String cardIssuerId;

    @Schema(description = "Card expiry date")
    @JsonFormat(pattern = "yyyy-MM")
    private LocalDate expiryDate;

    @Schema(description = "List of balances for the card account")
    private List<PSDBalanceDTO> balances;

    @Schema(description = "Links to related resources")
    private PSDLinksDTO _links;

    @Schema(description = "Date and time when the card account was last accessed")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime lastAccessedAt;

    @Schema(description = "Date and time when the card account was created")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @Schema(description = "Date and time when the card account was last updated")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;
}
