package ltd.idcu.est.plugin.api.marketplace;

public class PluginSearchCriteria {
    
    private String keyword;
    private String category;
    private String[] tags;
    private String author;
    private Double minRating;
    private Integer minDownloads;
    private Boolean certifiedOnly;
    private String frameworkVersion;
    private SortBy sortBy;
    private SortOrder sortOrder;
    private int page;
    private int pageSize;
    
    public enum SortBy {
        NAME, RATING, DOWNLOADS, PUBLISH_TIME, UPDATE_TIME
    }
    
    public enum SortOrder {
        ASC, DESC
    }
    
    public PluginSearchCriteria() {
        this.page = 0;
        this.pageSize = 20;
        this.sortBy = SortBy.DOWNLOADS;
        this.sortOrder = SortOrder.DESC;
    }
    
    public String getKeyword() {
        return keyword;
    }
    
    public void setKeyword(String keyword) {
        this.keyword = keyword;
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
    
    public String getAuthor() {
        return author;
    }
    
    public void setAuthor(String author) {
        this.author = author;
    }
    
    public Double getMinRating() {
        return minRating;
    }
    
    public void setMinRating(Double minRating) {
        this.minRating = minRating;
    }
    
    public Integer getMinDownloads() {
        return minDownloads;
    }
    
    public void setMinDownloads(Integer minDownloads) {
        this.minDownloads = minDownloads;
    }
    
    public Boolean getCertifiedOnly() {
        return certifiedOnly;
    }
    
    public void setCertifiedOnly(Boolean certifiedOnly) {
        this.certifiedOnly = certifiedOnly;
    }
    
    public String getFrameworkVersion() {
        return frameworkVersion;
    }
    
    public void setFrameworkVersion(String frameworkVersion) {
        this.frameworkVersion = frameworkVersion;
    }
    
    public SortBy getSortBy() {
        return sortBy;
    }
    
    public void setSortBy(SortBy sortBy) {
        this.sortBy = sortBy;
    }
    
    public SortOrder getSortOrder() {
        return sortOrder;
    }
    
    public void setSortOrder(SortOrder sortOrder) {
        this.sortOrder = sortOrder;
    }
    
    public int getPage() {
        return page;
    }
    
    public void setPage(int page) {
        this.page = page;
    }
    
    public int getPageSize() {
        return pageSize;
    }
    
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private PluginSearchCriteria criteria = new PluginSearchCriteria();
        
        public Builder keyword(String keyword) {
            criteria.keyword = keyword;
            return this;
        }
        
        public Builder category(String category) {
            criteria.category = category;
            return this;
        }
        
        public Builder tags(String... tags) {
            criteria.tags = tags;
            return this;
        }
        
        public Builder author(String author) {
            criteria.author = author;
            return this;
        }
        
        public Builder minRating(double minRating) {
            criteria.minRating = minRating;
            return this;
        }
        
        public Builder minDownloads(int minDownloads) {
            criteria.minDownloads = minDownloads;
            return this;
        }
        
        public Builder certifiedOnly(boolean certifiedOnly) {
            criteria.certifiedOnly = certifiedOnly;
            return this;
        }
        
        public Builder frameworkVersion(String frameworkVersion) {
            criteria.frameworkVersion = frameworkVersion;
            return this;
        }
        
        public Builder sortBy(SortBy sortBy) {
            criteria.sortBy = sortBy;
            return this;
        }
        
        public Builder sortOrder(SortOrder sortOrder) {
            criteria.sortOrder = sortOrder;
            return this;
        }
        
        public Builder page(int page) {
            criteria.page = page;
            return this;
        }
        
        public Builder pageSize(int pageSize) {
            criteria.pageSize = pageSize;
            return this;
        }
        
        public PluginSearchCriteria build() {
            return criteria;
        }
    }
}
