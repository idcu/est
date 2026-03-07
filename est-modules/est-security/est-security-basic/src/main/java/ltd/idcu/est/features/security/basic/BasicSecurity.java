package ltd.idcu.est.features.security.basic;

import ltd.idcu.est.features.security.api.*;

import java.util.Set;

public final class BasicSecurity {
    
    private BasicSecurity() {
    }
    
    public static DefaultUser newUser(String username, String password) {
        return SecurityFactory.newUser(username, password);
    }
    
    public static DefaultUser.DefaultUserBuilder userBuilder() {
        return SecurityFactory.userBuilder();
    }
    
    public static DefaultRole newRole(String name) {
        return SecurityFactory.newRole(name);
    }
    
    public static DefaultRole newRole(String name, String description) {
        return SecurityFactory.newRole(name, description);
    }
    
    public static DefaultPermission newPermission(String name) {
        return SecurityFactory.newPermission(name);
    }
    
    public static DefaultPermission newPermission(String name, String resource, String action) {
        return SecurityFactory.newPermission(name, resource, action);
    }
    
    public static BCryptPasswordEncoder passwordEncoder() {
        return SecurityFactory.passwordEncoder();
    }
    
    public static BCryptPasswordEncoder passwordEncoder(int strength) {
        return SecurityFactory.passwordEncoder(strength);
    }
    
    public static BasicCrypto crypto() {
        return new BasicCrypto();
    }
    
    public static BasicCrypto crypto(String secretKey) {
        return BasicCrypto.of(secretKey);
    }
    
    public static BasicCrypto crypto(String secretKey, String algorithm, int keyLength) {
        return BasicCrypto.of(secretKey, algorithm, keyLength);
    }
    
    public static InMemoryUserDetailsService inMemoryUserDetailsService() {
        return SecurityFactory.inMemoryUserDetailsService();
    }
    
    public static BasicAuthenticationProvider authenticationProvider(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {
        return BasicAuthenticationProvider.of(userDetailsService, passwordEncoder);
    }
    
    public static BasicTokenProvider tokenProvider() {
        return BasicTokenProvider.create();
    }
    
    public static BasicTokenProvider tokenProvider(String secretKey) {
        return BasicTokenProvider.create(secretKey);
    }
    
    public static DefaultSecurityContext securityContext() {
        return new DefaultSecurityContext();
    }
    
    public static DefaultSecurityContext securityContext(Authentication authentication) {
        return DefaultSecurityContext.of(authentication);
    }
    
    public static DefaultAuthorization authorization(User user, Set<String> roles, Set<String> permissions) {
        return SecurityFactory.authorization(user, roles, permissions);
    }
    
    public static DefaultAuthentication authentication(User user) {
        return SecurityFactory.authentication(user);
    }
    
    public static DefaultAuthentication authentication(User user, Object credentials) {
        return SecurityFactory.authentication(user, credentials);
    }
    
    public static DefaultAuthentication unauthenticated() {
        return SecurityFactory.unauthenticated();
    }
}
