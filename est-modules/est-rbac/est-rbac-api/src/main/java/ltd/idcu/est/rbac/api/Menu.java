package ltd.idcu.est.rbac.api;

import java.util.List;
import java.util.Set;

public interface Menu {
    
    String getId();
    
    String getParentId();
    
    String getName();
    
    String getPath();
    
    String getComponent();
    
    String getIcon();
    
    int getSort();
    
    MenuType getType();
    
    boolean isVisible();
    
    boolean isCache();
    
    Set<String> getPermissions();
    
    List<Menu> getChildren();
    
    enum MenuType {
        DIRECTORY,
        MENU,
        BUTTON
    }
}
