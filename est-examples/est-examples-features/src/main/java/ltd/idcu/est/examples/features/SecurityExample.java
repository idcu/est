package ltd.idcu.est.examples.features;

import ltd.idcu.est.security.api.Authentication;
import ltd.idcu.est.security.api.PasswordEncoder;
import ltd.idcu.est.security.api.User;
import ltd.idcu.est.security.api.UserDetailsService;
import ltd.idcu.est.security.basic.BasicSecurity;
import ltd.idcu.est.security.basic.DefaultAuthentication;

public class SecurityExample {
    public static void main(String[] args) {
        // Create password encoder
        PasswordEncoder passwordEncoder = BasicSecurity.passwordEncoder();
        
        // Encode password
        String encodedPassword = passwordEncoder.encode("password123");
        System.out.println("Encoded password: " + encodedPassword);
        
        // Verify password
        boolean matches = passwordEncoder.matches("password123", encodedPassword);
        System.out.println("Password matches: " + matches);
        
        // Create user details service
        UserDetailsService userDetailsService = BasicSecurity.inMemoryUserDetailsService();
        
        // Create user
        User user = BasicSecurity.newUser("admin", encodedPassword);
        userDetailsService.save(user);
        
        // Create authentication provider
        var authenticationProvider = BasicSecurity.authenticationProvider(userDetailsService, passwordEncoder);
        
        // Create authentication object
        Authentication authRequest = new DefaultAuthentication(null, "password123") {
            @Override
            public User getUser() {
                return BasicSecurity.newUser("admin", "");
            }
        };
        
        // Authenticate
        Authentication authentication = authenticationProvider.authenticate(authRequest);
        
        System.out.println("Authentication successful: " + authentication.isAuthenticated());
    }
}
