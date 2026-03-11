package ltd.idcu.est.plugin.marketplace.api;

import java.util.List;

public interface PluginSearchService {
    
    SearchResult search(PluginSearchQuery query);
    
    List<String> getSuggestions(String keyword);
    
    List<PluginCategory> getAllCategories();
    
    List<String> getPopularTags(int limit);
}
