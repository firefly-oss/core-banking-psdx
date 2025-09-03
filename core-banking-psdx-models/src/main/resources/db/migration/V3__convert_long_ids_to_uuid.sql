-- Migration script to convert all Long ID fields to UUID
-- This migration will:
-- 1. Add new UUID columns alongside existing BIGINT columns
-- 2. Generate UUIDs for existing data
-- 3. Update foreign key references
-- 4. Drop old columns and constraints
-- 5. Rename new columns to original names
-- 6. Add new constraints

-- Enable UUID extension if not already enabled
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Step 1: Add new UUID columns to all tables

-- Add UUID columns to third_party_providers table
ALTER TABLE third_party_providers ADD COLUMN id_uuid UUID DEFAULT uuid_generate_v4();

-- Add UUID columns to consents table
ALTER TABLE consents ADD COLUMN id_uuid UUID DEFAULT uuid_generate_v4();
ALTER TABLE consents ADD COLUMN party_id_uuid UUID;

-- Add UUID columns to access_logs table
ALTER TABLE access_logs ADD COLUMN id_uuid UUID DEFAULT uuid_generate_v4();
ALTER TABLE access_logs ADD COLUMN consent_id_uuid UUID;
ALTER TABLE access_logs ADD COLUMN party_id_uuid UUID;

-- Add UUID columns to payments table
ALTER TABLE payments ADD COLUMN id_uuid UUID DEFAULT uuid_generate_v4();
ALTER TABLE payments ADD COLUMN consent_id_uuid UUID;

-- Add UUID columns to funds_confirmations table
ALTER TABLE funds_confirmations ADD COLUMN id_uuid UUID DEFAULT uuid_generate_v4();
ALTER TABLE funds_confirmations ADD COLUMN consent_id_uuid UUID;

-- Step 2: Generate UUIDs for existing data
-- Update all existing records to have UUID values
UPDATE third_party_providers SET id_uuid = uuid_generate_v4() WHERE id_uuid IS NULL;
UPDATE consents SET id_uuid = uuid_generate_v4() WHERE id_uuid IS NULL;
UPDATE access_logs SET id_uuid = uuid_generate_v4() WHERE id_uuid IS NULL;
UPDATE payments SET id_uuid = uuid_generate_v4() WHERE id_uuid IS NULL;
UPDATE funds_confirmations SET id_uuid = uuid_generate_v4() WHERE id_uuid IS NULL;

-- Step 3: Create mapping table to preserve relationships during migration
CREATE TEMPORARY TABLE id_mapping (
    table_name VARCHAR(50),
    old_id BIGINT,
    new_uuid UUID
);

-- Populate mapping table
INSERT INTO id_mapping (table_name, old_id, new_uuid)
SELECT 'third_party_providers', id, id_uuid FROM third_party_providers;

INSERT INTO id_mapping (table_name, old_id, new_uuid)
SELECT 'consents', id, id_uuid FROM consents;

INSERT INTO id_mapping (table_name, old_id, new_uuid)
SELECT 'access_logs', id, id_uuid FROM access_logs;

INSERT INTO id_mapping (table_name, old_id, new_uuid)
SELECT 'payments', id, id_uuid FROM payments;

INSERT INTO id_mapping (table_name, old_id, new_uuid)
SELECT 'funds_confirmations', id, id_uuid FROM funds_confirmations;

-- Step 4: Update foreign key references using the mapping
-- Update consents.party_id_uuid (this will remain as external reference for now)
-- Note: party_id references external system, so we'll generate new UUIDs
UPDATE consents SET party_id_uuid = uuid_generate_v4();

-- Update access_logs foreign keys
UPDATE access_logs 
SET consent_id_uuid = (
    SELECT new_uuid FROM id_mapping 
    WHERE table_name = 'consents' AND old_id = access_logs.consent_id
);

UPDATE access_logs 
SET party_id_uuid = (
    SELECT party_id_uuid FROM consents 
    WHERE id = access_logs.party_id
);

-- Update payments foreign keys
UPDATE payments 
SET consent_id_uuid = (
    SELECT new_uuid FROM id_mapping 
    WHERE table_name = 'consents' AND old_id = payments.consent_id
);

-- Update funds_confirmations foreign keys
UPDATE funds_confirmations 
SET consent_id_uuid = (
    SELECT new_uuid FROM id_mapping 
    WHERE table_name = 'consents' AND old_id = funds_confirmations.consent_id
);

