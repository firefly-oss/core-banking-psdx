package com.firefly.core.banking.psdx.interfaces.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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

    @Size(max = 35, message = "Reference must not exceed 35 characters")
    @Pattern(regexp = "^[A-Za-z0-9/\\-?:().,'+\\s]*$", message = "Reference contains invalid characters")
    @Schema(description = "Reference type", example = "SCOR")
    private String reference;

    @Size(max = 35, message = "Reference issuer must not exceed 35 characters")
    @Pattern(regexp = "^[A-Za-z0-9/\\-?:().,'+\\s]*$", message = "Reference issuer contains invalid characters")
    @Schema(description = "Reference issuer", example = "CUR")
    private String referenceIssuer;

    @Schema(description = "Reference date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate referenceDate;
}
