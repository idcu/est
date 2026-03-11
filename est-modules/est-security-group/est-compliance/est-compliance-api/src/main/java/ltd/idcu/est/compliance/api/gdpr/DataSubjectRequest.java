package ltd.idcu.est.compliance.api.gdpr;

import java.time.LocalDateTime;
import java.util.Map;

public interface DataSubjectRequest {

    String getId();

    String getSubjectId();

    DataSubjectRight getRight();

    RequestStatus getStatus();

    LocalDateTime getSubmittedAt();

    LocalDateTime getProcessedAt();

    Map<String, Object> getRequestData();

    String getNotes();
}
