package ltd.idcu.est.security.basic;

import ltd.idcu.est.security.api.*;

import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

public class BasicAuthenticationProvider implements AuthenticationProvider {
    
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final SecurityStats stats;
    private final AtomicReference<SecurityConfig> config;
    
    public BasicAuthenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.stats = new SecurityStats();
        this.config = new AtomicReference<>(SecurityConfig.defaultConfig());
    }
    
    @Override
    public Authentication authenticate(Authentication authentication) throws ltd.idcu.est.security.api.SecurityException {
        stats.incrementAuthenticationCount();
        
        if (authentication == null) {
            stats.incrementFailedAuthentications();
            throw new AuthenticationException("Authentication cannot be null");
        }
        
        String username = null;
        if (authentication.getUser() != null) {
            username = authentication.getUser().getUsername();
        }
        
        if (username == null) {
            stats.incrementFailedAuthentications();
            throw new AuthenticationException("Username is required");
        }
        
        final String finalUsername = username;
        Object credentials = authentication.getCredentials();
        String password = credentials != null ? credentials.toString() : null;
        
        if (password == null || password.isEmpty()) {
            stats.incrementFailedAuthentications();
            throw new AuthenticationException("Password is required");
        }
        
        User user = userDetailsService.findByUsername(username)
                .orElseThrow(() -> {
                    stats.incrementFailedAuthentications();
                    return new AuthenticationException("User not found: " + finalUsername);
                });
        
        if (!user.isEnabled()) {
            stats.incrementFailedAuthentications();
            throw new AuthenticationException("User account is disabled");
        }
        
        if (!user.isAccountNonExpired()) {
            stats.incrementFailedAuthentications();
            throw new AuthenticationException("User account has expired");
        }
        
        if (!user.isAccountNonLocked()) {
            stats.incrementFailedAuthentications();
            throw new AuthenticationException("User account is locked");
        }
        
        if (!user.isCredentialsNonExpired()) {
            stats.incrementFailedAuthentications();
            throw new AuthenticationException("User credentials have expired");
        }
        
        String storedPassword = user.getPassword();
        if (storedPassword == null || !passwordEncoder.matches(password, storedPassword)) {
            stats.incrementFailedAuthentications();
            throw new AuthenticationException("Invalid password");
        }
        
        Set<Role> roles = userDetailsService.findRolesByUserId(user.getId());
        Set<Permission> permissions = userDetailsService.findPermissionsByUserId(user.getId());
        
        Set<String> roleNames = Set.copyOf(roles.stream().map(Role::getName).toList());
        Set<String> permissionNames = Set.copyOf(permissions.stream().map(Permission::getName).toList());
        
        DefaultAuthentication authenticated = new DefaultAuthentication(user, credentials);
        authenticated.setRoles(roleNames);
        authenticated.setPermissions(permissionNames);
        authenticated.setAuthenticated(true);
        
        stats.incrementSuccessfulAuthentications();
        
        return authenticated;
    }
    
    @Override
    public boolean supports(Class<?> authentication) {
        return DefaultAuthentication.class.isAssignableFrom(authentication);
    }
    
    public SecurityStats getStats() {
        return stats.snapshot();
    }
    
    public void setConfig(SecurityConfig config) {
        this.config.set(config);
    }
    
    public SecurityConfig getConfig() {
        return config.get();
    }
    
    public static BasicAuthenticationProvider of(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        return new BasicAuthenticationProvider(userDetailsService, passwordEncoder);
    }
}
