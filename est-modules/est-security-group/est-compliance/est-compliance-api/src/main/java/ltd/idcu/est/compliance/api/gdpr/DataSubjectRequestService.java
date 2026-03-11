package ltd.idcu.est.compliance.api.gdpr;

import java.util.List;
import java.util.Optional;

public interface DataSubjectRequestService {

    DataSubjectRequest submitRequest(String subjectId, DataSubjectRight right, RequestData data);

    DataSubjectRequest processRequest(String requestId, RequestStatus status, String notes);

    List<DataSubjectRequest> getRequestsBySubject(String subjectId);

    Optional<DataSubjectRequest> getRequest(String requestId);

    List<DataSubjectRequest> getPendingRequests();

    List<DataSubjectRequest> getRequestsByStatus(RequestStatus status);
}
