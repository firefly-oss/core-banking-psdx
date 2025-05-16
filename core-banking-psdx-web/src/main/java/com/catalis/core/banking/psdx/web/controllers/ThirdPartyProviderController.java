package com.catalis.core.banking.psdx.web.controllers;

import com.catalis.core.banking.psdx.interfaces.dtos.PSDThirdPartyProviderDTO;
import com.catalis.core.banking.psdx.interfaces.dtos.PSDThirdPartyProviderRegistrationDTO;
import com.catalis.core.banking.psdx.interfaces.services.ThirdPartyProviderService;
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
 * REST controller for Third Party Provider (TPP) management.
 */
@RestController
@RequestMapping("/api/providers")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Third Party Provider Management", description = "APIs for managing Third Party Providers according to PSD2/PSD3 regulations")
public class ThirdPartyProviderController {

    private final ThirdPartyProviderService thirdPartyProviderService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Register provider", description = "Registers a new Third Party Provider")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Provider registered successfully",
                    content = @Content(schema = @Schema(implementation = PSDThirdPartyProviderDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Mono<PSDThirdPartyProviderDTO> registerProvider(
            @Valid @RequestBody PSDThirdPartyProviderRegistrationDTO registration) {
        log.debug("REST request to register provider: {}", registration.getName());
        return thirdPartyProviderService.registerProvider(registration);
    }

    @GetMapping(value = "/{providerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get provider", description = "Gets a Third Party Provider by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Provider found",
                    content = @Content(schema = @Schema(implementation = PSDThirdPartyProviderDTO.class))),
            @ApiResponse(responseCode = "404", description = "Provider not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Mono<PSDThirdPartyProviderDTO> getProvider(
            @Parameter(description = "ID of the provider", required = true)
            @PathVariable Long providerId) {
        log.debug("REST request to get provider: {}", providerId);
        return thirdPartyProviderService.getProvider(providerId);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get all providers", description = "Gets all Third Party Providers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Providers found",
                    content = @Content(schema = @Schema(implementation = PSDThirdPartyProviderDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Flux<PSDThirdPartyProviderDTO> getAllProviders() {
        log.debug("REST request to get all providers");
        return thirdPartyProviderService.getAllProviders();
    }

    @PutMapping(value = "/{providerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update provider", description = "Updates a Third Party Provider")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Provider updated successfully",
                    content = @Content(schema = @Schema(implementation = PSDThirdPartyProviderDTO.class))),
            @ApiResponse(responseCode = "404", description = "Provider not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Mono<PSDThirdPartyProviderDTO> updateProvider(
            @Parameter(description = "ID of the provider", required = true)
            @PathVariable Long providerId,
            @Valid @RequestBody PSDThirdPartyProviderDTO providerUpdate) {
        log.debug("REST request to update provider: {}", providerId);
        return thirdPartyProviderService.updateProvider(providerId, providerUpdate);
    }

    @PostMapping(value = "/{providerId}/suspend", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Suspend provider", description = "Suspends a Third Party Provider")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Provider suspended successfully",
                    content = @Content(schema = @Schema(implementation = PSDThirdPartyProviderDTO.class))),
            @ApiResponse(responseCode = "404", description = "Provider not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Mono<PSDThirdPartyProviderDTO> suspendProvider(
            @Parameter(description = "ID of the provider", required = true)
            @PathVariable Long providerId) {
        log.debug("REST request to suspend provider: {}", providerId);
        return thirdPartyProviderService.suspendProvider(providerId);
    }

    @PostMapping(value = "/{providerId}/activate", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Activate provider", description = "Activates a Third Party Provider")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Provider activated successfully",
                    content = @Content(schema = @Schema(implementation = PSDThirdPartyProviderDTO.class))),
            @ApiResponse(responseCode = "404", description = "Provider not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Mono<PSDThirdPartyProviderDTO> activateProvider(
            @Parameter(description = "ID of the provider", required = true)
            @PathVariable Long providerId) {
        log.debug("REST request to activate provider: {}", providerId);
        return thirdPartyProviderService.activateProvider(providerId);
    }

    @DeleteMapping(value = "/{providerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Revoke provider", description = "Revokes a Third Party Provider")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Provider revoked successfully"),
            @ApiResponse(responseCode = "404", description = "Provider not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Mono<Boolean> revokeProvider(
            @Parameter(description = "ID of the provider", required = true)
            @PathVariable Long providerId) {
        log.debug("REST request to revoke provider: {}", providerId);
        return thirdPartyProviderService.revokeProvider(providerId);
    }

    @GetMapping(value = "/validate", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Validate API key", description = "Validates a Third Party Provider's API key")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "API key validated successfully",
                    content = @Content(schema = @Schema(implementation = PSDThirdPartyProviderDTO.class))),
            @ApiResponse(responseCode = "401", description = "Invalid API key"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Mono<PSDThirdPartyProviderDTO> validateApiKey(
            @Parameter(description = "API key to validate", required = true)
            @RequestHeader("X-API-KEY") String apiKey) {
        log.debug("REST request to validate API key");
        return thirdPartyProviderService.validateApiKey(apiKey);
    }
}
