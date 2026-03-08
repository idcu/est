package ltd.idcu.est.plugin.impl;

import ltd.idcu.est.plugin.api.Plugin;
import ltd.idcu.est.plugin.api.PluginConfig;
import ltd.idcu.est.plugin.api.PluginListener;
import ltd.idcu.est.plugin.api.PluginLoader;
import ltd.idcu.est.plugin.api.PluginManager;
import ltd.idcu.est.plugin.api.PluginState;
import ltd.idcu.est.plugin.api.PluginStats;

import java.util.List;
import java.util.Optional;

public final class Plugins {

    private static volatile PluginManager defaultManager;

    private Plugins() {
    }

    public static PluginManager createManager() {
        return new DefaultPluginManager();
    }

    public static PluginManager createManager(PluginConfig config) {
        return new DefaultPluginManager(config);
    }

    public static PluginManager getDefaultManager() {
        if (defaultManager == null) {
            synchronized (Plugins.class) {
                if (defaultManager == null) {
                    defaultManager = createManager();
                }
            }
        }
        return defaultManager;
    }

    public static void setDefaultManager(PluginManager manager) {
        defaultManager = manager;
    }

    public static Plugin load(String pluginPath) {
        return getDefaultManager().loadPlugin(pluginPath);
    }

    public static Plugin loadFromClass(Class<? extends Plugin> pluginClass) {
        return getDefaultManager().loadPluginFromClass(pluginClass);
    }

    public static void unload(String pluginName) {
        getDefaultManager().unloadPlugin(pluginName);
    }

    public static void unload(Plugin plugin) {
        getDefaultManager().unloadPlugin(plugin);
    }

    public static void start(String pluginName) {
        getDefaultManager().startPlugin(pluginName);
    }

    public static void stop(String pluginName) {
        getDefaultManager().stopPlugin(pluginName);
    }

    public static void startAll() {
        getDefaultManager().startAllPlugins();
    }

    public static void stopAll() {
        getDefaultManager().stopAllPlugins();
    }

    public static Optional<Plugin> get(String pluginName) {
        return getDefaultManager().getPlugin(pluginName);
    }

    public static List<Plugin> getAll() {
        return getDefaultManager().getPlugins();
    }

    public static List<Plugin> getByState(PluginState state) {
        return getDefaultManager().getPluginsByState(state);
    }

    public static boolean has(String pluginName) {
        return getDefaultManager().hasPlugin(pluginName);
    }

    public static int count() {
        return getDefaultManager().getPluginCount();
    }

    public static PluginStats stats() {
        return getDefaultManager().getStats();
    }

    public static void addListener(PluginListener listener) {
        getDefaultManager().addListener(listener);
    }

    public static void removeListener(PluginListener listener) {
        getDefaultManager().removeListener(listener);
    }

    public static PluginConfig defaultConfig() {
        return PluginConfig.defaultConfig();
    }

    public static PluginConfig.Builder configBuilder() {
        return PluginConfig.builder();
    }

    public static PluginManager.Builder managerBuilder() {
        return new PluginManagerBuilder();
    }

    private static class PluginManagerBuilder implements PluginManager.Builder {
        private PluginConfig config = PluginConfig.defaultConfig();
        private final java.util.List<PluginLoader> loaders = new java.util.ArrayList<>();
        private final java.util.List<PluginListener> listeners = new java.util.ArrayList<>();

        @Override
        public PluginManager.Builder config(PluginConfig config) {
            this.config = config;
            return this;
        }

        @Override
        public PluginManager.Builder addLoader(PluginLoader loader) {
            this.loaders.add(loader);
            return this;
        }

        @Override
        public PluginManager.Builder addListener(PluginListener listener) {
            this.listeners.add(listener);
            return this;
        }

        @Override
        public PluginManager build() {
            DefaultPluginManager manager = new DefaultPluginManager(config);
            for (PluginLoader loader : loaders) {
                manager.addLoader(loader);
            }
            for (PluginListener listener : listeners) {
                manager.addListener(listener);
            }
            return manager;
        }
    }
}
