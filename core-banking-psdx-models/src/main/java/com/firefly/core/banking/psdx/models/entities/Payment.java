package com.firefly.core.banking.psdx.models.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entity representing a payment according to PSD2/PSD3 standards.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("payments")
public class Payment {

    @Id
    private Long id;

    @Column("end_to_end_identification")
    private String endToEndIdentification;

    @Column("consent_id")
    private Long consentId;

    @Column("payment_type")
    private String paymentType;

    @Column("transaction_status")
    private String transactionStatus;

    @Column("debtor_account")
    private String debtorAccount;

    @Column("creditor_name")
    private String creditorName;

    @Column("creditor_account")
    private String creditorAccount;

    @Column("creditor_address")
    private String creditorAddress;

    @Column("amount")
    private BigDecimal amount;

    @Column("currency")
    private String currency;

    @Column("remittance_information_unstructured")
    private String remittanceInformationUnstructured;

    @Column("remittance_information_structured")
    private String remittanceInformationStructured;

    @Column("requested_execution_date")
    private LocalDate requestedExecutionDate;

    @Column("created_at")
    @CreatedDate
    private LocalDateTime createdAt;

    @Column("updated_at")
    @LastModifiedDate
    private LocalDateTime updatedAt;
}