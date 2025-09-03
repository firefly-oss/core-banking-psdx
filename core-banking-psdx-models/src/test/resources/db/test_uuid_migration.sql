-- Comprehensive test script for UUID migration
-- This script tests the UUID migration by inserting test data and validating relationships

-- Enable UUID extension
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Create test log table
CREATE TABLE IF NOT EXISTS uuid_migration_test_log (
    id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    test_name VARCHAR(100) NOT NULL,
    status VARCHAR(20) NOT NULL,
    message TEXT,
    execution_time_ms INTEGER,
    tested_at TIMESTAMP DEFAULT NOW()
);

-- Function to log test results
CREATE OR REPLACE FUNCTION log_test_result(
    p_test_name VARCHAR(100),
    p_status VARCHAR(20),
    p_message TEXT DEFAULT NULL,
    p_execution_time_ms INTEGER DEFAULT NULL
) RETURNS VOID AS $$
BEGIN
    INSERT INTO uuid_migration_test_log (test_name, status, message, execution_time_ms)
    VALUES (p_test_name, p_status, p_message, p_execution_time_ms);
END;
$$ LANGUAGE plpgsql;

-- Clear previous test logs
DELETE FROM uuid_migration_test_log;

-- Test 1: Insert test data and validate UUID generation
DO $$
DECLARE
    start_time TIMESTAMP;
    end_time TIMESTAMP;
    execution_time INTEGER;
    test_tpp_id UUID;
    test_consent_id UUID;
    test_access_log_id UUID;
    test_payment_id UUID;
    test_funds_conf_id UUID;
    test_party_id UUID;
BEGIN
    start_time := clock_timestamp();
    
    -- Generate test UUIDs
    test_tpp_id := uuid_generate_v4();
    test_consent_id := uuid_generate_v4();
    test_party_id := uuid_generate_v4();
    
    -- Insert test third party provider
    INSERT INTO third_party_providers (
        id, name, registration_number, api_key, redirect_uri, 
        status, provider_type, created_at, updated_at
    ) VALUES (
        test_tpp_id, 'Test TPP', 'TEST123', 'test-api-key', 'https://test.com/callback',
        'ACTIVE', 'AISP', NOW(), NOW()
    );
    
    -- Insert test consent
    INSERT INTO consents (
        id, party_id, consent_type, status, valid_from, valid_until,
        access_frequency, access_scope, created_at, updated_at
    ) VALUES (
        test_consent_id, test_party_id, 'ACCOUNT_INFORMATION', 'VALID',
        NOW() - INTERVAL '1 day', NOW() + INTERVAL '30 days',
        10, 'READ_ACCOUNTS', NOW(), NOW()
    );
    
    -- Insert test access log
    INSERT INTO access_logs (
        id, consent_id, party_id, third_party_id, access_type, resource_type,
        resource_id, ip_address, user_agent, status, created_at
    ) VALUES (
        uuid_generate_v4(), test_consent_id, test_party_id, 'test-tpp-id', 'READ', 'ACCOUNT',
        'test-account-123', '192.168.1.1', 'Test User Agent', 'SUCCESS', NOW()
    ) RETURNING id INTO test_access_log_id;
    
    -- Insert test payment
    INSERT INTO payments (
        id, consent_id, payment_type, transaction_status, debtor_account,
        creditor_name, creditor_account, amount, currency, created_at, updated_at
    ) VALUES (
        uuid_generate_v4(), test_consent_id, 'SEPA_CREDIT_TRANSFER', 'PENDING',
        'DE89370400440532013000', 'Test Creditor', 'DE89370400440532013001',
        100.50, 'EUR', NOW(), NOW()
    ) RETURNING id INTO test_payment_id;
    
    -- Insert test funds confirmation
    INSERT INTO funds_confirmations (
        id, consent_id, account_reference, amount, currency,
        creditor_name, creditor_account, funds_available, created_at
    ) VALUES (
        uuid_generate_v4(), test_consent_id, 'DE89370400440532013000',
        250.75, 'EUR', 'Test Merchant', 'DE89370400440532013002',
        true, NOW()
    ) RETURNING id INTO test_funds_conf_id;
    
    end_time := clock_timestamp();
    execution_time := EXTRACT(MILLISECONDS FROM (end_time - start_time));
    
    PERFORM log_test_result('INSERT_TEST_DATA', 'SUCCESS', 
        'Successfully inserted test data with UUID relationships', execution_time);
        
