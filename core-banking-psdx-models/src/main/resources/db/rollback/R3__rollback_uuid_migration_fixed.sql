-- Fixed rollback script for UUID migration (V3)
-- This script reverts all UUID changes back to BIGINT/BIGSERIAL
-- WARNING: This will cause data loss if UUIDs cannot be converted back to sequential integers
-- Use only in development or if you have a backup strategy

-- Create rollback log table
CREATE TABLE IF NOT EXISTS rollback_log (
    id SERIAL PRIMARY KEY,
    step_name VARCHAR(100) NOT NULL,
    status VARCHAR(20) NOT NULL,
    message TEXT,
    executed_at TIMESTAMP DEFAULT NOW()
);

-- Function to log rollback steps
CREATE OR REPLACE FUNCTION log_rollback(
    p_step_name VARCHAR(100),
    p_status VARCHAR(20),
    p_message TEXT DEFAULT NULL
) RETURNS VOID AS $$
BEGIN
    INSERT INTO rollback_log (step_name, status, message)
    VALUES (p_step_name, p_status, p_message);
END;
$$ LANGUAGE plpgsql;

-- Clear previous rollback logs
DELETE FROM rollback_log;

-- Start rollback
DO $$
BEGIN
    PERFORM log_rollback('ROLLBACK_START', 'INFO', 'Starting UUID to BIGINT rollback migration');
END $$;

-- Step 1: Create backup of current UUID data
CREATE TABLE IF NOT EXISTS uuid_backup_consents AS SELECT * FROM consents;
CREATE TABLE IF NOT EXISTS uuid_backup_access_logs AS SELECT * FROM access_logs;
CREATE TABLE IF NOT EXISTS uuid_backup_third_party_providers AS SELECT * FROM third_party_providers;
CREATE TABLE IF NOT EXISTS uuid_backup_payments AS SELECT * FROM payments;
CREATE TABLE IF NOT EXISTS uuid_backup_funds_confirmations AS SELECT * FROM funds_confirmations;

DO $$
BEGIN
    PERFORM log_rollback('BACKUP_CREATION', 'SUCCESS', 'Created backup tables for all entities');
END $$;

-- Step 2: Drop existing foreign key constraints
ALTER TABLE access_logs DROP CONSTRAINT IF EXISTS access_logs_consent_id_fkey;
ALTER TABLE payments DROP CONSTRAINT IF EXISTS payments_consent_id_fkey;
ALTER TABLE funds_confirmations DROP CONSTRAINT IF EXISTS funds_confirmations_consent_id_fkey;

DO $$
BEGIN
    PERFORM log_rollback('DROP_FOREIGN_KEYS', 'SUCCESS', 'Dropped foreign key constraints');
END $$;

-- Step 3: Add new BIGINT columns alongside UUID columns
ALTER TABLE third_party_providers ADD COLUMN id_bigint BIGSERIAL;
ALTER TABLE consents ADD COLUMN id_bigint BIGSERIAL;
ALTER TABLE consents ADD COLUMN party_id_bigint BIGINT;
ALTER TABLE access_logs ADD COLUMN id_bigint BIGSERIAL;
ALTER TABLE access_logs ADD COLUMN consent_id_bigint BIGINT;
ALTER TABLE access_logs ADD COLUMN party_id_bigint BIGINT;
ALTER TABLE payments ADD COLUMN id_bigint BIGSERIAL;
ALTER TABLE payments ADD COLUMN consent_id_bigint BIGINT;
ALTER TABLE funds_confirmations ADD COLUMN id_bigint BIGSERIAL;
ALTER TABLE funds_confirmations ADD COLUMN consent_id_bigint BIGINT;

DO $$
BEGIN
    PERFORM log_rollback('ADD_BIGINT_COLUMNS', 'SUCCESS', 'Added BIGINT columns to all tables');
END $$;

-- Step 4: Create mapping table for UUID to BIGINT conversion
CREATE TABLE uuid_to_bigint_mapping (
    table_name VARCHAR(50),
    uuid_value UUID,
    bigint_value BIGINT
);

-- Populate mapping for primary keys
INSERT INTO uuid_to_bigint_mapping (table_name, uuid_value, bigint_value)
SELECT 'third_party_providers', id, id_bigint FROM third_party_providers;

INSERT INTO uuid_to_bigint_mapping (table_name, uuid_value, bigint_value)
SELECT 'consents', id, id_bigint FROM consents;

INSERT INTO uuid_to_bigint_mapping (table_name, uuid_value, bigint_value)
SELECT 'access_logs', id, id_bigint FROM access_logs;

INSERT INTO uuid_to_bigint_mapping (table_name, uuid_value, bigint_value)
SELECT 'payments', id, id_bigint FROM payments;

INSERT INTO uuid_to_bigint_mapping (table_name, uuid_value, bigint_value)
SELECT 'funds_confirmations', id, id_bigint FROM funds_confirmations;

DO $$
BEGIN
    PERFORM log_rollback('CREATE_MAPPING', 'SUCCESS', 'Created UUID to BIGINT mapping');
