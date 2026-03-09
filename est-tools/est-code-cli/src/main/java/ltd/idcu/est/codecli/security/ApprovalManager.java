package ltd.idcu.est.codecli.security;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ApprovalManager {
    
    public enum ApprovalStatus {
        PENDING,
        APPROVED,
        REJECTED
    }
    
    public static class ApprovalRequest {
        private final String id;
        private final String toolName;
        private final Map<String, Object> args;
        private final String description;
        private ApprovalStatus status;
        private final long createdAt;
        private long resolvedAt;
        
        public ApprovalRequest(String id, String toolName, Map<String, Object> args, String description) {
            this.id = id;
            this.toolName = toolName;
            this.args = args;
            this.description = description;
            this.status = ApprovalStatus.PENDING;
            this.createdAt = System.currentTimeMillis();
        }
        
        public String getId() { return id; }
        public String getToolName() { return toolName; }
        public Map<String, Object> getArgs() { return new HashMap<>(args); }
        public String getDescription() { return description; }
        public ApprovalStatus getStatus() { return status; }
        public void setStatus(ApprovalStatus status) { this.status = status; }
        public long getCreatedAt() { return createdAt; }
        public long getResolvedAt() { return resolvedAt; }
        public void setResolvedAt(long resolvedAt) { this.resolvedAt = resolvedAt; }
    }
    
    private final Map<String, ApprovalRequest> pendingRequests = new ConcurrentHashMap<>();
    private final HitlSecurityPolicy policy;
    
    public ApprovalManager(HitlSecurityPolicy policy) {
        this.policy = policy;
    }
    
    public ApprovalRequest createRequest(String toolName, Map<String, Object> args, String description) {
        String id = UUID.randomUUID().toString();
        ApprovalRequest request = new ApprovalRequest(id, toolName, args, description);
        pendingRequests.put(id, request);
        return request;
    }
    
    public void approve(String requestId) {
        ApprovalRequest request = pendingRequests.get(requestId);
        if (request != null) {
            request.setStatus(ApprovalStatus.APPROVED);
            request.setResolvedAt(System.currentTimeMillis());
            pendingRequests.remove(requestId);
        }
    }
    
    public void reject(String requestId) {
        ApprovalRequest request = pendingRequests.get(requestId);
        if (request != null) {
            request.setStatus(ApprovalStatus.REJECTED);
            request.setResolvedAt(System.currentTimeMillis());
            pendingRequests.remove(requestId);
        }
    }
    
    public ApprovalRequest getRequest(String requestId) {
        return pendingRequests.get(requestId);
    }
    
    public List<ApprovalRequest> getPendingRequests() {
        return new ArrayList<>(pendingRequests.values());
    }
    
    public boolean requiresApproval(String toolName, Map<String, Object> args) {
        return policy.requiresApproval(toolName, args);
    }
    
    public HitlSecurityPolicy getPolicy() {
        return policy;
    }
}