EXCEPTION WHEN OTHERS THEN
    PERFORM log_test_result('INSERT_TEST_DATA', 'FAILED', 
        'Failed to insert test data: ' || SQLERRM, NULL);
END $$;

-- Test 2: Validate foreign key relationships
DO $$
DECLARE
    start_time TIMESTAMP;
    end_time TIMESTAMP;
    execution_time INTEGER;
    orphaned_records INTEGER;
    total_relationships INTEGER;
BEGIN
    start_time := clock_timestamp();
    
    -- Count total relationships
    SELECT 
        (SELECT COUNT(*) FROM access_logs) +
        (SELECT COUNT(*) FROM payments) +
        (SELECT COUNT(*) FROM funds_confirmations)
    INTO total_relationships;
    
    -- Check for orphaned records
    SELECT 
        (SELECT COUNT(*) FROM access_logs al 
         LEFT JOIN consents c ON al.consent_id = c.id 
         WHERE c.id IS NULL) +
        (SELECT COUNT(*) FROM payments p 
         LEFT JOIN consents c ON p.consent_id = c.id 
         WHERE c.id IS NULL) +
        (SELECT COUNT(*) FROM funds_confirmations fc 
         LEFT JOIN consents c ON fc.consent_id = c.id 
         WHERE c.id IS NULL)
    INTO orphaned_records;
    
    end_time := clock_timestamp();
    execution_time := EXTRACT(MILLISECONDS FROM (end_time - start_time));
    
    IF orphaned_records = 0 THEN
        PERFORM log_test_result('FOREIGN_KEY_INTEGRITY', 'SUCCESS', 
            'All ' || total_relationships || ' relationships are valid', execution_time);
    ELSE
        PERFORM log_test_result('FOREIGN_KEY_INTEGRITY', 'FAILED', 
            'Found ' || orphaned_records || ' orphaned records', execution_time);
    END IF;
END $$;

-- Test 3: Performance test for UUID lookups
DO $$
DECLARE
    start_time TIMESTAMP;
    end_time TIMESTAMP;
    execution_time INTEGER;
    test_consent_id UUID;
    lookup_count INTEGER;
BEGIN
    -- Get a test consent ID
    SELECT id INTO test_consent_id FROM consents LIMIT 1;
    
    IF test_consent_id IS NOT NULL THEN
        start_time := clock_timestamp();
        
        -- Perform multiple UUID lookups
        FOR i IN 1..100 LOOP
            SELECT COUNT(*) INTO lookup_count
            FROM access_logs 
            WHERE consent_id = test_consent_id;
        END LOOP;
        
        end_time := clock_timestamp();
        execution_time := EXTRACT(MILLISECONDS FROM (end_time - start_time));
        
        PERFORM log_test_result('UUID_LOOKUP_PERFORMANCE', 'SUCCESS', 
            '100 UUID lookups completed in ' || execution_time || 'ms', execution_time);
    ELSE
        PERFORM log_test_result('UUID_LOOKUP_PERFORMANCE', 'SKIPPED', 
            'No test data available for performance test', NULL);
    END IF;
END $$;

-- Test 4: UUID format validation
DO $$
DECLARE
    start_time TIMESTAMP;
    end_time TIMESTAMP;
    execution_time INTEGER;
    invalid_uuids INTEGER;
    total_uuids INTEGER;
