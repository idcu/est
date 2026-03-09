package ltd.idcu.est.plugin.api.marketplace;

public class PluginVersion {
    
    private final String version;
    private final String changelog;
    private final long publishTime;
    private final String downloadUrl;
    private final long fileSize;
    private final String[] compatibleVersions;
    private final boolean deprecated;
    
    public PluginVersion(String version, String changelog, long publishTime, 
                        String downloadUrl, long fileSize, String[] compatibleVersions, 
                        boolean deprecated) {
        this.version = version;
        this.changelog = changelog;
        this.publishTime = publishTime;
        this.downloadUrl = downloadUrl;
        this.fileSize = fileSize;
        this.compatibleVersions = compatibleVersions != null ? compatibleVersions : new String[0];
        this.deprecated = deprecated;
    }
    
    public String getVersion() {
        return version;
    }
    
    public String getChangelog() {
        return changelog;
    }
    
    public long getPublishTime() {
        return publishTime;
    }
    
    public String getDownloadUrl() {
        return downloadUrl;
    }
    
    public long getFileSize() {
        return fileSize;
    }
    
    public String[] getCompatibleVersions() {
        return compatibleVersions.clone();
    }
    
    public boolean isDeprecated() {
        return deprecated;
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private String version;
        private String changelog;
        private long publishTime;
        private String downloadUrl;
        private long fileSize;
        private String[] compatibleVersions;
        private boolean deprecated;
        
        public Builder version(String version) {
            this.version = version;
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
        
        public Builder downloadUrl(String downloadUrl) {
            this.downloadUrl = downloadUrl;
            return this;
        }
        
        public Builder fileSize(long fileSize) {
            this.fileSize = fileSize;
            return this;
        }
        
        public Builder compatibleVersions(String... compatibleVersions) {
            this.compatibleVersions = compatibleVersions;
            return this;
        }
        
        public Builder deprecated(boolean deprecated) {
            this.deprecated = deprecated;
            return this;
        }
        
        public PluginVersion build() {
            return new PluginVersion(version, changelog, publishTime, 
                                    downloadUrl, fileSize, compatibleVersions, deprecated);
        }
    }
}
