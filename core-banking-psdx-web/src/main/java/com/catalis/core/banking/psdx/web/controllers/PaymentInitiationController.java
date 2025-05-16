package com.catalis.core.banking.psdx.web.controllers;

import com.catalis.core.banking.psdx.interfaces.dtos.PSDPaymentDTO;
import com.catalis.core.banking.psdx.interfaces.dtos.PSDPaymentInitiationRequestDTO;
import com.catalis.core.banking.psdx.interfaces.dtos.PSDPaymentStatusDTO;
import com.catalis.core.banking.psdx.interfaces.services.PaymentInitiationService;
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
import reactor.core.publisher.Mono;



/**
 * REST controller for payment initiation services.
 */
@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Payment Initiation Services", description = "APIs for payment initiation services according to PSD2/PSD3 regulations")
public class PaymentInitiationController {

    private final PaymentInitiationService paymentInitiationService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Initiate payment", description = "Initiates a new payment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Payment initiated successfully",
                    content = @Content(schema = @Schema(implementation = PSDPaymentDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Mono<PSDPaymentDTO> initiatePayment(
            @Parameter(description = "ID of the consent", required = true)
            @RequestHeader("X-Consent-ID") Long consentId,
            @Valid @RequestBody PSDPaymentInitiationRequestDTO paymentRequest) {
        log.debug("REST request to initiate payment with consent ID: {}", consentId);
        return paymentInitiationService.initiatePayment(consentId, paymentRequest);
    }

    @GetMapping(value = "/{paymentId}/status", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get payment status", description = "Gets the status of a payment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment status found",
                    content = @Content(schema = @Schema(implementation = PSDPaymentStatusDTO.class))),
            @ApiResponse(responseCode = "404", description = "Payment not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Mono<PSDPaymentStatusDTO> getPaymentStatus(
            @Parameter(description = "ID of the consent", required = true)
            @RequestHeader("X-Consent-ID") Long consentId,
            @Parameter(description = "ID of the payment", required = true)
            @PathVariable Long paymentId) {
        log.debug("REST request to get status for payment: {} with consent ID: {}", paymentId, consentId);
        return paymentInitiationService.getPaymentStatus(consentId, paymentId);
    }

    @GetMapping(value = "/{paymentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get payment", description = "Gets a specific payment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment found",
                    content = @Content(schema = @Schema(implementation = PSDPaymentDTO.class))),
            @ApiResponse(responseCode = "404", description = "Payment not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Mono<PSDPaymentDTO> getPayment(
            @Parameter(description = "ID of the consent", required = true)
            @RequestHeader("X-Consent-ID") Long consentId,
            @Parameter(description = "ID of the payment", required = true)
            @PathVariable Long paymentId) {
        log.debug("REST request to get payment: {} with consent ID: {}", paymentId, consentId);
        return paymentInitiationService.getPayment(consentId, paymentId);
    }

    @DeleteMapping(value = "/{paymentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Cancel payment", description = "Cancels a payment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment cancelled successfully",
                    content = @Content(schema = @Schema(implementation = PSDPaymentDTO.class))),
            @ApiResponse(responseCode = "404", description = "Payment not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Mono<PSDPaymentDTO> cancelPayment(
            @Parameter(description = "ID of the consent", required = true)
            @RequestHeader("X-Consent-ID") Long consentId,
            @Parameter(description = "ID of the payment", required = true)
            @PathVariable Long paymentId) {
        log.debug("REST request to cancel payment: {} with consent ID: {}", paymentId, consentId);
        return paymentInitiationService.cancelPayment(consentId, paymentId);
    }

    @PostMapping(value = "/{paymentId}/authorize", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Authorize payment", description = "Authorizes a payment using SCA")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment authorized successfully",
                    content = @Content(schema = @Schema(implementation = PSDPaymentDTO.class))),
            @ApiResponse(responseCode = "404", description = "Payment not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Mono<PSDPaymentDTO> authorizePayment(
            @Parameter(description = "ID of the consent", required = true)
            @RequestHeader("X-Consent-ID") Long consentId,
            @Parameter(description = "ID of the payment", required = true)
            @PathVariable Long paymentId,
            @Parameter(description = "Authorization code from SCA", required = true)
            @RequestBody String authorizationCode) {
        log.debug("REST request to authorize payment: {} with consent ID: {}", paymentId, consentId);
        return paymentInitiationService.authorizePayment(consentId, paymentId, authorizationCode);
    }
}
