package com.pulsewatch.monitor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pulsewatch.monitor.dto.CreateMonitorRequest;
import com.pulsewatch.monitor.dto.MonitorResponse;
import com.pulsewatch.monitor.dto.UpdateMonitorRequest;

import jakarta.validation.Valid;

/**
 * REST API for monitor management.
 *
 * Controller's ONLY job: receive HTTP request → call service → return response.
 * No business logic here. That stays in MonitorService.
 *
 * userId is hardcoded to 1 for now. In Step 05 (JWT), it will come from the token.
 */
@RestController
@RequestMapping("/api/v1/monitors")
public class MonitorController {

    private final MonitorService monitorService;

    public MonitorController(MonitorService monitorService) {
        this.monitorService = monitorService;
    }

    // TODO: Replace hardcoded userId with JWT-authenticated user in Step 05
    private static final Long TEMP_USER_ID = 1L;

    /**
     * POST /api/v1/monitors
     * Create a new monitor.
     *
     * @Valid triggers the validation annotations on CreateMonitorRequest.
     * If validation fails, GlobalExceptionHandler returns field errors.
     */
    @PostMapping
    public ResponseEntity<MonitorResponse> create(@Valid @RequestBody CreateMonitorRequest request) {
        Monitor monitor = monitorService.createMonitor(TEMP_USER_ID, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(MonitorResponse.from(monitor));
    }

    /**
     * GET /api/v1/monitors
     * List all monitors for the current user.
     */
    @GetMapping
    public ResponseEntity<List<MonitorResponse>> list() {
        List<MonitorResponse> monitors = monitorService.getMonitorsByUser(TEMP_USER_ID)
                .stream()
                .map(MonitorResponse::from)   // Convert each entity to response DTO
                .toList();
        return ResponseEntity.ok(monitors);
    }

    /**
     * GET /api/v1/monitors/{id}
     * View one monitor's details.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MonitorResponse> getById(@PathVariable Long id) {
        Monitor monitor = monitorService.getMonitorById(id);
        return ResponseEntity.ok(MonitorResponse.from(monitor));
    }

    /**
     * PATCH /api/v1/monitors/{id}
     * Update only the fields the client sends.
     * Null fields = "don't change this".
     */
    @PatchMapping("/{id}")
    public ResponseEntity<MonitorResponse> update(@PathVariable Long id,
                                                  @Valid @RequestBody UpdateMonitorRequest request) {
        Monitor monitor = monitorService.updateMonitor(id, request);
        return ResponseEntity.ok(MonitorResponse.from(monitor));
    }

    /**
     * POST /api/v1/monitors/{id}/pause
     * Stop scheduled checks without deleting the monitor.
     */
    @PostMapping("/{id}/pause")
    public ResponseEntity<MonitorResponse> pause(@PathVariable Long id) {
        Monitor monitor = monitorService.pauseMonitor(id);
        return ResponseEntity.ok(MonitorResponse.from(monitor));
    }

    /**
     * POST /api/v1/monitors/{id}/resume
     * Restart scheduled checks.
     */
    @PostMapping("/{id}/resume")
    public ResponseEntity<MonitorResponse> resume(@PathVariable Long id) {
        Monitor monitor = monitorService.resumeMonitor(id);
        return ResponseEntity.ok(MonitorResponse.from(monitor));
    }

    /**
     * DELETE /api/v1/monitors/{id}
     * Delete the monitor and all related data (cascaded in DB).
     * Returns 204 No Content — standard for successful deletes.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        monitorService.deleteMonitor(id);
        return ResponseEntity.noContent().build();
    }
}
