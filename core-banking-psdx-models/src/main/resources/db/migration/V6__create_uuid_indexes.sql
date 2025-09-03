-- Fast UUID indexes for empty database
-- This migration creates indexes quickly for development/empty databases
-- Using regular CREATE INDEX (not CONCURRENT) for speed on empty tables

-- Hash indexes for primary key equality lookups
CREATE INDEX IF NOT EXISTS idx_consents_id_hash ON consents USING hash(id);
CREATE INDEX IF NOT EXISTS idx_access_logs_id_hash ON access_logs USING hash(id);
CREATE INDEX IF NOT EXISTS idx_third_party_providers_id_hash ON third_party_providers USING hash(id);
CREATE INDEX IF NOT EXISTS idx_payments_id_hash ON payments USING hash(id);
CREATE INDEX IF NOT EXISTS idx_funds_confirmations_id_hash ON funds_confirmations USING hash(id);

-- Hash indexes for foreign key equality lookups
CREATE INDEX IF NOT EXISTS idx_access_logs_consent_id_hash ON access_logs USING hash(consent_id);
CREATE INDEX IF NOT EXISTS idx_payments_consent_id_hash ON payments USING hash(consent_id);
CREATE INDEX IF NOT EXISTS idx_funds_confirmations_consent_id_hash ON funds_confirmations USING hash(consent_id);

-- Hash indexes for party_id lookups
CREATE INDEX IF NOT EXISTS idx_consents_party_id_hash ON consents USING hash(party_id);
CREATE INDEX IF NOT EXISTS idx_access_logs_party_id_hash ON access_logs USING hash(party_id);

-- Partial indexes for active/valid records
CREATE INDEX IF NOT EXISTS idx_consents_valid_status
ON consents(party_id, valid_until)
WHERE status = 'VALID';

CREATE INDEX IF NOT EXISTS idx_access_logs_success
ON access_logs(consent_id, created_at)
WHERE status = 'SUCCESS';

CREATE INDEX IF NOT EXISTS idx_payments_pending
ON payments(consent_id, created_at)
WHERE transaction_status IN ('PENDING', 'PROCESSING');

-- Additional performance indexes for common query patterns
CREATE INDEX IF NOT EXISTS idx_consents_status_created
ON consents(status, created_at);

CREATE INDEX IF NOT EXISTS idx_access_logs_created_desc
ON access_logs(created_at DESC);

CREATE INDEX IF NOT EXISTS idx_payments_created_desc
ON payments(created_at DESC);

CREATE INDEX IF NOT EXISTS idx_funds_confirmations_created_desc
ON funds_confirmations(created_at DESC);

-- Specialized indexes for third party providers
CREATE INDEX IF NOT EXISTS idx_third_party_providers_status
ON third_party_providers(status)
WHERE status = 'ACTIVE';
