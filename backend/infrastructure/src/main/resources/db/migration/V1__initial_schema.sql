-- ======================================================================
-- All Citizens — E-Atendimento v2 — PostgreSQL Schema
-- ======================================================================

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "unaccent";
CREATE EXTENSION IF NOT EXISTS "pg_trgm";

-- ────────────────────────────────────────────────────────────────
-- ENUM TYPES
-- ────────────────────────────────────────────────────────────────

CREATE TYPE person_type          AS ENUM ('INDIVIDUAL', 'ORGANIZATION');
CREATE TYPE gender_type          AS ENUM ('MALE', 'FEMALE', 'NOT_INFORMED');
CREATE TYPE channel_type         AS ENUM ('PHONE', 'WEB', 'MOBILE_APP', 'WHATSAPP', 'CHATBOT', 'IN_PERSON', 'EMAIL');
CREATE TYPE request_status_type  AS ENUM ('OPEN', 'IN_PROGRESS', 'FORWARDED', 'ANSWERED', 'CLOSED', 'CANCELLED', 'REOPENED');
CREATE TYPE forwarding_status_type AS ENUM ('PENDING', 'IN_REVIEW', 'ANSWERED', 'RETURNED', 'REDISTRIBUTED', 'COMPLETED');
CREATE TYPE priority_type        AS ENUM ('LOW', 'NORMAL', 'HIGH', 'URGENT');
CREATE TYPE attachment_type      AS ENUM ('IMAGE', 'VIDEO', 'DOCUMENT', 'AUDIO');
CREATE TYPE notification_channel_type AS ENUM ('PUSH', 'EMAIL', 'SMS', 'WHATSAPP', 'IN_APP');
CREATE TYPE notification_status_type  AS ENUM ('PENDING', 'SENT', 'FAILED', 'READ');
CREATE TYPE emotional_state_type AS ENUM ('CALM', 'ANXIOUS', 'IRRITATED', 'AGGRESSIVE', 'TEARFUL', 'INDIFFERENT');

-- ────────────────────────────────────────────────────────────────
-- 1. TENANT
-- ────────────────────────────────────────────────────────────────

CREATE TABLE tenant (
    id         UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name       VARCHAR(200)  NOT NULL,
    code       VARCHAR(20)   NOT NULL UNIQUE,
    active     BOOLEAN       NOT NULL DEFAULT TRUE,
    config     JSONB         NOT NULL DEFAULT '{}',
    created_at TIMESTAMPTZ   NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ   NOT NULL DEFAULT now()
);

-- ────────────────────────────────────────────────────────────────
-- 2. ADDRESS
-- ────────────────────────────────────────────────────────────────

CREATE TABLE state (
    code CHAR(2)      PRIMARY KEY,
    name VARCHAR(200) NOT NULL
);

CREATE TABLE city (
    id         UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    state_code CHAR(2)      NOT NULL REFERENCES state(code),
    name       VARCHAR(100) NOT NULL,
    ibge_code  VARCHAR(7)   UNIQUE,
    UNIQUE (state_code, name)
);
CREATE INDEX idx_city_state ON city(state_code);

CREATE TABLE district (
    id       UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    city_id  UUID         NOT NULL REFERENCES city(id),
    name     VARCHAR(200) NOT NULL,
    official BOOLEAN      NOT NULL DEFAULT TRUE,
    UNIQUE (city_id, name)
);
CREATE INDEX idx_district_city ON district(city_id);

CREATE TABLE address (
    id          UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    zip_code    VARCHAR(9),
    street      VARCHAR(255),
    number      VARCHAR(20),
    complement  VARCHAR(200),
    district_id UUID        REFERENCES district(id),
    city_id     UUID        NOT NULL REFERENCES city(id),
    state_code  CHAR(2)     NOT NULL REFERENCES state(code),
    landmark    VARCHAR(200),
    latitude    DOUBLE PRECISION,
    longitude   DOUBLE PRECISION,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at  TIMESTAMPTZ NOT NULL DEFAULT now()
);
CREATE INDEX idx_address_city     ON address(city_id);
CREATE INDEX idx_address_district ON address(district_id);

-- ────────────────────────────────────────────────────────────────
-- 3. PERSON
-- ────────────────────────────────────────────────────────────────

