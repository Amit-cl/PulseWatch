-- ============================================================
-- V5 — Notification logs table
-- ============================================================
-- Tracks every notification delivery attempt.
-- Unique constraint prevents duplicate OUTAGE or RECOVERY
-- notifications for the same incident.
-- ============================================================

CREATE TABLE notification_logs (
    id              BIGSERIAL       PRIMARY KEY,
    incident_id     BIGINT          NOT NULL REFERENCES incidents(id) ON DELETE CASCADE,
    event_type      VARCHAR(20)     NOT NULL,
    channel         VARCHAR(20)     NOT NULL DEFAULT 'EMAIL',
    status          VARCHAR(20)     NOT NULL DEFAULT 'PENDING',
    attempt_count   INT             NOT NULL DEFAULT 0,
    created_at      TIMESTAMP       NOT NULL DEFAULT NOW(),

    CONSTRAINT chk_notif_event_type CHECK (event_type IN ('OUTAGE', 'RECOVERY')),
    CONSTRAINT chk_notif_channel    CHECK (channel IN ('EMAIL')),
    CONSTRAINT chk_notif_status     CHECK (status IN ('PENDING', 'SENT', 'FAILED')),

    -- One notification per incident per event type
    CONSTRAINT uq_notification_incident_event UNIQUE (incident_id, event_type)
);

CREATE INDEX idx_notif_pending ON notification_logs (status) WHERE status = 'PENDING';
