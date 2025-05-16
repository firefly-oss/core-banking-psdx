# Core Banking PSDX Service Documentation

## Overview

The Core Banking PSDX Service is a centralized microservice for handling PSD2/PSD3 and FIDA regulatory requirements. It provides a comprehensive set of APIs for third-party providers (TPPs) to access customer account information, initiate payments, and confirm funds availability, all in compliance with the Payment Services Directive regulations.

## Features

### 1. Third-Party Provider Management
- Register and manage third-party providers
- Validate API keys for secure access
- Suspend and activate providers
- Track provider status and roles

### 2. Consent Management
- Create and manage customer consents for account access and payment initiation
- Track consent status and validity periods
- Revoke consents when requested by customers
- Support for different consent types (account information, payment initiation, funds confirmation)

### 3. Access Logging
- Log all access attempts by third-party providers
- Track resource access for audit and compliance purposes
- Provide detailed access history for customers and regulators
- Support for filtering access logs by customer, consent, or third-party provider

### 4. Account Information Services
- Retrieve account details and balances
- Access transaction history with filtering options
- Support for card accounts and payment accounts
- Filtering transactions by date range

### 5. Payment Initiation Services
- Initiate single payments (SEPA, instant, international)
- Check payment status
- Cancel pending payments
- Authorize payments with secure authentication

### 6. Funds Confirmation
- Confirm availability of funds for specific amounts
- Support for card-based payment instruments

## API Endpoints

### Third-Party Provider Management

| Method | Endpoint | Description | Request Body | Response |
|--------|----------|-------------|--------------|----------|
| POST | `/api/providers` | Register a new third-party provider | `PSDThirdPartyProviderRegistrationDTO` | `PSDThirdPartyProviderDTO` |
| GET | `/api/providers/{providerId}` | Get provider details | - | `PSDThirdPartyProviderDTO` |
| GET | `/api/providers` | Get all providers | - | List of `PSDThirdPartyProviderDTO` |
| PUT | `/api/providers/{providerId}` | Update provider details | `PSDThirdPartyProviderDTO` | `PSDThirdPartyProviderDTO` |
| PUT | `/api/providers/{providerId}/suspend` | Suspend a provider | - | `PSDThirdPartyProviderDTO` |
| PUT | `/api/providers/{providerId}/activate` | Activate a provider | - | `PSDThirdPartyProviderDTO` |
| GET | `/api/providers/validate` | Validate API key | Header: `X-API-KEY` | `PSDThirdPartyProviderDTO` |

### Consent Management

| Method | Endpoint | Description | Request Body | Response |
|--------|----------|-------------|--------------|----------|
| POST | `/api/consents` | Create a new consent | `PSDConsentRequestDTO` | `PSDConsentDTO` |
| GET | `/api/consents/{consentId}` | Get consent details | - | `PSDConsentDTO` |
| GET | `/api/consents?partyId={partyId}` | Get consents for a customer | - | List of `PSDConsentDTO` |
| PUT | `/api/consents/{consentId}/status` | Update consent status | `PSDConsentStatusDTO` | `PSDConsentDTO` |
| DELETE | `/api/consents/{consentId}` | Revoke a consent | - | `PSDConsentDTO` |

### Access Logging

| Method | Endpoint | Description | Request Body | Response |
|--------|----------|-------------|--------------|----------|
| POST | `/api/access-logs` | Log a new access | `PSDAccessLogRequestDTO` | `PSDAccessLogDTO` |
| GET | `/api/access-logs/{accessLogId}` | Get access log details | - | `PSDAccessLogDTO` |
| GET | `/api/access-logs?partyId={partyId}` | Get access logs for a customer | - | List of `PSDAccessLogDTO` |
| GET | `/api/access-logs?partyId={partyId}&fromDate={fromDate}&toDate={toDate}` | Get access logs for a customer in date range | - | List of `PSDAccessLogDTO` |
| GET | `/api/access-logs?consentId={consentId}` | Get access logs for a consent | - | List of `PSDAccessLogDTO` |
| GET | `/api/access-logs?thirdPartyId={thirdPartyId}` | Get access logs for a third party | - | List of `PSDAccessLogDTO` |

