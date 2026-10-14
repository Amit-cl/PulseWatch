package com.pulsewatch.monitor;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonitorRepository extends JpaRepository<Monitor, Long> {

    /** All monitors belonging to one user */
    List<Monitor> findByUserId(Long userId);

    /** Only the active monitors — used by the scheduler */
    List<Monitor> findByEnabledTrue();
}
