package ltd.idcu.est.compliance.api.cybersecurity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface SecurityAuditService {

    SecurityAuditLog logAccess(String userId, String action, String resource, String result, String ipAddress, String userAgent, Map<String, Object> additionalInfo, boolean sensitive);

    Optional<SecurityAuditLog> getAuditLog(String logId);

    List<SecurityAuditLog> getAuditLogsByUser(String userId);

    List<SecurityAuditLog> getAuditLogsByTimeRange(LocalDateTime start, LocalDateTime end);

    List<SecurityAuditLog> getSensitiveAuditLogs();

    List<SecurityAuditLog> searchAuditLogs(String keyword);
}