END $$;

-- Step 5: Update foreign key references using mapping
-- Update consents.party_id_bigint (generate sequential IDs for external references)
WITH party_mapping AS (
    SELECT DISTINCT party_id, ROW_NUMBER() OVER (ORDER BY party_id) as seq_id
    FROM consents
)
UPDATE consents 
SET party_id_bigint = pm.seq_id
FROM party_mapping pm
WHERE consents.party_id = pm.party_id;

-- Update access_logs foreign keys
UPDATE access_logs 
SET consent_id_bigint = (
    SELECT bigint_value FROM uuid_to_bigint_mapping 
    WHERE table_name = 'consents' AND uuid_value = access_logs.consent_id
);

UPDATE access_logs 
SET party_id_bigint = (
    SELECT party_id_bigint FROM consents 
    WHERE id = access_logs.consent_id
);

-- Update payments foreign keys
UPDATE payments 
SET consent_id_bigint = (
    SELECT bigint_value FROM uuid_to_bigint_mapping 
    WHERE table_name = 'consents' AND uuid_value = payments.consent_id
);

-- Update funds_confirmations foreign keys
UPDATE funds_confirmations 
SET consent_id_bigint = (
    SELECT bigint_value FROM uuid_to_bigint_mapping 
    WHERE table_name = 'consents' AND uuid_value = funds_confirmations.consent_id
);

DO $$
BEGIN
    PERFORM log_rollback('UPDATE_FOREIGN_KEYS', 'SUCCESS', 'Updated foreign key references');
END $$;

-- Step 6: Make BIGINT columns NOT NULL
ALTER TABLE third_party_providers ALTER COLUMN id_bigint SET NOT NULL;
ALTER TABLE consents ALTER COLUMN id_bigint SET NOT NULL;
ALTER TABLE consents ALTER COLUMN party_id_bigint SET NOT NULL;
ALTER TABLE access_logs ALTER COLUMN id_bigint SET NOT NULL;
ALTER TABLE access_logs ALTER COLUMN consent_id_bigint SET NOT NULL;
ALTER TABLE access_logs ALTER COLUMN party_id_bigint SET NOT NULL;
ALTER TABLE payments ALTER COLUMN id_bigint SET NOT NULL;
ALTER TABLE payments ALTER COLUMN consent_id_bigint SET NOT NULL;
ALTER TABLE funds_confirmations ALTER COLUMN id_bigint SET NOT NULL;
ALTER TABLE funds_confirmations ALTER COLUMN consent_id_bigint SET NOT NULL;

DO $$
BEGIN
    PERFORM log_rollback('SET_NOT_NULL', 'SUCCESS', 'Set BIGINT columns to NOT NULL');
END $$;

-- Step 7: Drop UUID columns
ALTER TABLE third_party_providers DROP COLUMN id;
ALTER TABLE consents DROP COLUMN id;
ALTER TABLE consents DROP COLUMN party_id;
ALTER TABLE access_logs DROP COLUMN id;
ALTER TABLE access_logs DROP COLUMN consent_id;
ALTER TABLE access_logs DROP COLUMN party_id;
ALTER TABLE payments DROP COLUMN id;
ALTER TABLE payments DROP COLUMN consent_id;
ALTER TABLE funds_confirmations DROP COLUMN id;
ALTER TABLE funds_confirmations DROP COLUMN consent_id;

DO $$
BEGIN
    PERFORM log_rollback('DROP_UUID_COLUMNS', 'SUCCESS', 'Dropped UUID columns');
END $$;

-- Step 8: Rename BIGINT columns to original names
ALTER TABLE third_party_providers RENAME COLUMN id_bigint TO id;
ALTER TABLE consents RENAME COLUMN id_bigint TO id;
ALTER TABLE consents RENAME COLUMN party_id_bigint TO party_id;
ALTER TABLE access_logs RENAME COLUMN id_bigint TO id;
ALTER TABLE access_logs RENAME COLUMN consent_id_bigint TO consent_id;
ALTER TABLE access_logs RENAME COLUMN party_id_bigint TO party_id;
ALTER TABLE payments RENAME COLUMN id_bigint TO id;
ALTER TABLE payments RENAME COLUMN consent_id_bigint TO consent_id;
ALTER TABLE funds_confirmations RENAME COLUMN id_bigint TO id;
ALTER TABLE funds_confirmations RENAME COLUMN consent_id_bigint TO consent_id;

DO $$
BEGIN
    PERFORM log_rollback('RENAME_COLUMNS', 'SUCCESS', 'Renamed BIGINT columns to original names');
END $$;

-- Step 9: Add primary key constraints
ALTER TABLE third_party_providers ADD PRIMARY KEY (id);
ALTER TABLE consents ADD PRIMARY KEY (id);
ALTER TABLE access_logs ADD PRIMARY KEY (id);
ALTER TABLE payments ADD PRIMARY KEY (id);
ALTER TABLE funds_confirmations ADD PRIMARY KEY (id);

