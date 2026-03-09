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
