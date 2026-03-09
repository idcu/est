package ltd.idcu.est.plugin.api;

public final class PluginInfo {
    
    private final String name;
    private final String version;
    private final String description;
    private final String author;
    private final String mainClass;
    private final String[] dependencies;
    private final String[] softDependencies;
    private final String category;
    private final String[] tags;
    private final String icon;
    private final String homepage;
    private final String repository;
    private final String license;
    private final double rating;
    private final int downloadCount;
    private final String[] screenshots;
    private final String changelog;
    private final long publishTime;
    private final long lastUpdateTime;
    private final boolean certified;
    private final String[] compatibleVersions;
    private final String minFrameworkVersion;
    
    public PluginInfo(String name, String version, String description, 
                      String author, String mainClass,
                      String[] dependencies, String[] softDependencies,
                      String category, String[] tags, String icon,
                      String homepage, String repository, String license,
                      double rating, int downloadCount, String[] screenshots,
                      String changelog, long publishTime, long lastUpdateTime,
                      boolean certified, String[] compatibleVersions,
                      String minFrameworkVersion) {
        this.name = name;
        this.version = version;
        this.description = description;
        this.author = author;
        this.mainClass = mainClass;
        this.dependencies = dependencies != null ? dependencies : new String[0];
        this.softDependencies = softDependencies != null ? softDependencies : new String[0];
        this.category = category;
        this.tags = tags != null ? tags : new String[0];
        this.icon = icon;
        this.homepage = homepage;
        this.repository = repository;
        this.license = license;
        this.rating = rating;
        this.downloadCount = downloadCount;
        this.screenshots = screenshots != null ? screenshots : new String[0];
        this.changelog = changelog;
        this.publishTime = publishTime;
        this.lastUpdateTime = lastUpdateTime;
        this.certified = certified;
        this.compatibleVersions = compatibleVersions != null ? compatibleVersions : new String[0];
        this.minFrameworkVersion = minFrameworkVersion;
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
    
    public String getCategory() {
        return category;
    }
    
    public String[] getTags() {
        return tags.clone();
    }
    
    public String getIcon() {
        return icon;
    }
    
    public String getHomepage() {
        return homepage;
    }
    
    public String getRepository() {
        return repository;
    }
    
    public String getLicense() {
        return license;
    }
    
    public double getRating() {
        return rating;
    }
    
    public int getDownloadCount() {
        return downloadCount;
    }
    
    public String[] getScreenshots() {
        return screenshots.clone();
    }
    
    public String getChangelog() {
        return changelog;
    }
    
    public long getPublishTime() {
        return publishTime;
    }
    
    public long getLastUpdateTime() {
        return lastUpdateTime;
    }
    
    public boolean isCertified() {
        return certified;
    }
    
    public String[] getCompatibleVersions() {
        return compatibleVersions.clone();
    }
    
    public String getMinFrameworkVersion() {
        return minFrameworkVersion;
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
        private String category;
        private String[] tags;
        private String icon;
        private String homepage;
        private String repository;
        private String license;
        private double rating;
        private int downloadCount;
        private String[] screenshots;
        private String changelog;
        private long publishTime;
        private long lastUpdateTime;
        private boolean certified;
        private String[] compatibleVersions;
        private String minFrameworkVersion;
        
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
        
        public Builder category(String category) {
            this.category = category;
            return this;
        }
        
        public Builder tags(String... tags) {
            this.tags = tags;
            return this;
        }
        
        public Builder icon(String icon) {
            this.icon = icon;
            return this;
        }
        
        public Builder homepage(String homepage) {
            this.homepage = homepage;
            return this;
        }
        
        public Builder repository(String repository) {
            this.repository = repository;
            return this;
        }
        
        public Builder license(String license) {
            this.license = license;
            return this;
        }
        
        public Builder rating(double rating) {
            this.rating = rating;
            return this;
        }
        
        public Builder downloadCount(int downloadCount) {
            this.downloadCount = downloadCount;
            return this;
        }
        
        public Builder screenshots(String... screenshots) {
            this.screenshots = screenshots;
            return this;
        }
        
        public Builder changelog(String changelog) {
            this.changelog = changelog;
            return this;
        }
        
        public Builder publishTime(long publishTime) {
            this.publishTime = publishTime;
            return this;
        }
        
        public Builder lastUpdateTime(long lastUpdateTime) {
            this.lastUpdateTime = lastUpdateTime;
            return this;
        }
        
        public Builder certified(boolean certified) {
            this.certified = certified;
            return this;
        }
        
        public Builder compatibleVersions(String... compatibleVersions) {
            this.compatibleVersions = compatibleVersions;
            return this;
        }
        
        public Builder minFrameworkVersion(String minFrameworkVersion) {
            this.minFrameworkVersion = minFrameworkVersion;
            return this;
        }
        
        public PluginInfo build() {
            return new PluginInfo(name, version, description, author, 
                                  mainClass, dependencies, softDependencies,
                                  category, tags, icon, homepage, repository,
                                  license, rating, downloadCount, screenshots,
                                  changelog, publishTime, lastUpdateTime,
                                  certified, compatibleVersions, minFrameworkVersion);
        }
    }
}
