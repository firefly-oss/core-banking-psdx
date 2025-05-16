package com.catalis.core.banking.psdx.interfaces.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO representing the status of a payment according to PSD2/PSD3 standards.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "PSD Payment status information")
public class PSDPaymentStatusDTO {

    @Schema(description = "Unique identifier of the payment")
    private Long paymentId;

    @Schema(description = "Status of the payment", example = "ACCP")
    private String transactionStatus;

    @Schema(description = "Status of the payment (alias for transactionStatus)", example = "ACCP")
    private String status;

    @Schema(description = "Funds availability", example = "true")
    private Boolean fundsAvailable;

    @Schema(description = "PSU message", example = "Payment accepted")
    private String psuMessage;

    @Schema(description = "Additional status information", example = "Payment accepted")
    private String statusReasonInformation;

    @Schema(description = "Links to related resources")
    private PSDLinksDTO _links;

    @Schema(description = "Date and time when the status was last updated")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime statusUpdateDateTime;
}