### Account Information Services

| Method | Endpoint | Description | Request Header | Response |
|--------|----------|-------------|----------------|----------|
| GET | `/api/accounts?partyId={partyId}` | Get accounts for a customer | `X-Consent-ID` | List of `PSDAccountDTO` |
| GET | `/api/accounts/{accountId}` | Get account details | `X-Consent-ID` | `PSDAccountDTO` |
| GET | `/api/accounts/{accountId}/balances` | Get account balances | `X-Consent-ID` | List of `PSDBalanceDTO` |
| GET | `/api/accounts/{accountId}/transactions` | Get account transactions | `X-Consent-ID` | List of `PSDTransactionDTO` |
| GET | `/api/accounts/{accountId}/transactions?fromDate={fromDate}&toDate={toDate}` | Get account transactions in date range | `X-Consent-ID` | List of `PSDTransactionDTO` |
| GET | `/api/accounts/{accountId}/transactions/{transactionId}` | Get transaction details | `X-Consent-ID` | `PSDTransactionDTO` |

### Card Account Services

| Method | Endpoint | Description | Request Header | Response |
|--------|----------|-------------|----------------|----------|
| GET | `/api/card-accounts?partyId={partyId}` | Get card accounts for a customer | `X-Consent-ID` | List of `PSDCardAccountDTO` |
| GET | `/api/card-accounts/{cardId}` | Get card account details | `X-Consent-ID` | `PSDCardAccountDTO` |
| GET | `/api/card-accounts/{cardId}/balances` | Get card balances | `X-Consent-ID` | List of `PSDBalanceDTO` |
| GET | `/api/card-accounts/{cardId}/transactions` | Get card transactions | `X-Consent-ID` | List of `PSDTransactionDTO` |
| GET | `/api/card-accounts/{cardId}/transactions?fromDate={fromDate}&toDate={toDate}` | Get card transactions in date range | `X-Consent-ID` | List of `PSDTransactionDTO` |
| GET | `/api/card-accounts/{cardId}/transactions/{transactionId}` | Get card transaction details | `X-Consent-ID` | `PSDTransactionDTO` |

### Payment Initiation Services

| Method | Endpoint | Description | Request Header | Request Body | Response |
|--------|----------|-------------|----------------|--------------|----------|
| POST | `/api/payments` | Initiate a payment | `X-Consent-ID` | `PSDPaymentInitiationRequestDTO` | `PSDPaymentDTO` |
| GET | `/api/payments/{paymentId}` | Get payment details | `X-Consent-ID` | - | `PSDPaymentDTO` |
| GET | `/api/payments/{paymentId}/status` | Get payment status | `X-Consent-ID` | - | `PSDPaymentStatusDTO` |
| PUT | `/api/payments/{paymentId}/authorize` | Authorize a payment | `X-Consent-ID` | Authorization code (String) | `PSDPaymentDTO` |
| DELETE | `/api/payments/{paymentId}` | Cancel a payment | `X-Consent-ID` | - | `PSDPaymentDTO` |

### Funds Confirmation Services

| Method | Endpoint | Description | Request Header | Request Body | Response |
|--------|----------|-------------|----------------|--------------|----------|
| POST | `/api/funds-confirmations` | Confirm funds | `X-Consent-ID` | `PSDFundsConfirmationDTO` | `PSDFundsConfirmationDTO` |
| GET | `/api/funds-confirmations/{fundsConfirmationId}` | Get funds confirmation details | `X-Consent-ID` | - | `PSDFundsConfirmationDTO` |

## Data Transfer Objects (DTOs)

All DTOs follow the naming convention with the "PSD" prefix.

### Third-Party Provider DTOs

