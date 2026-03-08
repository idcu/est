package ltd.idcu.est.security.api;

import java.util.List;
import java.util.Map;

public interface PolicyEngine {
    
    boolean evaluate(String resource, String action, Map<String, Object> context);
    
    boolean evaluate(List<Policy> policies, Map<String, Object> context);
    
    void addPolicy(Policy policy);
    
    void removePolicy(String policyId);
    
    List<Policy> getPolicies();
    
    void clearPolicies();
}
