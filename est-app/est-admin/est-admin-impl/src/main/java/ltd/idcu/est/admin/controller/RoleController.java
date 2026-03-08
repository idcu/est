package ltd.idcu.est.admin.controller;

import ltd.idcu.est.admin.Admin;
import ltd.idcu.est.admin.api.ApiResponse;
import ltd.idcu.est.admin.api.Role;
import ltd.idcu.est.admin.api.RoleService;
import ltd.idcu.est.web.api.Request;
import ltd.idcu.est.web.api.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class RoleController {
    
    private final RoleService roleService;
    
    public RoleController() {
        this.roleService = Admin.createRoleService();
    }
    
    public void list(Request req, Response res) {
        try {
            List<Role> roles = roleService.getAllRoles();
            List<Map<String, Object>> roleList = roles.stream()
                .map(this::toRoleMap)
                .collect(Collectors.toList());
            res.json(ApiResponse.success(roleList));
        } catch (Exception e) {
            res.setStatus(500);
            res.json(ApiResponse.error(e.getMessage()));
        }
    }
    
    public void get(Request req, Response res) {
        try {
            String id = req.getPathVariable("id");
            Role role = roleService.getRole(id);
            if (role == null) {
                res.setStatus(404);
                res.json(ApiResponse.notFound("Role not found"));
                return;
            }
            res.json(ApiResponse.success(toRoleMap(role)));
        } catch (Exception e) {
            res.setStatus(500);
            res.json(ApiResponse.error(e.getMessage()));
        }
    }
    
    public void create(Request req, Response res) {
        try {
            String name = req.getParameter("name");
            String description = req.getParameter("description");
            Boolean isDefault = req.getBooleanParameter("isDefault", false);
            
            @SuppressWarnings("unchecked")
            Set<String> permissions = (Set<String>) req.getAttribute("permissions");
            
            Role role = roleService.createRole(name, description, permissions, isDefault);
            res.setStatus(201);
            res.json(ApiResponse.success("Role created successfully", toRoleMap(role)));
        } catch (Exception e) {
            res.setStatus(400);
            res.json(ApiResponse.badRequest(e.getMessage()));
        }
    }
    
    public void update(Request req, Response res) {
        try {
            String id = req.getPathVariable("id");
            String name = req.getParameter("name");
            String description = req.getParameter("description");
            Boolean isDefault = req.getBooleanParameter("isDefault", false);
            
            @SuppressWarnings("unchecked")
            Set<String> permissions = (Set<String>) req.getAttribute("permissions");
            
            Role role = roleService.updateRole(id, name, description, permissions, isDefault);
            res.json(ApiResponse.success("Role updated successfully", toRoleMap(role)));
        } catch (Exception e) {
            res.setStatus(400);
            res.json(ApiResponse.badRequest(e.getMessage()));
        }
    }
    
    public void delete(Request req, Response res) {
        try {
            String id = req.getPathVariable("id");
            roleService.deleteRole(id);
            res.json(ApiResponse.success("Role deleted successfully", null));
        } catch (Exception e) {
            res.setStatus(400);
            res.json(ApiResponse.badRequest(e.getMessage()));
        }
    }
    
    public void assignPermissions(Request req, Response res) {
        try {
            String id = req.getPathVariable("id");
            @SuppressWarnings("unchecked")
            Set<String> permissions = (Set<String>) req.getAttribute("permissions");
            
            roleService.assignPermissions(id, permissions);
            res.json(ApiResponse.success("Permissions assigned successfully", null));
        } catch (Exception e) {
            res.setStatus(400);
            res.json(ApiResponse.badRequest(e.getMessage()));
        }
    }
    
    public void assignMenus(Request req, Response res) {
        try {
            String id = req.getPathVariable("id");
            @SuppressWarnings("unchecked")
            Set<String> menuIds = (Set<String>) req.getAttribute("menuIds");
            
            roleService.assignMenus(id, menuIds);
            res.json(ApiResponse.success("Menus assigned successfully", null));
        } catch (Exception e) {
            res.setStatus(400);
            res.json(ApiResponse.badRequest(e.getMessage()));
        }
    }
    
    private Map<String, Object> toRoleMap(Role role) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", role.getId());
        map.put("name", role.getName());
        map.put("description", role.getDescription());
        map.put("permissions", role.getPermissions());
        map.put("isDefault", role.isDefault());
        return map;
    }
}
