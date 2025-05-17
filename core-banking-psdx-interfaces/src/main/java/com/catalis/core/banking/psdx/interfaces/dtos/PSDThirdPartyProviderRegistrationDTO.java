package com.catalis.core.banking.psdx.interfaces.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

/**
 * DTO representing a request to register a Third Party Provider (TPP) according to PSD2/PSD3 standards.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "PSD Third Party Provider registration request")
public class PSDThirdPartyProviderRegistrationDTO {

    @NotBlank(message = "Name is required")
    @Schema(description = "Name of the TPP", required = true, example = "FinTech Solutions Ltd")
    private String name;

    @NotBlank(message = "Registration number is required")
    @Schema(description = "Registration number of the TPP", required = true, example = "TPP123456")
    private String registrationNumber;

    @NotBlank(message = "National competent authority is required")
    @Schema(description = "National competent authority ID", required = true, example = "DE-BAFIN")
    private String nationalCompetentAuthority;

    @NotBlank(message = "National competent authority country is required")
    @Schema(description = "National competent authority country", required = true, example = "DE")
    private String nationalCompetentAuthorityCountry;

    @NotBlank(message = "Redirect URI is required")
    @Schema(description = "Redirect URI for the TPP", required = true, example = "https://fintech-solutions.com/callback")
    private String redirectUri;

    @NotBlank(message = "Provider type is required")
    @Schema(description = "Type of the TPP", required = true, example = "AISP")
    private String providerType;

    @NotEmpty(message = "At least one role is required")
    @Schema(description = "Roles of the TPP", required = true)
    private List<String> roles;

    @Schema(description = "Certificate information")
    private PSDCertificateDTO certificate;

    /**
     * Inner class representing certificate information.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PSDCertificateDTO {
        @Schema(description = "Certificate serial number", example = "12345678")
        private String serialNumber;

        @Schema(description = "Certificate subject", example = "CN=FinTech Solutions Ltd,O=FinTech Solutions,C=DE")
        private String subject;

        @Schema(description = "Certificate issuer", example = "CN=PSD2 CA,O=European Banking Authority,C=EU")
        private String issuer;

        @Schema(description = "Certificate content (Base64 encoded)", example = "MIIEpAIBAAKCAQEA...")
        private String content;

        @Schema(description = "Certificate valid from date")
        private java.time.LocalDateTime validFrom;

        @Schema(description = "Certificate valid until date")
        private java.time.LocalDateTime validUntil;
    }
}
