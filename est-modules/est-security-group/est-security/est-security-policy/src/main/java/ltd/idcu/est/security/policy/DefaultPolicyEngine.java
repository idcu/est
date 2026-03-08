package ltd.idcu.est.security.policy;

import ltd.idcu.est.security.api.Policy;
import ltd.idcu.est.security.api.PolicyEffect;
import ltd.idcu.est.security.api.PolicyEngine;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class DefaultPolicyEngine implements PolicyEngine {
    
    private final Map<String, Policy> policies = new ConcurrentHashMap<>();
    
    @Override
    public boolean evaluate(String resource, String action, Map<String, Object> context) {
        Map<String, Object> fullContext = new HashMap<>(context != null ? context : Map.of());
        fullContext.put("resource", resource);
        fullContext.put("action", action);
        return evaluate(new ArrayList<>(policies.values()), fullContext);
    }
    
    @Override
    public boolean evaluate(List<Policy> policies, Map<String, Object> context) {
        List<Policy> sortedPolicies = policies.stream()
                .sorted(Comparator.comparingInt(Policy::getPriority).reversed())
                .collect(Collectors.toList());
        
        PolicyEffect finalDecision = PolicyEffect.ALLOW;
        boolean hasApplicablePolicy = false;
        
        for (Policy policy : sortedPolicies) {
            if (policy.evaluate(context)) {
                hasApplicablePolicy = true;
                if (policy.getEffect() == PolicyEffect.DENY) {
                    return false;
                }
                finalDecision = policy.getEffect();
            }
        }
        
        return hasApplicablePolicy ? finalDecision == PolicyEffect.ALLOW : false;
    }
    
    @Override
    public void addPolicy(Policy policy) {
        policies.put(policy.getId(), policy);
    }
    
    @Override
    public void removePolicy(String policyId) {
        policies.remove(policyId);
    }
    
    @Override
    public List<Policy> getPolicies() {
        return new ArrayList<>(policies.values());
    }
    
    @Override
    public void clearPolicies() {
        policies.clear();
    }
    
    public static DefaultPolicyEngine create() {
        return new DefaultPolicyEngine();
    }
}
