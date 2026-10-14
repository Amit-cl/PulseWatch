package com.pulsewatch.notification;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationLogRepository extends JpaRepository<NotificationLog, Long> {

    /** Find all pending notifications that need to be sent */
    List<NotificationLog> findByStatus(String status);

    /** Check if a notification already exists for this incident + event type */
    boolean existsByIncidentIdAndEventType(Long incidentId, String eventType);
}
