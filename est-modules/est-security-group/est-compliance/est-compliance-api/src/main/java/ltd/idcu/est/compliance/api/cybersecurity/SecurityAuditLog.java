package ltd.idcu.est.compliance.api.cybersecurity;

import java.time.LocalDateTime;
import java.util.Map;

public interface SecurityAuditLog {

    String getId();

    LocalDateTime getTimestamp();

    String getUserId();

    String getAction();

    String getResource();

    String getResult();

    String getIpAddress();

    String getUserAgent();

    Map<String, Object> getAdditionalInfo();

    boolean isSensitive();
}
