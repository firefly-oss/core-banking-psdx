package com.firefly.core.banking.psdx.models.entities;

import com.firefly.core.banking.psdx.interfaces.enums.ConsentStatus;
import com.firefly.core.banking.psdx.interfaces.enums.ConsentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * Entity representing a consent given by a customer for accessing their data
 * or performing operations on their behalf according to PSD2/PSD3 regulations.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("consents")
public class Consent {

    @Id
    private Long id;

    @Column("party_id")
    private Long partyId;

    @Column("consent_type")
    private ConsentType consentType;

    @Column("status")
    private ConsentStatus status;

    @Column("valid_from")
    private LocalDateTime validFrom;

    @Column("valid_until")
    private LocalDateTime validUntil;

    @Column("access_frequency")
    private Integer accessFrequency;

    @Column("access_scope")
    private String accessScope;

    @Column("last_action_date")
    private LocalDateTime lastActionDate;

    @Column("access")
    private String access;

    @Column("combined_service_indicator")
    private Boolean combinedServiceIndicator;

    @Column("recurring_indicator")
    private Boolean recurringIndicator;

    @Column("frequency_per_day")
    private Integer frequencyPerDay;

    @Column("created_at")
    @CreatedDate
    private LocalDateTime createdAt;

    @Column("updated_at")
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
