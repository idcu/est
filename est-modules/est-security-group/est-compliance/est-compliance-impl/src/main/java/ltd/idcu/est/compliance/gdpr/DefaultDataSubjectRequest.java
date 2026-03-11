package ltd.idcu.est.compliance.gdpr;

import ltd.idcu.est.compliance.api.gdpr.DataSubjectRequest;
import ltd.idcu.est.compliance.api.gdpr.DataSubjectRight;
import ltd.idcu.est.compliance.api.gdpr.RequestStatus;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DefaultDataSubjectRequest implements DataSubjectRequest {

    private final String id;
    private final String subjectId;
    private final DataSubjectRight right;
    private RequestStatus status;
    private final LocalDateTime submittedAt;
    private LocalDateTime processedAt;
    private final Map<String, Object> requestData;
    private String notes;

    public DefaultDataSubjectRequest(String subjectId, DataSubjectRight right, Map<String, Object> requestData) {
        this.id = UUID.randomUUID().toString();
        this.subjectId = subjectId;
        this.right = right;
        this.status = RequestStatus.SUBMITTED;
        this.submittedAt = LocalDateTime.now();
        this.requestData = requestData != null ? new HashMap<>(requestData) : new HashMap<>();
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
    public DataSubjectRight getRight() {
        return right;
    }

    @Override
    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    @Override
    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    @Override
    public LocalDateTime getProcessedAt() {
        return processedAt;
    }

    public void setProcessedAt(LocalDateTime processedAt) {
        this.processedAt = processedAt;
    }

    @Override
    public Map<String, Object> getRequestData() {
        return new HashMap<>(requestData);
    }

    @Override
    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
