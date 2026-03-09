package ltd.idcu.est.circuitbreaker.api;

public class CircuitBreakerRule {
    
    private final String ruleId;
    private final String name;
    private final CircuitBreakerStrategy strategy;
    private final CircuitBreakerConfig config;
    private final boolean enabled;
    private final long createTime;
    private final long updateTime;
    
    public CircuitBreakerRule(String ruleId, String name, CircuitBreakerStrategy strategy,
                              CircuitBreakerConfig config, boolean enabled) {
        this.ruleId = ruleId;
        this.name = name;
        this.strategy = strategy;
        this.config = config;
        this.enabled = enabled;
        this.createTime = System.currentTimeMillis();
        this.updateTime = System.currentTimeMillis();
    }
    
    public String getRuleId() {
        return ruleId;
    }
    
    public String getName() {
        return name;
    }
    
    public CircuitBreakerStrategy getStrategy() {
        return strategy;
    }
    
    public CircuitBreakerConfig getConfig() {
        return config;
    }
    
    public boolean isEnabled() {
        return enabled;
    }
    
    public long getCreateTime() {
        return createTime;
    }
    
    public long getUpdateTime() {
        return updateTime;
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private String ruleId;
        private String name;
        private CircuitBreakerStrategy strategy = CircuitBreakerStrategy.COUNT_BASED;
        private CircuitBreakerConfig config = new CircuitBreakerConfig();
        private boolean enabled = true;
        
        public Builder ruleId(String ruleId) {
            this.ruleId = ruleId;
            return this;
        }
        
        public Builder name(String name) {
            this.name = name;
            return this;
        }
        
        public Builder strategy(CircuitBreakerStrategy strategy) {
            this.strategy = strategy;
            return this;
        }
        
        public Builder config(CircuitBreakerConfig config) {
            this.config = config;
            return this;
        }
        
        public Builder enabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }
        
        public CircuitBreakerRule build() {
            return new CircuitBreakerRule(ruleId, name, strategy, config, enabled);
        }
    }
}
