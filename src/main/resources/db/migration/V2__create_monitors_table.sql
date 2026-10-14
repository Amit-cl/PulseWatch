-- ============================================================
-- V2 — Monitors table
-- ============================================================
-- Each monitor belongs to one user and represents
-- an HTTP endpoint the user wants to watch.
-- ============================================================

CREATE TABLE monitors (
    id                                BIGSERIAL       PRIMARY KEY,
    user_id                           BIGINT          NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    name                              VARCHAR(255)    NOT NULL,
    url                               TEXT            NOT NULL,
    interval_seconds                  INT             NOT NULL DEFAULT 60,
    timeout_ms                        INT             NOT NULL DEFAULT 10000,
    expected_status_code              INT             NOT NULL DEFAULT 200,
    consecutive_failures_threshold    INT             NOT NULL DEFAULT 3,
    consecutive_successes_threshold   INT             NOT NULL DEFAULT 2,
    enabled                           BOOLEAN         NOT NULL DEFAULT TRUE,
    created_at                        TIMESTAMP       NOT NULL DEFAULT NOW(),

    CONSTRAINT chk_monitors_interval   CHECK (interval_seconds >= 10),
    CONSTRAINT chk_monitors_timeout    CHECK (timeout_ms >= 1000 AND timeout_ms <= 60000)
);

CREATE INDEX idx_monitors_user_id ON monitors (user_id);
CREATE INDEX idx_monitors_enabled ON monitors (enabled) WHERE enabled = TRUE;
