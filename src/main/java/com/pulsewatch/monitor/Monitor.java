package com.pulsewatch.monitor;

import java.time.LocalDateTime;

import jakarta.persistence.*;

/**
 * Represents an API endpoint that a user wants to monitor.
 *
 * Key fields:
 * - url: the HTTP endpoint we'll call periodically
 * - intervalSeconds: how often (in seconds) to check it
 * - timeoutMs: max wait time before we consider the check failed
 * - enabled: pause/resume monitoring without deleting the monitor
 * - consecutiveFailuresThreshold: how many failures in a row before we open an incident
 * - consecutiveSuccessesThreshold: how many successes in a row before we resolve an incident
 */
@Entity
@Table(name = "monitors")
public class Monitor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String url;

    @Column(name = "interval_seconds", nullable = false)
    private int intervalSeconds = 60;

    @Column(name = "timeout_ms", nullable = false)
    private int timeoutMs = 10000;

    @Column(name = "expected_status_code", nullable = false)
    private int expectedStatusCode = 200;

    @Column(name = "consecutive_failures_threshold", nullable = false)
    private int consecutiveFailuresThreshold = 3;

    @Column(name = "consecutive_successes_threshold", nullable = false)
    private int consecutiveSuccessesThreshold = 2;

    @Column(nullable = false)
    private boolean enabled = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // -------- Constructors --------

    protected Monitor() {}

    public Monitor(Long userId, String name, String url) {
        this.userId = userId;
        this.name = name;
        this.url = url;
    }

    // -------- Getters & Setters --------

    public Long getId() { return id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public int getIntervalSeconds() { return intervalSeconds; }
    public void setIntervalSeconds(int intervalSeconds) { this.intervalSeconds = intervalSeconds; }

    public int getTimeoutMs() { return timeoutMs; }
    public void setTimeoutMs(int timeoutMs) { this.timeoutMs = timeoutMs; }

    public int getExpectedStatusCode() { return expectedStatusCode; }
    public void setExpectedStatusCode(int expectedStatusCode) { this.expectedStatusCode = expectedStatusCode; }

    public int getConsecutiveFailuresThreshold() { return consecutiveFailuresThreshold; }
    public void setConsecutiveFailuresThreshold(int t) { this.consecutiveFailuresThreshold = t; }

    public int getConsecutiveSuccessesThreshold() { return consecutiveSuccessesThreshold; }
    public void setConsecutiveSuccessesThreshold(int t) { this.consecutiveSuccessesThreshold = t; }

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }

    public LocalDateTime getCreatedAt() { return createdAt; }
}
