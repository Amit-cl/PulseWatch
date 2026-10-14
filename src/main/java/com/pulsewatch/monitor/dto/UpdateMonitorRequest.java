package com.pulsewatch.monitor.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

/**
 * What the client sends when updating a monitor.
 *
 * All fields are optional (nullable) — only the fields the client
 * includes will be updated. This is how PATCH works:
 * send only what changed, leave the rest alone.
 */
public class UpdateMonitorRequest {

    private String name;

    private String url;

    @Min(value = 10, message = "Interval must be at least 10 seconds")
    private Integer intervalSeconds;

    @Min(value = 1000, message = "Timeout must be at least 1000ms")
    @Max(value = 60000, message = "Timeout must be at most 60000ms")
    private Integer timeoutMs;

    private Integer expectedStatusCode;

    @Min(value = 1, message = "Failures threshold must be at least 1")
    private Integer consecutiveFailuresThreshold;

    @Min(value = 1, message = "Successes threshold must be at least 1")
    private Integer consecutiveSuccessesThreshold;

    // -------- Getters & Setters --------

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public Integer getIntervalSeconds() { return intervalSeconds; }
    public void setIntervalSeconds(Integer intervalSeconds) { this.intervalSeconds = intervalSeconds; }

    public Integer getTimeoutMs() { return timeoutMs; }
    public void setTimeoutMs(Integer timeoutMs) { this.timeoutMs = timeoutMs; }

    public Integer getExpectedStatusCode() { return expectedStatusCode; }
    public void setExpectedStatusCode(Integer expectedStatusCode) { this.expectedStatusCode = expectedStatusCode; }

    public Integer getConsecutiveFailuresThreshold() { return consecutiveFailuresThreshold; }
    public void setConsecutiveFailuresThreshold(Integer t) { this.consecutiveFailuresThreshold = t; }

    public Integer getConsecutiveSuccessesThreshold() { return consecutiveSuccessesThreshold; }
    public void setConsecutiveSuccessesThreshold(Integer t) { this.consecutiveSuccessesThreshold = t; }
}
