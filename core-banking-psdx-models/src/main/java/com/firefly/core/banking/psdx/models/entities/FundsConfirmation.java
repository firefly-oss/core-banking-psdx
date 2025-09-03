package com.firefly.core.banking.psdx.models.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity representing a funds confirmation request/response according to PSD2/PSD3 standards.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("funds_confirmations")
public class FundsConfirmation {

    @Id
    private UUID id;

    @Column("consent_id")
    private UUID consentId;

    @Column("account_reference")
    private String accountReference;

    @Column("amount")
    private BigDecimal amount;

    @Column("currency")
    private String currency;

    @Column("creditor_name")
    private String creditorName;

    @Column("creditor_account")
    private String creditorAccount;

    @Column("card_number")
    private String cardNumber;

    @Column("psu_name")
    private String psuName;

    @Column("funds_available")
    private Boolean fundsAvailable;

    @Column("confirmation_date_time")
    private LocalDateTime confirmationDateTime;

    @Column("created_at")
    @CreatedDate
    private LocalDateTime createdAt;
}