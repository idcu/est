package ltd.idcu.est.plugin.impl;

import ltd.idcu.est.plugin.api.Plugin;
import ltd.idcu.est.plugin.api.PluginConfig;
import ltd.idcu.est.plugin.api.PluginException;
import ltd.idcu.est.plugin.api.PluginInfo;
import ltd.idcu.est.plugin.api.PluginListener;
import ltd.idcu.est.plugin.api.PluginLoader;
import ltd.idcu.est.plugin.api.PluginManager;
import ltd.idcu.est.plugin.api.PluginState;
import ltd.idcu.est.plugin.api.PluginStats;
import ltd.idcu.est.utils.common.AssertUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

public class DefaultPluginManager implements PluginManager {

    private final Map<String, Plugin> plugins = new ConcurrentHashMap<>();
    private final Map<String, ClassLoader> classLoaders = new ConcurrentHashMap<>();
    private final List<PluginListener> listeners = new CopyOnWriteArrayList<>();
    private final List<PluginLoader> loaders = new CopyOnWriteArrayList<>();
    private final PluginConfig config;
    private final AtomicLong totalLoadTime = new AtomicLong(0);
    private final AtomicLong totalStartTime = new AtomicLong(0);

    public DefaultPluginManager() {
        this(PluginConfig.defaultConfig());
    }

    public DefaultPluginManager(PluginConfig config) {
        AssertUtils.notNull(config, "Plugin config must not be null");
        this.config = config;
        this.loaders.add(new DefaultPluginLoader());
        this.loaders.add(new JarPluginLoader());
    }

    @Override
    public Plugin loadPlugin(String pluginPath) {
        AssertUtils.notBlank(pluginPath, "Plugin path must not be blank");
        
        checkMaxPlugins();
        
        long startTime = System.currentTimeMillis();
        
        for (PluginLoader loader : loaders) {
            if (loader.isValidPlugin(pluginPath)) {
                try {
                    Plugin plugin = loader.load(pluginPath);
                    registerPlugin(plugin, loader.getClass().getClassLoader());
                    totalLoadTime.addAndGet(System.currentTimeMillis() - startTime);
                    return plugin;
                } catch (PluginException e) {
                    throw e;
                } catch (Exception e) {
                    throw PluginException.loadFailed(pluginPath, e);
                }
            }
        }
        
        throw new PluginException("No suitable loader found for plugin: " + pluginPath);
    }

    @Override
    public Plugin loadPluginFromClass(Class<? extends Plugin> pluginClass) {
        AssertUtils.notNull(pluginClass, "Plugin class must not be null");
        
        checkMaxPlugins();
        
        long startTime = System.currentTimeMillis();
        
        try {
            DefaultPluginLoader loader = new DefaultPluginLoader();
            Plugin plugin = loader.loadFromClass(pluginClass);
            registerPlugin(plugin, pluginClass.getClassLoader());
            totalLoadTime.addAndGet(System.currentTimeMillis() - startTime);
            return plugin;
        } catch (Exception e) {
            throw PluginException.loadFailed(pluginClass.getName(), e);
        }
    }

    private void registerPlugin(Plugin plugin, ClassLoader classLoader) {
        String pluginName = plugin.getName();
        
        if (plugins.containsKey(pluginName)) {
            throw PluginException.alreadyLoaded(pluginName);
        }
        
        checkDependencies(plugin);
        
        plugins.put(pluginName, plugin);
        classLoaders.put(pluginName, classLoader);
        
        notifyListeners(l -> l.onPluginLoaded(plugin));
        
        try {
            plugin.initialize();
            notifyListeners(l -> l.onPluginInitialized(plugin));
            
            if (config.isAutoStart()) {
                startPlugin(pluginName);
            }
        } catch (Exception e) {
            plugins.remove(pluginName);
            classLoaders.remove(pluginName);
            notifyListeners(l -> l.onPluginError(plugin, e));
            throw e;
        }
    }

    @Override
    public void unloadPlugin(String pluginName) {
        AssertUtils.notBlank(pluginName, "Plugin name must not be blank");
        
        Plugin plugin = getPluginOrThrow(pluginName);
        unloadPlugin(plugin);
    }

