package ltd.idcu.est.ratelimiter.api;

import java.util.List;
import java.util.Map;

public class DynamicRateLimitRule {
    
    private final String ruleId;
    private final String name;
    private final String resource;
    private final RateLimitConfig config;
    private final Map<String, RateLimitConfig> conditionConfigs;
    private final boolean enabled;
    private final int priority;
    private final long createTime;
    private final long updateTime;
    
    public DynamicRateLimitRule(String ruleId, String name, String resource,
                               RateLimitConfig config, Map<String, RateLimitConfig> conditionConfigs,
                               boolean enabled, int priority) {
        this.ruleId = ruleId;
        this.name = name;
        this.resource = resource;
        this.config = config;
        this.conditionConfigs = conditionConfigs != null ? conditionConfigs : new java.util.HashMap<>();
        this.enabled = enabled;
        this.priority = priority;
        this.createTime = System.currentTimeMillis();
        this.updateTime = System.currentTimeMillis();
    }
    
    public String getRuleId() {
        return ruleId;
    }
    
    public String getName() {
        return name;
    }
    
    public String getResource() {
        return resource;
    }
    
    public RateLimitConfig getConfig() {
        return config;
    }
    
    public Map<String, RateLimitConfig> getConditionConfigs() {
        return new java.util.HashMap<>(conditionConfigs);
    }
    
    public boolean isEnabled() {
        return enabled;
    }
    
    public int getPriority() {
        return priority;
    }
    
    public long getCreateTime() {
        return createTime;
    }
    
    public long getUpdateTime() {
        return updateTime;
    }
    
    public RateLimitConfig getConfigForCondition(String condition) {
        return conditionConfigs.getOrDefault(condition, config);
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private String ruleId;
        private String name;
        private String resource;
        private RateLimitConfig config = new RateLimitConfig();
        private Map<String, RateLimitConfig> conditionConfigs = new java.util.HashMap<>();
        private boolean enabled = true;
        private int priority = 0;
        
        public Builder ruleId(String ruleId) {
            this.ruleId = ruleId;
            return this;
        }
        
        public Builder name(String name) {
            this.name = name;
            return this;
        }
        
        public Builder resource(String resource) {
            this.resource = resource;
            return this;
        }
        
        public Builder config(RateLimitConfig config) {
            this.config = config;
            return this;
        }
        
        public Builder addConditionConfig(String condition, RateLimitConfig conditionConfig) {
            this.conditionConfigs.put(condition, conditionConfig);
            return this;
        }
        
        public Builder enabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }
        
        public Builder priority(int priority) {
            this.priority = priority;
            return this;
        }
        
        public DynamicRateLimitRule build() {
            return new DynamicRateLimitRule(ruleId, name, resource, config, 
                                          conditionConfigs, enabled, priority);
        }
    }
}
