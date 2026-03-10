package ltd.idcu.est.plugin.api.marketplace;

import java.util.List;
import ltd.idcu.est.plugin.api.PluginInfo;

public interface PluginMarketplace {
    
    List<PluginInfo> searchPlugins(PluginSearchCriteria criteria);
    
    PluginInfo getPlugin(String pluginId);
    
    List<PluginInfo> getPluginsByCategory(String category);
    
    List<PluginCategory> getAllCategories();
    
    List<PluginInfo> getPopularPlugins(int limit);
    
    List<PluginInfo> getNewestPlugins(int limit);
    
    List<PluginInfo> getCertifiedPlugins();
    
    boolean installPlugin(String pluginId);
    
    boolean uninstallPlugin(String pluginId);
    
    boolean updatePlugin(String pluginId);
    
    List<PluginVersion> getPluginVersions(String pluginId);
    
    List<PluginReview> getPluginReviews(String pluginId);
    
    boolean addReview(String pluginId, PluginReview review);
}