BEGIN
    start_time := clock_timestamp();
    
    -- Count total UUIDs
    SELECT 
        (SELECT COUNT(*) FROM consents) * 2 + -- id + party_id
        (SELECT COUNT(*) FROM access_logs) * 3 + -- id + consent_id + party_id
        (SELECT COUNT(*) FROM third_party_providers) + -- id
        (SELECT COUNT(*) FROM payments) * 2 + -- id + consent_id
        (SELECT COUNT(*) FROM funds_confirmations) * 2 -- id + consent_id
    INTO total_uuids;
    
    -- Count invalid UUIDs
    SELECT 
        (SELECT COUNT(*) FROM consents 
         WHERE id::text !~ '^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$'
         OR party_id::text !~ '^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$') +
        (SELECT COUNT(*) FROM access_logs 
         WHERE id::text !~ '^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$'
         OR consent_id::text !~ '^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$'
         OR party_id::text !~ '^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$') +
        (SELECT COUNT(*) FROM third_party_providers 
         WHERE id::text !~ '^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$') +
        (SELECT COUNT(*) FROM payments 
         WHERE id::text !~ '^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$'
         OR consent_id::text !~ '^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$') +
        (SELECT COUNT(*) FROM funds_confirmations 
         WHERE id::text !~ '^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$'
         OR consent_id::text !~ '^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$')
    INTO invalid_uuids;
    
    end_time := clock_timestamp();
    execution_time := EXTRACT(MILLISECONDS FROM (end_time - start_time));
    
    IF invalid_uuids = 0 THEN
        PERFORM log_test_result('UUID_FORMAT_VALIDATION', 'SUCCESS', 
            'All ' || total_uuids || ' UUIDs have valid format', execution_time);
    ELSE
        PERFORM log_test_result('UUID_FORMAT_VALIDATION', 'FAILED', 
            'Found ' || invalid_uuids || ' invalid UUIDs out of ' || total_uuids, execution_time);
    END IF;
END $$;

-- Test 5: Index usage validation
DO $$
DECLARE
    start_time TIMESTAMP;
    end_time TIMESTAMP;
    execution_time INTEGER;
    index_count INTEGER;
    expected_indexes TEXT[] := ARRAY[
        'idx_consents_party_id',
        'idx_consents_status',
        'idx_access_logs_consent_id',
        'idx_access_logs_party_id',
        'idx_third_party_providers_api_key',
        'idx_payments_consent_id',
        'idx_funds_confirmations_consent_id'
    ];
    missing_indexes INTEGER;
BEGIN
    start_time := clock_timestamp();
    
    -- Count existing indexes
    SELECT COUNT(*) INTO index_count
    FROM pg_indexes
    WHERE schemaname = 'public'
    AND indexname = ANY(expected_indexes);
    
    missing_indexes := array_length(expected_indexes, 1) - index_count;
    
    end_time := clock_timestamp();
    execution_time := EXTRACT(MILLISECONDS FROM (end_time - start_time));
    
    IF missing_indexes = 0 THEN
        PERFORM log_test_result('INDEX_VALIDATION', 'SUCCESS', 
            'All ' || array_length(expected_indexes, 1) || ' expected indexes exist', execution_time);
    ELSE
        PERFORM log_test_result('INDEX_VALIDATION', 'WARNING', 
            'Missing ' || missing_indexes || ' indexes', execution_time);
    END IF;
END $$;

-- Test 6: Constraint validation
DO $$
DECLARE
    start_time TIMESTAMP;
    end_time TIMESTAMP;
    execution_time INTEGER;
    constraint_count INTEGER;
    expected_constraints TEXT[] := ARRAY[
        'access_logs_consent_id_fkey',
        'payments_consent_id_fkey',
        'funds_confirmations_consent_id_fkey'
    ];
    missing_constraints INTEGER;
