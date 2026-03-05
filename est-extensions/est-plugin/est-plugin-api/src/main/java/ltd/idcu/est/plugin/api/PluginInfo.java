package ltd.idcu.est.plugin.api;

public final class PluginInfo {
    
    private final String name;
    private final String version;
    private final String description;
    private final String author;
    private final String mainClass;
    private final String[] dependencies;
    private final String[] softDependencies;
    
    public PluginInfo(String name, String version, String description, 
                      String author, String mainClass,
                      String[] dependencies, String[] softDependencies) {
        this.name = name;
        this.version = version;
        this.description = description;
        this.author = author;
        this.mainClass = mainClass;
        this.dependencies = dependencies != null ? dependencies : new String[0];
        this.softDependencies = softDependencies != null ? softDependencies : new String[0];
    }
    
    public String getName() {
        return name;
    }
    
    public String getVersion() {
        return version;
    }
    
    public String getDescription() {
        return description;
    }
    
    public String getAuthor() {
        return author;
    }
    
    public String getMainClass() {
        return mainClass;
    }
    
    public String[] getDependencies() {
        return dependencies.clone();
    }
    
    public String[] getSoftDependencies() {
        return softDependencies.clone();
    }
    
    public boolean hasDependencies() {
        return dependencies.length > 0;
    }
    
    public boolean hasSoftDependencies() {
        return softDependencies.length > 0;
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private String name;
        private String version;
        private String description;
        private String author;
        private String mainClass;
        private String[] dependencies;
        private String[] softDependencies;
        
        public Builder name(String name) {
            this.name = name;
            return this;
        }
        
        public Builder version(String version) {
            this.version = version;
            return this;
        }
        
        public Builder description(String description) {
            this.description = description;
            return this;
        }
        
        public Builder author(String author) {
            this.author = author;
            return this;
        }
        
        public Builder mainClass(String mainClass) {
            this.mainClass = mainClass;
            return this;
        }
        
        public Builder dependencies(String... dependencies) {
            this.dependencies = dependencies;
            return this;
        }
        
        public Builder softDependencies(String... softDependencies) {
            this.softDependencies = softDependencies;
            return this;
        }
        
        public PluginInfo build() {
            return new PluginInfo(name, version, description, author, 
                                  mainClass, dependencies, softDependencies);
        }
    }
}
