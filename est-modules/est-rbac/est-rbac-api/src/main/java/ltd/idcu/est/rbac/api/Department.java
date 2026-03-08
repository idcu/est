package ltd.idcu.est.rbac.api;

import java.util.List;

public interface Department {
    
    String getId();
    
    String getParentId();
    
    String getName();
    
    String getCode();
    
    int getSort();
    
    String getLeader();
    
    String getPhone();
    
    String getEmail();
    
    boolean isActive();
    
    List<Department> getChildren();
}
