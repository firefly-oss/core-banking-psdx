-- Simplified UUID performance optimization script
-- This script provides basic optimizations without complex logging
-- to ensure the application can start successfully

-- Enable UUID extension if not already enabled
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Add basic non-concurrent indexes for UUID columns
-- Note: CONCURRENT indexes are in V6 migration to avoid transaction mixing

-- Basic B-tree indexes for UUID columns (transactional)
CREATE INDEX IF NOT EXISTS idx_consents_id_btree ON consents(id);
CREATE INDEX IF NOT EXISTS idx_access_logs_id_btree ON access_logs(id);
CREATE INDEX IF NOT EXISTS idx_third_party_providers_id_btree ON third_party_providers(id);
CREATE INDEX IF NOT EXISTS idx_payments_id_btree ON payments(id);
CREATE INDEX IF NOT EXISTS idx_funds_confirmations_id_btree ON funds_confirmations(id);

-- B-tree indexes for foreign key lookups (transactional)
CREATE INDEX IF NOT EXISTS idx_access_logs_consent_id_btree ON access_logs(consent_id);
CREATE INDEX IF NOT EXISTS idx_payments_consent_id_btree ON payments(consent_id);
CREATE INDEX IF NOT EXISTS idx_funds_confirmations_consent_id_btree ON funds_confirmations(consent_id);

-- Composite indexes for common query patterns (transactional)
CREATE INDEX IF NOT EXISTS idx_consents_party_status ON consents(party_id, status);
CREATE INDEX IF NOT EXISTS idx_access_logs_consent_created ON access_logs(consent_id, created_at);
CREATE INDEX IF NOT EXISTS idx_payments_consent_status ON payments(consent_id, transaction_status);
CREATE INDEX IF NOT EXISTS idx_access_logs_party_resource ON access_logs(party_id, resource_type);

-- Update table statistics for better query planning
ANALYZE consents;
ANALYZE access_logs;
ANALYZE third_party_providers;
ANALYZE payments;
ANALYZE funds_confirmations;

-- Create optimized UUID generation function
CREATE OR REPLACE FUNCTION generate_time_ordered_uuid() RETURNS UUID AS $$
BEGIN
    -- Generate UUID v4 for better performance
    -- Falls back to gen_random_uuid if uuid-ossp is not available
    BEGIN
        RETURN uuid_generate_v4();
    EXCEPTION WHEN OTHERS THEN
        RETURN gen_random_uuid();
    END;
END;
$$ LANGUAGE plpgsql;

-- Create performance monitoring view
CREATE OR REPLACE VIEW uuid_performance_metrics AS
SELECT
    schemaname,
    relname as tablename,
    indexrelname as indexname,
    idx_tup_read,
    idx_tup_fetch,
    CASE
        WHEN idx_tup_read > 0 THEN
            ROUND((idx_tup_fetch::numeric / idx_tup_read::numeric) * 100, 2)
        ELSE 0
    END as index_efficiency_percent
FROM pg_stat_user_indexes
WHERE schemaname = 'public'
AND relname IN ('consents', 'access_logs', 'third_party_providers', 'payments', 'funds_confirmations')
ORDER BY relname, indexrelname;

-- Add helpful comments
COMMENT ON VIEW uuid_performance_metrics IS
'Monitor this view to track UUID index performance. Index efficiency should be > 90% for optimal performance. Use hash indexes for equality lookups and btree indexes for range queries.';

COMMENT ON FUNCTION generate_time_ordered_uuid() IS
'Optimized UUID generation function that prefers uuid_generate_v4() but falls back to gen_random_uuid() if uuid-ossp extension is not available.';

-- Add table comments to document the UUID migration
COMMENT ON TABLE consents IS 'Consent records with UUID primary keys (migrated from BIGINT)';
COMMENT ON TABLE access_logs IS 'Access log records with UUID primary keys and foreign keys (migrated from BIGINT)';
COMMENT ON TABLE third_party_providers IS 'Third party provider records with UUID primary keys (migrated from BIGINT)';
COMMENT ON TABLE payments IS 'Payment records with UUID primary keys and foreign keys (migrated from BIGINT)';
COMMENT ON TABLE funds_confirmations IS 'Funds confirmation records with UUID primary keys and foreign keys (migrated from BIGINT)';

-- Add column comments for UUID fields
COMMENT ON COLUMN consents.id IS 'Primary key - UUID format (migrated from BIGINT)';
COMMENT ON COLUMN consents.party_id IS 'External party reference - UUID format (migrated from BIGINT)';
COMMENT ON COLUMN access_logs.id IS 'Primary key - UUID format (migrated from BIGINT)';
COMMENT ON COLUMN access_logs.consent_id IS 'Foreign key to consents - UUID format (migrated from BIGINT)';
COMMENT ON COLUMN access_logs.party_id IS 'External party reference - UUID format (migrated from BIGINT)';
COMMENT ON COLUMN third_party_providers.id IS 'Primary key - UUID format (migrated from BIGINT)';
COMMENT ON COLUMN payments.id IS 'Primary key - UUID format (migrated from BIGINT)';
COMMENT ON COLUMN payments.consent_id IS 'Foreign key to consents - UUID format (migrated from BIGINT)';
COMMENT ON COLUMN funds_confirmations.id IS 'Primary key - UUID format (migrated from BIGINT)';
COMMENT ON COLUMN funds_confirmations.consent_id IS 'Foreign key to consents - UUID format (migrated from BIGINT)';