    @Override
    public void unloadPlugin(Plugin plugin) {
        AssertUtils.notNull(plugin, "Plugin must not be null");
        
        String pluginName = plugin.getName();
        
        if (plugin.getState().isRunning()) {
            stopPlugin(pluginName);
        }
        
        try {
            plugin.onUnload();
        } catch (Exception e) {
            notifyListeners(l -> l.onPluginError(plugin, e));
        }
        
        plugins.remove(pluginName);
        classLoaders.remove(pluginName);
        
        notifyListeners(l -> l.onPluginUnloaded(plugin));
    }

    @Override
    public void startPlugin(String pluginName) {
        AssertUtils.notBlank(pluginName, "Plugin name must not be blank");
        
        Plugin plugin = getPluginOrThrow(pluginName);
        
        if (!plugin.getState().canStart()) {
            throw PluginException.invalidState(pluginName, plugin.getState(), PluginState.INITIALIZED);
        }
        
        checkDependenciesForStart(plugin);
        
        long startTime = System.currentTimeMillis();
        
        try {
            plugin.start();
            totalStartTime.addAndGet(System.currentTimeMillis() - startTime);
            notifyListeners(l -> l.onPluginStarted(plugin));
        } catch (Exception e) {
            notifyListeners(l -> l.onPluginError(plugin, e));
            throw e;
        }
    }

    @Override
    public void stopPlugin(String pluginName) {
        AssertUtils.notBlank(pluginName, "Plugin name must not be blank");
        
        Plugin plugin = getPluginOrThrow(pluginName);
        
        if (!plugin.getState().canStop()) {
            throw PluginException.invalidState(pluginName, plugin.getState(), PluginState.RUNNING);
        }
        
        try {
            plugin.stop();
            notifyListeners(l -> l.onPluginStopped(plugin));
        } catch (Exception e) {
            notifyListeners(l -> l.onPluginError(plugin, e));
            throw e;
        }
    }

    @Override
    public void startAllPlugins() {
        List<String> pluginNames = new ArrayList<>(plugins.keySet());
        List<String> sortedPlugins = sortPluginsByDependencies(pluginNames);
        
        for (String pluginName : sortedPlugins) {
            try {
                Plugin plugin = plugins.get(pluginName);
                if (plugin != null && plugin.getState().canStart()) {
                    startPlugin(pluginName);
                }
            } catch (Exception e) {
                if (config.isFailFast()) {
                    throw e;
                }
            }
        }
    }

    @Override
    public void stopAllPlugins() {
        List<String> pluginNames = new ArrayList<>(plugins.keySet());
        List<String> sortedPlugins = sortPluginsByDependenciesReverse(pluginNames);
        
        for (String pluginName : sortedPlugins) {
            try {
                Plugin plugin = plugins.get(pluginName);
                if (plugin != null && plugin.getState().canStop()) {
                    stopPlugin(pluginName);
                }
            } catch (Exception e) {
                if (config.isFailFast()) {
                    throw e;
                }
            }
        }
    }

    @Override
    public Optional<Plugin> getPlugin(String pluginName) {
        return Optional.ofNullable(plugins.get(pluginName));
    }

    @Override
    public List<Plugin> getPlugins() {
        return Collections.unmodifiableList(new ArrayList<>(plugins.values()));
    }

    @Override
    public List<Plugin> getPluginsByState(PluginState state) {
        AssertUtils.notNull(state, "Plugin state must not be null");
        
        List<Plugin> result = new ArrayList<>();
        for (Plugin plugin : plugins.values()) {
            if (plugin.getState() == state) {
                result.add(plugin);
            }
        }
        return result;
    }

    @Override
    public boolean hasPlugin(String pluginName) {
        return plugins.containsKey(pluginName);
    }

    @Override
    public int getPluginCount() {
        return plugins.size();
    }

    @Override
    public void addListener(PluginListener listener) {
        AssertUtils.notNull(listener, "Plugin listener must not be null");
        listeners.add(listener);
    }

