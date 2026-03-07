package ltd.idcu.est.features.security.jwt;

import ltd.idcu.est.features.security.api.*;
import ltd.idcu.est.features.security.api.SecurityException;
import ltd.idcu.est.features.security.basic.DefaultAuthentication;
import ltd.idcu.est.features.security.basic.DefaultUser;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class JwtSecurityContext implements SecurityContext {
    
    private static final ConcurrentHashMap<Long, JwtSecurityContext> contextMap = new ConcurrentHashMap<>();
    
    private Authentication authentication;
    private final JwtTokenProvider tokenProvider;
    private final UserDetailsService userDetailsService;
    
    public JwtSecurityContext() {
        this.tokenProvider = null;
        this.userDetailsService = null;
    }
    
    public JwtSecurityContext(JwtTokenProvider tokenProvider, UserDetailsService userDetailsService) {
        this.tokenProvider = tokenProvider;
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
    
    public void setToken(String token) {
        if (token == null || token.isEmpty()) {
            this.authentication = null;
            return;
        }
        
        if (tokenProvider == null) {
            throw new SecurityException("TokenProvider not configured", "TOKEN_PROVIDER_NOT_CONFIGURED");
        }
        
        Optional<Token> validatedToken = tokenProvider.validateToken(token);
        if (validatedToken.isEmpty()) {
            this.authentication = null;
            return;
        }
        
        Token jwtToken = validatedToken.get();
        String username = jwtToken.getSubject();
        
        if (userDetailsService != null) {
            Optional<User> userOpt = userDetailsService.findByUsername(username);
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                
                DefaultAuthentication auth = new DefaultAuthentication(user, null);
                auth.setToken(token);
                auth.setRoles(jwtToken.getRoles());
                auth.setPermissions(jwtToken.getPermissions());
                auth.setAuthenticated(true);
                
                this.authentication = auth;
                return;
            }
        }
        
        DefaultUser user = new DefaultUser(username, null);
        
        DefaultAuthentication auth = new DefaultAuthentication(user, null);
        auth.setToken(token);
        auth.setRoles(jwtToken.getRoles());
        auth.setPermissions(jwtToken.getPermissions());
        auth.setAuthenticated(true);
        
        this.authentication = auth;
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
    
    public static JwtSecurityContext getCurrentContext() {
        return contextMap.computeIfAbsent(Thread.currentThread().getId(), id -> new JwtSecurityContext());
    }
    
    public static void setCurrentContext(JwtSecurityContext context) {
        contextMap.put(Thread.currentThread().getId(), context);
    }
    
    public static void clearCurrentContext() {
        contextMap.remove(Thread.currentThread().getId());
    }
    
    public static void clearAllContexts() {
        contextMap.clear();
    }
    
    public static JwtSecurityContext of(Authentication authentication) {
        JwtSecurityContext context = new JwtSecurityContext();
        context.setAuthentication(authentication);
        return context;
    }
    
    public static JwtSecurityContext of(String token, JwtTokenProvider tokenProvider, UserDetailsService userDetailsService) {
        JwtSecurityContext context = new JwtSecurityContext(tokenProvider, userDetailsService);
        context.setToken(token);
        return context;
    }
}
