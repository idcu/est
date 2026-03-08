package ltd.idcu.est.admin;

import ltd.idcu.est.admin.api.AdminException;
import ltd.idcu.est.admin.api.User;
import ltd.idcu.est.admin.api.UserService;
import ltd.idcu.est.test.Assertions;
import ltd.idcu.est.test.annotation.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DefaultUserServiceTest {

    @Test
    public void testCreateUser() {
        UserService userService = new DefaultUserService();
        
        Set<String> roles = new HashSet<>();
        roles.add("user");
        
        Set<String> permissions = new HashSet<>();
        permissions.add("user:read");
        
        User user = userService.createUser("testuser", "password123", "test@example.com", roles, permissions);
        
        Assertions.assertNotNull(user);
        Assertions.assertNotNull(user.getId());
        Assertions.assertEquals("testuser", user.getUsername());
        Assertions.assertEquals("test@example.com", user.getEmail());
        Assertions.assertTrue(user.getRoles().contains("user"));
        Assertions.assertTrue(user.getPermissions().contains("user:read"));
        Assertions.assertTrue(user.isActive());
    }

    @Test
    public void testCreateDuplicateUser() {
        UserService userService = new DefaultUserService();
        
        Assertions.assertThrows(AdminException.class, () -> {
            userService.createUser("admin", "password", "admin2@example.com", null, null);
        });
    }

    @Test
    public void testGetUser() {
        UserService userService = new DefaultUserService();
        
        User admin = userService.getUserByUsername("admin");
        Assertions.assertNotNull(admin);
        
        User user = userService.getUser(admin.getId());
        Assertions.assertNotNull(user);
        Assertions.assertEquals("admin", user.getUsername());
    }

    @Test
    public void testGetAllUsers() {
        UserService userService = new DefaultUserService();
        
        List<User> users = userService.getAllUsers();
        Assertions.assertNotNull(users);
        Assertions.assertTrue(users.size() >= 1);
    }

    @Test
    public void testUpdateUser() {
        UserService userService = new DefaultUserService();
        
        User admin = userService.getUserByUsername("admin");
        Assertions.assertNotNull(admin);
        
        Set<String> newRoles = new HashSet<>();
        newRoles.add("superadmin");
        
        Set<String> newPermissions = new HashSet<>();
        newPermissions.add("*");
        
        User updatedUser = userService.updateUser(
            admin.getId(), 
            "newadmin@example.com", 
            newRoles, 
            newPermissions, 
            false
        );
        
        Assertions.assertNotNull(updatedUser);
        Assertions.assertEquals("newadmin@example.com", updatedUser.getEmail());
        Assertions.assertTrue(updatedUser.getRoles().contains("superadmin"));
        Assertions.assertFalse(updatedUser.isActive());
    }

    @Test
    public void testDeleteUser() {
        UserService userService = new DefaultUserService();
        
        User user = userService.createUser("todelete", "password", "delete@example.com", null, null);
        Assertions.assertNotNull(user);
        
        userService.deleteUser(user.getId());
        User deletedUser = userService.getUser(user.getId());
        Assertions.assertNull(deletedUser);
    }

    @Test
    public void testChangePassword() {
        UserService userService = new DefaultUserService();
        DefaultUserService defaultService = (DefaultUserService) userService;
        
        User admin = userService.getUserByUsername("admin");
        Assertions.assertNotNull(admin);
        
        Assertions.assertEquals("admin123", defaultService.getPassword(admin.getId()));
        
        userService.changePassword(admin.getId(), "admin123", "newpassword456");
        Assertions.assertEquals("newpassword456", defaultService.getPassword(admin.getId()));
    }

    @Test
    public void testChangePasswordWithWrongOldPassword() {
        UserService userService = new DefaultUserService();
        
        User admin = userService.getUserByUsername("admin");
        Assertions.assertNotNull(admin);
        
        Assertions.assertThrows(AdminException.class, () -> {
            userService.changePassword(admin.getId(), "wrongpassword", "newpassword");
        });
    }

    @Test
    public void testResetPassword() {
        UserService userService = new DefaultUserService();
        DefaultUserService defaultService = (DefaultUserService) userService;
        
        User admin = userService.getUserByUsername("admin");
        Assertions.assertNotNull(admin);
        
        userService.resetPassword(admin.getId(), "resetpassword789");
        Assertions.assertEquals("resetpassword789", defaultService.getPassword(admin.getId()));
    }

    @Test
    public void testAssignRoles() {
        UserService userService = new DefaultUserService();
        
        User user = userService.createUser("roleuser", "password", "role@example.com", null, null);
        Assertions.assertNotNull(user);
        Assertions.assertTrue(user.getRoles().isEmpty());
        
        Set<String> roles = new HashSet<>();
        roles.add("role1");
        roles.add("role2");
        
        userService.assignRoles(user.getId(), roles);
        User updatedUser = userService.getUser(user.getId());
        
        Assertions.assertTrue(updatedUser.getRoles().contains("role1"));
        Assertions.assertTrue(updatedUser.getRoles().contains("role2"));
    }

    @Test
    public void testAssignPermissions() {
        UserService userService = new DefaultUserService();
        
        User user = userService.createUser("permuser", "password", "perm@example.com", null, null);
        Assertions.assertNotNull(user);
        Assertions.assertTrue(user.getPermissions().isEmpty());
        
        Set<String> permissions = new HashSet<>();
        permissions.add("perm1");
        permissions.add("perm2");
        
        userService.assignPermissions(user.getId(), permissions);
        User updatedUser = userService.getUser(user.getId());
        
        Assertions.assertTrue(updatedUser.getPermissions().contains("perm1"));
        Assertions.assertTrue(updatedUser.getPermissions().contains("perm2"));
    }
}
