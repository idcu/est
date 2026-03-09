package ltd.idcu.est.circuitbreaker.api;

import java.util.List;
import java.util.Optional;

public interface CircuitBreakerRuleRegistry {
    
    void registerRule(CircuitBreakerRule rule);
    
    void updateRule(CircuitBreakerRule rule);
    
    void removeRule(String ruleId);
    
    Optional<CircuitBreakerRule> getRule(String ruleId);
    
    List<CircuitBreakerRule> getAllRules();
    
    List<CircuitBreakerRule> getEnabledRules();
    
    void enableRule(String ruleId);
    
    void disableRule(String ruleId);
    
    CircuitBreaker createCircuitBreaker(String name, String ruleId);
}
