package ltd.idcu.est.plugin.api.marketplace;

public class PluginMetadata {
    
    private String pluginId;
    private String name;
    private String version;
    private String description;
    private String author;
    private String category;
    private String[] tags;
    private String mainClass;
    private String[] dependencies;
    private String[] softDependencies;
    private String license;
    private String homepage;
    private String repository;
    private String changelog;
    private String minFrameworkVersion;
    private String[] compatibleVersions;
    private long fileSize;
    private String checksum;
    private boolean certified;
    private boolean deprecated;
    
    public PluginMetadata() {
    }
    
    public String getPluginId() {
        return pluginId;
    }
    
    public void setPluginId(String pluginId) {
        this.pluginId = pluginId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getVersion() {
        return version;
    }
    
    public void setVersion(String version) {
        this.version = version;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getAuthor() {
        return author;
    }
    
    public void setAuthor(String author) {
        this.author = author;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public String[] getTags() {
        return tags;
    }
    
    public void setTags(String[] tags) {
        this.tags = tags;
    }
    
    public String getMainClass() {
        return mainClass;
    }
    
    public void setMainClass(String mainClass) {
        this.mainClass = mainClass;
    }
    
    public String[] getDependencies() {
        return dependencies;
    }
    
    public void setDependencies(String[] dependencies) {
        this.dependencies = dependencies;
    }
    
    public String[] getSoftDependencies() {
        return softDependencies;
    }
    
    public void setSoftDependencies(String[] softDependencies) {
        this.softDependencies = softDependencies;
    }
    
    public String getLicense() {
        return license;
    }
    
    public void setLicense(String license) {
        this.license = license;
    }
    
    public String getHomepage() {
        return homepage;
    }
    
    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }
    
    public String getRepository() {
        return repository;
    }
    
    public void setRepository(String repository) {
        this.repository = repository;
    }
    
    public String getChangelog() {
        return changelog;
    }
    
    public void setChangelog(String changelog) {
        this.changelog = changelog;
    }
    
    public String getMinFrameworkVersion() {
        return minFrameworkVersion;
    }
    
    public void setMinFrameworkVersion(String minFrameworkVersion) {
        this.minFrameworkVersion = minFrameworkVersion;
    }
    
    public String[] getCompatibleVersions() {
        return compatibleVersions;
    }
    
    public void setCompatibleVersions(String[] compatibleVersions) {
        this.compatibleVersions = compatibleVersions;
    }
    
    public long getFileSize() {
        return fileSize;
    }
    
    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }
    
    public String getChecksum() {
        return checksum;
    }
    
    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }
    
    public boolean isCertified() {
        return certified;
    }
    
    public void setCertified(boolean certified) {
        this.certified = certified;
    }
    
    public boolean isDeprecated() {
        return deprecated;
    }
    
    public void setDeprecated(boolean deprecated) {
        this.deprecated = deprecated;
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private PluginMetadata metadata = new PluginMetadata();
        
        public Builder pluginId(String pluginId) {
            metadata.pluginId = pluginId;
            return this;
        }
        
        public Builder name(String name) {
            metadata.name = name;
            return this;
        }
        
        public Builder version(String version) {
            metadata.version = version;
            return this;
        }
        
        public Builder description(String description) {
            metadata.description = description;
            return this;
        }
        
        public Builder author(String author) {
            metadata.author = author;
            return this;
        }
        
        public Builder category(String category) {
            metadata.category = category;
            return this;
        }
        
        public Builder tags(String... tags) {
            metadata.tags = tags;
            return this;
        }
        
        public Builder mainClass(String mainClass) {
            metadata.mainClass = mainClass;
            return this;
        }
        
        public Builder dependencies(String... dependencies) {
            metadata.dependencies = dependencies;
            return this;
        }
        
        public Builder license(String license) {
            metadata.license = license;
            return this;
        }
        
        public PluginMetadata build() {
            return metadata;
        }
    }
}
