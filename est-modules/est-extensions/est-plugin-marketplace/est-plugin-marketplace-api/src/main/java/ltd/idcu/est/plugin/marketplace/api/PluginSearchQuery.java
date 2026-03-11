package ltd.idcu.est.plugin.marketplace.api;

import java.util.ArrayList;
import java.util.List;

public final class PluginSearchQuery {
    
    private final String keyword;
    private final String category;
    private final List<String> tags;
    private final String language;
    private final Boolean certified;
    private final String license;
    private final String minFrameworkVersion;
    private final String sortBy;
    private final int page;
    private final int pageSize;
    
    private PluginSearchQuery(String keyword, String category, List<String> tags, 
                            String language, Boolean certified, String license,
                            String minFrameworkVersion, String sortBy, int page, int pageSize) {
        this.keyword = keyword;
        this.category = category;
        this.tags = tags != null ? new ArrayList<>(tags) : new ArrayList<>();
        this.language = language;
        this.certified = certified;
        this.license = license;
        this.minFrameworkVersion = minFrameworkVersion;
        this.sortBy = sortBy;
        this.page = page;
        this.pageSize = pageSize;
    }
    
    public String getKeyword() {
        return keyword;
    }
    
    public String getCategory() {
        return category;
    }
    
    public List<String> getTags() {
        return new ArrayList<>(tags);
    }
    
    public String getLanguage() {
        return language;
    }
    
    public Boolean getCertified() {
        return certified;
    }
    
    public String getLicense() {
        return license;
    }
    
    public String getMinFrameworkVersion() {
        return minFrameworkVersion;
    }
    
    public String getSortBy() {
        return sortBy;
    }
    
    public int getPage() {
        return page;
    }
    
    public int getPageSize() {
        return pageSize;
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private String keyword;
        private String category;
        private List<String> tags = new ArrayList<>();
        private String language;
        private Boolean certified;
        private String license;
        private String minFrameworkVersion;
        private String sortBy = "relevance";
        private int page = 0;
        private int pageSize = 20;
        
        public Builder keyword(String keyword) {
            this.keyword = keyword;
            return this;
        }
        
        public Builder category(String category) {
            this.category = category;
            return this;
        }
        
        public Builder addTag(String tag) {
            this.tags.add(tag);
            return this;
        }
        
        public Builder tags(List<String> tags) {
            this.tags = tags != null ? new ArrayList<>(tags) : new ArrayList<>();
            return this;
        }
        
        public Builder language(String language) {
            this.language = language;
            return this;
        }
        
        public Builder certified(Boolean certified) {
            this.certified = certified;
            return this;
        }
        
        public Builder license(String license) {
            this.license = license;
            return this;
        }
        
        public Builder minFrameworkVersion(String minFrameworkVersion) {
            this.minFrameworkVersion = minFrameworkVersion;
            return this;
        }
        
        public Builder sortBy(String sortBy) {
            this.sortBy = sortBy;
            return this;
        }
        
        public Builder page(int page) {
            this.page = page;
            return this;
        }
        
        public Builder pageSize(int pageSize) {
            this.pageSize = pageSize;
            return this;
        }
        
        public PluginSearchQuery build() {
            return new PluginSearchQuery(keyword, category, tags, language, 
                                        certified, license, minFrameworkVersion, 
                                        sortBy, page, pageSize);
        }
    }
}