DO $$
BEGIN
    PERFORM log_rollback('ADD_PRIMARY_KEYS', 'SUCCESS', 'Added primary key constraints');
END $$;

-- Step 10: Add foreign key constraints
ALTER TABLE access_logs ADD CONSTRAINT access_logs_consent_id_fkey 
    FOREIGN KEY (consent_id) REFERENCES consents(id);

ALTER TABLE payments ADD CONSTRAINT payments_consent_id_fkey 
    FOREIGN KEY (consent_id) REFERENCES consents(id);

ALTER TABLE funds_confirmations ADD CONSTRAINT funds_confirmations_consent_id_fkey 
    FOREIGN KEY (consent_id) REFERENCES consents(id);

DO $$
BEGIN
    PERFORM log_rollback('ADD_FOREIGN_KEYS', 'SUCCESS', 'Added foreign key constraints');
END $$;

-- Step 11: Recreate indexes
DROP INDEX IF EXISTS idx_consents_party_id;
DROP INDEX IF EXISTS idx_consents_status;
DROP INDEX IF EXISTS idx_access_logs_consent_id;
DROP INDEX IF EXISTS idx_access_logs_party_id;
DROP INDEX IF EXISTS idx_access_logs_third_party_id;
DROP INDEX IF EXISTS idx_third_party_providers_api_key;
DROP INDEX IF EXISTS idx_third_party_providers_registration_number;
DROP INDEX IF EXISTS idx_funds_confirmations_consent_id;
DROP INDEX IF EXISTS idx_payments_consent_id;
DROP INDEX IF EXISTS idx_payments_transaction_status;

CREATE INDEX idx_consents_party_id ON consents(party_id);
CREATE INDEX idx_consents_status ON consents(status);
CREATE INDEX idx_access_logs_consent_id ON access_logs(consent_id);
CREATE INDEX idx_access_logs_party_id ON access_logs(party_id);
CREATE INDEX idx_access_logs_third_party_id ON access_logs(third_party_id);
CREATE INDEX idx_third_party_providers_api_key ON third_party_providers(api_key);
CREATE INDEX idx_third_party_providers_registration_number ON third_party_providers(registration_number);
CREATE INDEX idx_funds_confirmations_consent_id ON funds_confirmations(consent_id);
CREATE INDEX idx_payments_consent_id ON payments(consent_id);
CREATE INDEX idx_payments_transaction_status ON payments(transaction_status);

DO $$
BEGIN
    PERFORM log_rollback('RECREATE_INDEXES', 'SUCCESS', 'Recreated indexes for BIGINT columns');
END $$;

-- Step 12: Clean up temporary tables
DROP TABLE IF EXISTS uuid_to_bigint_mapping;

DO $$
BEGIN
    PERFORM log_rollback('CLEANUP', 'SUCCESS', 'Cleaned up temporary tables');
END $$;

-- Step 13: Update column comments
COMMENT ON COLUMN third_party_providers.id IS 'Primary key - BIGINT format (rolled back from UUID)';
COMMENT ON COLUMN consents.id IS 'Primary key - BIGINT format (rolled back from UUID)';
COMMENT ON COLUMN consents.party_id IS 'External party reference - BIGINT format (rolled back from UUID)';
COMMENT ON COLUMN access_logs.id IS 'Primary key - BIGINT format (rolled back from UUID)';
COMMENT ON COLUMN access_logs.consent_id IS 'Foreign key to consents - BIGINT format (rolled back from UUID)';
COMMENT ON COLUMN access_logs.party_id IS 'External party reference - BIGINT format (rolled back from UUID)';
COMMENT ON COLUMN payments.id IS 'Primary key - BIGINT format (rolled back from UUID)';
COMMENT ON COLUMN payments.consent_id IS 'Foreign key to consents - BIGINT format (rolled back from UUID)';
COMMENT ON COLUMN funds_confirmations.id IS 'Primary key - BIGINT format (rolled back from UUID)';
COMMENT ON COLUMN funds_confirmations.consent_id IS 'Foreign key to consents - BIGINT format (rolled back from UUID)';

DO $$
BEGIN
    PERFORM log_rollback('UPDATE_COMMENTS', 'SUCCESS', 'Updated column comments');
END $$;

DO $$
BEGIN
    PERFORM log_rollback('ROLLBACK_COMPLETE', 'SUCCESS', 'UUID to BIGINT rollback completed successfully');
END $$;

-- Display rollback results
SELECT 
    step_name,
    status,
    message,
    executed_at
FROM rollback_log
ORDER BY executed_at;

-- Note: The backup tables (uuid_backup_*) are kept for safety
-- Drop them manually after confirming the rollback was successful:
-- DROP TABLE IF EXISTS uuid_backup_consents;
-- DROP TABLE IF EXISTS uuid_backup_access_logs;
-- DROP TABLE IF EXISTS uuid_backup_third_party_providers;
-- DROP TABLE IF EXISTS uuid_backup_payments;
-- DROP TABLE IF EXISTS uuid_backup_funds_confirmations;