CREATE TABLE person (
    id               UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    tenant_id        UUID        NOT NULL REFERENCES tenant(id),
    type             person_type NOT NULL,
    email            VARCHAR(150),
    address_id       UUID        REFERENCES address(id),
    keycloak_user_id UUID,
    active           BOOLEAN     NOT NULL DEFAULT TRUE,
    created_at       TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at       TIMESTAMPTZ NOT NULL DEFAULT now()
);
CREATE INDEX idx_person_tenant ON person(tenant_id);
CREATE INDEX idx_person_email  ON person(email);

CREATE TABLE individual (
    id          UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    person_id   UUID         NOT NULL UNIQUE REFERENCES person(id),
    full_name   VARCHAR(150) NOT NULL,
    tax_id      VARCHAR(14)  UNIQUE,
    mother_name VARCHAR(150),
    birth_date  DATE,
    gender      gender_type  DEFAULT 'NOT_INFORMED'
);
CREATE INDEX idx_individual_name ON individual USING gin(full_name gin_trgm_ops);

CREATE TABLE organization (
    id                UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    person_id         UUID         NOT NULL UNIQUE REFERENCES person(id),
    tax_id            VARCHAR(18)  UNIQUE,
    legal_name        VARCHAR(255) NOT NULL,
    trade_name        VARCHAR(255),
    business_activity VARCHAR(100),
    website           VARCHAR(255)
);

CREATE TABLE document_type (
    id     UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name   VARCHAR(60) NOT NULL UNIQUE,
    active BOOLEAN     NOT NULL DEFAULT TRUE
);

CREATE TABLE document (
    id                UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    person_id         UUID        NOT NULL REFERENCES person(id),
    document_type_id  UUID        NOT NULL REFERENCES document_type(id),
    number            VARCHAR(30) NOT NULL,
    issuing_authority VARCHAR(30),
    issuing_state     CHAR(2)     REFERENCES state(code),
    issue_date        DATE
);
CREATE INDEX idx_document_person ON document(person_id);

CREATE TABLE phone_type (
    id   UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(60) NOT NULL UNIQUE
);

CREATE TABLE phone (
    id               UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    person_id        UUID        NOT NULL REFERENCES person(id),
    phone_type_id    UUID        REFERENCES phone_type(id),
    number           VARCHAR(20) NOT NULL,
    preferred        BOOLEAN     NOT NULL DEFAULT FALSE,
    whatsapp_enabled BOOLEAN     NOT NULL DEFAULT FALSE
);
CREATE INDEX idx_phone_person ON phone(person_id);

CREATE TABLE organization_contact (
    id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    organization_id UUID         NOT NULL REFERENCES organization(id),
    name            VARCHAR(100) NOT NULL,
    phone           VARCHAR(20),
    department      VARCHAR(50),
    email           VARCHAR(100),
    active          BOOLEAN      NOT NULL DEFAULT TRUE
);

-- ────────────────────────────────────────────────────────────────
-- 4. DEPARTMENT
-- ────────────────────────────────────────────────────────────────

CREATE TABLE department (
    id            UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    tenant_id     UUID         NOT NULL REFERENCES tenant(id),
    parent_id     UUID         REFERENCES department(id),
    name          VARCHAR(500) NOT NULL,
    abbreviation  VARCHAR(100),
    email         VARCHAR(500),
    active        BOOLEAN      NOT NULL DEFAULT TRUE,
    can_respond   BOOLEAN      NOT NULL DEFAULT FALSE,
    is_root       BOOLEAN      NOT NULL DEFAULT FALSE,
    icon_url      VARCHAR(500),
    display_order INTEGER      DEFAULT 0,
    created_at    TIMESTAMPTZ  NOT NULL DEFAULT now(),
    updated_at    TIMESTAMPTZ  NOT NULL DEFAULT now()
);
CREATE INDEX idx_department_tenant ON department(tenant_id);
CREATE INDEX idx_department_parent ON department(parent_id);

CREATE TABLE user_department (
    id               UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    keycloak_user_id UUID    NOT NULL,
    department_id    UUID    NOT NULL REFERENCES department(id),
    can_respond      BOOLEAN NOT NULL DEFAULT FALSE,
    active           BOOLEAN NOT NULL DEFAULT TRUE,
    created_at       TIMESTAMPTZ NOT NULL DEFAULT now(),
    UNIQUE (keycloak_user_id, department_id)
);
CREATE INDEX idx_user_dept_user ON user_department(keycloak_user_id);
CREATE INDEX idx_user_dept_dept ON user_department(department_id);

