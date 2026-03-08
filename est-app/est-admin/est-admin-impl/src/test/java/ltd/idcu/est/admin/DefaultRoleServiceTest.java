package ltd.idcu.est.admin;

import ltd.idcu.est.admin.api.AdminException;
import ltd.idcu.est.admin.api.Role;
import ltd.idcu.est.admin.api.RoleService;
import ltd.idcu.est.test.Assertions;
import ltd.idcu.est.test.annotation.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DefaultRoleServiceTest {

    @Test
    public void testCreateRole() {
        RoleService roleService = new DefaultRoleService();
        
        Set<String> permissions = new HashSet<>();
        permissions.add("role:read");
        
        Role role = roleService.createRole("testrole", "测试角色", permissions, false);
        
        Assertions.assertNotNull(role);
        Assertions.assertNotNull(role.getId());
        Assertions.assertEquals("testrole", role.getName());
        Assertions.assertEquals("测试角色", role.getDescription());
        Assertions.assertTrue(role.getPermissions().contains("role:read"));
        Assertions.assertFalse(role.isDefault());
    }

    @Test
    public void testCreateDuplicateRole() {
        RoleService roleService = new DefaultRoleService();
        
        Assertions.assertThrows(AdminException.class, () -> {
            roleService.createRole("admin", "重复角色", null, false);
        });
    }

    @Test
    public void testGetRole() {
        RoleService roleService = new DefaultRoleService();
        
        Role admin = roleService.getRoleByName("admin");
        Assertions.assertNotNull(admin);
        
        Role role = roleService.getRole(admin.getId());
        Assertions.assertNotNull(role);
        Assertions.assertEquals("admin", role.getName());
    }

    @Test
    public void testGetAllRoles() {
        RoleService roleService = new DefaultRoleService();
        
        List<Role> roles = roleService.getAllRoles();
        Assertions.assertNotNull(roles);
        Assertions.assertTrue(roles.size() >= 2);
    }

    @Test
    public void testUpdateRole() {
        RoleService roleService = new DefaultRoleService();
        
        Role user = roleService.getRoleByName("user");
        Assertions.assertNotNull(user);
        
        Set<String> newPermissions = new HashSet<>();
        newPermissions.add("user:read");
        newPermissions.add("user:write");
        
        Role updatedRole = roleService.updateRole(
            user.getId(),
            "updateduser",
            "更新后的用户角色",
            newPermissions,
            false
        );
        
        Assertions.assertNotNull(updatedRole);
        Assertions.assertEquals("updateduser", updatedRole.getName());
        Assertions.assertEquals("更新后的用户角色", updatedRole.getDescription());
        Assertions.assertTrue(updatedRole.getPermissions().contains("user:read"));
        Assertions.assertFalse(updatedRole.isDefault());
    }

    @Test
    public void testUpdateRoleWithDuplicateName() {
        RoleService roleService = new DefaultRoleService();
        
        Role user = roleService.getRoleByName("user");
        Assertions.assertNotNull(user);
        
        Assertions.assertThrows(AdminException.class, () -> {
            roleService.updateRole(user.getId(), "admin", null, null, false);
        });
    }

    @Test
    public void testDeleteRole() {
        RoleService roleService = new DefaultRoleService();
        
        Role role = roleService.createRole("todelete", "要删除的角色", null, false);
        Assertions.assertNotNull(role);
        
        roleService.deleteRole(role.getId());
        Role deletedRole = roleService.getRole(role.getId());
        Assertions.assertNull(deletedRole);
    }

    @Test
    public void testAssignPermissions() {
        RoleService roleService = new DefaultRoleService();
        
        Role role = roleService.createRole("permrole", "权限角色", null, false);
        Assertions.assertNotNull(role);
        Assertions.assertTrue(role.getPermissions().isEmpty());
        
        Set<String> permissions = new HashSet<>();
        permissions.add("perm1");
        permissions.add("perm2");
        
        roleService.assignPermissions(role.getId(), permissions);
        Role updatedRole = roleService.getRole(role.getId());
        
        Assertions.assertTrue(updatedRole.getPermissions().contains("perm1"));
        Assertions.assertTrue(updatedRole.getPermissions().contains("perm2"));
    }

    @Test
    public void testAssignMenus() {
        DefaultRoleService roleService = new DefaultRoleService();
        
        Role role = roleService.createRole("menurole", "菜单角色", null, false);
        Assertions.assertNotNull(role);
        
        Set<String> menuIds = new HashSet<>();
        menuIds.add("menu1");
        menuIds.add("menu2");
        
        roleService.assignMenus(role.getId(), menuIds);
        Set<String> assignedMenus = roleService.getRoleMenus(role.getId());
        
        Assertions.assertTrue(assignedMenus.contains("menu1"));
        Assertions.assertTrue(assignedMenus.contains("menu2"));
    }
}
