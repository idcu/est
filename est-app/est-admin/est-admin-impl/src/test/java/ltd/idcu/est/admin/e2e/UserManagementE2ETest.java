package ltd.idcu.est.admin.e2e;

import ltd.idcu.est.admin.api.User;
import ltd.idcu.est.test.Assertions;
import ltd.idcu.est.test.annotation.Test;

import java.util.List;
import java.util.Set;

public class UserManagementE2ETest extends AdminE2ETestBase {
    
    @Test(displayName = "测试完整用户管理流程")
    public void testCompleteUserManagementFlow() {
        System.out.println("=== 测试完整用户管理流程 ===");
        setUp();
        
        testCreateUser();
        testGetUser();
        testGetAllUsers();
        testUpdateUser();
        testChangePassword();
        testAssignRoles();
        testAssignPermissions();
        testDeleteUser();
        
        System.out.println("�?完整用户管理流程测试通过\n");
    }
    
    @Test(displayName = "测试创建用户")
    public void testCreateUser() {
        System.out.println("1. 测试创建用户...");
        
        String username = "testuser001";
        String email = "testuser001@example.com";
        Set<String> roles = Set.of("user");
        Set<String> permissions = Set.of("system:user:list");
        
        User user = userService.createUser(username, "password123", email, roles, permissions);
        
        Assertions.assertNotNull(user, "Created user should not be null");
        Assertions.assertNotNull(user.getId(), "User ID should not be null");
        Assertions.assertEquals(username, user.getUsername(), "Username should match");
        Assertions.assertEquals(email, user.getEmail(), "Email should match");
        Assertions.assertTrue(user.getRoles().containsAll(roles), "Roles should match");
        Assertions.assertTrue(user.getPermissions().containsAll(permissions), "Permissions should match");
        Assertions.assertTrue(user.isActive(), "User should be active");
        
        System.out.println("   �?创建用户测试通过");
    }
    
    @Test(displayName = "测试获取用户")
    public void testGetUser() {
        System.out.println("2. 测试获取用户...");
        
        User createdUser = createTestUser("testuser002", "testuser002@example.com");
        
        User retrievedUser = userService.getUser(createdUser.getId());
        
        Assertions.assertNotNull(retrievedUser, "Retrieved user should not be null");
        assertUserEquals(createdUser, retrievedUser);
        
        System.out.println("   �?获取用户测试通过");
    }
    
