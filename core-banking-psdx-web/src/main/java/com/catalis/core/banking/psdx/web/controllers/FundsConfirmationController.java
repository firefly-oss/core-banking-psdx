package com.catalis.core.banking.psdx.web.controllers;

import com.catalis.core.banking.psdx.interfaces.dtos.PSDFundsConfirmationDTO;
import com.catalis.core.banking.psdx.interfaces.services.FundsConfirmationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import jakarta.validation.Valid;

/**
 * REST controller for funds confirmation.
 */
@RestController
@RequestMapping("/api/funds-confirmations")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Funds Confirmation", description = "APIs for funds confirmation according to PSD2/PSD3 regulations")
public class FundsConfirmationController {

    private final FundsConfirmationService fundsConfirmationService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Confirm funds", description = "Confirms the availability of funds for a specific amount")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Funds confirmation created successfully",
                    content = @Content(schema = @Schema(implementation = PSDFundsConfirmationDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Mono<PSDFundsConfirmationDTO> confirmFunds(
            @Parameter(description = "ID of the consent", required = true)
            @RequestHeader("X-Consent-ID") Long consentId,
            @Valid @RequestBody PSDFundsConfirmationDTO fundsConfirmationRequest) {
        log.debug("REST request to confirm funds for consent ID: {}", consentId);
        return fundsConfirmationService.confirmFunds(consentId, fundsConfirmationRequest);
    }

    @GetMapping(value = "/{fundsConfirmationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get funds confirmation", description = "Gets a funds confirmation by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Funds confirmation found",
                    content = @Content(schema = @Schema(implementation = PSDFundsConfirmationDTO.class))),
            @ApiResponse(responseCode = "404", description = "Funds confirmation not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Mono<PSDFundsConfirmationDTO> getFundsConfirmation(
            @Parameter(description = "ID of the consent", required = true)
            @RequestHeader("X-Consent-ID") Long consentId,
            @Parameter(description = "ID of the funds confirmation", required = true)
            @PathVariable Long fundsConfirmationId) {
        log.debug("REST request to get funds confirmation: {} for consent ID: {}", fundsConfirmationId, consentId);
        return fundsConfirmationService.getFundsConfirmation(consentId, fundsConfirmationId);
    }
}
