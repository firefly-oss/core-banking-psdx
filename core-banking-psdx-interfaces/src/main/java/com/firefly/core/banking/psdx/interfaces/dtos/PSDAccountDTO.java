package com.firefly.core.banking.psdx.interfaces.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO representing an account according to PSD2/PSD3 standards.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "PSD Account information")
public class PSDAccountDTO {

    @Schema(description = "Unique identifier of the account (resource ID)")
    private Long resourceId;

    @Schema(description = "IBAN of the account", example = "DE89370400440532013000")
    private String iban;

    @Schema(description = "BBAN of the account", example = "BARC12345612345678")
    private String bban;

    @Schema(description = "Currency of the account", example = "EUR")
    private String currency;

    @Schema(description = "Name of the account as defined by the PSU", example = "Main Account")
    private String name;

    @Schema(description = "Product name of the bank for this account", example = "Premium Account")
    private String product;

    @Schema(description = "Type of the account", example = "CACC")
    private String cashAccountType;

    @Schema(description = "Status of the account", example = "enabled")
    private String status;

    @Schema(description = "BIC associated with the account", example = "DEUTDEFF")
    private String bic;

    @Schema(description = "ID of the account holder (PSU)")
    private Long ownerPartyId;

    @Schema(description = "List of balances for the account")
    private List<PSDBalanceDTO> balances;

    @Schema(description = "Links to related resources")
    private PSDLinksDTO _links;

    @Schema(description = "Date and time when the account was last accessed")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime lastAccessedAt;

    @Schema(description = "Date and time when the account was created")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @Schema(description = "Date and time when the account was last updated")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;
}
