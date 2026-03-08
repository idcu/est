package ltd.idcu.est.security.api;

public interface AuthenticationProvider {
    
    Authentication authenticate(Authentication authentication) throws SecurityException;
    
    boolean supports(Class<?> authentication);
}
