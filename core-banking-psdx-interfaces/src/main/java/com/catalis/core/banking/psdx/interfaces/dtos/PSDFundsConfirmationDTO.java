package com.catalis.core.banking.psdx.interfaces.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;
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


}
