package ltd.idcu.est.compliance.gdpr;

import ltd.idcu.est.compliance.api.gdpr.Consent;
import ltd.idcu.est.compliance.api.gdpr.ConsentRequest;
import ltd.idcu.est.compliance.api.gdpr.ConsentService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class DefaultConsentService implements ConsentService {

    private final Map<String, Consent> consentStore = new ConcurrentHashMap<>();

    @Override
    public Consent grantConsent(String subjectId, String consentType, ConsentRequest request) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiresAt = request.getExpiresAt() != null ? request.getExpiresAt() : now.plusYears(1);
        
        DefaultConsent consent = new DefaultConsent(
            subjectId,
            consentType,
            true,
            now,
            expiresAt,
            request.getMetadata()
        );
        
        consentStore.put(consent.getId(), consent);
        return consent;
    }

    @Override
    public Consent revokeConsent(String consentId) {
        Consent existing = consentStore.get(consentId);
        if (existing != null) {
            DefaultConsent revoked = new DefaultConsent(
                existing.getSubjectId(),
                existing.getConsentType(),
                false,
                existing.getGrantedAt(),
                existing.getExpiresAt(),
                existing.getMetadata()
            );
            consentStore.put(consentId, revoked);
            return revoked;
        }
        return null;
    }

    @Override
    public List<Consent> getConsentsBySubject(String subjectId) {
        return consentStore.values().stream()
            .filter(c -> c.getSubjectId().equals(subjectId))
            .collect(Collectors.toList());
    }

    @Override
    public Optional<Consent> getConsent(String consentId) {
        return Optional.ofNullable(consentStore.get(consentId));
    }

    @Override
    public boolean hasConsent(String subjectId, String consentType) {
        return consentStore.values().stream()
            .anyMatch(c -> c.getSubjectId().equals(subjectId) 
                && c.getConsentType().equals(consentType) 
                && c.isGranted() 
                && !c.isExpired());
    }

    @Override
    public List<Consent> getExpiredConsents() {
        return consentStore.values().stream()
            .filter(Consent::isExpired)
            .collect(Collectors.toList());
    }
}