-- ────────────────────────────────────────────────────────────────
-- 5. SERVICE CATALOG
-- ────────────────────────────────────────────────────────────────

CREATE TABLE subject (
    id            UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    tenant_id     UUID         NOT NULL REFERENCES tenant(id),
    department_id UUID         REFERENCES department(id),
    name          VARCHAR(150) NOT NULL,
    active        BOOLEAN      NOT NULL DEFAULT TRUE,
    visible_web   BOOLEAN      NOT NULL DEFAULT TRUE,
    visible_app   BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at    TIMESTAMPTZ  NOT NULL DEFAULT now(),
    updated_at    TIMESTAMPTZ  NOT NULL DEFAULT now()
);
CREATE INDEX idx_subject_tenant ON subject(tenant_id);

CREATE TABLE subdivision (
    id     UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name   VARCHAR(200) NOT NULL,
    active BOOLEAN      NOT NULL DEFAULT TRUE
);

CREATE TABLE service (
    id                   UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    tenant_id            UUID          NOT NULL REFERENCES tenant(id),
    subject_id           UUID          NOT NULL REFERENCES subject(id),
    subdivision_id       UUID          NOT NULL REFERENCES subdivision(id),
    department_id        UUID          REFERENCES department(id),
    display_name         VARCHAR(300),
    description          TEXT,
    sla_days             INTEGER       NOT NULL DEFAULT 30,
    default_priority     priority_type NOT NULL DEFAULT 'NORMAL',
    allows_anonymous     BOOLEAN       NOT NULL DEFAULT FALSE,
    allows_multi_forward BOOLEAN       NOT NULL DEFAULT FALSE,
    visible_web          BOOLEAN       NOT NULL DEFAULT TRUE,
    visible_app          BOOLEAN       NOT NULL DEFAULT TRUE,
    dynamic_fields       JSONB         DEFAULT '[]',
    active               BOOLEAN       NOT NULL DEFAULT TRUE,
    created_at           TIMESTAMPTZ   NOT NULL DEFAULT now(),
    updated_at           TIMESTAMPTZ   NOT NULL DEFAULT now(),
    UNIQUE (subject_id, subdivision_id)
);
CREATE INDEX idx_service_tenant  ON service(tenant_id);
CREATE INDEX idx_service_subject ON service(subject_id);

CREATE TABLE standard_description (
    id         UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    service_id UUID         NOT NULL REFERENCES service(id),
    text       VARCHAR(500) NOT NULL,
    active     BOOLEAN      NOT NULL DEFAULT TRUE
);

CREATE TABLE response_method (
    id     UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name   VARCHAR(60) NOT NULL UNIQUE,
    active BOOLEAN     NOT NULL DEFAULT TRUE
);

CREATE TABLE service_responsible (
    id                     UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    service_id             UUID    NOT NULL REFERENCES service(id),
    department_id          UUID    NOT NULL REFERENCES department(id),
    principal_department_id UUID   REFERENCES department(id),
    phone                  VARCHAR(60),
    all_districts          BOOLEAN NOT NULL DEFAULT FALSE,
    is_secondary           BOOLEAN NOT NULL DEFAULT FALSE,
    sla_days_override      INTEGER
);
CREATE INDEX idx_srv_resp_service ON service_responsible(service_id);

CREATE TABLE responsible_district (
    id             UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    responsible_id UUID NOT NULL REFERENCES service_responsible(id),
    district_id    UUID NOT NULL REFERENCES district(id),
    UNIQUE (responsible_id, district_id)
);

CREATE TABLE responsible_city (
    id             UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    responsible_id UUID NOT NULL REFERENCES service_responsible(id),
    city_id        UUID NOT NULL REFERENCES city(id),
    UNIQUE (responsible_id, city_id)
);

-- ────────────────────────────────────────────────────────────────
-- 6. SERVICE REQUEST
-- ────────────────────────────────────────────────────────────────

CREATE TABLE request_type (
    id        UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    tenant_id UUID        NOT NULL REFERENCES tenant(id),
    name      VARCHAR(60) NOT NULL,
    active    BOOLEAN     NOT NULL DEFAULT TRUE,
    UNIQUE (tenant_id, name)
);

CREATE TABLE incident (
    id            UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    tenant_id     UUID         REFERENCES tenant(id),
    department_id UUID         REFERENCES department(id),
    name          VARCHAR(100) NOT NULL,
    weight        SMALLINT     DEFAULT 0
);

