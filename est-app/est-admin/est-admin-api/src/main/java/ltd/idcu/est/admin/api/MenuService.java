package ltd.idcu.est.admin.api;

import java.util.List;
import java.util.Set;

public interface MenuService {
    
    Menu createMenu(String parentId, String name, String path, String component, String icon, 
                    int sort, Menu.MenuType type, boolean visible, boolean cache, Set<String> permissions);
    
    Menu getMenu(String id);
    
    List<Menu> getAllMenus();
    
    List<Menu> getMenuTree();
    
    List<Menu> getUserMenus(User user);
    
    Menu updateMenu(String id, String parentId, String name, String path, String component, 
                    String icon, int sort, Menu.MenuType type, boolean visible, boolean cache, Set<String> permissions);
    
    void deleteMenu(String id);
}
