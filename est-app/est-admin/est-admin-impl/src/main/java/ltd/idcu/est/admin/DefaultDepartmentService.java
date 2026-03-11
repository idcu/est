package ltd.idcu.est.admin;

import ltd.idcu.est.admin.api.AdminException;
import ltd.idcu.est.admin.api.Department;
import ltd.idcu.est.admin.api.DepartmentService;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultDepartmentService implements DepartmentService {
    
    private final Map<String, Department> departmentsById;
    
    public DefaultDepartmentService() {
        this.departmentsById = new ConcurrentHashMap<>();
        initializeDefaultDepartments();
    }
    
    private void initializeDefaultDepartments() {
        DefaultDepartment headOffice = new DefaultDepartment(
            UUID.randomUUID().toString(),
            null,
            "Head Office",
            "HQ",
            1,
            "Zhang San",
            "13800138000",
            "hq@example.com",
            true
        );
        departmentsById.put(headOffice.getId(), headOffice);
        
        DefaultDepartment techDept = new DefaultDepartment(
            UUID.randomUUID().toString(),
            headOffice.getId(),
            "Tech Dept",
            "TECH",
            1,
            "Li Si",
            "13800138001",
            "tech@example.com",
            true
        );
        departmentsById.put(techDept.getId(), techDept);
        
        DefaultDepartment salesDept = new DefaultDepartment(
            UUID.randomUUID().toString(),
            headOffice.getId(),
            "Sales Dept",
            "SALES",
            2,
            "Wang Wu",
            "13800138002",
            "sales@example.com",
            true
        );
        departmentsById.put(salesDept.getId(), salesDept);
        
        DefaultDepartment hrDept = new DefaultDepartment(
            UUID.randomUUID().toString(),
            headOffice.getId(),
            "HR Dept",
            "HR",
            3,
            "Zhao Liu",
            "13800138003",
            "hr@example.com",
            true
        );
        departmentsById.put(hrDept.getId(), hrDept);
    }
    
    @Override
    public Department createDepartment(String parentId, String name, String code, int sort,
                                       String leader, String phone, String email, boolean active) {
        String id = UUID.randomUUID().toString();
        DefaultDepartment dept = new DefaultDepartment(
            id,
            parentId,
            name,
            code,
            sort,
            leader,
            phone,
            email,
            active
        );
        departmentsById.put(id, dept);
        return dept;
    }
    
    @Override
    public Department getDepartment(String id) {
        return departmentsById.get(id);
    }
    
    @Override
    public List<Department> getAllDepartments() {
        return new ArrayList<>(departmentsById.values());
    }
    
    @Override
    public List<Department> getDepartmentTree() {
        Map<String, List<Department>> childrenMap = new HashMap<>();
        List<Department> roots = new ArrayList<>();
        
        for (Department dept : departmentsById.values()) {
            if (dept.getParentId() == null) {
                roots.add(dept);
            } else {
                childrenMap.computeIfAbsent(dept.getParentId(), k -> new ArrayList<>()).add(dept);
            }
        }
        
        buildTree(roots, childrenMap);
        return roots;
    }
    
    private void buildTree(List<Department> depts, Map<String, List<Department>> childrenMap) {
        for (Department dept : depts) {
            List<Department> children = childrenMap.get(dept.getId());
            if (children != null) {
                for (Department child : children) {
                    ((DefaultDepartment) dept).addChild(child);
                }
                buildTree(children, childrenMap);
            }
        }
    }
    
    @Override
    public Department updateDepartment(String id, String parentId, String name, String code,
                                       int sort, String leader, String phone, String email, boolean active) {
        Department existingDept = departmentsById.get(id);
        if (existingDept == null) {
            throw new AdminException("Department not found: " + id);
        }
        
        DefaultDepartment updatedDept = new DefaultDepartment(
            id,
            parentId != null ? parentId : existingDept.getParentId(),
            name != null ? name : existingDept.getName(),
            code != null ? code : existingDept.getCode(),
            sort,
            leader != null ? leader : existingDept.getLeader(),
            phone != null ? phone : existingDept.getPhone(),
            email != null ? email : existingDept.getEmail(),
            active
        );
        departmentsById.put(id, updatedDept);
        return updatedDept;
    }
    
    @Override
    public void deleteDepartment(String id) {
        departmentsById.remove(id);
    }
}
