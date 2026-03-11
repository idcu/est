package ltd.idcu.est.plugin.marketplace.api;

import ltd.idcu.est.plugin.api.PluginInfo;

import java.util.List;
import java.util.Optional;

public interface PluginMarketplace {
    
    Optional<PluginInfo> getPlugin(String pluginId);
    
    List<PluginInfo> searchPlugins(String query);
    
    List<PluginInfo> searchPluginsByCategory(String category);
    
    List<PluginInfo> searchPluginsByTags(String... tags);
    
    SearchResult searchPlugins(PluginSearchQuery query);
    
    List<PluginInfo> getPopularPlugins(int limit);
    
    List<PluginInfo> getLatestPlugins(int limit);
    
    List<PluginInfo> getCertifiedPlugins();
    
    List<PluginInfo> getFeaturedPlugins();
    
    List<String> getCategories();
    
    List<PluginCategory> getPluginCategories();
    
    List<String> getPopularTags(int limit);
    
    List<String> getSearchSuggestions(String keyword);
    
    boolean installPlugin(String pluginId);
    
    boolean installPlugin(String pluginId, String version);
    
    boolean updatePlugin(String pluginId);
    
    boolean uninstallPlugin(String pluginId);
    
    Optional<PluginInfo> getInstalledPlugin(String pluginId);
    
    List<PluginInfo> getInstalledPlugins();
    
    List<PluginInfo> getUpdatesAvailable();
    
    void addPluginRepository(PluginRepository repository);
    
    void removePluginRepository(String repositoryId);
    
    List<PluginRepository> getRepositories();
    
    interface Builder {
        Builder addRepository(PluginRepository repository);
        Builder localCacheDirectory(String directory);
        PluginMarketplace build();
    }
}
