-- Verification script for UUID migration
-- This script validates that the UUID migration was successful
-- and all data integrity constraints are maintained

-- Enable UUID extension if not already enabled
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Create a verification log table to track validation results
CREATE TABLE IF NOT EXISTS migration_verification_log (
    id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    check_name VARCHAR(100) NOT NULL,
    status VARCHAR(20) NOT NULL, -- SUCCESS, FAILED, WARNING
    message TEXT,
    checked_at TIMESTAMP DEFAULT NOW()
);

-- Function to log verification results
CREATE OR REPLACE FUNCTION log_verification(
    p_check_name VARCHAR(100),
    p_status VARCHAR(20),
    p_message TEXT DEFAULT NULL
) RETURNS VOID AS $$
BEGIN
    INSERT INTO migration_verification_log (check_name, status, message)
    VALUES (p_check_name, p_status, p_message);
END;
$$ LANGUAGE plpgsql;

-- Clear previous verification logs
DELETE FROM migration_verification_log;

-- Verification 1: Check that all primary key columns are UUID type
DO $$
DECLARE
    non_uuid_pks INTEGER;
BEGIN
    SELECT COUNT(*) INTO non_uuid_pks
    FROM information_schema.columns c
    JOIN information_schema.table_constraints tc ON c.table_name = tc.table_name
    JOIN information_schema.key_column_usage kcu ON tc.constraint_name = kcu.constraint_name
    WHERE tc.constraint_type = 'PRIMARY KEY'
    AND c.table_schema = 'public'
    AND c.table_name IN ('consents', 'access_logs', 'third_party_providers', 'payments', 'funds_confirmations')
    AND c.column_name = kcu.column_name
    AND c.data_type != 'uuid';
    
    IF non_uuid_pks = 0 THEN
        PERFORM log_verification('PRIMARY_KEY_UUID_CHECK', 'SUCCESS', 'All primary keys are UUID type');
    ELSE
        PERFORM log_verification('PRIMARY_KEY_UUID_CHECK', 'FAILED', 
            'Found ' || non_uuid_pks || ' primary key columns that are not UUID type');
    END IF;
END $$;

-- Verification 2: Check that all foreign key columns are UUID type
DO $$
DECLARE
    non_uuid_fks INTEGER;
BEGIN
    SELECT COUNT(*) INTO non_uuid_fks
    FROM information_schema.columns
    WHERE table_schema = 'public'
    AND table_name IN ('access_logs', 'payments', 'funds_confirmations')
    AND column_name IN ('consent_id')
    AND data_type != 'uuid';
    
    IF non_uuid_fks = 0 THEN
        PERFORM log_verification('FOREIGN_KEY_UUID_CHECK', 'SUCCESS', 'All foreign keys are UUID type');
    ELSE
        PERFORM log_verification('FOREIGN_KEY_UUID_CHECK', 'FAILED', 
            'Found ' || non_uuid_fks || ' foreign key columns that are not UUID type');
    END IF;
END $$;

-- Verification 3: Check that party_id columns are UUID type
DO $$
DECLARE
    non_uuid_party_ids INTEGER;
BEGIN
    SELECT COUNT(*) INTO non_uuid_party_ids
    FROM information_schema.columns
    WHERE table_schema = 'public'
    AND table_name IN ('consents', 'access_logs')
    AND column_name = 'party_id'
    AND data_type != 'uuid';
    
    IF non_uuid_party_ids = 0 THEN
        PERFORM log_verification('PARTY_ID_UUID_CHECK', 'SUCCESS', 'All party_id columns are UUID type');
    ELSE
        PERFORM log_verification('PARTY_ID_UUID_CHECK', 'FAILED', 
            'Found ' || non_uuid_party_ids || ' party_id columns that are not UUID type');
    END IF;
END $$;

-- Verification 4: Check foreign key constraints exist
DO $$
DECLARE
    missing_fks INTEGER;
    expected_fks TEXT[] := ARRAY[
        'access_logs_consent_id_fkey',
        'payments_consent_id_fkey', 
        'funds_confirmations_consent_id_fkey'
    ];
    fk_name TEXT;
    existing_fks INTEGER;
BEGIN
    missing_fks := 0;
    
    FOREACH fk_name IN ARRAY expected_fks
    LOOP
        SELECT COUNT(*) INTO existing_fks
        FROM information_schema.table_constraints
        WHERE constraint_name = fk_name
        AND constraint_type = 'FOREIGN KEY'
        AND table_schema = 'public';
        
        IF existing_fks = 0 THEN
            missing_fks := missing_fks + 1;
            PERFORM log_verification('FOREIGN_KEY_CONSTRAINT_CHECK', 'FAILED', 
                'Missing foreign key constraint: ' || fk_name);
        END IF;
    END LOOP;
    
    IF missing_fks = 0 THEN
        PERFORM log_verification('FOREIGN_KEY_CONSTRAINT_CHECK', 'SUCCESS', 
            'All expected foreign key constraints exist');
    END IF;
END $$;

-- Verification 5: Check that all tables have data (if any existed before migration)
DO $$
DECLARE
    table_name TEXT;
    row_count INTEGER;
    tables_with_data INTEGER := 0;
