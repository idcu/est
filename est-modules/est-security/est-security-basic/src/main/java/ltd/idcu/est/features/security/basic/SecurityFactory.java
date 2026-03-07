package ltd.idcu.est.features.security.basic;

import ltd.idcu.est.features.security.api.*;

import java.util.Set;

public final class SecurityFactory {
    
    private SecurityFactory() {
    }
    
    public static DefaultUser newUser(String username, String password) {
        return DefaultUser.of(username, password);
    }
    
    public static DefaultUser.DefaultUserBuilder userBuilder() {
        return DefaultUser.builder();
    }
    
    public static DefaultRole newRole(String name) {
        return DefaultRole.of(name);
    }
    
    public static DefaultRole newRole(String name, String description) {
        return DefaultRole.of(name, description);
    }
    
    public static DefaultPermission newPermission(String name) {
        return DefaultPermission.of(name);
    }
    
    public static DefaultPermission newPermission(String name, String resource, String action) {
        return DefaultPermission.of(name, resource, action);
    }
    
    public static BCryptPasswordEncoder passwordEncoder() {
        return BCryptPasswordEncoder.create();
    }
    
    public static BCryptPasswordEncoder passwordEncoder(int strength) {
        return BCryptPasswordEncoder.create(strength);
    }
    
    public static InMemoryUserDetailsService inMemoryUserDetailsService() {
        return InMemoryUserDetailsService.create();
    }
    
    public static DefaultAuthorization authorization(User user, Set<String> roles, Set<String> permissions) {
        return DefaultAuthorization.of(user, roles, permissions);
    }
    
    public static DefaultAuthentication authentication(User user) {
        return DefaultAuthentication.of(user);
    }
    
    public static DefaultAuthentication authentication(User user, Object credentials) {
        return DefaultAuthentication.of(user, credentials);
    }
    
    public static DefaultAuthentication unauthenticated() {
        return DefaultAuthentication.unauthenticated();
    }
}
