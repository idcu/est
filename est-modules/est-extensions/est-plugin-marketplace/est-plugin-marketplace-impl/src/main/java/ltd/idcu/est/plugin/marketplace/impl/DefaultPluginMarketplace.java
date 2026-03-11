package ltd.idcu.est.plugin.marketplace.impl;

import ltd.idcu.est.plugin.api.PluginInfo;
import ltd.idcu.est.plugin.marketplace.api.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class DefaultPluginMarketplace implements PluginMarketplace {
    
    private final List<PluginRepository> repositories = new ArrayList<>();
    private final Map<String, PluginInfo> installedPlugins = new ConcurrentHashMap<>();
    private String localCacheDirectory;
    
    public DefaultPluginMarketplace() {
    }
    
    @Override
    public Optional<PluginInfo> getPlugin(String pluginId) {
        for (PluginRepository repo : repositories) {
            if (!repo.isEnabled()) continue;
            Optional<PluginInfo> plugin = repo.getPlugin(pluginId);
            if (plugin.isPresent()) {
                return plugin;
            }
        }
        return Optional.empty();
    }
    
    @Override
    public List<PluginInfo> searchPlugins(String query) {
        Set<PluginInfo> results = new LinkedHashSet<>();
        String lowerQuery = query.toLowerCase();
        for (PluginRepository repo : repositories) {
            if (!repo.isEnabled()) continue;
            results.addAll(repo.searchPlugins(query));
        }
        return new ArrayList<>(results);
    }
    
    @Override
    public List<PluginInfo> searchPluginsByCategory(String category) {
        Set<PluginInfo> results = new LinkedHashSet<>();
        for (PluginRepository repo : repositories) {
            if (!repo.isEnabled()) continue;
            results.addAll(repo.getAllPlugins().stream()
                .filter(p -> category.equals(p.getCategory()))
                .collect(Collectors.toList()));
        }
        return new ArrayList<>(results);
    }
    
    @Override
    public List<PluginInfo> searchPluginsByTags(String... tags) {
        Set<PluginInfo> results = new LinkedHashSet<>();
        Set<String> tagSet = new HashSet<>(Arrays.asList(tags));
        for (PluginRepository repo : repositories) {
            if (!repo.isEnabled()) continue;
            results.addAll(repo.getAllPlugins().stream()
                .filter(p -> Arrays.stream(p.getTags()).anyMatch(tagSet::contains))
                .collect(Collectors.toList()));
        }
        return new ArrayList<>(results);
    }
    
    @Override
    public SearchResult searchPlugins(PluginSearchQuery query) {
        List<PluginInfo> allPlugins = getAllPluginsFromRepos();
        
        List<PluginInfo> filtered = allPlugins.stream()
            .filter(p -> matchesQuery(p, query))
            .collect(Collectors.toList());
        
        List<PluginInfo> sorted = sortPlugins(filtered, query.getSortBy());
        
        int totalCount = sorted.size();
        int fromIndex = query.getPage() * query.getPageSize();
        int toIndex = Math.min(fromIndex + query.getPageSize(), sorted.size());
        
        if (fromIndex >= sorted.size()) {
            return SearchResult.builder()
                .plugins(Collections.emptyList())
                .totalCount(totalCount)
                .page(query.getPage())
                .pageSize(query.getPageSize())
                .build();
        }
        
        return SearchResult.builder()
            .plugins(sorted.subList(fromIndex, toIndex))
            .totalCount(totalCount)
            .page(query.getPage())
            .pageSize(query.getPageSize())
            .build();
    }
    
    private boolean matchesQuery(PluginInfo plugin, PluginSearchQuery query) {
        if (query.getKeyword() != null && !query.getKeyword().isEmpty()) {
            String lowerKeyword = query.getKeyword().toLowerCase();
            boolean matchesName = plugin.getName().toLowerCase().contains(lowerKeyword);
            boolean matchesDesc = plugin.getDescription() != null && 
                                  plugin.getDescription().toLowerCase().contains(lowerKeyword);
            boolean matchesTags = Arrays.stream(plugin.getTags())
                .anyMatch(t -> t.toLowerCase().contains(lowerKeyword));
            if (!matchesName && !matchesDesc && !matchesTags) {
                return false;
            }
        }
        
        if (query.getCategory() != null && !query.getCategory().equals(plugin.getCategory())) {
            return false;
        }
        
        if (!query.getTags().isEmpty()) {
            boolean hasAnyTag = query.getTags().stream()
                .anyMatch(t -> Arrays.asList(plugin.getTags()).contains(t));
            if (!hasAnyTag) {
                return false;
            }
        }
        
        if (query.getCertified() != null && query.getCertified() != plugin.isCertified()) {
            return false;
        }
        
        if (query.getLicense() != null && !query.getLicense().equals(plugin.getLicense())) {
            return false;
        }
        
        return true;
    }
    
    private List<PluginInfo> sortPlugins(List<PluginInfo> plugins, String sortBy) {
        Comparator<PluginInfo> comparator;
        switch (sortBy != null ? sortBy : "relevance") {
            case "downloads":
                comparator = Comparator.comparingInt(PluginInfo::getDownloadCount).reversed();
                break;
            case "rating":
                comparator = Comparator.comparingDouble(PluginInfo::getRating).reversed();
                break;
            case "updated":
                comparator = Comparator.comparingLong(PluginInfo::getLastUpdateTime).reversed();
                break;
            case "created":
                comparator = Comparator.comparingLong(PluginInfo::getPublishTime).reversed();
                break;
            case "name":
                comparator = Comparator.comparing(PluginInfo::getName);
                break;
            default:
                comparator = Comparator.comparingInt(PluginInfo::getDownloadCount).reversed();
        }
        return plugins.stream().sorted(comparator).collect(Collectors.toList());
    }
    
    @Override
    public List<PluginInfo> getPopularPlugins(int limit) {
        List<PluginInfo> all = getAllPluginsFromRepos();
        return all.stream()
            .sorted(Comparator.comparingInt(PluginInfo::getDownloadCount).reversed())
            .limit(limit)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<PluginInfo> getLatestPlugins(int limit) {
        List<PluginInfo> all = getAllPluginsFromRepos();
        return all.stream()
            .sorted(Comparator.comparingLong(PluginInfo::getPublishTime).reversed())
            .limit(limit)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<PluginInfo> getCertifiedPlugins() {
        return getAllPluginsFromRepos().stream()
            .filter(PluginInfo::isCertified)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<PluginInfo> getFeaturedPlugins() {
        return getPopularPlugins(10);
    }
    
    @Override
    public List<String> getCategories() {
        return getAllPluginsFromRepos().stream()
            .map(PluginInfo::getCategory)
            .filter(Objects::nonNull)
            .distinct()
            .collect(Collectors.toList());
    }
    
    @Override
    public List<PluginCategory> getPluginCategories() {
        Map<String, Integer> categoryCounts = new HashMap<>();
        for (PluginInfo plugin : getAllPluginsFromRepos()) {
            String category = plugin.getCategory();
            if (category != null) {
                categoryCounts.merge(category, 1, Integer::sum);
            }
        }
        
        return categoryCounts.entrySet().stream()
            .map(entry -> PluginCategory.builder()
                .id(entry.getKey())
                .name(entry.getKey())
                .description(entry.getKey() + " plugins")
                .icon(getCategoryIcon(entry.getKey()))
                .pluginCount(entry.getValue())
                .build())
            .sorted(Comparator.comparing(PluginCategory::getName))
            .collect(Collectors.toList());
    }
    
    private String getCategoryIcon(String category) {
        return switch (category != null ? category.toLowerCase() : "") {
            case "database" -> "🗄️";
            case "web" -> "🌐";
            case "security" -> "🔒";
            case "ai" -> "🤖";
            case "cloud" -> "☁️";
            case "microservices" -> "⚙️";
            case "testing" -> "🧪";
            case "monitoring" -> "📊";
            default -> "📦";
        };
    }
    
    @Override
    public List<String> getPopularTags(int limit) {
        Map<String, Integer> tagCount = new HashMap<>();
        for (PluginInfo plugin : getAllPluginsFromRepos()) {
            for (String tag : plugin.getTags()) {
                tagCount.merge(tag, 1, Integer::sum);
            }
        }
        return tagCount.entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .limit(limit)
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<String> getSearchSuggestions(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return Collections.emptyList();
        }
        
        String lowerKeyword = keyword.toLowerCase();
        Set<String> suggestions = new LinkedHashSet<>();
        
        for (PluginInfo plugin : getAllPluginsFromRepos()) {
            if (plugin.getName().toLowerCase().startsWith(lowerKeyword)) {
                suggestions.add(plugin.getName());
            }
            for (String tag : plugin.getTags()) {
                if (tag.toLowerCase().startsWith(lowerKeyword)) {
                    suggestions.add(tag);
                }
            }
        }
        
        return suggestions.stream().limit(10).collect(Collectors.toList());
    }
    
    @Override
    public boolean installPlugin(String pluginId) {
        Optional<PluginInfo> pluginOpt = getPlugin(pluginId);
        if (pluginOpt.isEmpty()) {
            return false;
        }
        installedPlugins.put(pluginId, pluginOpt.get());
        return true;
    }
    
    @Override
    public boolean installPlugin(String pluginId, String version) {
        for (PluginRepository repo : repositories) {
            if (!repo.isEnabled()) continue;
            Optional<PluginInfo> pluginOpt = repo.getPlugin(pluginId, version);
            if (pluginOpt.isPresent()) {
                installedPlugins.put(pluginId, pluginOpt.get());
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean updatePlugin(String pluginId) {
        Optional<PluginInfo> latestOpt = getPlugin(pluginId);
        if (latestOpt.isEmpty()) {
            return false;
        }
        installedPlugins.put(pluginId, latestOpt.get());
        return true;
    }
    
    @Override
    public boolean uninstallPlugin(String pluginId) {
        return installedPlugins.remove(pluginId) != null;
    }
    
    @Override
    public Optional<PluginInfo> getInstalledPlugin(String pluginId) {
        return Optional.ofNullable(installedPlugins.get(pluginId));
    }
    
    @Override
    public List<PluginInfo> getInstalledPlugins() {
        return new ArrayList<>(installedPlugins.values());
    }
    
    @Override
    public List<PluginInfo> getUpdatesAvailable() {
        List<PluginInfo> updates = new ArrayList<>();
        for (Map.Entry<String, PluginInfo> entry : installedPlugins.entrySet()) {
            Optional<PluginInfo> latestOpt = getPlugin(entry.getKey());
            if (latestOpt.isPresent()) {
                PluginInfo latest = latestOpt.get();
                PluginInfo installed = entry.getValue();
                if (!latest.getVersion().equals(installed.getVersion())) {
                    updates.add(latest);
                }
            }
        }
        return updates;
    }
    
    @Override
    public void addPluginRepository(PluginRepository repository) {
        repositories.add(repository);
    }
    
    @Override
    public void removePluginRepository(String repositoryId) {
        repositories.removeIf(r -> repositoryId.equals(r.getId()));
    }
    
    @Override
    public List<PluginRepository> getRepositories() {
        return new ArrayList<>(repositories);
    }
    
    private List<PluginInfo> getAllPluginsFromRepos() {
        Set<PluginInfo> all = new LinkedHashSet<>();
        for (PluginRepository repo : repositories) {
            if (!repo.isEnabled()) continue;
            all.addAll(repo.getAllPlugins());
        }
        return new ArrayList<>(all);
    }
    
    public static class Builder implements PluginMarketplace.Builder {
        private final DefaultPluginMarketplace marketplace = new DefaultPluginMarketplace();
        
        @Override
        public PluginMarketplace.Builder addRepository(PluginRepository repository) {
            marketplace.addPluginRepository(repository);
            return this;
        }
        
        @Override
        public PluginMarketplace.Builder localCacheDirectory(String directory) {
            marketplace.localCacheDirectory = directory;
            return this;
        }
        
        @Override
        public PluginMarketplace build() {
            return marketplace;
        }
    }
}
