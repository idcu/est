package ltd.idcu.est.admin.e2e;

import ltd.idcu.est.admin.api.Menu;
import ltd.idcu.est.test.Assertions;
import ltd.idcu.est.test.annotation.Test;


import java.util.List;
import java.util.Set;

public class MenuManagementE2ETest extends AdminE2ETestBase {
    
    @Test(displayName = "测试完整菜单管理流程")
    public void testCompleteMenuManagementFlow() {
        System.out.println("=== 测试完整菜单管理流程 ===");
        setUp();
        
        testCreateMenu();
        testGetMenu();
        testGetAllMenus();
        testGetMenuTree();
        testGetUserMenus();
        testUpdateMenu();
        testDeleteMenu();
        
        System.out.println("�?完整菜单管理流程测试通过\n");
    }
    
    @Test(displayName = "测试创建菜单")
    public void testCreateMenu() {
        System.out.println("1. 测试创建菜单...");
        
        String name = "测试菜单";
        String path = "/test-menu";
        String component = "test-menu/index";
        String icon = "setting";
        int sort = 1;
        Menu.MenuType type = Menu.MenuType.MENU;
        boolean visible = true;
        boolean cache = true;
        Set<String> permissions = Set.of("system:menu:list");
        
        Menu menu = menuService.createMenu(null, name, path, component, icon, sort, type, visible, cache, permissions);
        
        Assertions.assertNotNull(menu, "Created menu should not be null");
        Assertions.assertNotNull(menu.getId(), "Menu ID should not be null");
        Assertions.assertEquals(name, menu.getName(), "Menu name should match");
        Assertions.assertEquals(path, menu.getPath(), "Menu path should match");
        Assertions.assertEquals(component, menu.getComponent(), "Menu component should match");
        Assertions.assertEquals(icon, menu.getIcon(), "Menu icon should match");
        Assertions.assertEquals(sort, menu.getSort(), "Menu sort should match");
        Assertions.assertEquals(type, menu.getType(), "Menu type should match");
        Assertions.assertTrue(menu.isVisible(), "Menu should be visible");
        Assertions.assertTrue(menu.isCache(), "Menu should be cacheable");
        Assertions.assertTrue(menu.getPermissions().containsAll(permissions), "Menu permissions should match");
        
        System.out.println("   �?创建菜单测试通过");
    }
    
    @Test(displayName = "测试获取菜单")
    public void testGetMenu() {
        System.out.println("2. 测试获取菜单...");
        
        Menu createdMenu = menuService.createMenu(
            null, "获取测试菜单", "/get-test-menu", 
            "get-test/index", "document", 2, 
            Menu.MenuType.MENU, true, true, Set.of()
        );
        
        Menu retrievedMenu = menuService.getMenu(createdMenu.getId());
        
        Assertions.assertNotNull(retrievedMenu, "Retrieved menu should not be null");
        assertMenuEquals(createdMenu, retrievedMenu);
        
        System.out.println("   �?获取菜单测试通过");
    }
    
