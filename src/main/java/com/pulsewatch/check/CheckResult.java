package com.pulsewatch.check;

import java.time.LocalDateTime;

import jakarta.persistence.*;

/**
 * One row = one health-check attempt.
 *
 * If success=true:  statusCode and latencyMs are populated.
 * If success=false: errorType explains what went wrong (TIMEOUT, CONNECTION_FAILED, etc.)
 */
@Entity
@Table(name = "check_results")
public class CheckResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "monitor_id", nullable = false)
    private Long monitorId;

    @Column(name = "checked_at", nullable = false)
    private LocalDateTime checkedAt = LocalDateTime.now();

    @Column(nullable = false)
    private boolean success;

    @Column(name = "status_code")
    private Integer statusCode;

    @Column(name = "latency_ms")
    private Long latencyMs;

    @Column(name = "error_type", length = 100)
    private String errorType;

    // -------- Constructors --------

    protected CheckResult() {}

    /** Factory: successful check */
    public static CheckResult success(Long monitorId, int statusCode, long latencyMs) {
        CheckResult r = new CheckResult();
        r.monitorId = monitorId;
        r.success = true;
        r.statusCode = statusCode;
        r.latencyMs = latencyMs;
        return r;
    }

    /** Factory: failed check */
    public static CheckResult failure(Long monitorId, String errorType) {
        CheckResult r = new CheckResult();
        r.monitorId = monitorId;
        r.success = false;
        r.errorType = errorType;
        return r;
    }

    /** Factory: failed check with partial info (e.g. got a response but wrong status) */
    public static CheckResult failure(Long monitorId, String errorType, Integer statusCode, Long latencyMs) {
        CheckResult r = new CheckResult();
        r.monitorId = monitorId;
        r.success = false;
        r.errorType = errorType;
        r.statusCode = statusCode;
        r.latencyMs = latencyMs;
        return r;
    }

    // -------- Getters --------

    public Long getId() { return id; }
    public Long getMonitorId() { return monitorId; }
    public LocalDateTime getCheckedAt() { return checkedAt; }
    public boolean isSuccess() { return success; }
    public Integer getStatusCode() { return statusCode; }
    public Long getLatencyMs() { return latencyMs; }
    public String getErrorType() { return errorType; }
}
