package ltd.idcu.est.ratelimiter.api;

import java.util.List;
import java.util.Optional;

public interface DynamicRateLimitRegistry {
    
    void registerRule(DynamicRateLimitRule rule);
    
    void updateRule(DynamicRateLimitRule rule);
    
    void removeRule(String ruleId);
    
    Optional<DynamicRateLimitRule> getRule(String ruleId);
    
    List<DynamicRateLimitRule> getRulesForResource(String resource);
    
    List<DynamicRateLimitRule> getAllRules();
    
    List<DynamicRateLimitRule> getEnabledRules();
    
    void enableRule(String ruleId);
    
    void disableRule(String ruleId);
    
    RateLimiter createRateLimiter(String name, String resource);
    
    RateLimiter createRateLimiter(String name, String resource, String condition);
}
