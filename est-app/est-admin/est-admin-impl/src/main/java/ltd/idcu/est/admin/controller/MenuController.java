package ltd.idcu.est.admin.controller;

import ltd.idcu.est.admin.Admin;
import ltd.idcu.est.admin.api.ApiResponse;
import ltd.idcu.est.admin.api.Menu;
import ltd.idcu.est.admin.api.MenuService;
import ltd.idcu.est.admin.api.RequirePermission;
import ltd.idcu.est.admin.api.User;
import ltd.idcu.est.web.api.Request;
import ltd.idcu.est.web.api.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MenuController {
    
    private final MenuService menuService;
    
    public MenuController() {
        this.menuService = Admin.createMenuService();
    }
    
    @RequirePermission("system:menu:list")
    public void list(Request req, Response res) {
        try {
            List<Menu> menus = menuService.getAllMenus();
            List<Map<String, Object>> menuList = new ArrayList<>();
            for (Menu menu : menus) {
                menuList.add(toMenuMap(menu));
            }
            res.json(ApiResponse.success(menuList));
        } catch (Exception e) {
            res.setStatus(500);
            res.json(ApiResponse.error(e.getMessage()));
        }
    }
    
    public void tree(Request req, Response res) {
        try {
            List<Menu> menus = menuService.getMenuTree();
            List<Map<String, Object>> menuTree = new ArrayList<>();
            for (Menu menu : menus) {
                menuTree.add(toMenuMapWithChildren(menu));
            }
            res.json(ApiResponse.success(menuTree));
        } catch (Exception e) {
            res.setStatus(500);
            res.json(ApiResponse.error(e.getMessage()));
        }
    }
    
    public void userMenus(Request req, Response res) {
        try {
            User currentUser = (User) req.getAttribute("currentUser");
            if (currentUser == null) {
                res.setStatus(401);
                res.json(ApiResponse.unauthorized("Not authenticated"));
                return;
            }
            
            List<Menu> menus = menuService.getUserMenus(currentUser);
            List<Map<String, Object>> menuTree = new ArrayList<>();
            for (Menu menu : menus) {
                menuTree.add(toMenuMapWithChildren(menu));
            }
            res.json(ApiResponse.success(menuTree));
        } catch (Exception e) {
            res.setStatus(500);
            res.json(ApiResponse.error(e.getMessage()));
        }
    }
    
    @RequirePermission("system:menu:query")
    public void get(Request req, Response res) {
        try {
            String id = req.getPathVariable("id");
            Menu menu = menuService.getMenu(id);
            if (menu == null) {
                res.setStatus(404);
                res.json(ApiResponse.notFound("Menu not found"));
                return;
            }
            res.json(ApiResponse.success(toMenuMap(menu)));
        } catch (Exception e) {
            res.setStatus(500);
            res.json(ApiResponse.error(e.getMessage()));
        }
    }
    
    @RequirePermission("system:menu:add")
    public void create(Request req, Response res) {
        try {
            String name = req.getParameter("name");
            String path = req.getParameter("path");
            String component = req.getParameter("component");
            String icon = req.getParameter("icon");
            String parentId = req.getParameter("parentId");
            int sort = req.getIntParameter("sort", 0);
            int type = req.getIntParameter("type", 0);
            boolean visible = req.getBooleanParameter("visible", true);
            boolean cache = req.getBooleanParameter("cache", true);
            
            @SuppressWarnings("unchecked")
            Set<String> permissions = (Set<String>) req.getAttribute("permissions");
            
            Menu.MenuType menuType = Menu.MenuType.values()[type];
            
            Menu menu = menuService.createMenu(parentId, name, path, component, icon, sort, menuType, visible, cache, permissions);
            res.setStatus(201);
            res.json(ApiResponse.success("Menu created successfully", toMenuMap(menu)));
        } catch (Exception e) {
            res.setStatus(400);
            res.json(ApiResponse.badRequest(e.getMessage()));
        }
    }
    
    @RequirePermission("system:menu:edit")
    public void update(Request req, Response res) {
        try {
            String id = req.getPathVariable("id");
            String name = req.getParameter("name");
            String path = req.getParameter("path");
            String component = req.getParameter("component");
            String icon = req.getParameter("icon");
            String parentId = req.getParameter("parentId");
            Integer sort = req.getParameter("sort") != null ? req.getIntParameter("sort", 0) : null;
            Integer type = req.getParameter("type") != null ? req.getIntParameter("type", 0) : null;
            Boolean visible = req.getParameter("visible") != null ? req.getBooleanParameter("visible", true) : null;
            Boolean cache = req.getParameter("cache") != null ? req.getBooleanParameter("cache", true) : null;
            
            @SuppressWarnings("unchecked")
            Set<String> permissions = (Set<String>) req.getAttribute("permissions");
            
            Menu.MenuType menuType = type != null ? Menu.MenuType.values()[type] : null;
            
            Menu menu = menuService.updateMenu(id, parentId, name, path, component, icon, sort != null ? sort : 0, menuType, visible != null ? visible : true, cache != null ? cache : true, permissions);
            res.json(ApiResponse.success("Menu updated successfully", toMenuMap(menu)));
        } catch (Exception e) {
            res.setStatus(400);
            res.json(ApiResponse.badRequest(e.getMessage()));
        }
    }
    
    @RequirePermission("system:menu:delete")
    public void delete(Request req, Response res) {
        try {
            String id = req.getPathVariable("id");
            menuService.deleteMenu(id);
            res.json(ApiResponse.success("Menu deleted successfully", null));
        } catch (Exception e) {
            res.setStatus(400);
            res.json(ApiResponse.badRequest(e.getMessage()));
        }
    }
    
    private Map<String, Object> toMenuMap(Menu menu) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", menu.getId());
        map.put("name", menu.getName());
        map.put("path", menu.getPath());
        map.put("component", menu.getComponent());
        map.put("icon", menu.getIcon());
        map.put("parentId", menu.getParentId());
        map.put("type", menu.getType().ordinal());
        map.put("sort", menu.getSort());
        map.put("permissions", menu.getPermissions());
        map.put("visible", menu.isVisible());
        map.put("cache", menu.isCache());
        return map;
    }
    
    private Map<String, Object> toMenuMapWithChildren(Menu menu) {
        Map<String, Object> map = toMenuMap(menu);
        List<Menu> children = menu.getChildren();
        if (children != null && !children.isEmpty()) {
            List<Map<String, Object>> childMaps = new ArrayList<>();
            for (Menu child : children) {
                childMaps.add(toMenuMapWithChildren(child));
            }
            map.put("children", childMaps);
        }
        return map;
    }
}
