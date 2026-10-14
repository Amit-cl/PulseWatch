package com.pulsewatch.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Binds the custom 'pulsewatch' properties from application.yml.
 * Used across services for consistent default values.
 */
@Configuration
@ConfigurationProperties(prefix = "pulsewatch")
public class PulseWatchProperties {

    private Jwt jwt = new Jwt();
    private HealthCheck healthCheck = new HealthCheck();

    // -------- Getters/Setters --------

    public Jwt getJwt() { return jwt; }
    public void setJwt(Jwt jwt) { this.jwt = jwt; }
    public HealthCheck getHealthCheck() { return healthCheck; }
    public void setHealthCheck(HealthCheck healthCheck) { this.healthCheck = healthCheck; }

    // -------- Inner classes --------

    public static class Jwt {
        private String secret;
        private long expirationMs = 86400000; // 24h

        public String getSecret() { return secret; }
        public void setSecret(String secret) { this.secret = secret; }
        public long getExpirationMs() { return expirationMs; }
        public void setExpirationMs(long expirationMs) { this.expirationMs = expirationMs; }
    }

    public static class HealthCheck {
        private int defaultTimeoutMs = 10000;
        private int defaultIntervalSeconds = 60;
        private int maxConcurrentChecks = 20;
        private int consecutiveFailuresThreshold = 3;
        private int consecutiveSuccessesThreshold = 2;

        public int getDefaultTimeoutMs() { return defaultTimeoutMs; }
        public void setDefaultTimeoutMs(int defaultTimeoutMs) { this.defaultTimeoutMs = defaultTimeoutMs; }
        public int getDefaultIntervalSeconds() { return defaultIntervalSeconds; }
        public void setDefaultIntervalSeconds(int defaultIntervalSeconds) { this.defaultIntervalSeconds = defaultIntervalSeconds; }
        public int getMaxConcurrentChecks() { return maxConcurrentChecks; }
        public void setMaxConcurrentChecks(int maxConcurrentChecks) { this.maxConcurrentChecks = maxConcurrentChecks; }
        public int getConsecutiveFailuresThreshold() { return consecutiveFailuresThreshold; }
        public void setConsecutiveFailuresThreshold(int consecutiveFailuresThreshold) { this.consecutiveFailuresThreshold = consecutiveFailuresThreshold; }
        public int getConsecutiveSuccessesThreshold() { return consecutiveSuccessesThreshold; }
        public void setConsecutiveSuccessesThreshold(int consecutiveSuccessesThreshold) { this.consecutiveSuccessesThreshold = consecutiveSuccessesThreshold; }
    }
}
