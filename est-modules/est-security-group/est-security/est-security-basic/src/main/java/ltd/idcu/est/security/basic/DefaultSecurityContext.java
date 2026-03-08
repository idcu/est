package ltd.idcu.est.security.basic;

import ltd.idcu.est.security.api.*;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultSecurityContext implements SecurityContext {
    
    private static final ConcurrentHashMap<Long, SecurityContext> contextMap = new ConcurrentHashMap<>();
    
    private Authentication authentication;
    private final UserDetailsService userDetailsService;
    
    public DefaultSecurityContext() {
        this.userDetailsService = null;
    }
    
    public DefaultSecurityContext(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
    
    @Override
    public Optional<Authentication> getAuthentication() {
        return Optional.ofNullable(authentication);
    }
    
    @Override
    public void setAuthentication(Authentication authentication) {
        this.authentication = authentication;
    }
    
    @Override
    public Optional<User> getCurrentUser() {
        return getAuthentication()
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getUser);
    }
    
    @Override
    public String getCurrentUsername() {
        return getCurrentUser()
                .map(User::getUsername)
                .orElse("anonymous");
    }
    
    @Override
    public Set<String> getCurrentRoles() {
        return getAuthentication()
                .filter(Authentication::isAuthenticated)
                .map(auth -> {
                    if (auth instanceof DefaultAuthentication defaultAuth) {
                        return defaultAuth.getRoles();
                    }
                    return Set.<String>of();
                })
                .orElse(Set.of());
    }
    
    @Override
    public Set<String> getCurrentPermissions() {
        return getAuthentication()
                .filter(Authentication::isAuthenticated)
                .map(auth -> {
                    if (auth instanceof DefaultAuthentication defaultAuth) {
                        return defaultAuth.getPermissions();
                    }
                    return Set.<String>of();
                })
                .orElse(Set.of());
    }
    
    @Override
    public boolean hasRole(String roleName) {
        return getCurrentRoles().contains(roleName);
    }
    
    @Override
    public boolean hasPermission(String permissionName) {
        return getCurrentPermissions().contains(permissionName);
    }
    
    @Override
    public boolean isAuthenticated() {
        return getAuthentication()
                .map(Authentication::isAuthenticated)
                .orElse(false);
    }
    
    @Override
    public void clear() {
        this.authentication = null;
    }
    
    public static SecurityContext getCurrentContext() {
        return contextMap.computeIfAbsent(Thread.currentThread().getId(), id -> new DefaultSecurityContext());
    }
    
    public static void setCurrentContext(SecurityContext context) {
        contextMap.put(Thread.currentThread().getId(), context);
    }
    
    public static void clearCurrentContext() {
        contextMap.remove(Thread.currentThread().getId());
    }
    
    public static void clearAllContexts() {
        contextMap.clear();
    }
    
    public static DefaultSecurityContext of(Authentication authentication) {
        DefaultSecurityContext context = new DefaultSecurityContext();
        context.setAuthentication(authentication);
        return context;
    }
}