#### PSDThirdPartyProviderDTO
```java
public class PSDThirdPartyProviderDTO {
    private Long providerId;
    private String name;
    private String email;
    private String website;
    private String phone;
    private String status;
    private String apiKey;
    private String registrationNumber;
    private String nationalCompetentAuthority;
    private String nationalCompetentAuthorityCountry;
    private List<String> roles;
    private String certificate;
    private LocalDateTime registeredAt;
    private LocalDateTime updatedAt;
    private PSDLinksDTO _links;
}
```

#### PSDThirdPartyProviderRegistrationDTO
```java
public class PSDThirdPartyProviderRegistrationDTO {
    private String name;
    private String email;
    private String website;
    private String phone;
    private String registrationNumber;
    private String nationalCompetentAuthority;
    private String nationalCompetentAuthorityCountry;
    private List<String> roles;
    private String certificate;
    private String redirectUri;
}
```

### Consent DTOs

#### PSDConsentDTO
```java
public class PSDConsentDTO {
    private Long id;
    private Long partyId;
    private String consentType;
    private String consentStatus;
    private String status;
    private LocalDateTime validFrom;
    private LocalDateTime validUntil;
    private Integer frequencyPerDay;
    private Integer accessFrequency;
    private String accessScope;
    private LocalDateTime lastActionDate;
    private List<PSDConsentRequestDTO.PSDAccessDTO> access;
    private Boolean combinedServiceIndicator;
    private Boolean recurringIndicator;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private PSDLinksDTO _links;
}
```

#### PSDConsentRequestDTO
```java
public class PSDConsentRequestDTO {
    private Long partyId;
    private String consentType;
    private LocalDateTime validFrom;
    private LocalDateTime validUntil;
    private Integer frequencyPerDay;
    private List<PSDAccessDTO> access;
    private Boolean combinedServiceIndicator;
    private Boolean recurringIndicator;
    
    public static class PSDAccessDTO {
        private String type;
        private List<PSDAccountReferenceDTO> accounts;
        private List<PSDBalanceTypeDTO> balanceTypes;
        private List<PSDTransactionStatusDTO> transactionStatus;
    }
}
```

#### PSDConsentStatusDTO
```java
public class PSDConsentStatusDTO {
    private String status;
}
```

### Access Log DTOs

#### PSDAccessLogDTO
```java
public class PSDAccessLogDTO {
    private Long id;
    private Long consentId;
    private Long partyId;
    private String thirdPartyId;
    private String accessType;
    private String resourceType;
    private String resourceId;
    private String ipAddress;
    private String userAgent;
    private String status;
    private String errorMessage;
    private LocalDateTime createdAt;
    private String xRequestId;
    private String tppRequestId;
    private String psuId;
    private String psuIdType;
    private String psuCorporateId;
    private String psuCorporateIdType;
    private String tppRedirectUri;
}
```

#### PSDAccessLogRequestDTO
```java
public class PSDAccessLogRequestDTO {
    private Long consentId;
    private Long partyId;
    private String thirdPartyId;
    private String accessType;
    private String resourceType;
    private String resourceId;
    private String ipAddress;
    private String userAgent;
    private String status;
    private String errorMessage;
    private String xRequestId;
    private String tppRequestId;
    private String psuId;
    private String psuIdType;
    private String psuCorporateId;
    private String psuCorporateIdType;
    private String tppRedirectUri;
}
```

### Account Information DTOs

#### PSDAccountDTO
```java
public class PSDAccountDTO {
    private String resourceId;
    private String iban;
    private String bban;
    private String currency;
    private String ownerName;
    private String name;
    private String displayName;
    private String product;
    private String cashAccountType;
    private String status;
    private String bic;
    private String linkedAccounts;
    private String usage;
    private LocalDate balanceLastUpdateDate;
    private LocalDateTime balanceLastUpdateDateTime;
    private PSDLinksDTO _links;
}
```

#### PSDBalanceDTO
```java
public class PSDBalanceDTO {
    private String balanceType;
    private PSDAmountDTO balanceAmount;
    private LocalDate referenceDate;
    private LocalDateTime lastChangeDateTime;
    private String lastCommittedTransaction;
    
    public static class PSDAmountDTO {
        private String currency;
        private BigDecimal amount;
    }
}
```

