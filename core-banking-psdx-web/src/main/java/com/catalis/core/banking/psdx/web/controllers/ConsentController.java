package com.catalis.core.banking.psdx.web.controllers;

import com.catalis.core.banking.psdx.interfaces.dtos.PSDConsentDTO;
import com.catalis.core.banking.psdx.interfaces.dtos.PSDConsentRequestDTO;
import com.catalis.core.banking.psdx.interfaces.dtos.PSDConsentStatusDTO;
import com.catalis.core.banking.psdx.interfaces.services.ConsentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;



/**
 * REST controller for consent management.
 */
@RestController
@RequestMapping("/api/consents")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Consent Management", description = "APIs for managing consents according to PSD2/PSD3 regulations")
public class ConsentController {

    private final ConsentService consentService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new consent", description = "Creates a new consent for a customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Consent created successfully",
                    content = @Content(schema = @Schema(implementation = PSDConsentDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Mono<PSDConsentDTO> createConsent(
            @Valid @RequestBody PSDConsentRequestDTO consentRequest) {
        log.debug("REST request to create consent: {}", consentRequest);
        return consentService.createConsent(consentRequest);
    }

    @GetMapping(value = "/{consentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get a consent", description = "Gets a consent by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consent found",
                    content = @Content(schema = @Schema(implementation = PSDConsentDTO.class))),
            @ApiResponse(responseCode = "404", description = "Consent not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Mono<PSDConsentDTO> getConsent(
            @Parameter(description = "ID of the consent", required = true)
            @PathVariable Long consentId) {
        log.debug("REST request to get consent: {}", consentId);
        return consentService.getConsent(consentId);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get consents for a customer", description = "Gets all consents for a customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consents found",
                    content = @Content(schema = @Schema(implementation = PSDConsentDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Flux<PSDConsentDTO> getConsentsForCustomer(
            @Parameter(description = "ID of the customer", required = true)
            @RequestParam Long partyId) {
        log.debug("REST request to get consents for customer: {}", partyId);
        return consentService.getConsentsForCustomer(partyId);
    }

    @PutMapping(value = "/{consentId}/status", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update consent status", description = "Updates the status of a consent")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consent status updated",
                    content = @Content(schema = @Schema(implementation = PSDConsentDTO.class))),
            @ApiResponse(responseCode = "404", description = "Consent not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Mono<PSDConsentDTO> updateConsentStatus(
            @Parameter(description = "ID of the consent", required = true)
            @PathVariable Long consentId,
            @Valid @RequestBody PSDConsentStatusDTO statusUpdate) {
        log.debug("REST request to update consent status: {} to {}", consentId, statusUpdate.getStatus());
        return consentService.updateConsentStatus(consentId, statusUpdate);
    }

    @DeleteMapping(value = "/{consentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Revoke a consent", description = "Revokes a consent")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consent revoked",
                    content = @Content(schema = @Schema(implementation = PSDConsentDTO.class))),
            @ApiResponse(responseCode = "404", description = "Consent not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Mono<PSDConsentDTO> revokeConsent(
            @Parameter(description = "ID of the consent", required = true)
            @PathVariable Long consentId) {
        log.debug("REST request to revoke consent: {}", consentId);
        return consentService.revokeConsent(consentId);
    }
}
