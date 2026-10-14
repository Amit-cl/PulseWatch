-- ============================================================
-- V4 — Incidents table
-- ============================================================
-- Tracks outage windows from detection to resolution.
-- The partial unique index enforces: at most ONE open
-- incident per monitor at the database level.
-- ============================================================

CREATE TABLE incidents (
    id              BIGSERIAL       PRIMARY KEY,
    monitor_id      BIGINT          NOT NULL REFERENCES monitors(id) ON DELETE CASCADE,
    started_at      TIMESTAMP       NOT NULL DEFAULT NOW(),
    resolved_at     TIMESTAMP,
    status          VARCHAR(20)     NOT NULL DEFAULT 'OPEN',
    failure_reason  VARCHAR(500)    NOT NULL,

    CONSTRAINT chk_incidents_status CHECK (status IN ('OPEN', 'RESOLVED')),
    CONSTRAINT chk_incidents_resolved CHECK (
        (status = 'OPEN'     AND resolved_at IS NULL) OR
        (status = 'RESOLVED' AND resolved_at IS NOT NULL)
    )
);

-- Only one OPEN incident per monitor — critical business rule
CREATE UNIQUE INDEX uq_incidents_monitor_open
    ON incidents (monitor_id) WHERE status = 'OPEN';

CREATE INDEX idx_incidents_monitor_id ON incidents (monitor_id, started_at DESC);
