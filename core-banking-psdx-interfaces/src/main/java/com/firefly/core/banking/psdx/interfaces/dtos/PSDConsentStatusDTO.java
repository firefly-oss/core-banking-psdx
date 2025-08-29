package com.firefly.core.banking.psdx.interfaces.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * DTO representing the status of a consent according to PSD2/PSD3 standards.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "PSD Consent status information")
public class PSDConsentStatusDTO {

    @NotBlank(message = "Status is required")
    @Schema(description = "Status of the consent", required = true, example = "valid")
    private String consentStatus;

    @Schema(description = "Status of the consent (alias for consentStatus)", example = "valid")
    private String status;

    @Schema(description = "PSU message", example = "Consent accepted")
    private String psuMessage;

    @Schema(description = "Links to related resources")
    private PSDLinksDTO _links;

    @Schema(description = "Date and time when the status was last updated")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime statusUpdateDateTime;
}
