package ltd.idcu.est.compliance.api.gdpr;

import java.time.LocalDateTime;
import java.util.Map;

public interface Consent {

    String getId();

    String getSubjectId();

    String getConsentType();

    boolean isGranted();

    LocalDateTime getGrantedAt();

    LocalDateTime getExpiresAt();

    Map<String, Object> getMetadata();

    boolean isExpired();
}
