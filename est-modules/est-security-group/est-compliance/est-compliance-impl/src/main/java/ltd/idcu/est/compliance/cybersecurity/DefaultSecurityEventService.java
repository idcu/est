package ltd.idcu.est.compliance.cybersecurity;

import ltd.idcu.est.compliance.api.cybersecurity.SecurityEvent;
import ltd.idcu.est.compliance.api.cybersecurity.SecurityEventType;
import ltd.idcu.est.compliance.api.cybersecurity.SecurityEventSeverity;
import ltd.idcu.est.compliance.api.cybersecurity.SecurityEventService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class DefaultSecurityEventService implements SecurityEventService {

    private final Map<String, SecurityEvent> eventStore = new ConcurrentHashMap<>();

    @Override
    public SecurityEvent logEvent(SecurityEventType type, SecurityEventSeverity severity, String source, String message, Map<String, Object> details) {
        return logEvent(type, severity, source, message, details, null, null);
    }

    public SecurityEvent logEvent(SecurityEventType type, SecurityEventSeverity severity, String source, String message, Map<String, Object> details, String userId, String ipAddress) {
        DefaultSecurityEvent event = new DefaultSecurityEvent(
            type,
            severity,
            source,
            message,
            details,
            userId,
            ipAddress
        );
        eventStore.put(event.getId(), event);
        
        if (severity == SecurityEventSeverity.HIGH || severity == SecurityEventSeverity.CRITICAL) {
            alertOnEvent(event);
        }
        
        return event;
    }

    @Override
    public Optional<SecurityEvent> getEvent(String eventId) {
        return Optional.ofNullable(eventStore.get(eventId));
    }

    @Override
    public List<SecurityEvent> getEventsByType(SecurityEventType type) {
        return eventStore.values().stream()
            .filter(e -> e.getType() == type)
            .collect(Collectors.toList());
    }

    @Override
    public List<SecurityEvent> getEventsBySeverity(SecurityEventSeverity severity) {
        return eventStore.values().stream()
            .filter(e -> e.getSeverity() == severity)
            .collect(Collectors.toList());
    }

    @Override
    public List<SecurityEvent> getEventsByTimeRange(LocalDateTime start, LocalDateTime end) {
        return eventStore.values().stream()
            .filter(e -> !e.getTimestamp().isBefore(start) && !e.getTimestamp().isAfter(end))
            .collect(Collectors.toList());
    }

    @Override
    public List<SecurityEvent> getHighSeverityEvents() {
        return eventStore.values().stream()
            .filter(e -> e.getSeverity() == SecurityEventSeverity.HIGH || e.getSeverity() == SecurityEventSeverity.CRITICAL)
            .collect(Collectors.toList());
    }

    @Override
    public void alertOnEvent(SecurityEvent event) {
        System.out.println("[ALERT] Security Event: " + event.getSeverity() + " - " + event.getMessage());
    }
}
