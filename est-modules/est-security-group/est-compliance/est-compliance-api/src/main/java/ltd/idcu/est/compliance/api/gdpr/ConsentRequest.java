package ltd.idcu.est.compliance.api.gdpr;

import java.time.LocalDateTime;
import java.util.Map;

public interface ConsentRequest {

    LocalDateTime getExpiresAt();

    Map<String, Object> getMetadata();

    String getDescription();
}
