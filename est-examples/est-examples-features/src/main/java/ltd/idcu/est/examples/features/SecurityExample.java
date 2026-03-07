package ltd.idcu.est.examples.features;

import ltd.idcu.est.security.api.Authentication;
import ltd.idcu.est.security.api.PasswordEncoder;
import ltd.idcu.est.security.api.User;
import ltd.idcu.est.security.api.UserDetailsService;
import ltd.idcu.est.security.basic.BasicSecurity;
import ltd.idcu.est.security.basic.DefaultAuthentication;

public class SecurityExample {
    public static void main(String[] args) {
        // 创建密码编码�?
        PasswordEncoder passwordEncoder = BasicSecurity.passwordEncoder();
        
        // 编码密码
        String encodedPassword = passwordEncoder.encode("password123");
        System.out.println("Encoded password: " + encodedPassword);
        
        // 验证密码
        boolean matches = passwordEncoder.matches("password123", encodedPassword);
        System.out.println("Password matches: " + matches);
        
        // 创建用户详情服务
        UserDetailsService userDetailsService = BasicSecurity.inMemoryUserDetailsService();
        
        // 创建用户
        User user = BasicSecurity.newUser("admin", encodedPassword);
        userDetailsService.save(user);
        
        // 创建认证提供�?
        var authenticationProvider = BasicSecurity.authenticationProvider(userDetailsService, passwordEncoder);
        
        // 创建认证对象
        Authentication authRequest = new DefaultAuthentication(null, "password123") {
            @Override
            public User getUser() {
                return BasicSecurity.newUser("admin", "");
            }
        };
        
        // 认证
        Authentication authentication = authenticationProvider.authenticate(authRequest);
        
        System.out.println("Authentication successful: " + authentication.isAuthenticated());
    }
}