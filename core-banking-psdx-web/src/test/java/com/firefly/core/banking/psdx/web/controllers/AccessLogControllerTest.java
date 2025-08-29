package com.firefly.core.banking.psdx.web.controllers;

import com.firefly.core.banking.psdx.interfaces.dtos.PSDAccessLogDTO;
import com.firefly.core.banking.psdx.interfaces.dtos.PSDAccessLogRequestDTO;
import com.firefly.core.banking.psdx.interfaces.services.AccessLogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Arrays;

import com.firefly.core.banking.psdx.web.utils.TestUtils;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccessLogControllerTest {

    @Mock
    private AccessLogService accessLogService;

    @InjectMocks
    private AccessLogController accessLogController;

    private WebTestClient webTestClient;

    private final Long ACCESS_LOG_ID = 1L;
    private final Long CONSENT_ID = 10L;
    private final Long PARTY_ID = 100L;
    private final String THIRD_PARTY_ID = "TPP123456";

    private PSDAccessLogDTO accessLog1;
    private PSDAccessLogDTO accessLog2;
    private PSDAccessLogRequestDTO accessLogRequest;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToController(accessLogController).build();

        // Setup test data
        accessLog1 = new PSDAccessLogDTO();
        accessLog1.setId(ACCESS_LOG_ID);
        accessLog1.setConsentId(CONSENT_ID);
        accessLog1.setPartyId(PARTY_ID);
        accessLog1.setThirdPartyId(THIRD_PARTY_ID);
        accessLog1.setAccessType("READ");
        accessLog1.setResourceType("ACCOUNT");
        accessLog1.setResourceId("ACC123456");
        accessLog1.setIpAddress("192.168.1.1");
        accessLog1.setStatus("SUCCESS");
        accessLog1.setCreatedAt(LocalDateTime.now().withNano(0));

        accessLog2 = new PSDAccessLogDTO();
        accessLog2.setId(ACCESS_LOG_ID + 1);
        accessLog2.setConsentId(CONSENT_ID);
        accessLog2.setPartyId(PARTY_ID);
        accessLog2.setThirdPartyId(THIRD_PARTY_ID);
        accessLog2.setAccessType("READ");
        accessLog2.setResourceType("TRANSACTION");
        accessLog2.setResourceId("TRX789012");
        accessLog2.setIpAddress("192.168.1.1");
        accessLog2.setStatus("SUCCESS");
        accessLog2.setCreatedAt(LocalDateTime.now().minusHours(1).withNano(0));

        accessLogRequest = new PSDAccessLogRequestDTO();
        accessLogRequest.setConsentId(CONSENT_ID);
        accessLogRequest.setPartyId(PARTY_ID);
        accessLogRequest.setThirdPartyId(THIRD_PARTY_ID);
        accessLogRequest.setAccessType("READ");
        accessLogRequest.setResourceType("ACCOUNT");
        accessLogRequest.setResourceId("ACC123456");
        accessLogRequest.setIpAddress("192.168.1.1");
        accessLogRequest.setStatus("SUCCESS");
    }

    @Test
    void logAccess_shouldReturnCreatedAccessLog() {
        // Given
        when(accessLogService.logAccess(any(PSDAccessLogRequestDTO.class)))
                .thenReturn(Mono.just(accessLog1));

        // When & Then
        webTestClient.post()
                .uri("/api/v1/access-logs")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(accessLogRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(PSDAccessLogDTO.class)
                .isEqualTo(accessLog1);
    }

    @Test
    void getAccessLog_shouldReturnAccessLog() {
        // Given
        when(accessLogService.getAccessLog(ACCESS_LOG_ID))
                .thenReturn(Mono.just(accessLog1));

        // When & Then
        webTestClient.get()
                .uri("/api/v1/access-logs/{accessLogId}", ACCESS_LOG_ID)
                .exchange()
                .expectStatus().isOk()
                .expectBody(PSDAccessLogDTO.class)
                .isEqualTo(accessLog1);
    }

    @Test
    void getAccessLogsForCustomer_shouldReturnAccessLogs() {
        // Given
        when(accessLogService.getAccessLogsForCustomer(PARTY_ID))
                .thenReturn(Flux.fromIterable(Arrays.asList(accessLog1, accessLog2)));

        // When & Then
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/v1/access-logs")
                        .queryParam("partyId", PARTY_ID)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(PSDAccessLogDTO.class)
                .hasSize(2);
    }

    @Test
    void getAccessLogsForCustomerInDateRange_shouldReturnAccessLogs() {
        // Given
        LocalDateTime fromDate = LocalDateTime.now().minusDays(1);
        LocalDateTime toDate = LocalDateTime.now();

        when(accessLogService.getAccessLogsForCustomerInDateRange(eq(PARTY_ID), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Flux.fromIterable(Arrays.asList(accessLog1, accessLog2)));

        // When & Then
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/v1/access-logs")
                        .queryParam("partyId", PARTY_ID)
                        .queryParam("fromDate", fromDate.toString())
                        .queryParam("toDate", toDate.toString())
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(PSDAccessLogDTO.class)
                .hasSize(2);
    }

    @Test
    void getAccessLogsForConsent_shouldReturnAccessLogs() {
        // Given
        when(accessLogService.getAccessLogsForConsent(CONSENT_ID))
                .thenReturn(Flux.fromIterable(Arrays.asList(accessLog1, accessLog2)));

        // When & Then
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/v1/access-logs")
                        .queryParam("consentId", CONSENT_ID)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(PSDAccessLogDTO.class)
                .hasSize(2);
    }

    @Test
    void getAccessLogsForThirdParty_shouldReturnAccessLogs() {
        // Given
        when(accessLogService.getAccessLogsForThirdParty(THIRD_PARTY_ID))
                .thenReturn(Flux.fromIterable(Arrays.asList(accessLog1, accessLog2)));

        // When & Then
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/v1/access-logs")
                        .queryParam("thirdPartyId", THIRD_PARTY_ID)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(PSDAccessLogDTO.class)
                .hasSize(2);
    }
}
