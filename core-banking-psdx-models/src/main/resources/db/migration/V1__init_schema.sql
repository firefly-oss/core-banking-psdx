-- Create consents table
CREATE TABLE consents (
    id BIGSERIAL PRIMARY KEY,
    party_id BIGINT NOT NULL,
    consent_type VARCHAR(50) NOT NULL,
    status VARCHAR(20) NOT NULL,
    valid_from TIMESTAMP NOT NULL,
    valid_until TIMESTAMP NOT NULL,
    access_frequency INTEGER,
    access_scope VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

-- Create access_logs table
CREATE TABLE access_logs (
    id BIGSERIAL PRIMARY KEY,
    consent_id BIGINT NOT NULL,
    party_id BIGINT NOT NULL,
    third_party_id VARCHAR(100) NOT NULL,
    access_type VARCHAR(20) NOT NULL,
    resource_type VARCHAR(50) NOT NULL,
    resource_id VARCHAR(100) NOT NULL,
    ip_address VARCHAR(50),
    user_agent VARCHAR(255),
    status VARCHAR(20) NOT NULL,
    error_message VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    FOREIGN KEY (consent_id) REFERENCES consents(id)
);

-- Create third_party_providers table
CREATE TABLE third_party_providers (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    registration_number VARCHAR(50) NOT NULL UNIQUE,
    api_key VARCHAR(100) NOT NULL UNIQUE,
    redirect_uri VARCHAR(255) NOT NULL,
    status VARCHAR(20) NOT NULL,
    provider_type VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

-- Create indexes
CREATE INDEX idx_consents_party_id ON consents(party_id);
CREATE INDEX idx_consents_status ON consents(status);
CREATE INDEX idx_access_logs_consent_id ON access_logs(consent_id);
CREATE INDEX idx_access_logs_party_id ON access_logs(party_id);
CREATE INDEX idx_access_logs_third_party_id ON access_logs(third_party_id);
CREATE INDEX idx_third_party_providers_api_key ON third_party_providers(api_key);
CREATE INDEX idx_third_party_providers_registration_number ON third_party_providers(registration_number);
