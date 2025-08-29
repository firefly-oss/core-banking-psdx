package com.firefly.core.banking.psdx.interfaces.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO representing an account reference according to PSD2/PSD3 standards.
 * This is used to identify an account in a standardized way.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "PSD Account reference information")
public class PSDAccountReferenceDTO {

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
