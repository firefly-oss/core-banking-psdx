package com.catalis.core.banking.psdx.interfaces.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * DTO representing links to related resources according to PSD2/PSD3 standards.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "PSD Links information")
public class PSDLinksDTO {

    @Schema(description = "Map of link relations to link objects")
    private Map<String, PSDLinkDTO> links;

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