BEGIN
    start_time := clock_timestamp();
    
    -- Count existing foreign key constraints
    SELECT COUNT(*) INTO constraint_count
    FROM information_schema.table_constraints
    WHERE constraint_type = 'FOREIGN KEY'
    AND table_schema = 'public'
    AND constraint_name = ANY(expected_constraints);
    
    missing_constraints := array_length(expected_constraints, 1) - constraint_count;
    
    end_time := clock_timestamp();
    execution_time := EXTRACT(MILLISECONDS FROM (end_time - start_time));
    
    IF missing_constraints = 0 THEN
        PERFORM log_test_result('CONSTRAINT_VALIDATION', 'SUCCESS', 
            'All ' || array_length(expected_constraints, 1) || ' expected constraints exist', execution_time);
    ELSE
        PERFORM log_test_result('CONSTRAINT_VALIDATION', 'FAILED', 
            'Missing ' || missing_constraints || ' constraints', execution_time);
    END IF;
END $$;

-- Test 7: Data type validation
DO $$
DECLARE
    start_time TIMESTAMP;
    end_time TIMESTAMP;
    execution_time INTEGER;
    non_uuid_columns INTEGER;
BEGIN
    start_time := clock_timestamp();
    
    -- Check that all ID columns are UUID type
    SELECT COUNT(*) INTO non_uuid_columns
    FROM information_schema.columns
    WHERE table_schema = 'public'
    AND table_name IN ('consents', 'access_logs', 'third_party_providers', 'payments', 'funds_confirmations')
    AND column_name IN ('id', 'consent_id', 'party_id')
    AND data_type != 'uuid';
    
    end_time := clock_timestamp();
    execution_time := EXTRACT(MILLISECONDS FROM (end_time - start_time));
    
    IF non_uuid_columns = 0 THEN
        PERFORM log_test_result('DATA_TYPE_VALIDATION', 'SUCCESS', 
            'All ID columns are UUID type', execution_time);
    ELSE
        PERFORM log_test_result('DATA_TYPE_VALIDATION', 'FAILED', 
            'Found ' || non_uuid_columns || ' non-UUID ID columns', execution_time);
    END IF;
END $$;

-- Clean up test data (optional - comment out to keep test data)
-- DELETE FROM funds_confirmations WHERE creditor_name = 'Test Merchant';
-- DELETE FROM payments WHERE creditor_name = 'Test Creditor';
-- DELETE FROM access_logs WHERE third_party_id = 'test-tpp-id';
-- DELETE FROM consents WHERE access_scope = 'READ_ACCOUNTS';
-- DELETE FROM third_party_providers WHERE name = 'Test TPP';

-- Display test results
SELECT 
    test_name,
    status,
    message,
    execution_time_ms,
    tested_at
FROM uuid_migration_test_log
ORDER BY tested_at;

-- Test summary
SELECT 
    status,
    COUNT(*) as test_count,
    ROUND(AVG(execution_time_ms), 2) as avg_execution_time_ms
FROM uuid_migration_test_log
WHERE execution_time_ms IS NOT NULL
GROUP BY status
ORDER BY status;

-- Overall test result
SELECT 
    CASE 
        WHEN (SELECT COUNT(*) FROM uuid_migration_test_log WHERE status = 'FAILED') > 0 THEN 'FAILED'
        WHEN (SELECT COUNT(*) FROM uuid_migration_test_log WHERE status = 'WARNING') > 0 THEN 'WARNING'
        ELSE 'SUCCESS'
    END as overall_test_result,
    (SELECT COUNT(*) FROM uuid_migration_test_log) as total_tests,
    (SELECT COUNT(*) FROM uuid_migration_test_log WHERE status = 'SUCCESS') as successful_tests,
    (SELECT COUNT(*) FROM uuid_migration_test_log WHERE status = 'FAILED') as failed_tests,
    (SELECT COUNT(*) FROM uuid_migration_test_log WHERE status = 'WARNING') as warning_tests;

-- Clean up test function (optional)
-- DROP FUNCTION IF EXISTS log_test_result(VARCHAR(100), VARCHAR(20), TEXT, INTEGER);
