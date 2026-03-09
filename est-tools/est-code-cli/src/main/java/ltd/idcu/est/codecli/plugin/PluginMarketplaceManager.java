package ltd.idcu.est.codecli.plugin;

import ltd.idcu.est.plugin.api.PluginInfo;
import ltd.idcu.est.plugin.marketplace.api.PluginMarketplace;
import ltd.idcu.est.plugin.marketplace.api.PluginRepository;
import ltd.idcu.est.plugin.marketplace.impl.DefaultPluginMarketplace;
import ltd.idcu.est.plugin.marketplace.impl.LocalPluginRepository;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

public class PluginMarketplaceManager {
    
    private final PluginMarketplace marketplace;
    private final Path workDir;
    
    public PluginMarketplaceManager(Path workDir) {
        this.workDir = workDir;
        Path localRepoDir = workDir.resolve(".est-marketplace");
        PluginRepository localRepo = new LocalPluginRepository(localRepoDir);
        this.marketplace = new DefaultPluginMarketplace.Builder()
            .addRepository(localRepo)
            .localCacheDirectory(localRepoDir.toString())
            .build();
    }
    
    public Optional<PluginInfo> getPlugin(String pluginId) {
        return marketplace.getPlugin(pluginId);
    }
    
    public List<PluginInfo> searchPlugins(String query) {
        return marketplace.searchPlugins(query);
    }
    
    public List<PluginInfo> searchPluginsByCategory(String category) {
        return marketplace.searchPluginsByCategory(category);
    }
    
    public List<PluginInfo> searchPluginsByTags(String... tags) {
        return marketplace.searchPluginsByTags(tags);
    }
    
    public List<PluginInfo> getPopularPlugins(int limit) {
        return marketplace.getPopularPlugins(limit);
    }
    
    public List<PluginInfo> getLatestPlugins(int limit) {
        return marketplace.getLatestPlugins(limit);
    }
    
    public List<PluginInfo> getCertifiedPlugins() {
        return marketplace.getCertifiedPlugins();
    }
    
    public List<PluginInfo> getFeaturedPlugins() {
        return marketplace.getFeaturedPlugins();
    }
    
    public List<String> getCategories() {
        return marketplace.getCategories();
    }
    
    public List<String> getPopularTags(int limit) {
        return marketplace.getPopularTags(limit);
    }
    
    public boolean installPlugin(String pluginId) {
        return marketplace.installPlugin(pluginId);
    }
    
    public boolean installPlugin(String pluginId, String version) {
        return marketplace.installPlugin(pluginId, version);
    }
    
    public boolean updatePlugin(String pluginId) {
        return marketplace.updatePlugin(pluginId);
    }
    
    public boolean uninstallPlugin(String pluginId) {
        return marketplace.uninstallPlugin(pluginId);
    }
    
    public Optional<PluginInfo> getInstalledPlugin(String pluginId) {
        return marketplace.getInstalledPlugin(pluginId);
    }
    
    public List<PluginInfo> getInstalledPlugins() {
        return marketplace.getInstalledPlugins();
    }
    
    public List<PluginInfo> getUpdatesAvailable() {
        return marketplace.getUpdatesAvailable();
    }
    
    public void addPluginRepository(PluginRepository repository) {
        marketplace.addPluginRepository(repository);
    }
    
    public void removePluginRepository(String repositoryId) {
        marketplace.removePluginRepository(repositoryId);
    }
    
    public List<PluginRepository> getRepositories() {
        return marketplace.getRepositories();
    }
    
    public String listPluginsAsString() {
        StringBuilder sb = new StringBuilder();
        List<PluginInfo> installed = getInstalledPlugins();
        if (installed.isEmpty()) {
            return "没有安装任何插件";
        }
        
        sb.append("已安装插件 (").append(installed.size()).append("):\n");
        for (PluginInfo plugin : installed) {
            sb.append("  - ").append(plugin.getName())
              .append(" v").append(plugin.getVersion())
              .append(" (").append(plugin.getDescription()).append(")\n");
        }
        return sb.toString();
    }
    
    public String searchPluginsAsString(String query) {
        List<PluginInfo> results = searchPlugins(query);
        if (results.isEmpty()) {
            return "未找到匹配的插件: " + query;
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("搜索结果 (").append(results.size()).append("):\n");
        for (PluginInfo plugin : results) {
            sb.append("  - ").append(plugin.getName())
              .append(" v").append(plugin.getVersion());
            if (plugin.isCertified()) {
                sb.append(" [认证]");
            }
            sb.append("\n    ").append(plugin.getDescription()).append("\n");
        }
        return sb.toString();
    }
}