CREATE TABLE service_request (
    id                 UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    tenant_id          UUID                NOT NULL REFERENCES tenant(id),
    protocol           VARCHAR(30)         NOT NULL,
    service_id         UUID                NOT NULL REFERENCES service(id),
    person_id          UUID                REFERENCES person(id),
    address_id         UUID                REFERENCES address(id),
    request_type_id    UUID                REFERENCES request_type(id),
    incident_id        UUID                REFERENCES incident(id),
    response_method_id UUID                REFERENCES response_method(id),
    channel            channel_type        NOT NULL DEFAULT 'PHONE',
    status             request_status_type NOT NULL DEFAULT 'OPEN',
    priority           priority_type       NOT NULL DEFAULT 'NORMAL',
    description        TEXT,
    internal_note      TEXT,
    dynamic_fields     JSONB               DEFAULT '{}',
    latitude           DOUBLE PRECISION,
    longitude          DOUBLE PRECISION,
    confidential       BOOLEAN             NOT NULL DEFAULT FALSE,
    anonymous          BOOLEAN             NOT NULL DEFAULT FALSE,
    external_doc_type  VARCHAR(60),
    external_doc_number VARCHAR(60),
    due_date           DATE,
    agent_user_id      UUID,
    emotional_state    emotional_state_type,
    created_at         TIMESTAMPTZ         NOT NULL DEFAULT now(),
    updated_at         TIMESTAMPTZ         NOT NULL DEFAULT now(),
    closed_at          TIMESTAMPTZ
);
CREATE UNIQUE INDEX idx_request_protocol ON service_request(tenant_id, protocol);
CREATE INDEX idx_request_tenant    ON service_request(tenant_id);
CREATE INDEX idx_request_service   ON service_request(service_id);
CREATE INDEX idx_request_person    ON service_request(person_id);
CREATE INDEX idx_request_status    ON service_request(status);
CREATE INDEX idx_request_created   ON service_request(created_at);
CREATE INDEX idx_request_channel   ON service_request(channel);