#### PSDTransactionDTO
```java
public class PSDTransactionDTO {
    private String transactionId;
    private String entryReference;
    private String endToEndId;
    private String mandateId;
    private String checkId;
    private String creditorId;
    private LocalDate bookingDate;
    private LocalDate valueDate;
    private PSDAmountDTO transactionAmount;
    private List<PSDReportExchangeRateDTO> currencyExchange;
    private String creditorName;
    private PSDAccountReferenceDTO creditorAccount;
    private String creditorAgent;
    private String ultimateCreditor;
    private String debtorName;
    private PSDAccountReferenceDTO debtorAccount;
    private String debtorAgent;
    private String ultimateDebtor;
    private String remittanceInformationUnstructured;
    private List<String> remittanceInformationStructured;
    private String additionalInformation;
    private String purposeCode;
    private String bankTransactionCode;
    private String proprietaryBankTransactionCode;
    private String transactionStatus;
    private PSDLinksDTO _links;
    
    public static class PSDAmountDTO {
        private String currency;
        private BigDecimal amount;
    }
    
    public static class PSDReportExchangeRateDTO {
        private String sourceCurrency;
        private String exchangeRate;
        private String unitCurrency;
        private String targetCurrency;
        private String quotationDate;
    }
}
```

#### PSDCardAccountDTO
```java
public class PSDCardAccountDTO {
    private Long resourceId;
    private String maskedPan;
    private String currency;
    private String ownerName;
    private Long ownerPartyId;
    private String name;
    private String displayName;
    private String product;
    private String status;
    private String usage;
    private String details;
    private LocalDate balanceLastUpdateDate;
    private LocalDateTime balanceLastUpdateDateTime;
    private PSDLinksDTO _links;
}
```

### Payment DTOs

#### PSDPaymentDTO
```java
public class PSDPaymentDTO {
    private Long paymentId;
    private String paymentType;
    private String transactionStatus;
    private PSDAccountReferenceDTO debtorAccount;
    private PSDAccountReferenceDTO creditorAccount;
    private PSDAmountDTO instructedAmount;
    private String creditorName;
    private String creditorAgent;
    private String creditorAddress;
    private String remittanceInformationUnstructured;
    private LocalDate requestedExecutionDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private PSDLinksDTO _links;
    
    public static class PSDAccountReferenceDTO {
        private String iban;
        private String bban;
        private String pan;
        private String maskedPan;
        private String msisdn;
        private String currency;
    }
    
    public static class PSDAmountDTO {
        private String currency;
        private BigDecimal amount;
    }
}
```

#### PSDPaymentInitiationRequestDTO
```java
public class PSDPaymentInitiationRequestDTO {
    private String paymentType;
    private PSDAccountReferenceDTO debtorAccount;
    private PSDAccountReferenceDTO creditorAccount;
    private PSDAmountDTO instructedAmount;
    private String creditorName;
    private String creditorAgent;
    private String creditorAddress;
    private String remittanceInformationUnstructured;
    private LocalDate requestedExecutionDate;
    
    public static class PSDAccountReferenceDTO {
        private String iban;
        private String bban;
        private String pan;
        private String maskedPan;
        private String msisdn;
        private String currency;
    }
    
    public static class PSDAmountDTO {
        private String currency;
        private BigDecimal amount;
    }
}
```

#### PSDPaymentStatusDTO
```java
public class PSDPaymentStatusDTO {
    private Long paymentId;
    private String status;
}
```

### Funds Confirmation DTOs

