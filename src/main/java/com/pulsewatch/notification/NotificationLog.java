package com.pulsewatch.notification;

import java.time.LocalDateTime;

import jakarta.persistence.*;

/**
 * Tracks every notification delivery attempt.
 *
 * DB constraint: only one OUTAGE notification and one RECOVERY notification
 * per incident — prevents duplicate alert spam.
 */
@Entity
@Table(name = "notification_logs")
public class NotificationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "incident_id", nullable = false)
    private Long incidentId;

    @Column(name = "event_type", nullable = false, length = 20)
    private String eventType;   // OUTAGE or RECOVERY

    @Column(nullable = false, length = 20)
    private String channel = "EMAIL";

    @Column(nullable = false, length = 20)
    private String status = "PENDING";  // PENDING → SENT or FAILED

    @Column(name = "attempt_count", nullable = false)
    private int attemptCount = 0;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // -------- Constructors --------

    protected NotificationLog() {}

    public NotificationLog(Long incidentId, String eventType) {
        this.incidentId = incidentId;
        this.eventType = eventType;
    }

    // -------- Business methods --------

    public void markSent() {
        this.status = "SENT";
        this.attemptCount++;
    }

    public void markFailed() {
        this.status = "FAILED";
        this.attemptCount++;
    }

    // -------- Getters --------

    public Long getId() { return id; }
    public Long getIncidentId() { return incidentId; }
    public String getEventType() { return eventType; }
    public String getChannel() { return channel; }
    public String getStatus() { return status; }
    public int getAttemptCount() { return attemptCount; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
