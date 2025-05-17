package com.catalis.core.banking.psdx.interfaces.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO representing structured remittance information according to PSD2/PSD3 standards.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "PSD Remittance information")
public class PSDRemittanceDTO {

    @Schema(description = "Reference type", example = "SCOR")
    private String reference;

    @Schema(description = "Reference issuer", example = "CUR")
    private String referenceIssuer;

    @Schema(description = "Reference date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate referenceDate;
}
