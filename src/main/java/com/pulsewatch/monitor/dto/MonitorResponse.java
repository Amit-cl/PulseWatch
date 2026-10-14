package com.pulsewatch.monitor.dto;

import java.time.LocalDateTime;

import com.pulsewatch.monitor.Monitor;

/**
 * What the API returns to the client.
 *
 * Why not return the Monitor entity directly?
 * - Entity has userId but we might not want to expose it
 * - Entity has JPA annotations that leak persistence details
 * - If we add internal fields later, they won't accidentally show up in the API
 *
 * The static from() method converts Entity → Response in one place.
 */
public class MonitorResponse {

    private Long id;
    private String name;
    private String url;
    private int intervalSeconds;
    private int timeoutMs;
    private int expectedStatusCode;
    private int consecutiveFailuresThreshold;
    private int consecutiveSuccessesThreshold;
    private boolean enabled;
    private LocalDateTime createdAt;

    // -------- Factory method: Entity → Response --------

    public static MonitorResponse from(Monitor monitor) {
        MonitorResponse r = new MonitorResponse();
        r.id = monitor.getId();
        r.name = monitor.getName();
        r.url = monitor.getUrl();
        r.intervalSeconds = monitor.getIntervalSeconds();
        r.timeoutMs = monitor.getTimeoutMs();
        r.expectedStatusCode = monitor.getExpectedStatusCode();
        r.consecutiveFailuresThreshold = monitor.getConsecutiveFailuresThreshold();
        r.consecutiveSuccessesThreshold = monitor.getConsecutiveSuccessesThreshold();
        r.enabled = monitor.isEnabled();
        r.createdAt = monitor.getCreatedAt();
        return r;
    }

    // -------- Getters (Jackson needs these to serialize to JSON) --------

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getUrl() { return url; }
    public int getIntervalSeconds() { return intervalSeconds; }
    public int getTimeoutMs() { return timeoutMs; }
    public int getExpectedStatusCode() { return expectedStatusCode; }
    public int getConsecutiveFailuresThreshold() { return consecutiveFailuresThreshold; }
    public int getConsecutiveSuccessesThreshold() { return consecutiveSuccessesThreshold; }
    public boolean isEnabled() { return enabled; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
