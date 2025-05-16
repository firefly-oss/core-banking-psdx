-- Add missing tables

-- Create funds_confirmations table
CREATE TABLE funds_confirmations (
    id BIGSERIAL PRIMARY KEY,
    consent_id BIGINT NOT NULL,
    account_reference VARCHAR(100) NOT NULL,
    amount DECIMAL(19, 2) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    creditor_name VARCHAR(70),
    creditor_account VARCHAR(100),
    card_number VARCHAR(35),
    psu_name VARCHAR(70),
    funds_available BOOLEAN,
    confirmation_date_time TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    FOREIGN KEY (consent_id) REFERENCES consents(id)
);

-- Create payments table
CREATE TABLE payments (
    id BIGSERIAL PRIMARY KEY,
    end_to_end_identification VARCHAR(35),
    consent_id BIGINT NOT NULL,
    payment_type VARCHAR(50) NOT NULL,
    transaction_status VARCHAR(20) NOT NULL,
    debtor_account VARCHAR(100) NOT NULL,
    creditor_name VARCHAR(70) NOT NULL,
    creditor_account VARCHAR(100) NOT NULL,
    creditor_address VARCHAR(255),
    amount DECIMAL(19, 2) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    remittance_information_unstructured VARCHAR(140),
    remittance_information_structured VARCHAR(140),
    requested_execution_date DATE,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    FOREIGN KEY (consent_id) REFERENCES consents(id)
);

-- Add missing columns to existing tables

-- Add missing columns to third_party_providers table
ALTER TABLE third_party_providers ADD COLUMN national_competent_authority VARCHAR(50);
ALTER TABLE third_party_providers ADD COLUMN national_competent_authority_country VARCHAR(2);
ALTER TABLE third_party_providers ADD COLUMN roles VARCHAR(255);
ALTER TABLE third_party_providers ADD COLUMN certificate_serial_number VARCHAR(50);
ALTER TABLE third_party_providers ADD COLUMN certificate_subject VARCHAR(255);
ALTER TABLE third_party_providers ADD COLUMN certificate_issuer VARCHAR(255);
ALTER TABLE third_party_providers ADD COLUMN certificate_valid_from TIMESTAMP;
ALTER TABLE third_party_providers ADD COLUMN certificate_valid_until TIMESTAMP;
ALTER TABLE third_party_providers ADD COLUMN certificate_content TEXT;

-- Add missing columns to access_logs table
ALTER TABLE access_logs ADD COLUMN x_request_id VARCHAR(100);
ALTER TABLE access_logs ADD COLUMN tpp_request_id VARCHAR(100);
ALTER TABLE access_logs ADD COLUMN psu_id VARCHAR(100);
ALTER TABLE access_logs ADD COLUMN psu_id_type VARCHAR(50);
ALTER TABLE access_logs ADD COLUMN psu_corporate_id VARCHAR(100);
ALTER TABLE access_logs ADD COLUMN psu_corporate_id_type VARCHAR(50);
ALTER TABLE access_logs ADD COLUMN tpp_redirect_uri VARCHAR(255);

-- Add missing columns to consents table
ALTER TABLE consents ADD COLUMN last_action_date TIMESTAMP;
ALTER TABLE consents ADD COLUMN access TEXT;
ALTER TABLE consents ADD COLUMN combined_service_indicator BOOLEAN DEFAULT FALSE;
ALTER TABLE consents ADD COLUMN recurring_indicator BOOLEAN DEFAULT FALSE;
ALTER TABLE consents ADD COLUMN frequency_per_day INTEGER;

-- Create indexes
CREATE INDEX idx_funds_confirmations_consent_id ON funds_confirmations(consent_id);
CREATE INDEX idx_payments_consent_id ON payments(consent_id);
CREATE INDEX idx_payments_transaction_status ON payments(transaction_status);