    @Test
    @TestCase("测试获取所有用�?)
    public void testGetAllUsers() {
        System.out.println("3. 测试获取所有用�?..");
        
        createTestUser("testuser003", "testuser003@example.com");
        createTestUser("testuser004", "testuser004@example.com");
        
        List<User> users = userService.getAllUsers();
        
        Assertions.assertNotNull(users, "User list should not be null");
        Assertions.assertTrue(users.size() >= 3, "Should have at least 3 users (admin + 2 test users)");
        
        System.out.println("   �?获取所有用户测试通过");
    }
    
    @Test(displayName = "测试更新用户")
    public void testUpdateUser() {
        System.out.println("4. 测试更新用户...");
        
        User createdUser = createTestUser("testuser005", "testuser005@example.com");
        
        String newEmail = "updated@example.com";
        Set<String> newRoles = Set.of("user", "editor");
        Set<String> newPermissions = Set.of("system:user:list", "system:user:edit");
        
        User updatedUser = userService.updateUser(
            createdUser.getId(), 
            newEmail, 
            newRoles, 
            newPermissions, 
            true
        );
        
        Assertions.assertNotNull(updatedUser, "Updated user should not be null");
        Assertions.assertEquals(newEmail, updatedUser.getEmail(), "Email should be updated");
        Assertions.assertTrue(updatedUser.getRoles().containsAll(newRoles), "Roles should be updated");
        Assertions.assertTrue(updatedUser.getPermissions().containsAll(newPermissions), "Permissions should be updated");
        
        System.out.println("   �?更新用户测试通过");
    }
    
    @Test(displayName = "测试修改密码")
    public void testChangePassword() {
        System.out.println("5. 测试修改密码...");
        
        User createdUser = createTestUser("testuser006", "testuser006@example.com");
        
        try {
            userService.changePassword(createdUser.getId(), "password123", "newpassword123");
            System.out.println("   �?修改密码测试通过");
        } catch (Exception e) {
            Assertions.fail("Password change should not throw exception: " + e.getMessage());
        }
    }
    
    @Test(displayName = "测试重置密码")
    public void testResetPassword() {
        System.out.println("6. 测试重置密码...");
        
        User createdUser = createTestUser("testuser007", "testuser007@example.com");
        
        try {
            userService.resetPassword(createdUser.getId(), "resetpassword123");
            System.out.println("   �?重置密码测试通过");
        } catch (Exception e) {
            Assertions.fail("Password reset should not throw exception: " + e.getMessage());
        }
    }
    
    @Test(displayName = "测试分配角色")
    public void testAssignRoles() {
        System.out.println("7. 测试分配角色...");
        
        User createdUser = createTestUser("testuser008", "testuser008@example.com");
        Set<String> newRoles = Set.of("admin", "editor", "viewer");
        
        try {
            userService.assignRoles(createdUser.getId(), newRoles);
            
            User updatedUser = userService.getUser(createdUser.getId());
            Assertions.assertTrue(updatedUser.getRoles().containsAll(newRoles), "Roles should be assigned");
            
            System.out.println("   �?分配角色测试通过");
        } catch (Exception e) {
            Assertions.fail("Role assignment should not throw exception: " + e.getMessage());
        }
    }
    
    @Test(displayName = "测试分配权限")
    public void testAssignPermissions() {
        System.out.println("8. 测试分配权限...");
        
        User createdUser = createTestUser("testuser009", "testuser009@example.com");
        Set<String> newPermissions = Set.of("system:user:*", "system:role:*", "system:menu:*");
        
        try {
            userService.assignPermissions(createdUser.getId(), newPermissions);
            
            User updatedUser = userService.getUser(createdUser.getId());
            Assertions.assertTrue(updatedUser.getPermissions().containsAll(newPermissions), "Permissions should be assigned");
            
            System.out.println("   �?分配权限测试通过");
        } catch (Exception e) {
            Assertions.fail("Permission assignment should not throw exception: " + e.getMessage());
        }
    }
    
    @Test(displayName = "测试删除用户")
    public void testDeleteUser() {
        System.out.println("9. 测试删除用户...");
        
        User createdUser = createTestUser("testuser010", "testuser010@example.com");
        
        User beforeDelete = userService.getUser(createdUser.getId());
        Assertions.assertNotNull(beforeDelete, "User should exist before deletion");
        
        userService.deleteUser(createdUser.getId());
        
        User afterDelete = userService.getUser(createdUser.getId());
        Assertions.assertNull(afterDelete, "User should not exist after deletion");
        
        System.out.println("   �?删除用户测试通过");
    }
    
    @Test(displayName = "测试用户激�?停用")
    public void testUserActivation() {
        System.out.println("10. 测试用户激�?停用...");
        
        User createdUser = createTestUser("testuser011", "testuser011@example.com");
        Assertions.assertTrue(createdUser.isActive(), "User should be active initially");
        
        User deactivatedUser = userService.updateUser(
            createdUser.getId(),
            createdUser.getEmail(),
            createdUser.getRoles(),
            createdUser.getPermissions(),
            false
        );
        Assertions.assertFalse(deactivatedUser.isActive(), "User should be deactivated");
        
        User reactivatedUser = userService.updateUser(
            deactivatedUser.getId(),
            deactivatedUser.getEmail(),
            deactivatedUser.getRoles(),
            deactivatedUser.getPermissions(),
            true
        );
        Assertions.assertTrue(reactivatedUser.isActive(), "User should be reactivated");
        
        System.out.println("   �?用户激�?停用测试通过");
    }
}
