-- Trigram search support for service_request list filtering (see JpaServiceRequestRepository.searchByTenantId)
CREATE EXTENSION IF NOT EXISTS pg_trgm;

CREATE INDEX IF NOT EXISTS idx_service_request_protocol_gin
    ON service_request USING gin (protocol gin_trgm_ops);

CREATE INDEX IF NOT EXISTS idx_service_request_description_gin
    ON service_request USING gin (description gin_trgm_ops);
