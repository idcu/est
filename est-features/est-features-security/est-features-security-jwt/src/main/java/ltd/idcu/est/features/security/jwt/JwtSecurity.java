package ltd.idcu.est.features.security.jwt;

import ltd.idcu.est.features.security.api.*;
import ltd.idcu.est.features.security.basic.*;

import java.util.*;

public final class JwtSecurity {
    
    private JwtSecurity() {
    }
    
    public static JwtTokenProvider tokenProvider() {
        return JwtTokenProvider.create();
    }
    
    public static JwtTokenProvider tokenProvider(String secretKey) {
        return JwtTokenProvider.create(secretKey);
    }
    
    public static JwtAuthenticationProvider authenticationProvider(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder,
            JwtTokenProvider tokenProvider) {
        return JwtAuthenticationProvider.of(userDetailsService, passwordEncoder, tokenProvider);
    }
    
    public static JwtAuthenticationProvider authenticationProvider(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder,
            String secretKey) {
        return JwtAuthenticationProvider.of(userDetailsService, passwordEncoder, secretKey);
    }
    
    public static JwtSecurityContext securityContext() {
        return new JwtSecurityContext();
    }
    
    public static JwtSecurityContext securityContext(
            JwtTokenProvider tokenProvider,
            UserDetailsService userDetailsService) {
        return new JwtSecurityContext(tokenProvider, userDetailsService);
    }
    
    public static JwtSecurityContext securityContext(
            String token,
            JwtTokenProvider tokenProvider,
            UserDetailsService userDetailsService) {
        return JwtSecurityContext.of(token, tokenProvider, userDetailsService);
    }
    
    public static JwtSecurityContext securityContext(Authentication authentication) {
        return JwtSecurityContext.of(authentication);
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
    
    public static String generateToken(User user, String secretKey) {
        JwtTokenProvider provider = tokenProvider(secretKey);
        return provider.generateToken(user);
    }
    
    public static String generateToken(User user, String secretKey, long expirationMillis) {
        JwtTokenProvider provider = tokenProvider(secretKey);
        return provider.generateToken(user, expirationMillis);
    }
    
    public static String generateToken(User user, String secretKey, Map<String, Object> claims) {
        JwtTokenProvider provider = tokenProvider(secretKey);
        return provider.generateToken(user, claims);
    }
    
    public static Optional<Token> validateToken(String token, String secretKey) {
        JwtTokenProvider provider = tokenProvider(secretKey);
        return provider.validateToken(token);
    }
}
