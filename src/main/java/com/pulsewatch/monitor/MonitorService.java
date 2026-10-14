package com.pulsewatch.monitor;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pulsewatch.common.exception.DuplicateResourceException;
import com.pulsewatch.common.exception.ResourceNotFoundException;
import com.pulsewatch.monitor.dto.CreateMonitorRequest;
import com.pulsewatch.monitor.dto.UpdateMonitorRequest;

/**
 * All monitor business logic lives here.
 *
 * Controller → Service → Repository
 * Controller handles HTTP, Service handles rules, Repository handles SQL.
 *
 * Why @Transactional on write methods?
 * If any step fails (e.g. DB constraint violation), the entire
 * operation rolls back. No half-saved data.
 */
@Service
public class MonitorService {

    private static final Logger log = LoggerFactory.getLogger(MonitorService.class);

    private final MonitorRepository monitorRepository;

    // Constructor injection — Spring automatically injects the repository
    public MonitorService(MonitorRepository monitorRepository) {
        this.monitorRepository = monitorRepository;
    }

    /**
     * Create a new monitor for the given user.
     * userId will come from JWT token in Step 05. For now, we pass it manually.
     */
    @Transactional
    public Monitor createMonitor(Long userId, CreateMonitorRequest request) {
        Monitor monitor = new Monitor(userId, request.getName(), request.getUrl());
        monitor.setIntervalSeconds(request.getIntervalSeconds());
        monitor.setTimeoutMs(request.getTimeoutMs());
        monitor.setExpectedStatusCode(request.getExpectedStatusCode());
        monitor.setConsecutiveFailuresThreshold(request.getConsecutiveFailuresThreshold());
        monitor.setConsecutiveSuccessesThreshold(request.getConsecutiveSuccessesThreshold());

        Monitor saved = monitorRepository.save(monitor);
        log.info("Monitor created: id={}, name={}, url={}", saved.getId(), saved.getName(), saved.getUrl());
        return saved;
    }

    /**
     * Get all monitors for a specific user.
     */
    public List<Monitor> getMonitorsByUser(Long userId) {
        return monitorRepository.findByUserId(userId);
    }

    /**
     * Get one monitor by ID.
     * Throws 404 if not found.
     */
    public Monitor getMonitorById(Long id) {
        return monitorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Monitor", id));
    }

    /**
     * Update only the fields that the client sent (non-null values).
     * This is PATCH behavior — if a field is null, it means "don't change it".
     */
    @Transactional
    public Monitor updateMonitor(Long id, UpdateMonitorRequest request) {
        Monitor monitor = getMonitorById(id);

        // Only update fields the client actually sent
        if (request.getName() != null) monitor.setName(request.getName());
        if (request.getUrl() != null) monitor.setUrl(request.getUrl());
        if (request.getIntervalSeconds() != null) monitor.setIntervalSeconds(request.getIntervalSeconds());
        if (request.getTimeoutMs() != null) monitor.setTimeoutMs(request.getTimeoutMs());
        if (request.getExpectedStatusCode() != null) monitor.setExpectedStatusCode(request.getExpectedStatusCode());
        if (request.getConsecutiveFailuresThreshold() != null)
            monitor.setConsecutiveFailuresThreshold(request.getConsecutiveFailuresThreshold());
        if (request.getConsecutiveSuccessesThreshold() != null)
            monitor.setConsecutiveSuccessesThreshold(request.getConsecutiveSuccessesThreshold());

        Monitor saved = monitorRepository.save(monitor);
        log.info("Monitor updated: id={}", saved.getId());
        return saved;
    }

    /**
     * Pause monitoring — stops scheduled checks without deleting data.
     */
    @Transactional
    public Monitor pauseMonitor(Long id) {
        Monitor monitor = getMonitorById(id);
        if (!monitor.isEnabled()) {
            throw new DuplicateResourceException("Monitor is already paused");
        }
        monitor.setEnabled(false);
        log.info("Monitor paused: id={}", id);
        return monitorRepository.save(monitor);
    }

    /**
     * Resume monitoring — restarts scheduled checks.
     */
    @Transactional
    public Monitor resumeMonitor(Long id) {
        Monitor monitor = getMonitorById(id);
        if (monitor.isEnabled()) {
            throw new DuplicateResourceException("Monitor is already active");
        }
        monitor.setEnabled(true);
        log.info("Monitor resumed: id={}", id);
        return monitorRepository.save(monitor);
    }

    /**
     * Delete a monitor and all its dependent data.
     * CASCADE in the database handles deleting check_results, incidents, etc.
     */
    @Transactional
    public void deleteMonitor(Long id) {
        Monitor monitor = getMonitorById(id);  // throws 404 if not found
        monitorRepository.delete(monitor);
        log.info("Monitor deleted: id={}", id);
    }
}
