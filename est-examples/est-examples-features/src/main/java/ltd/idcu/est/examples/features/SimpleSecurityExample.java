package ltd.idcu.est.examples.features;

import ltd.idcu.est.security.api.Authentication;
import ltd.idcu.est.security.api.PasswordEncoder;
import ltd.idcu.est.security.api.User;
import ltd.idcu.est.security.api.UserDetailsService;
import ltd.idcu.est.security.basic.BasicSecurity;
import ltd.idcu.est.security.basic.DefaultAuthentication;
import ltd.idcu.est.logging.api.Logger;
import ltd.idcu.est.logging.console.ConsoleLogs;

public class SimpleSecurityExample {
    
    private static final Logger logger = ConsoleLogs.getLogger(SimpleSecurityExample.class);
    
    public static void main(String[] args) {
        System.out.println("=== EST Security Authentication Example ===");
        
        passwordEncoderExample();
        basicAuthenticationExample();
        
        System.out.println("\n[X] All examples complete!");
    }
    
    private static void passwordEncoderExample() {
        System.out.println("\n--- Password Encryption ---");
        
        PasswordEncoder encoder = BasicSecurity.passwordEncoder();
        
        String plainPassword = "myPassword123";
        System.out.println("  Plain password: " + plainPassword);
        
        String encoded = encoder.encode(plainPassword);
        System.out.println("  Encoded: " + encoded);
        
        boolean matches = encoder.matches(plainPassword, encoded);
        System.out.println("  Password verification: " + matches);
        
        logger.info("Password encryption example complete");
    }
    
    private static void basicAuthenticationExample() {
        System.out.println("\n--- User Authentication ---");
        
        PasswordEncoder encoder = BasicSecurity.passwordEncoder();
        UserDetailsService userService = BasicSecurity.inMemoryUserDetailsService();
        
        User admin = BasicSecurity.newUser("admin", encoder.encode("admin123"));
        userService.save(admin);
        
        var authProvider = BasicSecurity.authenticationProvider(userService, encoder);
        
        Authentication authRequest = createAuthRequest("admin", "admin123");
        Authentication result = authProvider.authenticate(authRequest);
        
        System.out.println("  Login successful: " + result.isAuthenticated());
        
        logger.info("User authentication example complete");
    }
    
    private static Authentication createAuthRequest(String username, String password) {
        return new DefaultAuthentication(null, password) {
            @Override
            public User getUser() {
                return BasicSecurity.newUser(username, "");
            }
        };
    }
}
