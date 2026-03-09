package ltd.idcu.est.plugin.impl.marketplace;

import ltd.idcu.est.plugin.api.PluginInfo;
import ltd.idcu.est.plugin.api.marketplace.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class DefaultPluginMarketplace implements PluginMarketplace {
    
    private final Map<String, PluginInfo> plugins = new ConcurrentHashMap<>();
    private final Map<String, List<PluginVersion>> pluginVersions = new ConcurrentHashMap<>();
    private final Map<String, List<PluginReview>> pluginReviews = new ConcurrentHashMap<>();
    private final List<PluginCategory> categories = new ArrayList<>();
    
    public DefaultPluginMarketplace() {
        initializeDefaultCategories();
    }
    
    private void initializeDefaultCategories() {
        categories.add(PluginCategory.builder()
                .id("web")
                .name("Web开发")
                .description("Web框架、路由、模板等")
                .icon("🌐")
                .pluginCount(0)
                .build());
        categories.add(PluginCategory.builder()
                .id("database")
                .name("数据库")
                .description("数据库驱动、ORM、缓存等")
                .icon("🗄️")
                .pluginCount(0)
                .build());
        categories.add(PluginCategory.builder()
                .id("security")
                .name("安全")
                .description("认证、授权、加密等")
                .icon("🔒")
                .pluginCount(0)
                .build());
        categories.add(PluginCategory.builder()
                .id("ai")
                .name("AI")
                .description("AI助手、LLM集成、代码生成等")
                .icon("🤖")
                .pluginCount(0)
                .build());
        categories.add(PluginCategory.builder()
                .id("monitoring")
                .name("监控")
                .description("性能监控、日志、追踪等")
                .icon("📊")
                .pluginCount(0)
                .build());
        categories.add(PluginCategory.builder()
                .id("tools")
                .name("工具")
                .description("开发工具、代码生成等")
                .icon("🛠️")
                .pluginCount(0)
                .build());
    }
    
    @Override
    public List<PluginInfo> searchPlugins(PluginSearchCriteria criteria) {
        return plugins.values().stream()
                .filter(plugin -> filterByCriteria(plugin, criteria))
                .sorted((p1, p2) -> sortByCriteria(p1, p2, criteria))
                .skip(criteria.getPage() * criteria.getPageSize())
                .limit(criteria.getPageSize())
                .collect(Collectors.toList());
    }
    
    private boolean filterByCriteria(PluginInfo plugin, PluginSearchCriteria criteria) {
        if (criteria.getKeyword() != null && !criteria.getKeyword().isEmpty()) {
            String keyword = criteria.getKeyword().toLowerCase();
            if (!plugin.getName().toLowerCase().contains(keyword) && 
                !plugin.getDescription().toLowerCase().contains(keyword)) {
                return false;
            }
        }
        
        if (criteria.getCategory() != null && !criteria.getCategory().isEmpty()) {
            if (!criteria.getCategory().equals(plugin.getCategory())) {
                return false;
            }
        }
        
        if (criteria.getTags() != null && criteria.getTags().length > 0) {
            Set<String> pluginTags = new HashSet<>(Arrays.asList(plugin.getTags()));
            boolean hasTag = Arrays.stream(criteria.getTags())
                    .anyMatch(pluginTags::contains);
            if (!hasTag) {
                return false;
            }
        }
        
        if (criteria.getAuthor() != null && !criteria.getAuthor().isEmpty()) {
            if (!criteria.getAuthor().equals(plugin.getAuthor())) {
                return false;
            }
        }
        
        if (criteria.getMinRating() != null) {
            if (plugin.getRating() < criteria.getMinRating()) {
                return false;
            }
        }
        
        if (criteria.getMinDownloads() != null) {
            if (plugin.getDownloadCount() < criteria.getMinDownloads()) {
                return false;
            }
        }
        
        if (criteria.getCertifiedOnly() != null && criteria.getCertifiedOnly()) {
            if (!plugin.isCertified()) {
                return false;
            }
        }
        
        return true;
    }
    
    private int sortByCriteria(PluginInfo p1, PluginInfo p2, PluginSearchCriteria criteria) {
        int result;
        switch (criteria.getSortBy()) {
            case NAME:
                result = p1.getName().compareToIgnoreCase(p2.getName());
                break;
            case RATING:
                result = Double.compare(p2.getRating(), p1.getRating());
                break;
            case DOWNLOADS:
                result = Integer.compare(p2.getDownloadCount(), p1.getDownloadCount());
                break;
            case PUBLISH_TIME:
                result = Long.compare(p2.getPublishTime(), p1.getPublishTime());
                break;
            case UPDATE_TIME:
                result = Long.compare(p2.getLastUpdateTime(), p1.getLastUpdateTime());
                break;
            default:
                result = Integer.compare(p2.getDownloadCount(), p1.getDownloadCount());
        }
        
        return criteria.getSortOrder() == PluginSearchCriteria.SortOrder.DESC ? result : -result;
    }
    
    @Override
    public PluginInfo getPlugin(String pluginId) {
        return plugins.get(pluginId);
    }
    
    @Override
    public List<PluginInfo> getPluginsByCategory(String category) {
        return plugins.values().stream()
                .filter(p -> category.equals(p.getCategory()))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<PluginCategory> getAllCategories() {
        return new ArrayList<>(categories);
    }
    
    @Override
    public List<PluginInfo> getPopularPlugins(int limit) {
        return plugins.values().stream()
                .sorted((p1, p2) -> Integer.compare(p2.getDownloadCount(), p1.getDownloadCount()))
                .limit(limit)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<PluginInfo> getNewestPlugins(int limit) {
        return plugins.values().stream()
                .sorted((p1, p2) -> Long.compare(p2.getPublishTime(), p1.getPublishTime()))
                .limit(limit)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<PluginInfo> getCertifiedPlugins() {
        return plugins.values().stream()
                .filter(PluginInfo::isCertified)
                .collect(Collectors.toList());
    }
    
    @Override
    public boolean installPlugin(String pluginId) {
        return plugins.containsKey(pluginId);
    }
    
    @Override
    public boolean uninstallPlugin(String pluginId) {
        return plugins.containsKey(pluginId);
    }
    
    @Override
    public boolean updatePlugin(String pluginId) {
        return plugins.containsKey(pluginId);
    }
    
    @Override
    public List<PluginVersion> getPluginVersions(String pluginId) {
        return pluginVersions.getOrDefault(pluginId, Collections.emptyList());
    }
    
    @Override
    public List<PluginReview> getPluginReviews(String pluginId) {
        return pluginReviews.getOrDefault(pluginId, Collections.emptyList());
    }
    
    @Override
    public boolean addReview(String pluginId, PluginReview review) {
        if (!plugins.containsKey(pluginId)) {
            return false;
        }
        pluginReviews.computeIfAbsent(pluginId, k -> new ArrayList<>()).add(review);
        return true;
    }
    
    public void registerPlugin(PluginInfo plugin) {
        plugins.put(plugin.getName(), plugin);
    }
    
    public void registerPluginVersion(String pluginId, PluginVersion version) {
        pluginVersions.computeIfAbsent(pluginId, k -> new ArrayList<>()).add(version);
    }
}
