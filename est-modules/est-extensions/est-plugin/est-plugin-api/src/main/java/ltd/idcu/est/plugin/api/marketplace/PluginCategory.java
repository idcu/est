package ltd.idcu.est.plugin.api.marketplace;

public class PluginCategory {
    
    private final String id;
    private final String name;
    private final String description;
    private final String icon;
    private final int pluginCount;
    
    public PluginCategory(String id, String name, String description, String icon, int pluginCount) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.pluginCount = pluginCount;
    }
    
    public String getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public String getIcon() {
        return icon;
    }
    
    public int getPluginCount() {
        return pluginCount;
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private String id;
        private String name;
        private String description;
        private String icon;
        private int pluginCount;
        
        public Builder id(String id) {
            this.id = id;
            return this;
        }
        
        public Builder name(String name) {
            this.name = name;
            return this;
        }
        
        public Builder description(String description) {
            this.description = description;
            return this;
        }
        
        public Builder icon(String icon) {
            this.icon = icon;
            return this;
        }
        
        public Builder pluginCount(int pluginCount) {
            this.pluginCount = pluginCount;
            return this;
        }
        
        public PluginCategory build() {
            return new PluginCategory(id, name, description, icon, pluginCount);
        }
    }
}
