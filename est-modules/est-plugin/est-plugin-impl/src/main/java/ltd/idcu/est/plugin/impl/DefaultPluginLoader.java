package ltd.idcu.est.plugin.impl;

import ltd.idcu.est.plugin.api.AbstractPlugin;
import ltd.idcu.est.plugin.api.Plugin;
import ltd.idcu.est.plugin.api.PluginException;
import ltd.idcu.est.plugin.api.PluginInfo;
import ltd.idcu.est.plugin.api.PluginLoader;
import ltd.idcu.est.utils.common.AssertUtils;
import ltd.idcu.est.utils.common.ClassUtils;

import java.io.File;
import java.util.Optional;

public class DefaultPluginLoader implements PluginLoader {

    @Override
    public Plugin load(String pluginPath) throws PluginException {
        AssertUtils.notBlank(pluginPath, "Plugin path must not be blank");
        
        try {
            Class<?> clazz = ClassUtils.forName(pluginPath);
            if (!Plugin.class.isAssignableFrom(clazz)) {
                throw new PluginException("Class does not implement Plugin: " + pluginPath);
            }
            
            @SuppressWarnings("unchecked")
            Class<? extends Plugin> pluginClass = (Class<? extends Plugin>) clazz;
            return loadFromClass(pluginClass);
        } catch (ClassNotFoundException e) {
            throw new PluginException("Plugin class not found: " + pluginPath, e);
        } catch (Exception e) {
            throw new PluginException("Failed to load plugin: " + pluginPath, e);
        }
    }

    public Plugin loadFromClass(Class<? extends Plugin> pluginClass) throws PluginException {
        AssertUtils.notNull(pluginClass, "Plugin class must not be null");
        
        try {
            PluginInfo info = extractPluginInfo(pluginClass);
            Plugin plugin = createPluginInstance(pluginClass, info);
            plugin.onLoad();
            return plugin;
        } catch (PluginException e) {
            throw e;
        } catch (Exception e) {
            throw new PluginException("Failed to load plugin from class: " + pluginClass.getName(), e);
        }
    }

    @Override
    public void unload(Plugin plugin) throws PluginException {
        AssertUtils.notNull(plugin, "Plugin must not be null");
        
        try {
            plugin.onUnload();
        } catch (Exception e) {
            throw new PluginException(plugin.getName(), "Failed to unload plugin", e);
        }
    }

    @Override
    public Optional<PluginInfo> loadInfo(String pluginPath) {
        AssertUtils.notBlank(pluginPath, "Plugin path must not be blank");
        
        try {
            Class<?> clazz = ClassUtils.forName(pluginPath);
            if (!Plugin.class.isAssignableFrom(clazz)) {
                return Optional.empty();
            }
            
            @SuppressWarnings("unchecked")
            Class<? extends Plugin> pluginClass = (Class<? extends Plugin>) clazz;
            return Optional.of(extractPluginInfo(pluginClass));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean isValidPlugin(String pluginPath) {
        AssertUtils.notBlank(pluginPath, "Plugin path must not be blank");
        
        try {
            Class<?> clazz = ClassUtils.forName(pluginPath);
            return Plugin.class.isAssignableFrom(clazz) && 
                   !clazz.isInterface() && 
                   !java.lang.reflect.Modifier.isAbstract(clazz.getModifiers());
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String[] scanDirectory(String directory) {
        AssertUtils.notBlank(directory, "Directory must not be blank");
        
        File dir = new File(directory);
        if (!dir.exists() || !dir.isDirectory()) {
            return new String[0];
        }
        
        java.util.List<String> pluginClasses = new java.util.ArrayList<>();
        scanDirectoryForPlugins(dir, "", pluginClasses);
        
        return pluginClasses.toArray(new String[0]);
    }

    private PluginInfo extractPluginInfo(Class<? extends Plugin> pluginClass) {
        return PluginInfo.builder()
                .name(pluginClass.getSimpleName())
                .version("1.0.0")
                .description("Plugin: " + pluginClass.getSimpleName())
                .author("Unknown")
                .mainClass(pluginClass.getName())
                .build();
    }

    private Plugin createPluginInstance(Class<? extends Plugin> pluginClass, PluginInfo info) throws Exception {
        try {
            Plugin plugin = ClassUtils.newInstance(pluginClass);
            
            if (plugin instanceof AbstractPlugin abstractPlugin) {
                setAbstractPluginInfo(abstractPlugin, info);
            }
            
            return plugin;
        } catch (Exception e) {
            throw new PluginException("Failed to create plugin instance: " + pluginClass.getName(), e);
        }
    }

    private void setAbstractPluginInfo(AbstractPlugin plugin, PluginInfo info) {
        try {
            java.lang.reflect.Field infoField = AbstractPlugin.class.getDeclaredField("info");
            infoField.setAccessible(true);
            infoField.set(plugin, info);
        } catch (Exception ignored) {
        }
    }

    private void scanDirectoryForPlugins(File dir, String packageName, java.util.List<String> pluginClasses) {
        File[] files = dir.listFiles();
        if (files == null) {
            return;
        }
        
        for (File file : files) {
            if (file.isDirectory()) {
                String subPackage = packageName.isEmpty() ? file.getName() : packageName + "." + file.getName();
                scanDirectoryForPlugins(file, subPackage, pluginClasses);
            } else if (file.getName().endsWith(".class")) {
                String className = packageName.isEmpty() 
                        ? file.getName().replace(".class", "")
                        : packageName + "." + file.getName().replace(".class", "");
                
                if (isValidPlugin(className)) {
                    pluginClasses.add(className);
                }
            }
        }
    }
}
