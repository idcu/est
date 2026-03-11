package ltd.idcu.est.compliance.cybersecurity;

import ltd.idcu.est.compliance.api.cybersecurity.SecurityAuditLog;
import ltd.idcu.est.compliance.api.cybersecurity.SecurityAuditService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class DefaultSecurityAuditService implements SecurityAuditService {

    private final Map<String, SecurityAuditLog> auditLogStore = new ConcurrentHashMap<>();

    @Override
    public SecurityAuditLog logAccess(String userId, String action, String resource, String result, String ipAddress, String userAgent, Map<String, Object> additionalInfo, boolean sensitive) {
        DefaultSecurityAuditLog log = new DefaultSecurityAuditLog(
            userId,
            action,
            resource,
            result,
            ipAddress,
            userAgent,
            additionalInfo,
            sensitive
        );
        auditLogStore.put(log.getId(), log);
        return log;
    }

    @Override
    public Optional<SecurityAuditLog> getAuditLog(String logId) {
        return Optional.ofNullable(auditLogStore.get(logId));
    }

    @Override
    public List<SecurityAuditLog> getAuditLogsByUser(String userId) {
        return auditLogStore.values().stream()
            .filter(log -> log.getUserId() != null && log.getUserId().equals(userId))
            .collect(Collectors.toList());
    }

    @Override
    public List<SecurityAuditLog> getAuditLogsByTimeRange(LocalDateTime start, LocalDateTime end) {
        return auditLogStore.values().stream()
            .filter(log -> !log.getTimestamp().isBefore(start) && !log.getTimestamp().isAfter(end))
            .collect(Collectors.toList());
    }

    @Override
    public List<SecurityAuditLog> getSensitiveAuditLogs() {
        return auditLogStore.values().stream()
            .filter(SecurityAuditLog::isSensitive)
            .collect(Collectors.toList());
    }

    @Override
    public List<SecurityAuditLog> searchAuditLogs(String keyword) {
        String lowerKeyword = keyword.toLowerCase();
        return auditLogStore.values().stream()
            .filter(log -> 
                (log.getAction() != null && log.getAction().toLowerCase().contains(lowerKeyword)) ||
                (log.getResource() != null && log.getResource().toLowerCase().contains(lowerKeyword)) ||
                (log.getResult() != null && log.getResult().toLowerCase().contains(lowerKeyword))
            )
            .collect(Collectors.toList());
    }
}