CREATE TABLE history_type (
    id   UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE request_history (
    id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    request_id      UUID        NOT NULL REFERENCES service_request(id),
    history_type_id UUID        REFERENCES history_type(id),
    user_id         UUID,
    description     TEXT,
    previous_data   JSONB,
    new_data        JSONB,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT now()
);
CREATE INDEX idx_req_hist_request ON request_history(request_id);
CREATE INDEX idx_req_hist_created ON request_history(created_at);

CREATE TABLE attachment (
    id             UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    request_id     UUID            NOT NULL REFERENCES service_request(id),
    type           attachment_type NOT NULL,
    file_name      VARCHAR(255)    NOT NULL,
    content_type   VARCHAR(100),
    size_bytes     BIGINT,
    storage_path   VARCHAR(500)    NOT NULL,
    thumbnail_path VARCHAR(500),
    user_id        UUID,
    created_at     TIMESTAMPTZ     NOT NULL DEFAULT now()
);
CREATE INDEX idx_attachment_request ON attachment(request_id);

CREATE TABLE urgency (
    id            UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    request_id    UUID    NOT NULL REFERENCES service_request(id),
    department_id UUID    NOT NULL REFERENCES department(id),
    alert_sent    BOOLEAN NOT NULL DEFAULT FALSE,
    created_at    TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- ────────────────────────────────────────────────────────────────
-- 7. FORWARDING WORKFLOW
-- ────────────────────────────────────────────────────────────────

CREATE TABLE forwarding_reason (
    id   UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(100) NOT NULL,
    type CHAR(1)      NOT NULL
);

CREATE TABLE forwarding (
    id                   UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    request_id           UUID                   NOT NULL REFERENCES service_request(id),
    target_department_id UUID                   NOT NULL REFERENCES department(id),
    source_department_id UUID                   REFERENCES department(id),
    reason_id            UUID                   REFERENCES forwarding_reason(id),
    user_id              UUID,
    status               forwarding_status_type NOT NULL DEFAULT 'PENDING',
    notes                TEXT,
    due_date             DATE,
    read                 BOOLEAN                NOT NULL DEFAULT FALSE,
    read_at              TIMESTAMPTZ,
    created_at           TIMESTAMPTZ            NOT NULL DEFAULT now(),
    updated_at           TIMESTAMPTZ            NOT NULL DEFAULT now(),
    answered_at          TIMESTAMPTZ
);
CREATE INDEX idx_fwd_request ON forwarding(request_id);
CREATE INDEX idx_fwd_target  ON forwarding(target_department_id);
CREATE INDEX idx_fwd_status  ON forwarding(status);

CREATE TABLE forwarding_response (
    id            UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    forwarding_id UUID        NOT NULL REFERENCES forwarding(id),
    department_id UUID        NOT NULL REFERENCES department(id),
    user_id       UUID,
    reason_id     UUID        REFERENCES forwarding_reason(id),
    response      TEXT        NOT NULL,
    created_at    TIMESTAMPTZ NOT NULL DEFAULT now()
);
CREATE INDEX idx_fwd_resp_fwd ON forwarding_response(forwarding_id);

CREATE TABLE response_evaluation (
    id            UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    forwarding_id UUID    NOT NULL REFERENCES forwarding(id),
    department_id UUID    NOT NULL REFERENCES department(id),
    user_id       UUID,
    response      TEXT,
    reviewed      BOOLEAN NOT NULL DEFAULT FALSE,
    return_reason TEXT,
    created_at    TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE redistribution (
    id                   UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    forwarding_id        UUID                   NOT NULL REFERENCES forwarding(id),
    target_department_id UUID                   NOT NULL REFERENCES department(id),
    user_id              UUID,
    status               forwarding_status_type NOT NULL DEFAULT 'PENDING',
    read                 BOOLEAN                NOT NULL DEFAULT FALSE,
    notes                TEXT,
    created_at           TIMESTAMPTZ            NOT NULL DEFAULT now()
);
CREATE INDEX idx_redist_fwd ON redistribution(forwarding_id);

-- ────────────────────────────────────────────────────────────────
-- 8. CONTACT CENTER
-- ────────────────────────────────────────────────────────────────

CREATE TABLE no_contact_type (
    id   UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(60) NOT NULL UNIQUE
);

CREATE TABLE not_found_type (
    id   UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(60) NOT NULL UNIQUE
);

CREATE TABLE agent_task (
    id           UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    request_id   UUID    NOT NULL REFERENCES service_request(id),
    user_id      UUID,
    status       CHAR(1) NOT NULL DEFAULT 'P',
    released     BOOLEAN NOT NULL DEFAULT FALSE,
    created_at   TIMESTAMPTZ NOT NULL DEFAULT now(),
    attempted_at TIMESTAMPTZ
);
CREATE INDEX idx_agent_task_request ON agent_task(request_id);
CREATE INDEX idx_agent_task_user    ON agent_task(user_id);

CREATE TABLE citizen_contact (
    id                UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    agent_task_id     UUID    NOT NULL REFERENCES agent_task(id),
    user_id           UUID,
    found             BOOLEAN NOT NULL,
    not_found_type_id UUID    REFERENCES not_found_type(id),
    created_at        TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE citizen_no_contact (
    id                 UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    agent_task_id      UUID NOT NULL REFERENCES agent_task(id),
    no_contact_type_id UUID NOT NULL REFERENCES no_contact_type(id),
    user_id            UUID,
    created_at         TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE no_service_record (
    id          UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    incident_id UUID REFERENCES incident(id),
    user_id     UUID,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE sla_escalation_level (
    id            UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    department_id UUID    NOT NULL REFERENCES department(id),
    days_before   INTEGER NOT NULL,
    level_order   INTEGER NOT NULL DEFAULT 1
);

CREATE TABLE sla_escalation_user (
    id                      UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    sla_escalation_level_id UUID NOT NULL REFERENCES sla_escalation_level(id),
    keycloak_user_id        UUID NOT NULL,
    UNIQUE (sla_escalation_level_id, keycloak_user_id)
);

CREATE TABLE auto_closure (
    id            UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    tenant_id     UUID NOT NULL REFERENCES tenant(id),
    department_id UUID REFERENCES department(id),
    subject_id    UUID REFERENCES subject(id),
    service_id    UUID REFERENCES service(id),
    notes         TEXT,
    active        BOOLEAN     NOT NULL DEFAULT TRUE,
    created_at    TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- ────────────────────────────────────────────────────────────────
-- 9. SATISFACTION SURVEY
-- ────────────────────────────────────────────────────────────────

CREATE TABLE survey (
    id         UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    tenant_id  UUID         NOT NULL REFERENCES tenant(id),
    title      VARCHAR(200) NOT NULL,
    active     BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at TIMESTAMPTZ  NOT NULL DEFAULT now()
);

CREATE TABLE question (
    id         UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    survey_id  UUID         NOT NULL REFERENCES survey(id),
    service_id UUID         REFERENCES service(id),
    text       VARCHAR(500) NOT NULL,
    type       VARCHAR(20)  NOT NULL DEFAULT 'MULTIPLE_CHOICE',
    required   BOOLEAN      NOT NULL DEFAULT FALSE,
    sort_order INTEGER      NOT NULL DEFAULT 0,
    active     BOOLEAN      NOT NULL DEFAULT TRUE
);

CREATE TABLE answer_option (
    id          UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    question_id UUID         NOT NULL REFERENCES question(id),
    text        VARCHAR(255) NOT NULL,
    sort_order  INTEGER      NOT NULL DEFAULT 0
);

CREATE TABLE survey_response (
    id               UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    request_id       UUID NOT NULL REFERENCES service_request(id),
    question_id      UUID NOT NULL REFERENCES question(id),
    answer_option_id UUID REFERENCES answer_option(id),
    free_text        TEXT,
    rating           SMALLINT,
    created_at       TIMESTAMPTZ NOT NULL DEFAULT now()
);
CREATE INDEX idx_survey_resp_request ON survey_response(request_id);

-- ────────────────────────────────────────────────────────────────
-- 10. NOTIFICATIONS
-- ────────────────────────────────────────────────────────────────

CREATE TABLE notification (
    id           UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    tenant_id    UUID                      NOT NULL REFERENCES tenant(id),
    request_id   UUID                      REFERENCES service_request(id),
    recipient_id UUID,
    channel      notification_channel_type NOT NULL,
    title        VARCHAR(200),
    message      TEXT                      NOT NULL,
    status       notification_status_type  NOT NULL DEFAULT 'PENDING',
    extra_data   JSONB                     DEFAULT '{}',
    created_at   TIMESTAMPTZ               NOT NULL DEFAULT now(),
    sent_at      TIMESTAMPTZ,
    read_at      TIMESTAMPTZ
);
CREATE INDEX idx_notif_request   ON notification(request_id);
CREATE INDEX idx_notif_pending   ON notification(status) WHERE status = 'PENDING';
CREATE INDEX idx_notif_recipient ON notification(recipient_id);

CREATE TABLE email_log (
    id         UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    request_id UUID REFERENCES service_request(id),
    user_id    UUID,
    recipient  VARCHAR(255) NOT NULL,
    subject    VARCHAR(200),
    message    TEXT,
    sent       BOOLEAN      NOT NULL DEFAULT FALSE,
    error      TEXT,
    created_at TIMESTAMPTZ  NOT NULL DEFAULT now()
);
CREATE INDEX idx_email_log_request ON email_log(request_id);

CREATE TABLE notification_rule (
    id         UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    service_id UUID                      NOT NULL REFERENCES service(id),
    event      VARCHAR(50)               NOT NULL,
    channel    notification_channel_type NOT NULL,
    template   TEXT                      NOT NULL,
    active     BOOLEAN                   NOT NULL DEFAULT TRUE
);

-- ────────────────────────────────────────────────────────────────
-- 11. VIEWS
-- ────────────────────────────────────────────────────────────────

CREATE OR REPLACE VIEW vw_request_summary AS
SELECT
    sr.id,
    sr.protocol,
    sr.channel,
    sr.status,
    sr.priority,
    sr.description,
    sr.created_at,
    sr.closed_at,
    sr.due_date,
    CASE
        WHEN sr.status IN ('CLOSED', 'CANCELLED') THEN NULL
        WHEN sr.due_date < CURRENT_DATE THEN 'OVERDUE'
        WHEN sr.due_date <= CURRENT_DATE + INTERVAL '3 days' THEN 'DUE_SOON'
        ELSE 'ON_TRACK'
    END AS sla_status,
    t.name          AS tenant_name,
    sub.name        AS subject_name,
    sd.name         AS subdivision_name,
    sv.display_name AS service_name,
    d.name          AS department_name,
    COALESCE(ind.full_name, org.legal_name, 'ANONYMOUS') AS citizen_name,
    c.name          AS city_name,
    di.name         AS district_name
FROM service_request sr
JOIN tenant t            ON t.id  = sr.tenant_id
JOIN service sv          ON sv.id = sr.service_id
JOIN subject sub         ON sub.id = sv.subject_id
JOIN subdivision sd      ON sd.id  = sv.subdivision_id
LEFT JOIN department d   ON d.id  = sv.department_id
LEFT JOIN person p       ON p.id  = sr.person_id
LEFT JOIN individual ind ON ind.person_id = p.id
LEFT JOIN organization org ON org.person_id = p.id
LEFT JOIN address a      ON a.id  = sr.address_id
LEFT JOIN city c         ON c.id  = a.city_id
LEFT JOIN district di    ON di.id = a.district_id;

CREATE OR REPLACE VIEW vw_department_dashboard AS
SELECT
    d.id          AS department_id,
    d.name        AS department_name,
    d.tenant_id,
    COUNT(*) FILTER (WHERE sr.status NOT IN ('CLOSED','CANCELLED'))
        AS total_open,
    COUNT(*) FILTER (WHERE sr.due_date < CURRENT_DATE
                     AND sr.status NOT IN ('CLOSED','CANCELLED'))
        AS total_overdue,
    COUNT(*) FILTER (WHERE sr.due_date BETWEEN CURRENT_DATE AND CURRENT_DATE + 3
                     AND sr.status NOT IN ('CLOSED','CANCELLED'))
        AS total_due_soon,
    COUNT(*) FILTER (WHERE sr.status = 'CLOSED'
                     AND sr.closed_at >= date_trunc('month', CURRENT_DATE))
        AS closed_this_month,
    AVG(EXTRACT(EPOCH FROM (sr.closed_at - sr.created_at)) / 86400)
        FILTER (WHERE sr.status = 'CLOSED'
                AND sr.closed_at >= date_trunc('month', CURRENT_DATE))
        AS avg_days_to_close
FROM forwarding f
JOIN service_request sr ON sr.id = f.request_id
JOIN department d       ON d.id  = f.target_department_id
GROUP BY d.id, d.name, d.tenant_id;

-- ────────────────────────────────────────────────────────────────
-- 12. PROTOCOL SEQUENCE & GENERATOR
-- ────────────────────────────────────────────────────────────────

CREATE SEQUENCE seq_protocol START 1;

CREATE OR REPLACE FUNCTION generate_protocol(p_tenant_id UUID)
RETURNS VARCHAR AS $$
DECLARE
    v_year TEXT := to_char(now(), 'YYYY');
    v_seq  BIGINT;
    v_code TEXT;
BEGIN
    SELECT code INTO v_code FROM tenant WHERE id = p_tenant_id;
    v_seq := nextval('seq_protocol');
    RETURN v_code || '-' || v_year || '-' || lpad(v_seq::text, 8, '0');
END;
$$ LANGUAGE plpgsql;

-- ────────────────────────────────────────────────────────────────
-- 13. AUTO updated_at TRIGGER
-- ────────────────────────────────────────────────────────────────

CREATE OR REPLACE FUNCTION fn_set_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = now();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DO $$
DECLARE
    t TEXT;
BEGIN
    FOR t IN
        SELECT table_name
        FROM information_schema.columns
        WHERE table_schema = 'public'
          AND column_name  = 'updated_at'
    LOOP
        EXECUTE format(
            'CREATE TRIGGER trg_updated_at BEFORE UPDATE ON %I '
            'FOR EACH ROW EXECUTE FUNCTION fn_set_updated_at()', t
        );
    END LOOP;
END;
$$;

-- ────────────────────────────────────────────────────────────────
-- 14. AUTO HISTORY ON STATUS CHANGE
-- ────────────────────────────────────────────────────────────────

CREATE OR REPLACE FUNCTION fn_request_status_history()
RETURNS TRIGGER AS $$
BEGIN
    IF OLD.status IS DISTINCT FROM NEW.status THEN
        INSERT INTO request_history (request_id, description, previous_data, new_data)
        VALUES (
            NEW.id,
            'Status changed from ' || OLD.status || ' to ' || NEW.status,
            jsonb_build_object('status', OLD.status),
            jsonb_build_object('status', NEW.status)
        );
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_request_status_history
    AFTER UPDATE ON service_request
    FOR EACH ROW
    EXECUTE FUNCTION fn_request_status_history();
