package ltd.idcu.est.compliance.gdpr;

import ltd.idcu.est.compliance.api.gdpr.Consent;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DefaultConsent implements Consent {

    private final String id;
    private final String subjectId;
    private final String consentType;
    private final boolean granted;
    private final LocalDateTime grantedAt;
    private final LocalDateTime expiresAt;
    private final Map<String, Object> metadata;

    public DefaultConsent(String subjectId, String consentType, boolean granted, LocalDateTime grantedAt, LocalDateTime expiresAt, Map<String, Object> metadata) {
        this.id = UUID.randomUUID().toString();
        this.subjectId = subjectId;
        this.consentType = consentType;
        this.granted = granted;
        this.grantedAt = grantedAt;
        this.expiresAt = expiresAt;
        this.metadata = metadata != null ? new HashMap<>(metadata) : new HashMap<>();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getSubjectId() {
        return subjectId;
    }

    @Override
    public String getConsentType() {
        return consentType;
    }

    @Override
    public boolean isGranted() {
        return granted;
    }

    @Override
    public LocalDateTime getGrantedAt() {
        return grantedAt;
    }

    @Override
    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    @Override
    public Map<String, Object> getMetadata() {
        return new HashMap<>(metadata);
    }

    @Override
    public boolean isExpired() {
        return expiresAt != null && LocalDateTime.now().isAfter(expiresAt);
    }
}
