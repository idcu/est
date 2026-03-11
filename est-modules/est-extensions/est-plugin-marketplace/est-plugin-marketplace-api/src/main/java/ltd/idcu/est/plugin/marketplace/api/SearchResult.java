package ltd.idcu.est.plugin.marketplace.api;

import ltd.idcu.est.plugin.api.PluginInfo;

import java.util.ArrayList;
import java.util.List;

public final class SearchResult {
    
    private final List<PluginInfo> plugins;
    private final int totalCount;
    private final int page;
    private final int pageSize;
    private final int totalPages;
    
    public SearchResult(List<PluginInfo> plugins, int totalCount, int page, int pageSize) {
        this.plugins = plugins != null ? new ArrayList<>(plugins) : new ArrayList<>();
        this.totalCount = totalCount;
        this.page = page;
        this.pageSize = pageSize;
        this.totalPages = pageSize > 0 ? (int) Math.ceil((double) totalCount / pageSize) : 0;
    }
    
    public List<PluginInfo> getPlugins() {
        return new ArrayList<>(plugins);
    }
    
    public int getTotalCount() {
        return totalCount;
    }
    
    public int getPage() {
        return page;
    }
    
    public int getPageSize() {
        return pageSize;
    }
    
    public int getTotalPages() {
        return totalPages;
    }
    
    public boolean hasNextPage() {
        return page < totalPages - 1;
    }
    
    public boolean hasPreviousPage() {
        return page > 0;
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private List<PluginInfo> plugins = new ArrayList<>();
        private int totalCount;
        private int page;
        private int pageSize;
        
        public Builder plugins(List<PluginInfo> plugins) {
            this.plugins = plugins != null ? new ArrayList<>(plugins) : new ArrayList<>();
            return this;
        }
        
        public Builder totalCount(int totalCount) {
            this.totalCount = totalCount;
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
        
        public SearchResult build() {
            return new SearchResult(plugins, totalCount, page, pageSize);
        }
    }
}