#### PSDFundsConfirmationDTO
```java
public class PSDFundsConfirmationDTO {
    private Long fundsConfirmationId;
    private Long consentId;
    private PSDAccountReferenceDTO account;
    private PSDAmountDTO instructedAmount;
    private String creditorName;
    private PSDAccountReferenceDTO creditorAccount;
    private Boolean fundsAvailable;
    private LocalDateTime confirmationDateTime;
    
    public static class PSDAccountReferenceDTO {
        private String iban;
        private String bban;
        private String pan;
        private String maskedPan;
        private String msisdn;
        private String currency;
    }
    
    public static class PSDAmountDTO {
        private String currency;
        private BigDecimal amount;
    }
}
```

## Testing

The service includes comprehensive unit and integration tests for all components:

- Controller tests in the `core-banking-psdx-web` module
- Service tests in the `core-banking-psdx-core` module
- Repository tests in the `core-banking-psdx-models` module

### Controller Tests

The following controller test classes have been implemented:

1. `ThirdPartyProviderControllerTest`
2. `ConsentControllerTest`
3. `AccessLogControllerTest`
4. `CardAccountControllerTest`
5. `PaymentInitiationControllerTest`
6. `FundsConfirmationControllerTest`

These tests verify that the controllers handle requests correctly and return the expected responses.

### Running Tests

To run the tests:
```bash
mvn test
```

## Configuration

The application can be configured using the `application.yaml` file in the `core-banking-psdx-web/src/main/resources` directory.

### Key Configuration Properties

```yaml
spring:
  application:
    name: core-banking-psdx
    version: 1.0.0
    description: PSD2/PSD3 and FIDA Regulatory Compliance Service
  r2dbc:
    url: r2dbc:postgresql://${DB_HOST}:${DB_PORT}/${DB_NAME}?sslMode=${DB_SSL_MODE}
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}

integration:
  customer-mgmt:
    enabled: ${CUSTOMER_MGMT_ENABLED:false}
    base-url: "${CUSTOMER_MGMT_URL:http://common-platform-customer-mgmt:8080}"
  contract-mgmt:
    enabled: ${CONTRACT_MGMT_ENABLED:false}
    base-url: "${CONTRACT_MGMT_URL:http://common-platform-contract-mgmt:8080}"
  accounts:
    enabled: ${ACCOUNTS_ENABLED:false}
    base-url: "${ACCOUNTS_URL:http://core-banking-accounts:8080}"
  cards:
    enabled: ${CARDS_ENABLED:false}
    base-url: "${CARDS_URL:http://core-banking-cards:8080}"
  payment-hub:
    enabled: ${PAYMENT_HUB_ENABLED:false}
    base-url: "${PAYMENT_HUB_URL:http://core-banking-payment-hub:8080}"
  ledger:
    enabled: ${LEDGER_ENABLED:false}
    base-url: "${LEDGER_URL:http://core-banking-ledger:8080}"
  sca-mgmt:
    enabled: ${SCA_MGMT_ENABLED:false}
    base-url: "${SCA_MGMT_URL:http://common-platform-sca-mgmt:8080}"

psdx:
  consent:
    default-expiry-days: 90
  security:
    token-validity-seconds: 3600
    refresh-token-validity-days: 30
```

## API Documentation

The API is documented using Swagger/OpenAPI. When the service is running, you can access the API documentation at:
```
http://localhost:8080/swagger-ui.html
```

## Compliance

This service is designed to comply with:
- PSD2 (Payment Services Directive 2)
- PSD3 (upcoming Payment Services Directive 3)
- FIDA (Financial Data Access) regulations
- GDPR (General Data Protection Regulation)
- eIDAS (Electronic Identification, Authentication and Trust Services)

## Integration with Other Microservices

The Core Banking PSDX Service integrates with other microservices in the Core Banking Platform:

- **Core Banking Accounts**: For retrieving account information
- **Core Banking Cards**: For retrieving card account information
- **Core Banking Ledger**: For retrieving transaction information
- **Core Banking Payment Hub**: For initiating and managing payments
- **Common Platform Customer Management**: For customer information
- **Common Platform Contract Management**: For contract information
- **Common Platform SCA Management**: For strong customer authentication

The integration is implemented using a hexagonal architecture with ports and adapters, allowing for easy replacement of the underlying implementations.
