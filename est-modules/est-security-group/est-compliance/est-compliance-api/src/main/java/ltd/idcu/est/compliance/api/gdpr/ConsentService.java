package ltd.idcu.est.compliance.api.gdpr;

import java.util.List;
import java.util.Optional;

public interface ConsentService {

    Consent grantConsent(String subjectId, String consentType, ConsentRequest request);

    Consent revokeConsent(String consentId);

    List<Consent> getConsentsBySubject(String subjectId);

    Optional<Consent> getConsent(String consentId);

    boolean hasConsent(String subjectId, String consentType);

    List<Consent> getExpiredConsents();
}
