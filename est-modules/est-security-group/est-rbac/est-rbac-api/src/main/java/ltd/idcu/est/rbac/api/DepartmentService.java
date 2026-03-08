package ltd.idcu.est.rbac.api;

import java.util.List;

public interface DepartmentService {
    
    Department createDepartment(String parentId, String name, String code, int sort, 
                                 String leader, String phone, String email, boolean active);
    
    Department getDepartment(String id);
    
    List<Department> getAllDepartments();
    
    List<Department> getDepartmentTree();
    
    Department updateDepartment(String id, String parentId, String name, String code, 
                                 int sort, String leader, String phone, String email, boolean active);
    
    void deleteDepartment(String id);
}