-- Step 5: Make UUID columns NOT NULL
ALTER TABLE third_party_providers ALTER COLUMN id_uuid SET NOT NULL;
ALTER TABLE consents ALTER COLUMN id_uuid SET NOT NULL;
ALTER TABLE consents ALTER COLUMN party_id_uuid SET NOT NULL;
ALTER TABLE access_logs ALTER COLUMN id_uuid SET NOT NULL;
ALTER TABLE access_logs ALTER COLUMN consent_id_uuid SET NOT NULL;
ALTER TABLE access_logs ALTER COLUMN party_id_uuid SET NOT NULL;
ALTER TABLE payments ALTER COLUMN id_uuid SET NOT NULL;
ALTER TABLE payments ALTER COLUMN consent_id_uuid SET NOT NULL;
ALTER TABLE funds_confirmations ALTER COLUMN id_uuid SET NOT NULL;
ALTER TABLE funds_confirmations ALTER COLUMN consent_id_uuid SET NOT NULL;

-- Step 6: Drop existing foreign key constraints
ALTER TABLE access_logs DROP CONSTRAINT IF EXISTS access_logs_consent_id_fkey;
ALTER TABLE payments DROP CONSTRAINT IF EXISTS payments_consent_id_fkey;
ALTER TABLE funds_confirmations DROP CONSTRAINT IF EXISTS funds_confirmations_consent_id_fkey;

-- Step 7: Drop old columns
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

-- Step 8: Rename UUID columns to original names
ALTER TABLE third_party_providers RENAME COLUMN id_uuid TO id;
ALTER TABLE consents RENAME COLUMN id_uuid TO id;
ALTER TABLE consents RENAME COLUMN party_id_uuid TO party_id;
ALTER TABLE access_logs RENAME COLUMN id_uuid TO id;
ALTER TABLE access_logs RENAME COLUMN consent_id_uuid TO consent_id;
ALTER TABLE access_logs RENAME COLUMN party_id_uuid TO party_id;
ALTER TABLE payments RENAME COLUMN id_uuid TO id;
ALTER TABLE payments RENAME COLUMN consent_id_uuid TO consent_id;
ALTER TABLE funds_confirmations RENAME COLUMN id_uuid TO id;
ALTER TABLE funds_confirmations RENAME COLUMN consent_id_uuid TO consent_id;

-- Step 9: Add primary key constraints
ALTER TABLE third_party_providers ADD PRIMARY KEY (id);
ALTER TABLE consents ADD PRIMARY KEY (id);
ALTER TABLE access_logs ADD PRIMARY KEY (id);
ALTER TABLE payments ADD PRIMARY KEY (id);
ALTER TABLE funds_confirmations ADD PRIMARY KEY (id);

-- Step 10: Add foreign key constraints
ALTER TABLE access_logs ADD CONSTRAINT access_logs_consent_id_fkey 
    FOREIGN KEY (consent_id) REFERENCES consents(id);

ALTER TABLE payments ADD CONSTRAINT payments_consent_id_fkey 
    FOREIGN KEY (consent_id) REFERENCES consents(id);

ALTER TABLE funds_confirmations ADD CONSTRAINT funds_confirmations_consent_id_fkey 
    FOREIGN KEY (consent_id) REFERENCES consents(id);

-- Step 11: Recreate indexes with UUID columns
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

-- Recreate indexes
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

-- Clean up temporary table
DROP TABLE id_mapping;

-- Add comments to document the change
COMMENT ON COLUMN third_party_providers.id IS 'Primary key - UUID format (migrated from BIGINT)';
COMMENT ON COLUMN consents.id IS 'Primary key - UUID format (migrated from BIGINT)';
COMMENT ON COLUMN consents.party_id IS 'External party reference - UUID format (migrated from BIGINT)';
COMMENT ON COLUMN access_logs.id IS 'Primary key - UUID format (migrated from BIGINT)';
COMMENT ON COLUMN access_logs.consent_id IS 'Foreign key to consents - UUID format (migrated from BIGINT)';
COMMENT ON COLUMN access_logs.party_id IS 'External party reference - UUID format (migrated from BIGINT)';
COMMENT ON COLUMN payments.id IS 'Primary key - UUID format (migrated from BIGINT)';
COMMENT ON COLUMN payments.consent_id IS 'Foreign key to consents - UUID format (migrated from BIGINT)';
COMMENT ON COLUMN funds_confirmations.id IS 'Primary key - UUID format (migrated from BIGINT)';
COMMENT ON COLUMN funds_confirmations.consent_id IS 'Foreign key to consents - UUID format (migrated from BIGINT)';
