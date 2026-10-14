-- ============================================================
-- V3 — Check results table
-- ============================================================
-- One row per health-check attempt. This table grows fast,
-- so indexes are selective to balance read and write speed.
-- ============================================================

CREATE TABLE check_results (
    id              BIGSERIAL       PRIMARY KEY,
    monitor_id      BIGINT          NOT NULL REFERENCES monitors(id) ON DELETE CASCADE,
    checked_at      TIMESTAMP       NOT NULL DEFAULT NOW(),
    success         BOOLEAN         NOT NULL,
    status_code     INT,
    latency_ms      BIGINT,
    error_type      VARCHAR(100),

    CONSTRAINT chk_results_latency CHECK (latency_ms IS NULL OR latency_ms >= 0)
);

-- Query pattern: "last N checks for a monitor, newest first"
CREATE INDEX idx_check_results_monitor_time ON check_results (monitor_id, checked_at DESC);
