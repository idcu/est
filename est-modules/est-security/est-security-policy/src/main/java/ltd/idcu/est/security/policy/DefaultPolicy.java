package ltd.idcu.est.security.policy;

import ltd.idcu.est.security.api.Policy;
import ltd.idcu.est.security.api.PolicyEffect;

import java.util.Map;
import java.util.function.Predicate;

public class DefaultPolicy implements Policy {
    
    private final String id;
    private final String name;
    private final String description;
    private final PolicyEffect effect;
    private final Predicate<Map<String, Object>> condition;
    private final int priority;
    
    public DefaultPolicy(String id, String name, String description, PolicyEffect effect,
                        Predicate<Map<String, Object>> condition, int priority) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.effect = effect;
        this.condition = condition;
        this.priority = priority;
    }
    
    @Override
    public String getId() {
        return id;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public String getDescription() {
        return description;
    }
    
    @Override
    public PolicyEffect getEffect() {
        return effect;
    }
    
    @Override
    public boolean evaluate(Map<String, Object> context) {
        return condition.test(context);
    }
    
    @Override
    public int getPriority() {
        return priority;
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private String id;
        private String name;
        private String description = "";
        private PolicyEffect effect = PolicyEffect.ALLOW;
        private Predicate<Map<String, Object>> condition = ctx -> true;
        private int priority = 0;
        
        public Builder id(String id) {
            this.id = id;
            return this;
        }
        
        public Builder name(String name) {
            this.name = name;
            return this;
        }
        
        public Builder description(String description) {
            this.description = description;
            return this;
        }
        
        public Builder effect(PolicyEffect effect) {
            this.effect = effect;
            return this;
        }
        
        public Builder condition(Predicate<Map<String, Object>> condition) {
            this.condition = condition;
            return this;
        }
        
        public Builder priority(int priority) {
            this.priority = priority;
            return this;
        }
        
        public DefaultPolicy build() {
            if (id == null || id.isBlank()) {
                throw new IllegalArgumentException("Policy id must not be blank");
            }
            if (name == null || name.isBlank()) {
                throw new IllegalArgumentException("Policy name must not be blank");
            }
            return new DefaultPolicy(id, name, description, effect, condition, priority);
        }
    }
}
