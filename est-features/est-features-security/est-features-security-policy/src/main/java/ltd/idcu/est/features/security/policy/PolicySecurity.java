package ltd.idcu.est.features.security.policy;

import ltd.idcu.est.features.security.api.Policy;
import ltd.idcu.est.features.security.api.PolicyEffect;
import ltd.idcu.est.features.security.api.PolicyEngine;

import java.util.Map;
import java.util.function.Predicate;

public class PolicySecurity {
    
    private PolicySecurity() {
    }
    
    public static PolicyEngine createPolicyEngine() {
        return DefaultPolicyEngine.create();
    }
    
    public static Policy createPolicy(String id, String name, PolicyEffect effect,
                                       Predicate<Map<String, Object>> condition) {
        return DefaultPolicy.builder()
                .id(id)
                .name(name)
                .effect(effect)
                .condition(condition)
                .build();
    }
    
    public static Policy createPolicy(String id, String name, String description,
                                       PolicyEffect effect, Predicate<Map<String, Object>> condition,
                                       int priority) {
        return DefaultPolicy.builder()
                .id(id)
                .name(name)
                .description(description)
                .effect(effect)
                .condition(condition)
                .priority(priority)
                .build();
    }
    
    public static Predicate<Map<String, Object>> hasRole(String role) {
        return ctx -> {
            Object roles = ctx.get("roles");
            if (roles instanceof Iterable) {
                for (Object r : (Iterable<?>) roles) {
                    if (role.equals(r)) {
                        return true;
                    }
                }
            }
            return false;
        };
    }
    
    public static Predicate<Map<String, Object>> hasPermission(String permission) {
        return ctx -> {
            Object permissions = ctx.get("permissions");
            if (permissions instanceof Iterable) {
                for (Object p : (Iterable<?>) permissions) {
                    if (permission.equals(p)) {
                        return true;
                    }
                }
            }
            return false;
        };
    }
    
    public static Predicate<Map<String, Object>> resourceEquals(String resource) {
        return ctx -> resource.equals(ctx.get("resource"));
    }
    
    public static Predicate<Map<String, Object>> actionEquals(String action) {
        return ctx -> action.equals(ctx.get("action"));
    }
    
    public static Predicate<Map<String, Object>> subjectAttributeEquals(String key, Object value) {
        return ctx -> value.equals(ctx.get(key));
    }
}
