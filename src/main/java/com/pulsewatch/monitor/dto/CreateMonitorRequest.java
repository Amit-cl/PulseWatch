package com.pulsewatch.monitor.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

/**
 * What the client sends when creating a new monitor.
 *
 * Why a DTO instead of using the Monitor entity directly?
 * - We don't want the client to set id, userId, or createdAt
 * - Validation annotations belong on the request, not on the entity
 * - If the API shape changes, the entity stays stable
 */
public class CreateMonitorRequest {

    @NotBlank(message = "Monitor name is required")
    private String name;

    @NotBlank(message = "URL is required")
    private String url;

    @Min(value = 10, message = "Interval must be at least 10 seconds")
    private int intervalSeconds = 60;

    @Min(value = 1000, message = "Timeout must be at least 1000ms")
    @Max(value = 60000, message = "Timeout must be at most 60000ms")
    private int timeoutMs = 10000;

    private int expectedStatusCode = 200;

    @Min(value = 1, message = "Failures threshold must be at least 1")
    private int consecutiveFailuresThreshold = 3;

    @Min(value = 1, message = "Successes threshold must be at least 1")
    private int consecutiveSuccessesThreshold = 2;

    // -------- Getters & Setters --------

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
}
