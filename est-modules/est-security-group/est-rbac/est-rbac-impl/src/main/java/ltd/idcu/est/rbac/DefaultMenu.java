package ltd.idcu.est.rbac;

import ltd.idcu.est.rbac.api.Menu;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DefaultMenu implements Menu {
    
    private final String id;
    private final String parentId;
    private final String name;
    private final String path;
    private final String component;
    private final String icon;
    private final int sort;
    private final MenuType type;
    private final boolean visible;
    private final boolean cache;
    private final Set<String> permissions;
    private final List<Menu> children;
    
    public DefaultMenu(String id, String parentId, String name, String path, String component, 
                       String icon, int sort, MenuType type, boolean visible, boolean cache, 
                       Set<String> permissions) {
        this.id = id;
        this.parentId = parentId;
        this.name = name;
        this.path = path;
        this.component = component;
        this.icon = icon;
        this.sort = sort;
        this.type = type;
        this.visible = visible;
        this.cache = cache;
        this.permissions = new HashSet<>(permissions);
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
    public String getPath() {
        return path;
    }
    
    @Override
    public String getComponent() {
        return component;
    }
    
    @Override
    public String getIcon() {
        return icon;
    }
    
    @Override
    public int getSort() {
        return sort;
    }
    
    @Override
    public MenuType getType() {
        return type;
    }
    
    @Override
    public boolean isVisible() {
        return visible;
    }
    
    @Override
    public boolean isCache() {
        return cache;
    }
    
    @Override
    public Set<String> getPermissions() {
        return new HashSet<>(permissions);
    }
    
    @Override
    public List<Menu> getChildren() {
        return new ArrayList<>(children);
    }
    
    public void addChild(Menu child) {
        children.add(child);
    }
}