    @Override
    public void removeListener(PluginListener listener) {
        listeners.remove(listener);
    }

    @Override
    public PluginStats getStats() {
        int total = plugins.size();
        int running = 0;
        int stopped = 0;
        int error = 0;
        
        for (Plugin plugin : plugins.values()) {
            PluginState state = plugin.getState();
            if (state.isRunning()) {
                running++;
            } else if (state.isStopped()) {
                stopped++;
            } else if (state.isError()) {
                error++;
            }
        }
        
        return PluginStats.builder()
                .totalPlugins(total)
                .runningPlugins(running)
                .stoppedPlugins(stopped)
                .errorPlugins(error)
                .totalLoadTime(totalLoadTime.get())
                .totalStartTime(totalStartTime.get())
                .build();
    }

    public void addLoader(PluginLoader loader) {
        AssertUtils.notNull(loader, "Plugin loader must not be null");
        loaders.add(0, loader);
    }

    public void removeLoader(PluginLoader loader) {
        loaders.remove(loader);
    }

    public ClassLoader getClassLoader(String pluginName) {
        return classLoaders.get(pluginName);
    }

    public PluginConfig getConfig() {
        return config;
    }

    private Plugin getPluginOrThrow(String pluginName) {
        Plugin plugin = plugins.get(pluginName);
        if (plugin == null) {
            throw PluginException.notFound(pluginName);
        }
        return plugin;
    }

    private void checkMaxPlugins() {
        if (plugins.size() >= config.getMaxPlugins()) {
            throw new PluginException("Maximum number of plugins reached: " + config.getMaxPlugins());
        }
    }

    private void checkDependencies(Plugin plugin) {
        PluginInfo info = plugin.getInfo();
        if (info == null) {
            return;
        }
        
        for (String dependency : info.getDependencies()) {
            if (!plugins.containsKey(dependency)) {
                if (config.isFailFast()) {
                    throw PluginException.dependencyNotFound(plugin.getName(), dependency);
                }
            }
        }
    }

    private void checkDependenciesForStart(Plugin plugin) {
        PluginInfo info = plugin.getInfo();
        if (info == null) {
            return;
        }
        
        for (String dependency : info.getDependencies()) {
            Plugin depPlugin = plugins.get(dependency);
            if (depPlugin == null) {
                throw PluginException.dependencyNotFound(plugin.getName(), dependency);
            }
            if (!depPlugin.getState().isRunning()) {
                throw new PluginException(plugin.getName(), 
                    "Required dependency is not running: " + dependency);
            }
        }
    }

    private List<String> sortPluginsByDependencies(List<String> pluginNames) {
        List<String> result = new ArrayList<>();
        java.util.Set<String> visited = new java.util.HashSet<>();
        
        for (String pluginName : pluginNames) {
            visitPlugin(pluginName, visited, result, new java.util.HashSet<>());
        }
        
        return result;
    }

    private List<String> sortPluginsByDependenciesReverse(List<String> pluginNames) {
        List<String> sorted = sortPluginsByDependencies(pluginNames);
        Collections.reverse(sorted);
        return sorted;
    }

    private void visitPlugin(String pluginName, java.util.Set<String> visited, 
                             List<String> result, java.util.Set<String> visiting) {
        if (visited.contains(pluginName)) {
            return;
        }
        
        if (visiting.contains(pluginName)) {
            throw PluginException.circularDependency(pluginName);
        }
        
        visiting.add(pluginName);
        
        Plugin plugin = plugins.get(pluginName);
        if (plugin != null && plugin.getInfo() != null) {
            for (String dependency : plugin.getInfo().getDependencies()) {
                if (plugins.containsKey(dependency)) {
                    visitPlugin(dependency, visited, result, visiting);
                }
            }
        }
        
        visiting.remove(pluginName);
        visited.add(pluginName);
        result.add(pluginName);
    }

    private void notifyListeners(java.util.function.Consumer<PluginListener> action) {
        for (PluginListener listener : listeners) {
            try {
                action.accept(listener);
            } catch (Exception ignored) {
            }
        }
    }
}
