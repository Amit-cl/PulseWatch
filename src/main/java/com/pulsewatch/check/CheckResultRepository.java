package com.pulsewatch.check;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CheckResultRepository extends JpaRepository<CheckResult, Long> {

    /** Paginated check history for a monitor, newest first */
    Page<CheckResult> findByMonitorIdOrderByCheckedAtDesc(Long monitorId, Pageable pageable);

    /** Last N checks for a monitor — used by incident detection to count consecutive failures */
    List<CheckResult> findTopNByMonitorIdOrderByCheckedAtDesc(Long monitorId, Pageable pageable);

    /** Count checks in a time window — used by analytics */
    @Query("SELECT COUNT(c) FROM CheckResult c WHERE c.monitorId = :monitorId " +
           "AND c.checkedAt >= :from AND c.checkedAt <= :to")
    long countByMonitorIdAndPeriod(Long monitorId, LocalDateTime from, LocalDateTime to);

    /** Count successful checks in a time window — used for uptime calculation */
    @Query("SELECT COUNT(c) FROM CheckResult c WHERE c.monitorId = :monitorId " +
           "AND c.success = true AND c.checkedAt >= :from AND c.checkedAt <= :to")
    long countSuccessByMonitorIdAndPeriod(Long monitorId, LocalDateTime from, LocalDateTime to);
}
