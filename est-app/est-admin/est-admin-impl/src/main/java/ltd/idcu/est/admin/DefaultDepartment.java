package ltd.idcu.est.admin;

import ltd.idcu.est.admin.api.Department;

import java.util.ArrayList;
import java.util.List;

public class DefaultDepartment implements Department {
    
    private final String id;
    private final String parentId;
    private final String name;
    private final String code;
    private final int sort;
    private final String leader;
    private final String phone;
    private final String email;
    private final boolean active;
    private final List<Department> children;
    
    public DefaultDepartment(String id, String parentId, String name, String code, int sort, 
                             String leader, String phone, String email, boolean active) {
        this.id = id;
        this.parentId = parentId;
        this.name = name;
        this.code = code;
        this.sort = sort;
        this.leader = leader;
        this.phone = phone;
        this.email = email;
        this.active = active;
        this.children = new ArrayList<>();
    }
    
    @Override
    public String getId() {
        return id;
    }
    
    @Override
    public String getParentId() {
        return parentId;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public String getCode() {
        return code;
    }
    
    @Override
    public int getSort() {
        return sort;
    }
    
    @Override
    public String getLeader() {
        return leader;
    }
    
    @Override
    public String getPhone() {
        return phone;
    }
    
    @Override
    public String getEmail() {
        return email;
    }
    
    @Override
    public boolean isActive() {
        return active;
    }
    
    @Override
    public List<Department> getChildren() {
        return new ArrayList<>(children);
    }
    
    public void addChild(Department child) {
        children.add(child);
    }
}
