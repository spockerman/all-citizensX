-- HTTP audit trail (who requested what against the API)

CREATE TABLE audit_log (
    id               UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    occurred_at      TIMESTAMPTZ   NOT NULL DEFAULT now(),
    actor_subject    VARCHAR(255),
    actor_username   VARCHAR(255),
    actor_roles      VARCHAR(500),
    http_method      VARCHAR(10)   NOT NULL,
    request_path     VARCHAR(2000) NOT NULL,
    client_ip        VARCHAR(64),
    response_status  INT           NOT NULL,
    correlation_id   VARCHAR(64)
);

CREATE INDEX idx_audit_log_occurred_at ON audit_log (occurred_at DESC);
