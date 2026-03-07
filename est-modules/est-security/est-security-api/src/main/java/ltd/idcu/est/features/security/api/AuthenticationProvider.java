package ltd.idcu.est.features.security.api;

public interface AuthenticationProvider {
    
    Authentication authenticate(Authentication authentication) throws SecurityException;
    
    boolean supports(Class<?> authentication);
}