    @Test
    @TestCase("测试获取所有菜�?)
    public void testGetAllMenus() {
        System.out.println("3. 测试获取所有菜�?..");
        
        menuService.createMenu(
            null, "测试菜单1", "/test-menu-1", 
            "test1/index", "user", 3, 
            Menu.MenuType.MENU, true, true, Set.of()
        );
        menuService.createMenu(
            null, "测试菜单2", "/test-menu-2", 
            "test2/index", "team", 4, 
            Menu.MenuType.MENU, true, true, Set.of()
        );
        
        List<Menu> menus = menuService.getAllMenus();
        
        Assertions.assertNotNull(menus, "Menu list should not be null");
        Assertions.assertTrue(menus.size() >= 2, "Should have at least 2 test menus");
        
        System.out.println("   �?获取所有菜单测试通过");
    }
    
    @Test
    @TestCase("测试获取菜单�?)
    public void testGetMenuTree() {
        System.out.println("4. 测试获取菜单�?..");
        
        List<Menu> menuTree = menuService.getMenuTree();
        
        Assertions.assertNotNull(menuTree, "Menu tree should not be null");
        Assertions.assertFalse(menuTree.isEmpty(), "Menu tree should not be empty");
        
        Menu rootMenu = menuTree.get(0);
        Assertions.assertNull(rootMenu.getParentId(), "Root menu should have no parent");
        
        System.out.println("   �?获取菜单树测试通过");
    }
    
    @Test(displayName = "测试获取用户菜单")
    public void testGetUserMenus() {
        System.out.println("5. 测试获取用户菜单...");
        
        List<Menu> userMenus = menuService.getUserMenus(adminUser);
        
        Assertions.assertNotNull(userMenus, "User menus should not be null");
        Assertions.assertFalse(userMenus.isEmpty(), "User menus should not be empty");
        
        System.out.println("   �?获取用户菜单测试通过");
    }
    
    @Test(displayName = "测试更新菜单")
    public void testUpdateMenu() {
        System.out.println("6. 测试更新菜单...");
        
        Menu createdMenu = menuService.createMenu(
            null, "更新测试菜单", "/update-test-menu", 
            "update-test/index", "edit", 5, 
            Menu.MenuType.MENU, true, true, Set.of("old:permission")
        );
        
        String newName = "已更新菜�?;
        String newPath = "/updated-menu";
        String newComponent = "updated/index";
        String newIcon = "check";
        int newSort = 10;
        Menu.MenuType newType = Menu.MenuType.DIRECTORY;
        boolean newVisible = false;
        boolean newCache = false;
        Set<String> newPermissions = Set.of("new:permission");
        
        Menu updatedMenu = menuService.updateMenu(
            createdMenu.getId(),
            createdMenu.getParentId(),
            newName,
            newPath,
            newComponent,
            newIcon,
            newSort,
            newType,
            newVisible,
            newCache,
            newPermissions
        );
        
        Assertions.assertNotNull(updatedMenu, "Updated menu should not be null");
        Assertions.assertEquals(newName, updatedMenu.getName(), "Menu name should be updated");
        Assertions.assertEquals(newPath, updatedMenu.getPath(), "Menu path should be updated");
        Assertions.assertEquals(newComponent, updatedMenu.getComponent(), "Menu component should be updated");
        Assertions.assertEquals(newIcon, updatedMenu.getIcon(), "Menu icon should be updated");
        Assertions.assertEquals(newSort, updatedMenu.getSort(), "Menu sort should be updated");
        Assertions.assertEquals(newType, updatedMenu.getType(), "Menu type should be updated");
        Assertions.assertFalse(updatedMenu.isVisible(), "Menu visibility should be updated");
        Assertions.assertFalse(updatedMenu.isCache(), "Menu cache should be updated");
        Assertions.assertTrue(updatedMenu.getPermissions().containsAll(newPermissions), "Menu permissions should be updated");
        
        System.out.println("   �?更新菜单测试通过");
    }
    
    @Test(displayName = "测试删除菜单")
    public void testDeleteMenu() {
        System.out.println("7. 测试删除菜单...");
        
        Menu createdMenu = menuService.createMenu(
            null, "删除测试菜单", "/delete-test-menu", 
            "delete-test/index", "delete", 6, 
            Menu.MenuType.MENU, true, true, Set.of()
        );
        
        Menu beforeDelete = menuService.getMenu(createdMenu.getId());
        Assertions.assertNotNull(beforeDelete, "Menu should exist before deletion");
        
        menuService.deleteMenu(createdMenu.getId());
        
        Menu afterDelete = menuService.getMenu(createdMenu.getId());
        Assertions.assertNull(afterDelete, "Menu should not exist after deletion");
        
        System.out.println("   �?删除菜单测试通过");
    }
    
    @Test(displayName = "测试嵌套菜单")
    public void testNestedMenus() {
        System.out.println("8. 测试嵌套菜单...");
        
        Menu parentMenu = menuService.createMenu(
            null, "父菜�?, "/parent-menu", 
            null, "folder", 7, 
            Menu.MenuType.DIRECTORY, true, true, Set.of()
        );
        
        Menu childMenu = menuService.createMenu(
            parentMenu.getId(), "子菜�?, "/parent-menu/child", 
            "child/index", "document", 1, 
            Menu.MenuType.MENU, true, true, Set.of()
        );
        
        Assertions.assertEquals(parentMenu.getId(), childMenu.getParentId(), "Child menu should reference parent");
        
        List<Menu> menuTree = menuService.getMenuTree();
        Menu foundParent = menuTree.stream()
            .filter(m -> m.getId().equals(parentMenu.getId()))
            .findFirst()
            .orElse(null);
        
        Assertions.assertNotNull(foundParent, "Parent menu should be in tree");
        Assertions.assertNotNull(foundParent.getChildren(), "Parent should have children");
        Assertions.assertTrue(foundParent.getChildren().size() >= 1, "Parent should have at least one child");
        
        System.out.println("   �?嵌套菜单测试通过");
    }
}
