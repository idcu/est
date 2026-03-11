package ltd.idcu.est.compliance.api.cybersecurity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SecurityEventService {

    SecurityEvent logEvent(SecurityEventType type, SecurityEventSeverity severity, String source, String message, Map<String, Object> details);

    Optional<SecurityEvent> getEvent(String eventId);

    List<SecurityEvent> getEventsByType(SecurityEventType type);

    List<SecurityEvent> getEventsBySeverity(SecurityEventSeverity severity);

    List<SecurityEvent> getEventsByTimeRange(LocalDateTime start, LocalDateTime end);

    List<SecurityEvent> getHighSeverityEvents();

    void alertOnEvent(SecurityEvent event);
}
