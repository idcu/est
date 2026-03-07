package ltd.idcu.est.features.security.jwt;

import ltd.idcu.est.features.security.api.*;
import ltd.idcu.est.features.security.api.SecurityException;
import ltd.idcu.est.features.security.basic.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class JwtAuthenticationProvider implements AuthenticationProvider {
    
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final SecurityStats stats;
    private final AtomicReference<SecurityConfig> config;
    
    public JwtAuthenticationProvider(UserDetailsService userDetailsService, 
                                     PasswordEncoder passwordEncoder,
                                     JwtTokenProvider tokenProvider) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
        this.stats = new SecurityStats();
        this.config = new AtomicReference<>(SecurityConfig.defaultConfig());
    }
    
    public JwtAuthenticationProvider(UserDetailsService userDetailsService, 
                                     PasswordEncoder passwordEncoder,
                                     String secretKey) {
        this(userDetailsService, passwordEncoder, JwtTokenProvider.create(secretKey));
    }
    
    @Override
    public Authentication authenticate(Authentication authentication) throws SecurityException {
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
        
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", roleNames);
        claims.put("permissions", permissionNames);
        claims.put("user_id", user.getId());
        
        String token = tokenProvider.generateToken(user, claims);
        
        DefaultAuthentication authenticated = new DefaultAuthentication(user, credentials);
        authenticated.setToken(token);
        authenticated.setRoles(roleNames);
        authenticated.setPermissions(permissionNames);
        authenticated.setAuthenticated(true);
        
        stats.incrementSuccessfulAuthentications();
        
        return authenticated;
    }
    
    public Authentication authenticate(String token) throws SecurityException {
        stats.incrementAuthenticationCount();
        
        Optional<Token> validatedToken = tokenProvider.validateToken(token);
        if (validatedToken.isEmpty()) {
            stats.incrementFailedAuthentications();
            throw new AuthenticationException("Invalid or expired token");
        }
        
        Token jwtToken = validatedToken.get();
        String username = jwtToken.getSubject();
        
        User user = userDetailsService.findByUsername(username)
                .orElseThrow(() -> {
                    stats.incrementFailedAuthentications();
                    return new AuthenticationException("User not found: " + username);
                });
        
        if (!user.isEnabled()) {
            stats.incrementFailedAuthentications();
            throw new AuthenticationException("User account is disabled");
        }
        
        Set<String> roles = jwtToken.getRoles();
        Set<String> permissions = jwtToken.getPermissions();
        
        DefaultAuthentication authenticated = new DefaultAuthentication(user, null);
        authenticated.setToken(token);
        authenticated.setRoles(roles);
        authenticated.setPermissions(permissions);
        authenticated.setAuthenticated(true);
        
        stats.incrementSuccessfulAuthentications();
        
        return authenticated;
    }
    
    @Override
    public boolean supports(Class<?> authentication) {
        return DefaultAuthentication.class.isAssignableFrom(authentication) 
                || String.class.isAssignableFrom(authentication);
    }
    
    public String refreshToken(String token) {
        Optional<Token> validatedToken = tokenProvider.validateToken(token);
        if (validatedToken.isEmpty()) {
            throw new AuthenticationException("Invalid or expired token");
        }
        
        Token jwtToken = validatedToken.get();
        String username = jwtToken.getSubject();
        
        User user = userDetailsService.findByUsername(username)
                .orElseThrow(() -> new AuthenticationException("User not found: " + username));
        
        tokenProvider.invalidate(token);
        
        Set<Role> roles = userDetailsService.findRolesByUserId(user.getId());
        Set<Permission> permissions = userDetailsService.findPermissionsByUserId(user.getId());
        
        Set<String> roleNames = Set.copyOf(roles.stream().map(Role::getName).toList());
        Set<String> permissionNames = Set.copyOf(permissions.stream().map(Permission::getName).toList());
        
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", roleNames);
        claims.put("permissions", permissionNames);
        claims.put("user_id", user.getId());
        
        return tokenProvider.generateToken(user, claims);
    }
    
    public void invalidateToken(String token) {
        tokenProvider.invalidate(token);
    }
    
    public JwtTokenProvider getTokenProvider() {
        return tokenProvider;
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
    
    public static JwtAuthenticationProvider of(UserDetailsService userDetailsService, 
                                               PasswordEncoder passwordEncoder,
                                               JwtTokenProvider tokenProvider) {
        return new JwtAuthenticationProvider(userDetailsService, passwordEncoder, tokenProvider);
    }
    
    public static JwtAuthenticationProvider of(UserDetailsService userDetailsService, 
                                               PasswordEncoder passwordEncoder,
                                               String secretKey) {
        return new JwtAuthenticationProvider(userDetailsService, passwordEncoder, secretKey);
    }
}
