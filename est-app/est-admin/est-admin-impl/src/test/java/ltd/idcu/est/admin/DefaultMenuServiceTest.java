package ltd.idcu.est.admin;

import ltd.idcu.est.admin.api.AdminException;
import ltd.idcu.est.admin.api.Menu;
import ltd.idcu.est.admin.api.MenuService;
import ltd.idcu.est.test.Assertions;
import ltd.idcu.est.test.annotation.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DefaultMenuServiceTest {

    @Test
    public void testCreateMenu() {
        MenuService menuService = new DefaultMenuService();
        
        Set<String> permissions = new HashSet<>();
        permissions.add("menu:view");
        
        Menu menu = menuService.createMenu(
            null, 
            "系统管理", 
            "/system", 
            "SystemView", 
            "setting", 
            1, 
            Menu.MenuType.DIRECTORY, 
            true, 
            true, 
            permissions
        );
        
        Assertions.assertNotNull(menu);
        Assertions.assertNotNull(menu.getId());
        Assertions.assertEquals("系统管理", menu.getName());
        Assertions.assertEquals("/system", menu.getPath());
    }

    @Test
    public void testGetMenu() {
        MenuService menuService = new DefaultMenuService();
        
        Set<String> permissions = new HashSet<>();
        Menu menu = menuService.createMenu(
            null, 
            "用户管理", 
            "/system/user", 
            "UserView", 
            "user", 
            2, 
            Menu.MenuType.MENU, 
            true, 
            true, 
            permissions
        );
        
        Menu foundMenu = menuService.getMenu(menu.getId());
        
        Assertions.assertNotNull(foundMenu);
        Assertions.assertEquals(menu.getId(), foundMenu.getId());
        Assertions.assertEquals("用户管理", foundMenu.getName());
    }

    @Test
    public void testGetAllMenus() {
        MenuService menuService = new DefaultMenuService();
        
        Set<String> permissions = new HashSet<>();
        menuService.createMenu(null, "菜单1", "/menu1", "Menu1", "icon1", 1, Menu.MenuType.MENU, true, true, permissions);
        menuService.createMenu(null, "菜单2", "/menu2", "Menu2", "icon2", 2, Menu.MenuType.MENU, true, true, permissions);
        
        List<Menu> menus = menuService.getAllMenus();
        
        Assertions.assertNotNull(menus);
        Assertions.assertTrue(menus.size() >= 2);
    }

    @Test
    public void testUpdateMenu() {
        MenuService menuService = new DefaultMenuService();
        
        Set<String> permissions = new HashSet<>();
        Menu menu = menuService.createMenu(
            null, 
            "原始名称", 
            "/original", 
            "OriginalView", 
            "original", 
            1, 
            Menu.MenuType.MENU, 
            true, 
            true, 
            permissions
        );
        
        Set<String> newPermissions = new HashSet<>();
        newPermissions.add("menu:edit");
        
        Menu updatedMenu = menuService.updateMenu(
            menu.getId(), 
            null, 
            "更新后的名称", 
            "/updated", 
            "UpdatedView", 
            "updated", 
            5, 
            Menu.MenuType.DIRECTORY, 
            false, 
            false, 
            newPermissions
        );
        
        Assertions.assertNotNull(updatedMenu);
        Assertions.assertEquals("更新后的名称", updatedMenu.getName());
        Assertions.assertEquals("/updated", updatedMenu.getPath());
        Assertions.assertEquals(5, updatedMenu.getSort());
        Assertions.assertFalse(updatedMenu.isVisible());
        Assertions.assertFalse(updatedMenu.isCache());
    }

    @Test
    public void testDeleteMenu() {
        MenuService menuService = new DefaultMenuService();
        
        Set<String> permissions = new HashSet<>();
        Menu menu = menuService.createMenu(
            null, 
            "待删除菜单", 
            "/delete", 
            "DeleteView", 
            "delete", 
            1, 
            Menu.MenuType.MENU, 
            true, 
            true, 
            permissions
        );
        
        menuService.deleteMenu(menu.getId());
        
        Menu deletedMenu = menuService.getMenu(menu.getId());
        Assertions.assertNull(deletedMenu);
    }

    @Test
    public void testCreateSubMenu() {
        MenuService menuService = new DefaultMenuService();
        
        Set<String> permissions = new HashSet<>();
        Menu parentMenu = menuService.createMenu(
            null, 
            "父菜单", 
            "/parent", 
            "ParentView", 
            "parent", 
            1, 
            Menu.MenuType.DIRECTORY, 
            true, 
            true, 
            permissions
        );
        
        Menu subMenu = menuService.createMenu(
            parentMenu.getId(), 
            "子菜单", 
            "/parent/sub", 
            "SubView", 
            "sub", 
            1, 
            Menu.MenuType.MENU, 
            true, 
            true, 
            permissions
        );
        
        Assertions.assertNotNull(subMenu);
        Assertions.assertEquals(parentMenu.getId(), subMenu.getParentId());
    }

    @Test
    public void testMenuType() {
        MenuService menuService = new DefaultMenuService();
        
        Set<String> permissions = new HashSet<>();
        
        Menu directory = menuService.createMenu(null, "目录", "/dir", "DirView", "dir", 1, Menu.MenuType.DIRECTORY, true, true, permissions);
        Menu menu = menuService.createMenu(null, "菜单", "/menu", "MenuView", "menu", 2, Menu.MenuType.MENU, true, true, permissions);
        Menu button = menuService.createMenu(null, "按钮", "/button", "ButtonView", "button", 3, Menu.MenuType.BUTTON, true, true, permissions);
        
        Assertions.assertEquals(Menu.MenuType.DIRECTORY, directory.getType());
        Assertions.assertEquals(Menu.MenuType.MENU, menu.getType());
        Assertions.assertEquals(Menu.MenuType.BUTTON, button.getType());
    }

    @Test
    public void testMenuPermissions() {
        MenuService menuService = new DefaultMenuService();
        
        Set<String> permissions = new HashSet<>();
        permissions.add("menu:view");
        permissions.add("menu:edit");
        permissions.add("menu:delete");
        
        Menu menu = menuService.createMenu(
            null, 
            "权限菜单", 
            "/permission", 
            "PermissionView", 
            "permission", 
            1, 
            Menu.MenuType.MENU, 
            true, 
            true, 
            permissions
        );
        
        Assertions.assertNotNull(menu.getPermissions());
        Assertions.assertTrue(menu.getPermissions().contains("menu:view"));
        Assertions.assertTrue(menu.getPermissions().contains("menu:edit"));
        Assertions.assertTrue(menu.getPermissions().contains("menu:delete"));
        Assertions.assertEquals(3, menu.getPermissions().size());
    }

    @Test
    public void testUpdateNonExistentMenu() {
        MenuService menuService = new DefaultMenuService();
        
        Set<String> permissions = new HashSet<>();
        
        Assertions.assertThrows(AdminException.class, () -> {
            menuService.updateMenu(
                "non-existent-id", 
                null, 
                "名称", 
                "/path", 
                "View", 
                "icon", 
                1, 
                Menu.MenuType.MENU, 
                true, 
                true, 
                permissions
            );
        });
    }

    @Test
    public void testDeleteNonExistentMenu() {
        MenuService menuService = new DefaultMenuService();
        
        Assertions.assertThrows(AdminException.class, () -> {
            menuService.deleteMenu("non-existent-id");
        });
    }
}
