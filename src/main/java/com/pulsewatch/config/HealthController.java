package com.pulsewatch.config;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Simple endpoint to confirm the app is running.
 * Hit GET / in the browser or Postman after starting the app.
 */
@RestController
public class HealthController {

    @GetMapping("/")
    public Map<String, Object> health() {
        return Map.of(
            "application", "PulseWatch",
            "status", "UP",
            "timestamp", LocalDateTime.now()
        );
    }
}
