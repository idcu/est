package ltd.idcu.est.admin.e2e;

import ltd.idcu.est.admin.api.User;
import ltd.idcu.est.test.Assertions;
import ltd.idcu.est.test.annotation.Test;
import ltd.idcu.est.test.annotation.DisplayName;

public class AuthE2ETest extends AdminE2ETestBase {
    
    @Test(displayName = "测试完整认证流程")
    public void testCompleteAuthFlow() {
        System.out.println("=== 测试完整认证流程 ===");
        setUp();
        
        testAdminLogin();
        testTokenGeneration();
        testTokenValidation();
        testTokenRefresh();
        testTokenInvalidation();
        testInvalidCredentials();
        
        System.out.println("�?完整认证流程测试通过\n");
    }
    
    @Test(displayName = "测试管理员登�?)
    public void testAdminLogin() {
        System.out.println("1. 测试管理员登�?..");
        
        User user = authService.authenticate("admin", "admin123");
        Assertions.assertNotNull(user, "Admin user should not be null");
        Assertions.assertEquals("admin", user.getUsername(), "Username should be admin");
        Assertions.assertTrue(user.getRoles().contains("admin"), "User should have admin role");
        Assertions.assertTrue(user.getPermissions().contains("*"), "User should have all permissions");
        
        System.out.println("   �?管理员登录测试通过");
    }
    
    @Test(displayName = "测试Token生成")
    public void testTokenGeneration() {
        System.out.println("2. 测试Token生成...");
        
        String token = authService.generateToken(adminUser);
        Assertions.assertNotNull(token, "Token should not be null");
        Assertions.assertFalse(token.isEmpty(), "Token should not be empty");
        
        System.out.println("   �?Token生成测试通过");
    }
    
    @Test(displayName = "测试Token验证")
    public void testTokenValidation() {
        System.out.println("3. 测试Token验证...");
        
        String token = authService.generateToken(adminUser);
        User validatedUser = authService.validateToken(token);
        
        Assertions.assertNotNull(validatedUser, "Validated user should not be null");
        Assertions.assertEquals(adminUser.getId(), validatedUser.getId(), "User IDs should match");
        Assertions.assertEquals(adminUser.getUsername(), validatedUser.getUsername(), "Usernames should match");
        
        System.out.println("   �?Token验证测试通过");
    }
    
    @Test(displayName = "测试Token刷新")
    public void testTokenRefresh() {
        System.out.println("4. 测试Token刷新...");
        
        String originalToken = authService.generateToken(adminUser);
        String refreshedToken = authService.refreshToken(originalToken);
        
        Assertions.assertNotNull(refreshedToken, "Refreshed token should not be null");
        Assertions.assertFalse(refreshedToken.isEmpty(), "Refreshed token should not be empty");
        
        User validatedUser = authService.validateToken(refreshedToken);
        Assertions.assertNotNull(validatedUser, "Refreshed token should be valid");
        
        System.out.println("   �?Token刷新测试通过");
    }
    
    @Test(displayName = "测试Token失效")
    public void testTokenInvalidation() {
        System.out.println("5. 测试Token失效...");
        
        String token = authService.generateToken(adminUser);
        
        User beforeInvalidation = authService.validateToken(token);
        Assertions.assertNotNull(beforeInvalidation, "Token should be valid before invalidation");
        
        authService.invalidateToken(token);
        
        User afterInvalidation = authService.validateToken(token);
        Assertions.assertNull(afterInvalidation, "Token should be invalid after invalidation");
        
        System.out.println("   �?Token失效测试通过");
    }
    
    @Test(displayName = "测试无效凭证")
    public void testInvalidCredentials() {
        System.out.println("6. 测试无效凭证...");
        
        try {
            authService.authenticate("admin", "wrongpassword");
            Assertions.fail("Should throw exception for wrong password");
        } catch (Exception e) {
            Assertions.assertNotNull(e.getMessage(), "Exception message should not be null");
        }
        
        try {
            authService.authenticate("nonexistentuser", "password");
            Assertions.fail("Should throw exception for nonexistent user");
        } catch (Exception e) {
            Assertions.assertNotNull(e.getMessage(), "Exception message should not be null");
        }
        
        System.out.println("   �?无效凭证测试通过");
    }
    
    @Test
    @TestCase("测试权限检�?)
    public void testPermissionCheck() {
        System.out.println("7. 测试权限检�?..");
        
        boolean hasAllPermission = authService.hasPermission(adminUser, "*");
        Assertions.assertTrue(hasAllPermission, "Admin should have all permissions");
        
        boolean hasUserPermission = authService.hasPermission(adminUser, "system:user:list");
        Assertions.assertTrue(hasUserPermission, "Admin should have user list permission");
        
        boolean hasRole = authService.hasRole(adminUser, "admin");
        Assertions.assertTrue(hasRole, "Admin should have admin role");
        
        boolean hasNonExistentRole = authService.hasRole(adminUser, "nonexistentrole");
        Assertions.assertFalse(hasNonExistentRole, "Should not have nonexistent role");
        
        System.out.println("   �?权限检查测试通过");
    }
}
