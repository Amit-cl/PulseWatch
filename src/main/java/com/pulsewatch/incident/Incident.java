package com.pulsewatch.incident;

import java.time.LocalDateTime;

import jakarta.persistence.*;

/**
 * Tracks an outage from detection to recovery.
 *
 * Lifecycle:
 *   1. Consecutive failures reach threshold → status = OPEN
 *   2. Consecutive successes reach threshold → status = RESOLVED, resolvedAt = now
 *
 * DB constraint ensures only ONE open incident per monitor at a time.
 */
@Entity
@Table(name = "incidents")
public class Incident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "monitor_id", nullable = false)
    private Long monitorId;

    @Column(name = "started_at", nullable = false)
    private LocalDateTime startedAt = LocalDateTime.now();

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

    @Column(nullable = false, length = 20)
    private String status = "OPEN";

    @Column(name = "failure_reason", nullable = false, length = 500)
    private String failureReason;

    // -------- Constructors --------

    protected Incident() {}

    public Incident(Long monitorId, String failureReason) {
        this.monitorId = monitorId;
        this.failureReason = failureReason;
    }

    // -------- Business methods --------

    /** Mark this incident as resolved. Called when recovery threshold is met. */
    public void resolve() {
        this.status = "RESOLVED";
        this.resolvedAt = LocalDateTime.now();
    }

    public boolean isOpen() {
        return "OPEN".equals(this.status);
    }

    // -------- Getters --------

    public Long getId() { return id; }
    public Long getMonitorId() { return monitorId; }
    public LocalDateTime getStartedAt() { return startedAt; }
    public LocalDateTime getResolvedAt() { return resolvedAt; }
    public String getStatus() { return status; }
    public String getFailureReason() { return failureReason; }
}
