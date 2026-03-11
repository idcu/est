package ltd.idcu.est.compliance.gdpr;

import ltd.idcu.est.compliance.api.gdpr.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class DefaultDataSubjectRequestService implements DataSubjectRequestService {

    private final Map<String, DataSubjectRequest> requestStore = new ConcurrentHashMap<>();

    @Override
    public DataSubjectRequest submitRequest(String subjectId, DataSubjectRight right, RequestData data) {
        DefaultDataSubjectRequest request = new DefaultDataSubjectRequest(
            subjectId,
            right,
            data.toMap()
        );
        requestStore.put(request.getId(), request);
        return request;
    }

    @Override
    public DataSubjectRequest processRequest(String requestId, RequestStatus status, String notes) {
        DefaultDataSubjectRequest request = (DefaultDataSubjectRequest) requestStore.get(requestId);
        if (request != null) {
            request.setStatus(status);
            request.setNotes(notes);
            if (status == RequestStatus.COMPLETED || status == RequestStatus.DENIED) {
                request.setProcessedAt(LocalDateTime.now());
            }
        }
        return request;
    }

    @Override
    public List<DataSubjectRequest> getRequestsBySubject(String subjectId) {
        return requestStore.values().stream()
            .filter(r -> r.getSubjectId().equals(subjectId))
            .collect(Collectors.toList());
    }

    @Override
    public Optional<DataSubjectRequest> getRequest(String requestId) {
        return Optional.ofNullable(requestStore.get(requestId));
    }

    @Override
    public List<DataSubjectRequest> getPendingRequests() {
        return requestStore.values().stream()
            .filter(r -> r.getStatus() == RequestStatus.SUBMITTED || r.getStatus() == RequestStatus.IN_PROGRESS)
            .collect(Collectors.toList());
    }

    @Override
    public List<DataSubjectRequest> getRequestsByStatus(RequestStatus status) {
        return requestStore.values().stream()
            .filter(r -> r.getStatus() == status)
            .collect(Collectors.toList());
    }
}
