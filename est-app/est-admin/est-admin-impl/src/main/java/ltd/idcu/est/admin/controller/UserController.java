package ltd.idcu.est.admin.controller;

import ltd.idcu.est.admin.Admin;
import ltd.idcu.est.admin.api.ApiResponse;
import ltd.idcu.est.admin.api.User;
import ltd.idcu.est.admin.api.UserService;
import ltd.idcu.est.web.api.Request;
import ltd.idcu.est.web.api.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class UserController {
    
    private final UserService userService;
    
    public UserController() {
        this.userService = Admin.createUserService();
    }
    
    public void list(Request req, Response res) {
        try {
            List<User> users = userService.getAllUsers();
            List<Map<String, Object>> userList = users.stream()
                .map(this::toUserMap)
                .collect(Collectors.toList());
            res.json(ApiResponse.success(userList));
        } catch (Exception e) {
            res.setStatus(500);
            res.json(ApiResponse.error(e.getMessage()));
        }
    }
    
    public void get(Request req, Response res) {
        try {
            String id = req.getPathVariable("id");
            User user = userService.getUser(id);
            if (user == null) {
                res.setStatus(404);
                res.json(ApiResponse.notFound("User not found"));
                return;
            }
            res.json(ApiResponse.success(toUserMap(user)));
        } catch (Exception e) {
            res.setStatus(500);
            res.json(ApiResponse.error(e.getMessage()));
        }
    }
    
    public void create(Request req, Response res) {
        try {
            String username = req.getParameter("username");
            String password = req.getParameter("password");
            String email = req.getParameter("email");
            
            @SuppressWarnings("unchecked")
            Set<String> roles = (Set<String>) req.getAttribute("roles");
            @SuppressWarnings("unchecked")
            Set<String> permissions = (Set<String>) req.getAttribute("permissions");
            
            User user = userService.createUser(username, password, email, roles, permissions);
            res.setStatus(201);
            res.json(ApiResponse.success("User created successfully", toUserMap(user)));
        } catch (Exception e) {
            res.setStatus(400);
            res.json(ApiResponse.badRequest(e.getMessage()));
        }
    }
    
    public void update(Request req, Response res) {
        try {
            String id = req.getPathVariable("id");
            String email = req.getParameter("email");
            Boolean active = req.getBooleanParameter("active", true);
            
            @SuppressWarnings("unchecked")
            Set<String> roles = (Set<String>) req.getAttribute("roles");
            @SuppressWarnings("unchecked")
            Set<String> permissions = (Set<String>) req.getAttribute("permissions");
            
            User user = userService.updateUser(id, email, roles, permissions, active);
            res.json(ApiResponse.success("User updated successfully", toUserMap(user)));
        } catch (Exception e) {
            res.setStatus(400);
            res.json(ApiResponse.badRequest(e.getMessage()));
        }
    }
    
    public void delete(Request req, Response res) {
        try {
            String id = req.getPathVariable("id");
            userService.deleteUser(id);
            res.json(ApiResponse.success("User deleted successfully", null));
        } catch (Exception e) {
            res.setStatus(400);
            res.json(ApiResponse.badRequest(e.getMessage()));
        }
    }
    
    public void changePassword(Request req, Response res) {
        try {
            String id = req.getPathVariable("id");
            String oldPassword = req.getParameter("oldPassword");
            String newPassword = req.getParameter("newPassword");
            
            userService.changePassword(id, oldPassword, newPassword);
            res.json(ApiResponse.success("Password changed successfully", null));
        } catch (Exception e) {
            res.setStatus(400);
            res.json(ApiResponse.badRequest(e.getMessage()));
        }
    }
    
    public void resetPassword(Request req, Response res) {
        try {
            String id = req.getPathVariable("id");
            String newPassword = req.getParameter("newPassword");
            
            userService.resetPassword(id, newPassword);
            res.json(ApiResponse.success("Password reset successfully", null));
        } catch (Exception e) {
            res.setStatus(400);
            res.json(ApiResponse.badRequest(e.getMessage()));
        }
    }
    
    public void assignRoles(Request req, Response res) {
        try {
            String id = req.getPathVariable("id");
            @SuppressWarnings("unchecked")
            Set<String> roleIds = (Set<String>) req.getAttribute("roleIds");
            
            userService.assignRoles(id, roleIds);
            res.json(ApiResponse.success("Roles assigned successfully", null));
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
            
            userService.assignPermissions(id, permissions);
            res.json(ApiResponse.success("Permissions assigned successfully", null));
        } catch (Exception e) {
            res.setStatus(400);
            res.json(ApiResponse.badRequest(e.getMessage()));
        }
    }
    
    private Map<String, Object> toUserMap(User user) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", user.getId());
        map.put("username", user.getUsername());
        map.put("email", user.getEmail());
        map.put("roles", user.getRoles());
        map.put("permissions", user.getPermissions());
        map.put("active", user.isActive());
        map.put("createdAt", user.getCreatedAt());
        return map;
    }
}
