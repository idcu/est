package ltd.idcu.est.examples.features;

import ltd.idcu.est.security.api.Authentication;
import ltd.idcu.est.security.api.PasswordEncoder;
import ltd.idcu.est.security.api.User;
import ltd.idcu.est.security.api.UserDetailsService;
import ltd.idcu.est.security.api.Role;
import ltd.idcu.est.security.api.Permission;
import ltd.idcu.est.security.basic.BasicSecurity;
import ltd.idcu.est.security.basic.DefaultAuthentication;
import ltd.idcu.est.security.jwt.JwtSecurity;
import ltd.idcu.est.security.apikey.ApiKeySecurity;
import ltd.idcu.est.logging.api.Logger;
import ltd.idcu.est.logging.console.ConsoleLogs;

import java.util.List;
import java.util.ArrayList;

public class CompleteSecurityExample {
    
    private static final Logger logger = ConsoleLogs.getLogger(CompleteSecurityExample.class);
    
    public static void main(String[] args) {
        System.out.println("=".repeat(70));
        System.out.println("EST Security Authentication Module - Complete Example");
        System.out.println("=".repeat(70));
        System.out.println("\nThis example demonstrates various features of the EST Security module:");
        System.out.println("  - Password Encoding (Prevent plaintext storage)");
        System.out.println("  - User Authentication (Login verification)");
        System.out.println("  - Multiple authentication methods (Basic, JWT, API Key)");
        System.out.println("  - Role and Permission Management");
        System.out.println("  - Integration with Web module to protect APIs");
        System.out.println();
        
        System.out.println("=".repeat(70));
        System.out.println("Part 1: Understanding the Importance of Security");
        System.out.println("=".repeat(70));
        System.out.println("\n[Why Do We Need Security Authentication?]");
        System.out.println("  - Protect user data from being stolen");
        System.out.println("  - Prevent unauthorized access");
        System.out.println("  - Passwords cannot be stored in plaintext (hackers will steal them!)");
        System.out.println("  - Different users have different permissions (Admin vs Regular User)\n");
        
        passwordEncoderExample();
        basicAuthenticationExample();
        jwtAuthenticationExample();
        apiKeyAuthenticationExample();
        roleAndPermissionExample();
        
        System.out.println("\n".repeat(2));
        System.out.println("=".repeat(70));
        System.out.println("[X] All security authentication examples completed!");
        System.out.println("=".repeat(70));
    }
    
    private static void passwordEncoderExample() {
        System.out.println("\n--- Approach 1: Password Encoder (Most Important!) ---");
        System.out.println("\n[What is a Password Encoder?]");
        System.out.println("  - Turns password into a string of random characters (hash)");
        System.out.println("  - Even if database is stolen, hackers can't see real passwords");
        System.out.println("  - Same password generates different hash each time (with salt)\n");
        
        PasswordEncoder passwordEncoder = BasicSecurity.passwordEncoder();
        
        System.out.println("Step 1: Create a plaintext password");
        String plainPassword = "mySecurePassword123";
        System.out.println("   Plaintext password: " + plainPassword);
        
        System.out.println("\nStep 2: Encode password (hash)");
        String encodedPassword1 = passwordEncoder.encode(plainPassword);
        System.out.println("   First encoding result: " + encodedPassword1);
        
        System.out.println("\nStep 3: Encode same password again");
        String encodedPassword2 = passwordEncoder.encode(plainPassword);
        System.out.println("   Second encoding result: " + encodedPassword2);
        
        System.out.println("\n   [Tip] Note: Two results are different! This is for better security!");
        
        System.out.println("\nStep 4: Verify if password is correct");
        boolean matches1 = passwordEncoder.matches(plainPassword, encodedPassword1);
        boolean matches2 = passwordEncoder.matches(plainPassword, encodedPassword2);
        boolean matchesWrong = passwordEncoder.matches("wrongPassword", encodedPassword1);
        
        System.out.println("   Verify correct password (First): " + matches1);
        System.out.println("   Verify correct password (Second): " + matches2);
        System.out.println("   Verify wrong password: " + matchesWrong);
        
        System.out.println("\nStep 5: Practical application scenarios");
        System.out.println("   User registration: Encode password, store in database");
        System.out.println("   User login: Encode input password, compare with database");
        
        logger.info("Password encoder example completed");
    }
    
