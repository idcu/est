package ltd.idcu.est.plugin.api.marketplace;

import java.io.File;
import java.io.InputStream;

public class PluginPublishRequest {
    
    private String pluginId;
    private String name;
    private String version;
    private String description;
    private String author;
    private String category;
    private String[] tags;
    private String[] dependencies;
    private String license;
    private String homepage;
    private String repository;
    private String changelog;
    private String minFrameworkVersion;
    private String[] compatibleVersions;
    private File pluginFile;
    private InputStream pluginInputStream;
    private boolean certified;
    private boolean prerelease;
    
    public PluginPublishRequest() {
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
    
    public String getDependencies() {
        return dependencies;
    }
    
    public void setDependencies(String dependencies) {
        this.dependencies = dependencies;
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
    
    public File getPluginFile() {
        return pluginFile;
    }
    
    public void setPluginFile(File pluginFile) {
        this.pluginFile = pluginFile;
    }
    
    public InputStream getPluginInputStream() {
        return pluginInputStream;
    }
    
    public void setPluginInputStream(InputStream pluginInputStream) {
        this.pluginInputStream = pluginInputStream;
    }
    
    public boolean isCertified() {
        return certified;
    }
    
    public void setCertified(boolean certified) {
        this.certified = certified;
    }
    
    public boolean isPrerelease() {
        return prerelease;
    }
    
    public void setPrerelease(boolean prerelease) {
        this.prerelease = prerelease;
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private PluginPublishRequest request = new PluginPublishRequest();
        
        public Builder pluginId(String pluginId) {
            request.pluginId = pluginId;
            return this;
        }
        
        public Builder name(String name) {
            request.name = name;
            return this;
        }
        
        public Builder version(String version) {
            request.version = version;
            return this;
        }
        
        public Builder description(String description) {
            request.description = description;
            return this;
        }
        
        public Builder author(String author) {
            request.author = author;
            return this;
        }
        
        public Builder category(String category) {
            request.category = category;
            return this;
        }
        
        public Builder tags(String... tags) {
            request.tags = tags;
            return this;
        }
        
        public Builder license(String license) {
            request.license = license;
            return this;
        }
        
        public Builder pluginFile(File pluginFile) {
            request.pluginFile = pluginFile;
            return this;
        }
        
        public Builder pluginInputStream(InputStream inputStream) {
            request.pluginInputStream = inputStream;
            return this;
        }
        
        public PluginPublishRequest build() {
            return request;
        }
    }
}
