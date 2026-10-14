package com.pulsewatch.incident;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IncidentRepository extends JpaRepository<Incident, Long> {

    /** Find the currently open incident for a monitor (at most one due to DB constraint) */
    Optional<Incident> findByMonitorIdAndStatus(Long monitorId, String status);

    /** All incidents for a monitor, newest first */
    List<Incident> findByMonitorIdOrderByStartedAtDesc(Long monitorId);
}
