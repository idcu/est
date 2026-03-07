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
        System.out.println("=== EST 安全认证示例 ===");
        
        passwordEncoderExample();
        basicAuthenticationExample();
        
        System.out.println("\n�?所有示例完成！");
    }
    
    private static void passwordEncoderExample() {
        System.out.println("\n--- 密码加密 ---");
        
        PasswordEncoder encoder = BasicSecurity.passwordEncoder();
        
        String plainPassword = "myPassword123";
        System.out.println("  明文密码: " + plainPassword);
        
        String encoded = encoder.encode(plainPassword);
        System.out.println("  加密�? " + encoded);
        
        boolean matches = encoder.matches(plainPassword, encoded);
        System.out.println("  密码验证: " + matches);
        
        logger.info("密码加密示例完成");
    }
    
    private static void basicAuthenticationExample() {
        System.out.println("\n--- 用户认证 ---");
        
        PasswordEncoder encoder = BasicSecurity.passwordEncoder();
        UserDetailsService userService = BasicSecurity.inMemoryUserDetailsService();
        
        User admin = BasicSecurity.newUser("admin", encoder.encode("admin123"));
        userService.save(admin);
        
        var authProvider = BasicSecurity.authenticationProvider(userService, encoder);
        
        Authentication authRequest = createAuthRequest("admin", "admin123");
        Authentication result = authProvider.authenticate(authRequest);
        
        System.out.println("  登录成功: " + result.isAuthenticated());
        
        logger.info("用户认证示例完成");
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