    private static void basicAuthenticationExample() {
        System.out.println("\n--- Approach 2: Basic Authentication (Simplest Login Method) ---");
        System.out.println("\n[Basic Authentication Features]");
        System.out.println("  - Simplest authentication method");
        System.out.println("  - Suitable for internal systems, test environments");
        System.out.println("  - Production environment recommends using HTTPS\n");
        
        PasswordEncoder passwordEncoder = BasicSecurity.passwordEncoder();
        
        System.out.println("Step 1: Create user details service (store users)");
        UserDetailsService userDetailsService = BasicSecurity.inMemoryUserDetailsService();
        
        System.out.println("\nStep 2: Create users");
        String adminPassword = passwordEncoder.encode("admin123");
        User adminUser = BasicSecurity.newUser("admin", adminPassword);
        userDetailsService.save(adminUser);
        
        String userPassword = passwordEncoder.encode("user123");
        User normalUser = BasicSecurity.newUser("user", userPassword);
        userDetailsService.save(normalUser);
        
        System.out.println("   Created users: admin, user");
        
        System.out.println("\nStep 3: Create authentication provider");
        var authProvider = BasicSecurity.authenticationProvider(userDetailsService, passwordEncoder);
        
        System.out.println("\nStep 4: Test login (correct username and password)");
        Authentication adminAuthRequest = createAuthRequest("admin", "admin123");
        Authentication adminResult = authProvider.authenticate(adminAuthRequest);
        System.out.println("   Admin login successful: " + adminResult.isAuthenticated());
        
        System.out.println("\nStep 5: Test login (correct username, wrong password)");
        Authentication wrongPasswordRequest = createAuthRequest("admin", "wrongPassword");
        try {
            Authentication wrongResult = authProvider.authenticate(wrongPasswordRequest);
            System.out.println("   Wrong password login: " + wrongResult.isAuthenticated());
        } catch (Exception e) {
            System.out.println("   Wrong password login failed: " + e.getMessage());
        }
        
        System.out.println("\nStep 6: Test login (non-existent user)");
        Authentication notFoundRequest = createAuthRequest("nonexistent", "anyPassword");
        try {
            Authentication notFoundResult = authProvider.authenticate(notFoundRequest);
            System.out.println("   Non-existent user login: " + notFoundResult.isAuthenticated());
        } catch (Exception e) {
            System.out.println("   Non-existent user login failed: " + e.getMessage());
        }
        
        logger.info("Basic authentication example completed");
    }
    
    private static void jwtAuthenticationExample() {
        System.out.println("\n--- Approach 3: JWT Authentication (Common in Modern Web Apps) ---");
        System.out.println("\n[What is JWT?]");
        System.out.println("  - JSON Web Token, a stateless authentication method");
        System.out.println("  - Server doesn't need to store sessions, good scalability");
        System.out.println("  - Suitable for front-end/back-end separation, mobile apps\n");
        System.out.println("[JWT Workflow]");
        System.out.println("  1. User logs in successfully -> Server generates JWT Token");
        System.out.println("  2. Client stores Token (e.g., in browser LocalStorage)");
        System.out.println("  3. Each request carries Token");
        System.out.println("  4. Server verifies Token, confirms user identity\n");
        
        System.out.println("   [Tip] JWT is like a ticket, with it you can enter!");
        
        logger.info("JWT authentication example description completed");
    }
    
    private static void apiKeyAuthenticationExample() {
        System.out.println("\n--- Approach 4: API Key Authentication (Suitable for Machine-to-Machine) ---");
        System.out.println("\n[What is API Key?]");
        System.out.println("  - A string of keys, used to verify API caller identity");
        System.out.println("  - Suitable for server-to-server communication, third-party integration");
        System.out.println("  - No username/password needed, simple and efficient\n");
        System.out.println("[API Key Usage Scenarios]");
        System.out.println("  - Another service calling your API");
        System.out.println("  - Mobile App calling backend API");
        System.out.println("  - Third-party developers integrating your service\n");
        
        System.out.println("   [Tip] API Key is like a key, only those with the key can open the door!");
        
        logger.info("API Key authentication example description completed");
    }
    
    private static void roleAndPermissionExample() {
        System.out.println("\n--- Approach 5: Role and Permission Management ---");
        System.out.println("\n[Why Need Roles and Permissions?]");
        System.out.println("  - Not everyone can do everything");
        System.out.println("  - Admin can delete users, regular users cannot");
        System.out.println("  - Editor can write articles, guests can only view\n");
        
        System.out.println("[Common Roles]");
        System.out.println("  - ADMIN (Admin): All permissions");
        System.out.println("  - EDITOR (Editor): Can create, edit content");
        System.out.println("  - USER (Regular User): Can only view own content");
        System.out.println("  - GUEST (Guest): Can only view public content\n");
        
        System.out.println("[Permission Examples]");
        System.out.println("  - user:read (View user)");
        System.out.println("  - user:write (Create user)");
        System.out.println("  - user:delete (Delete user)");
        System.out.println("  - article:read (View article)");
        System.out.println("  - article:write (Write article)\n");
        
        System.out.println("Step 1: Define role and permission relationships");
        System.out.println("   ADMIN has: user:read, user:write, user:delete, article:read, article:write");
        System.out.println("   EDITOR has: article:read, article:write");
        System.out.println("   USER has: article:read");
        
        System.out.println("\nStep 2: Practical application scenarios");
        System.out.println("   User accesses /admin/delete-user -> Check if has user:delete permission");
        System.out.println("   User accesses /article/create -> Check if has article:write permission");
        
        logger.info("Role and permission management example completed");
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
