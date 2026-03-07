package ltd.idcu.est.features.security.policy;

import ltd.idcu.est.features.security.api.AttributeBasedAuthorization;
import ltd.idcu.est.features.security.api.PolicyEngine;
import ltd.idcu.est.features.security.api.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DefaultAttributeBasedAuthorization implements AttributeBasedAuthorization {
    
    private final User user;
    private final Set<String> roles;
    private final Set<String> permissions;
    private final PolicyEngine policyEngine;
    private final Map<String, Object> subjectAttributes;
    private final Map<String, Map<String, Object>> resourceAttributes;
    private final Map<String, Object> environmentAttributes;
    
    public DefaultAttributeBasedAuthorization(User user, Set<String> roles, Set<String> permissions,
                                             PolicyEngine policyEngine,
                                             Map<String, Object> subjectAttributes,
                                             Map<String, Map<String, Object>> resourceAttributes,
                                             Map<String, Object> environmentAttributes) {
        this.user = user;
        this.roles = roles != null ? roles : Set.of();
        this.permissions = permissions != null ? permissions : Set.of();
        this.policyEngine = policyEngine;
        this.subjectAttributes = subjectAttributes != null ? new HashMap<>(subjectAttributes) : new HashMap<>();
        this.resourceAttributes = resourceAttributes != null ? new HashMap<>(resourceAttributes) : new HashMap<>();
        this.environmentAttributes = environmentAttributes != null ? new HashMap<>(environmentAttributes) : new HashMap<>();
    }
    
    @Override
    public boolean evaluate(String resource, String action, Map<String, Object> attributes) {
        Map<String, Object> context = new HashMap<>();
        context.putAll(subjectAttributes);
        context.putAll(environmentAttributes);
        
        Map<String, Object> resourceAttrs = getResourceAttributes(resource);
        if (resourceAttrs != null) {
            context.putAll(resourceAttrs);
        }
        
        if (attributes != null) {
            context.putAll(attributes);
        }
        
        context.put("user", user);
        context.put("roles", roles);
        context.put("permissions", permissions);
        
        return policyEngine.evaluate(resource, action, context);
    }
    
    @Override
    public Map<String, Object> getSubjectAttributes() {
        return new HashMap<>(subjectAttributes);
    }
    
    @Override
    public Map<String, Object> getResourceAttributes(String resource) {
        return resourceAttributes.get(resource);
    }
    
    @Override
    public Map<String, Object> getEnvironmentAttributes() {
        return new HashMap<>(environmentAttributes);
    }
    
    @Override
    public boolean hasRole(String roleName) {
        if (roleName == null || roleName.isEmpty()) {
            return false;
        }
        return roles.contains(roleName);
    }
    
    @Override
    public boolean hasAnyRole(String... roleNames) {
        if (roleNames == null || roleNames.length == 0) {
            return false;
        }
        for (String role : roleNames) {
            if (hasRole(role)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean hasAllRoles(String... roleNames) {
        if (roleNames == null || roleNames.length == 0) {
            return true;
        }
        for (String role : roleNames) {
            if (!hasRole(role)) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean hasPermission(String permissionName) {
        if (permissionName == null || permissionName.isEmpty()) {
            return false;
        }
        return permissions.contains(permissionName);
    }
    
    @Override
    public boolean hasAnyPermission(String... permissionNames) {
        if (permissionNames == null || permissionNames.length == 0) {
            return false;
        }
        for (String permission : permissionNames) {
            if (hasPermission(permission)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean hasAllPermissions(String... permissionNames) {
        if (permissionNames == null || permissionNames.length == 0) {
            return true;
        }
        for (String permission : permissionNames) {
            if (!hasPermission(permission)) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean hasPermission(String resource, String action) {
        if (resource == null || action == null) {
            return false;
        }
        String permission = resource + ":" + action;
        return hasPermission(permission);
    }
    
    @Override
    public boolean canAccess(String resource, String action) {
        return evaluate(resource, action, null) || hasRole("ADMIN") || hasRole("SUPER_ADMIN");
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private User user;
        private Set<String> roles = Set.of();
        private Set<String> permissions = Set.of();
        private PolicyEngine policyEngine;
        private Map<String, Object> subjectAttributes = new HashMap<>();
        private Map<String, Map<String, Object>> resourceAttributes = new HashMap<>();
        private Map<String, Object> environmentAttributes = new HashMap<>();
        
        public Builder user(User user) {
            this.user = user;
            return this;
        }
        
        public Builder roles(Set<String> roles) {
            this.roles = roles;
            return this;
        }
        
        public Builder permissions(Set<String> permissions) {
            this.permissions = permissions;
            return this;
        }
        
        public Builder policyEngine(PolicyEngine policyEngine) {
            this.policyEngine = policyEngine;
            return this;
        }
        
        public Builder subjectAttribute(String key, Object value) {
            this.subjectAttributes.put(key, value);
            return this;
        }
        
        public Builder resourceAttribute(String resource, String key, Object value) {
            this.resourceAttributes.computeIfAbsent(resource, k -> new HashMap<>()).put(key, value);
            return this;
        }
        
        public Builder environmentAttribute(String key, Object value) {
            this.environmentAttributes.put(key, value);
            return this;
        }
        
        public DefaultAttributeBasedAuthorization build() {
            if (policyEngine == null) {
                policyEngine = DefaultPolicyEngine.create();
            }
            return new DefaultAttributeBasedAuthorization(user, roles, permissions, policyEngine,
                    subjectAttributes, resourceAttributes, environmentAttributes);
        }
    }
}
