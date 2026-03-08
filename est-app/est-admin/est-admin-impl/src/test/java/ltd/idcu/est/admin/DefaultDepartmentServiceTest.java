package ltd.idcu.est.admin;

import ltd.idcu.est.admin.api.AdminException;
import ltd.idcu.est.admin.api.Department;
import ltd.idcu.est.admin.api.DepartmentService;
import ltd.idcu.est.test.Assertions;
import ltd.idcu.est.test.annotation.Test;

import java.util.List;

public class DefaultDepartmentServiceTest {

    @Test
    public void testCreateDepartment() {
        DepartmentService departmentService = new DefaultDepartmentService();
        
        Department dept = departmentService.createDepartment(
            null, 
            "技术部", 
            "TECH", 
            1, 
            "张三", 
            "13800138000", 
            "tech@example.com", 
            true
        );
        
        Assertions.assertNotNull(dept);
        Assertions.assertNotNull(dept.getId());
        Assertions.assertEquals("技术部", dept.getName());
        Assertions.assertEquals("TECH", dept.getCode());
    }

    @Test
    public void testGetDepartment() {
        DepartmentService departmentService = new DefaultDepartmentService();
        
        Department dept = departmentService.createDepartment(
            null, 
            "市场部", 
            "MARKET", 
            2, 
            "李四", 
            "13900139000", 
            "market@example.com", 
            true
        );
        
        Department foundDept = departmentService.getDepartment(dept.getId());
        
        Assertions.assertNotNull(foundDept);
        Assertions.assertEquals(dept.getId(), foundDept.getId());
        Assertions.assertEquals("市场部", foundDept.getName());
    }

    @Test
    public void testGetAllDepartments() {
        DepartmentService departmentService = new DefaultDepartmentService();
        
        departmentService.createDepartment(null, "部门1", "DEPT1", 1, "领导1", "13800000001", "dept1@example.com", true);
        departmentService.createDepartment(null, "部门2", "DEPT2", 2, "领导2", "13800000002", "dept2@example.com", true);
        
        List<Department> depts = departmentService.getAllDepartments();
        
        Assertions.assertNotNull(depts);
        Assertions.assertTrue(depts.size() >= 2);
    }

    @Test
    public void testUpdateDepartment() {
        DepartmentService departmentService = new DefaultDepartmentService();
        
        Department dept = departmentService.createDepartment(
            null, 
            "原始部门", 
            "ORIGINAL", 
            1, 
            "原始领导", 
            "13800000000", 
            "original@example.com", 
            true
        );
        
        Department updatedDept = departmentService.updateDepartment(
            dept.getId(), 
            null, 
            "更新后的部门", 
            "UPDATED", 
            5, 
            "新领导", 
            "13900000000", 
            "updated@example.com", 
            false
        );
        
        Assertions.assertNotNull(updatedDept);
        Assertions.assertEquals("更新后的部门", updatedDept.getName());
        Assertions.assertEquals("UPDATED", updatedDept.getCode());
        Assertions.assertEquals(5, updatedDept.getSort());
        Assertions.assertEquals("新领导", updatedDept.getLeader());
        Assertions.assertEquals("13900000000", updatedDept.getPhone());
        Assertions.assertEquals("updated@example.com", updatedDept.getEmail());
        Assertions.assertFalse(updatedDept.isActive());
    }

    @Test
    public void testDeleteDepartment() {
        DepartmentService departmentService = new DefaultDepartmentService();
        
        Department dept = departmentService.createDepartment(
            null, 
            "待删除部门", 
            "DELETE", 
            1, 
            "领导", 
            "13800000000", 
            "delete@example.com", 
            true
        );
        
        departmentService.deleteDepartment(dept.getId());
        
        Department deletedDept = departmentService.getDepartment(dept.getId());
        Assertions.assertNull(deletedDept);
    }

    @Test
    public void testCreateSubDepartment() {
        DepartmentService departmentService = new DefaultDepartmentService();
        
        Department parentDept = departmentService.createDepartment(
            null, 
            "总公司", 
            "HEAD", 
            1, 
            "总经理", 
            "13800000000", 
            "head@example.com", 
            true
        );
        
        Department subDept = departmentService.createDepartment(
            parentDept.getId(), 
            "分公司", 
            "BRANCH", 
            1, 
            "分公司经理", 
            "13900000000", 
            "branch@example.com", 
            true
        );
        
        Assertions.assertNotNull(subDept);
        Assertions.assertEquals(parentDept.getId(), subDept.getParentId());
    }

    @Test
    public void testDepartmentActiveStatus() {
        DepartmentService departmentService = new DefaultDepartmentService();
        
        Department activeDept = departmentService.createDepartment(
            null, 
            "活跃部门", 
            "ACTIVE", 
            1, 
            "领导", 
            "13800000000", 
            "active@example.com", 
            true
        );
        
        Department inactiveDept = departmentService.createDepartment(
            null, 
            "非活跃部门", 
            "INACTIVE", 
            2, 
            "领导", 
            "13800000001", 
            "inactive@example.com", 
            false
        );
        
        Assertions.assertTrue(activeDept.isActive());
        Assertions.assertFalse(inactiveDept.isActive());
    }

    @Test
    public void testDepartmentSort() {
        DepartmentService departmentService = new DefaultDepartmentService();
        
        Department dept1 = departmentService.createDepartment(null, "部门A", "DEPT_A", 3, "领导A", "13800000001", "a@example.com", true);
        Department dept2 = departmentService.createDepartment(null, "部门B", "DEPT_B", 1, "领导B", "13800000002", "b@example.com", true);
        Department dept3 = departmentService.createDepartment(null, "部门C", "DEPT_C", 2, "领导C", "13800000003", "c@example.com", true);
        
        Assertions.assertEquals(3, dept1.getSort());
        Assertions.assertEquals(1, dept2.getSort());
        Assertions.assertEquals(2, dept3.getSort());
    }

    @Test
    public void testUpdateNonExistentDepartment() {
        DepartmentService departmentService = new DefaultDepartmentService();
        
        Assertions.assertThrows(AdminException.class, () -> {
            departmentService.updateDepartment(
                "non-existent-id", 
                null, 
                "名称", 
                "CODE", 
                1, 
                "领导", 
                "13800000000", 
                "email@example.com", 
                true
            );
        });
    }

    @Test
    public void testDeleteNonExistentDepartment() {
        DepartmentService departmentService = new DefaultDepartmentService();
        
        Assertions.assertThrows(AdminException.class, () -> {
            departmentService.deleteDepartment("non-existent-id");
        });
    }
}
