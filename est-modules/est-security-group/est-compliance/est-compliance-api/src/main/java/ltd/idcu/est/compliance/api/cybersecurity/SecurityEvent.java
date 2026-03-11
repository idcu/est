package ltd.idcu.est.compliance.api.cybersecurity;

import java.time.LocalDateTime;
import java.util.Map;

public interface SecurityEvent {

    String getId();

    SecurityEventType getType();

    SecurityEventSeverity getSeverity();

    String getSource();

    String getMessage();

    LocalDateTime getTimestamp();

    Map<String, Object> getDetails();

    String getUserId();

    String getIpAddress();
}