BEGIN
    FOR table_name IN SELECT unnest(ARRAY['consents', 'access_logs', 'third_party_providers', 'payments', 'funds_confirmations'])
    LOOP
        EXECUTE 'SELECT COUNT(*) FROM ' || table_name INTO row_count;
        IF row_count > 0 THEN
            tables_with_data := tables_with_data + 1;
            PERFORM log_verification('DATA_PRESERVATION_CHECK', 'SUCCESS', 
                'Table ' || table_name || ' has ' || row_count || ' rows');
        END IF;
    END LOOP;
    
    PERFORM log_verification('DATA_PRESERVATION_SUMMARY', 'SUCCESS', 
        'Found data in ' || tables_with_data || ' tables');
END $$;

-- Verification 6: Check referential integrity
DO $$
DECLARE
    orphaned_access_logs INTEGER;
    orphaned_payments INTEGER;
    orphaned_funds_confirmations INTEGER;
BEGIN
    -- Check for orphaned access_logs
    SELECT COUNT(*) INTO orphaned_access_logs
    FROM access_logs al
    LEFT JOIN consents c ON al.consent_id = c.id
    WHERE c.id IS NULL;
    
    -- Check for orphaned payments
    SELECT COUNT(*) INTO orphaned_payments
    FROM payments p
    LEFT JOIN consents c ON p.consent_id = c.id
    WHERE c.id IS NULL;
    
    -- Check for orphaned funds_confirmations
    SELECT COUNT(*) INTO orphaned_funds_confirmations
    FROM funds_confirmations fc
    LEFT JOIN consents c ON fc.consent_id = c.id
    WHERE c.id IS NULL;
    
    IF orphaned_access_logs = 0 AND orphaned_payments = 0 AND orphaned_funds_confirmations = 0 THEN
        PERFORM log_verification('REFERENTIAL_INTEGRITY_CHECK', 'SUCCESS', 
            'No orphaned records found');
    ELSE
        PERFORM log_verification('REFERENTIAL_INTEGRITY_CHECK', 'FAILED', 
            'Found orphaned records: access_logs=' || orphaned_access_logs || 
            ', payments=' || orphaned_payments || 
            ', funds_confirmations=' || orphaned_funds_confirmations);
    END IF;
END $$;

-- Verification 7: Check that all required indexes exist
DO $$
DECLARE
    missing_indexes INTEGER;
    expected_indexes TEXT[] := ARRAY[
        'idx_consents_party_id',
        'idx_consents_status',
        'idx_access_logs_consent_id',
        'idx_access_logs_party_id',
        'idx_access_logs_third_party_id',
        'idx_third_party_providers_api_key',
        'idx_third_party_providers_registration_number',
        'idx_funds_confirmations_consent_id',
        'idx_payments_consent_id',
        'idx_payments_transaction_status'
    ];
    index_name TEXT;
    existing_indexes INTEGER;
BEGIN
    missing_indexes := 0;
    
    FOREACH index_name IN ARRAY expected_indexes
    LOOP
        SELECT COUNT(*) INTO existing_indexes
        FROM pg_indexes
        WHERE schemaname = 'public'
        AND indexname = index_name;
        
        IF existing_indexes = 0 THEN
            missing_indexes := missing_indexes + 1;
            PERFORM log_verification('INDEX_CHECK', 'FAILED', 
                'Missing index: ' || index_name);
        END IF;
    END LOOP;
    
    IF missing_indexes = 0 THEN
        PERFORM log_verification('INDEX_CHECK', 'SUCCESS', 
            'All expected indexes exist');
    END IF;
END $$;

-- Verification 8: Check UUID format validity
DO $$
DECLARE
    invalid_uuids INTEGER := 0;
    table_name TEXT;
    column_name TEXT;
    sql_query TEXT;
    temp_count INTEGER;
BEGIN
    -- Check all UUID columns for valid format
    FOR table_name, column_name IN 
        VALUES 
        ('consents', 'id'),
        ('consents', 'party_id'),
        ('access_logs', 'id'),
        ('access_logs', 'consent_id'),
        ('access_logs', 'party_id'),
        ('third_party_providers', 'id'),
        ('payments', 'id'),
        ('payments', 'consent_id'),
        ('funds_confirmations', 'id'),
        ('funds_confirmations', 'consent_id')
    LOOP
        sql_query := 'SELECT COUNT(*) FROM ' || table_name || 
                    ' WHERE ' || column_name || ' IS NOT NULL AND ' ||
                    column_name || '::text !~ ''^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$''';
        
        EXECUTE sql_query INTO temp_count;
        invalid_uuids := invalid_uuids + temp_count;
        
        IF temp_count > 0 THEN
            PERFORM log_verification('UUID_FORMAT_CHECK', 'FAILED', 
                'Found ' || temp_count || ' invalid UUIDs in ' || table_name || '.' || column_name);
        END IF;
    END LOOP;
    
    IF invalid_uuids = 0 THEN
        PERFORM log_verification('UUID_FORMAT_CHECK', 'SUCCESS', 
            'All UUIDs have valid format');
    END IF;
END $$;

-- Display verification results
SELECT 
    check_name,
    status,
    message,
    checked_at
FROM migration_verification_log
ORDER BY checked_at;

-- Summary of verification results
SELECT 
    status,
    COUNT(*) as count
FROM migration_verification_log
GROUP BY status
ORDER BY status;

-- Clean up verification function (optional)
-- DROP FUNCTION IF EXISTS log_verification(VARCHAR(100), VARCHAR(20), TEXT);
