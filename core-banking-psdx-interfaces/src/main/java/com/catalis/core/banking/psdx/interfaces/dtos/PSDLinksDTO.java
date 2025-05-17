package com.catalis.core.banking.psdx.interfaces.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * DTO representing links to related resources according to PSD2/PSD3 standards.
 * This follows the HATEOAS principle.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "PSD Links information")
public class PSDLinksDTO {

    @Schema(description = "Self link")
    private PSDLinkDTO self;

    @Schema(description = "First link")
    private PSDLinkDTO first;

    @Schema(description = "Previous link")
    private PSDLinkDTO prev;

    @Schema(description = "Next link")
    private PSDLinkDTO next;

    @Schema(description = "Last link")
    private PSDLinkDTO last;

    @Schema(description = "Status link")
    private PSDLinkDTO status;

    @Schema(description = "Account link")
    private PSDLinkDTO account;

    @Schema(description = "Balances link")
    private PSDLinkDTO balances;

    @Schema(description = "Transactions link")
    private PSDLinkDTO transactions;

    @Schema(description = "SCA redirect link")
    private PSDLinkDTO scaRedirect;

    @Schema(description = "SCA status link")
    private PSDLinkDTO scaStatus;

    @Schema(description = "Confirmation link")
    private PSDLinkDTO confirmation;

    @Schema(description = "Authorization link")
    private PSDLinkDTO authorization;

    @Schema(description = "Additional custom links")
    private Map<String, PSDLinkDTO> additionalLinks;

    /**
     * Inner class representing a link.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PSDLinkDTO {
        @Schema(description = "URL of the link", example = "https://api.bank.com/v1/accounts/12345")
        private String href;
    }
}
