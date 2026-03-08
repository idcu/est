package ltd.idcu.est.admin.controller;

import ltd.idcu.est.admin.Admin;
import ltd.idcu.est.admin.api.ApiResponse;
import ltd.idcu.est.admin.api.Department;
import ltd.idcu.est.admin.api.DepartmentService;
import ltd.idcu.est.web.api.Request;
import ltd.idcu.est.web.api.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DepartmentController {
    
    private final DepartmentService departmentService;
    
    public DepartmentController() {
        this.departmentService = Admin.createDepartmentService();
    }
    
    public void list(Request req, Response res) {
        try {
            List<Department> departments = departmentService.getAllDepartments();
            List<Map<String, Object>> deptList = new ArrayList<>();
            for (Department dept : departments) {
                deptList.add(toDepartmentMap(dept));
            }
            res.json(ApiResponse.success(deptList));
        } catch (Exception e) {
            res.setStatus(500);
            res.json(ApiResponse.error(e.getMessage()));
        }
    }
    
    public void get(Request req, Response res) {
        try {
            String id = req.getPathVariable("id");
            Department dept = departmentService.getDepartment(id);
            if (dept == null) {
                res.setStatus(404);
                res.json(ApiResponse.notFound("Department not found"));
                return;
            }
            res.json(ApiResponse.success(toDepartmentMap(dept)));
        } catch (Exception e) {
            res.setStatus(500);
            res.json(ApiResponse.error(e.getMessage()));
        }
    }
    
    public void create(Request req, Response res) {
        try {
            String name = req.getParameter("name");
            String code = req.getParameter("code");
            String parentId = req.getParameter("parentId");
            String leader = req.getParameter("leader");
            String phone = req.getParameter("phone");
            String email = req.getParameter("email");
            int sort = req.getIntParameter("sort", 0);
            boolean active = req.getBooleanParameter("active", true);
            
            Department dept = departmentService.createDepartment(parentId, name, code, sort, leader, phone, email, active);
            res.setStatus(201);
            res.json(ApiResponse.success("Department created successfully", toDepartmentMap(dept)));
        } catch (Exception e) {
            res.setStatus(400);
            res.json(ApiResponse.badRequest(e.getMessage()));
        }
    }
    
    public void update(Request req, Response res) {
        try {
            String id = req.getPathVariable("id");
            String name = req.getParameter("name");
            String code = req.getParameter("code");
            String parentId = req.getParameter("parentId");
            String leader = req.getParameter("leader");
            String phone = req.getParameter("phone");
            String email = req.getParameter("email");
            Integer sort = req.getParameter("sort") != null ? req.getIntParameter("sort", 0) : null;
            Boolean active = req.getParameter("active") != null ? req.getBooleanParameter("active", true) : null;
            
            Department dept = departmentService.updateDepartment(id, parentId, name, code, sort != null ? sort : 0, leader, phone, email, active != null ? active : true);
            res.json(ApiResponse.success("Department updated successfully", toDepartmentMap(dept)));
        } catch (Exception e) {
            res.setStatus(400);
            res.json(ApiResponse.badRequest(e.getMessage()));
        }
    }
    
    public void delete(Request req, Response res) {
        try {
            String id = req.getPathVariable("id");
            departmentService.deleteDepartment(id);
            res.json(ApiResponse.success("Department deleted successfully", null));
        } catch (Exception e) {
            res.setStatus(400);
            res.json(ApiResponse.badRequest(e.getMessage()));
        }
    }
    
    private Map<String, Object> toDepartmentMap(Department dept) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", dept.getId());
        map.put("name", dept.getName());
        map.put("code", dept.getCode());
        map.put("parentId", dept.getParentId());
        map.put("leader", dept.getLeader());
        map.put("phone", dept.getPhone());
        map.put("email", dept.getEmail());
        map.put("sort", dept.getSort());
        map.put("active", dept.isActive());
        return map;
    }
}
