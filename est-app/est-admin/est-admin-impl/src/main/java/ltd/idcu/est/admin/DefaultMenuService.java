package ltd.idcu.est.admin;

import ltd.idcu.est.admin.api.AdminException;
import ltd.idcu.est.admin.api.Menu;
import ltd.idcu.est.admin.api.MenuService;
import ltd.idcu.est.admin.api.User;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class DefaultMenuService implements MenuService {
    
    private final Map<String, Menu> menusById;
    
    public DefaultMenuService() {
        this.menusById = new ConcurrentHashMap<>();
        initializeDefaultMenus();
    }
    
    private void initializeDefaultMenus() {
        Set<String> emptyPermissions = new HashSet<>();
        
        DefaultMenu systemMenu = new DefaultMenu(
            UUID.randomUUID().toString(),
            null,
            "系统管理",
            "/system",
            null,
            "setting",
            1,
            Menu.MenuType.DIRECTORY,
            true,
            true,
            emptyPermissions
        );
        menusById.put(systemMenu.getId(), systemMenu);
        
        DefaultMenu userMenu = new DefaultMenu(
            UUID.randomUUID().toString(),
            systemMenu.getId(),
            "用户管理",
            "/system/user",
            "system/user/index",
            "user",
            1,
            Menu.MenuType.MENU,
            true,
            true,
            new HashSet<>(Arrays.asList("system:user:list", "system:user:query"))
        );
        menusById.put(userMenu.getId(), userMenu);
        
        DefaultMenu roleMenu = new DefaultMenu(
            UUID.randomUUID().toString(),
            systemMenu.getId(),
            "角色管理",
            "/system/role",
            "system/role/index",
            "team",
            2,
            Menu.MenuType.MENU,
            true,
            true,
            new HashSet<>(Arrays.asList("system:role:list", "system:role:query"))
        );
        menusById.put(roleMenu.getId(), roleMenu);
        
        DefaultMenu menuMenu = new DefaultMenu(
            UUID.randomUUID().toString(),
            systemMenu.getId(),
            "菜单管理",
            "/system/menu",
            "system/menu/index",
            "menu",
            3,
            Menu.MenuType.MENU,
            true,
            true,
            new HashSet<>(Arrays.asList("system:menu:list", "system:menu:query"))
        );
        menusById.put(menuMenu.getId(), menuMenu);
        
        DefaultMenu deptMenu = new DefaultMenu(
            UUID.randomUUID().toString(),
            systemMenu.getId(),
            "部门管理",
            "/system/dept",
            "system/dept/index",
            "tree",
            4,
            Menu.MenuType.MENU,
            true,
            true,
            new HashSet<>(Arrays.asList("system:dept:list", "system:dept:query"))
        );
        menusById.put(deptMenu.getId(), deptMenu);
    }
    
    @Override
    public Menu createMenu(String parentId, String name, String path, String component, String icon,
                           int sort, Menu.MenuType type, boolean visible, boolean cache, Set<String> permissions) {
        String id = UUID.randomUUID().toString();
        DefaultMenu menu = new DefaultMenu(
            id,
            parentId,
            name,
            path,
            component,
            icon,
            sort,
            type,
            visible,
            cache,
            permissions != null ? permissions : new HashSet<>()
        );
        menusById.put(id, menu);
        return menu;
    }
    
    @Override
    public Menu getMenu(String id) {
        return menusById.get(id);
    }
    
    @Override
    public List<Menu> getAllMenus() {
        return new ArrayList<>(menusById.values());
    }
    
    @Override
    public List<Menu> getMenuTree() {
        Map<String, List<Menu>> childrenMap = new HashMap<>();
        List<Menu> roots = new ArrayList<>();
        
        for (Menu menu : menusById.values()) {
            if (menu.getParentId() == null) {
                roots.add(menu);
            } else {
                childrenMap.computeIfAbsent(menu.getParentId(), k -> new ArrayList<>()).add(menu);
            }
        }
        
        buildTree(roots, childrenMap);
        return roots;
    }
    
    private void buildTree(List<Menu> menus, Map<String, List<Menu>> childrenMap) {
        for (Menu menu : menus) {
            List<Menu> children = childrenMap.get(menu.getId());
            if (children != null) {
                for (Menu child : children) {
                    ((DefaultMenu) menu).addChild(child);
                }
                buildTree(children, childrenMap);
            }
        }
    }
    
    @Override
    public List<Menu> getUserMenus(User user) {
        Set<String> userPermissions = user.getPermissions();
        boolean isAdmin = userPermissions.contains("*");
        
        List<Menu> allMenus = new ArrayList<>();
        for (Menu menu : menusById.values()) {
            if (isAdmin || hasPermission(userPermissions, menu.getPermissions())) {
                allMenus.add(menu);
            }
        }
        
        Map<String, Menu> menuMap = allMenus.stream().collect(Collectors.toMap(Menu::getId, m -> m));
        Map<String, List<Menu>> childrenMap = new HashMap<>();
        List<Menu> roots = new ArrayList<>();
        
        for (Menu menu : allMenus) {
            if (menu.getParentId() == null) {
                roots.add(menu);
            } else if (menuMap.containsKey(menu.getParentId())) {
                childrenMap.computeIfAbsent(menu.getParentId(), k -> new ArrayList<>()).add(menu);
            }
        }
        
        buildTree(roots, childrenMap);
        return roots;
    }
    
    private boolean hasPermission(Set<String> userPermissions, Set<String> menuPermissions) {
        for (String perm : menuPermissions) {
            if (userPermissions.contains(perm)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public Menu updateMenu(String id, String parentId, String name, String path, String component,
                           String icon, int sort, Menu.MenuType type, boolean visible, boolean cache,
                           Set<String> permissions) {
        Menu existingMenu = menusById.get(id);
        if (existingMenu == null) {
            throw new AdminException("Menu not found: " + id);
        }
        
        DefaultMenu updatedMenu = new DefaultMenu(
            id,
            parentId != null ? parentId : existingMenu.getParentId(),
            name != null ? name : existingMenu.getName(),
            path != null ? path : existingMenu.getPath(),
            component != null ? component : existingMenu.getComponent(),
            icon != null ? icon : existingMenu.getIcon(),
            sort,
            type != null ? type : existingMenu.getType(),
            visible,
            cache,
            permissions != null ? permissions : existingMenu.getPermissions()
        );
        menusById.put(id, updatedMenu);
        return updatedMenu;
    }
    
    @Override
    public void deleteMenu(String id) {
        menusById.remove(id);
    }
}
