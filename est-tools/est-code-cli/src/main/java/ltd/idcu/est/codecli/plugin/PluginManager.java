package ltd.idcu.est.codecli.plugin;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class PluginManager {
    
    private final Path pluginsDir;
    private final Map<String, EstPlugin> plugins;
    private final Map<String, PluginClassLoader> classLoaders;
    private final DefaultPluginContext context;
    private final Map<String, Object> services;
    
    public PluginManager(Path workDir) {
        this.pluginsDir = workDir.resolve(".est-plugins");
        this.plugins = new ConcurrentHashMap<>();
        this.classLoaders = new ConcurrentHashMap<>();
        this.services = new ConcurrentHashMap<>();
        this.context = new DefaultPluginContext(pluginsDir, services);
        ensurePluginsDir();
    }
    
    private void ensurePluginsDir() {
        try {
            if (!Files.exists(pluginsDir)) {
                Files.createDirectories(pluginsDir);
            }
        } catch (IOException e) {
            System.err.println("无法创建插件目录: " + e.getMessage());
        }
    }
    
    public void registerService(String name, Object service) {
        services.put(name, service);
    }
    
    public void loadPlugin(Path pluginPath) throws PluginException {
        String pluginId = pluginPath.getFileName().toString();
        
        if (plugins.containsKey(pluginId)) {
            throw new PluginException("插件已加载: " + pluginId);
        }
        
        try {
            PluginClassLoader classLoader = new PluginClassLoader(
                new URL[]{pluginPath.toUri().toURL()},
                getClass().getClassLoader()
            );
            
            EstPlugin plugin = instantiatePlugin(classLoader, pluginPath);
            
            if (plugin != null) {
                plugin.initialize(context);
                plugins.put(pluginId, plugin);
                classLoaders.put(pluginId, classLoader);
                context.logInfo("插件加载成功: " + plugin.getName() + " v" + plugin.getVersion());
            }
        } catch (Exception e) {
            throw new PluginException("加载插件失败: " + pluginPath, e);
        }
    }
    
    private EstPlugin instantiatePlugin(PluginClassLoader classLoader, Path pluginPath) throws Exception {
        if (pluginPath.toString().endsWith(".jar")) {
            return loadFromJar(classLoader, pluginPath);
        } else if (Files.isDirectory(pluginPath)) {
            return loadFromDirectory(classLoader, pluginPath);
        }
        return null;
    }
    
    private EstPlugin loadFromJar(PluginClassLoader classLoader, Path jarPath) throws Exception {
        try (JarFile jarFile = new JarFile(jarPath.toFile())) {
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (entry.getName().endsWith(".class")) {
                    String className = entry.getName()
                        .replace(".class", "")
                        .replace("/", ".");
                    
                    try {
                        Class<?> clazz = classLoader.loadClass(className);
                        if (EstPlugin.class.isAssignableFrom(clazz)) {
                            return (EstPlugin) clazz.getDeclaredConstructor().newInstance();
                        }
                    } catch (ClassNotFoundException | NoClassDefFoundError ignored) {
                    }
                }
            }
        }
        return null;
    }
    
    private EstPlugin loadFromDirectory(PluginClassLoader classLoader, Path dirPath) throws Exception {
        try (var paths = Files.walk(dirPath)) {
            return paths
                .filter(Files::isRegularFile)
                .filter(p -> p.toString().endsWith(".class"))
                .map(p -> {
                    try {
                        String relativePath = dirPath.relativize(p).toString();
                        String className = relativePath
                            .replace(".class", "")
                            .replace("\\", ".")
                            .replace("/", ".");
                        
                        Class<?> clazz = classLoader.loadClass(className);
                        if (EstPlugin.class.isAssignableFrom(clazz)) {
                            return (EstPlugin) clazz.getDeclaredConstructor().newInstance();
                        }
                    } catch (Exception ignored) {
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
        }
    }
    
    public void loadAllPlugins() {
        if (!Files.exists(pluginsDir)) {
            return;
        }
        
        try {
            Files.list(pluginsDir)
                .filter(path -> path.toString().endsWith(".jar") || Files.isDirectory(path))
                .forEach(path -> {
                    try {
                        loadPlugin(path);
                    } catch (PluginException e) {
                        context.logError("加载插件失败: " + path, e);
                    }
                });
        } catch (IOException e) {
            context.logError("扫描插件目录失败", e);
        }
    }
    
    public void unloadPlugin(String pluginId) throws PluginException {
        EstPlugin plugin = plugins.remove(pluginId);
        if (plugin == null) {
            throw new PluginException("插件不存在: " + pluginId);
        }
        
        try {
            plugin.shutdown();
            PluginClassLoader classLoader = classLoaders.remove(pluginId);
            if (classLoader != null) {
                classLoader.close();
            }
            context.logInfo("插件卸载成功: " + plugin.getName());
        } catch (Exception e) {
            throw new PluginException("卸载插件失败: " + pluginId, e);
        }
    }
    
    public void unloadAllPlugins() {
        new ArrayList<>(plugins.keySet()).forEach(pluginId -> {
            try {
                unloadPlugin(pluginId);
            } catch (PluginException e) {
                context.logError("卸载插件失败: " + pluginId, e);
            }
        });
    }
    
    public EstPlugin getPlugin(String pluginId) {
        return plugins.get(pluginId);
    }
    
    public List<EstPlugin> getAllPlugins() {
        return new ArrayList<>(plugins.values());
    }
    
    public List<EstPlugin> getEnabledPlugins() {
        return plugins.values().stream()
            .filter(EstPlugin::isEnabled)
            .toList();
    }
    
    public Path getPluginsDir() {
        return pluginsDir;
    }
    
    public void shutdown() {
        unloadAllPlugins();
    }
    
    private static class PluginClassLoader extends URLClassLoader {
        public PluginClassLoader(URL[] urls, ClassLoader parent) {
            super(urls, parent);
        }
        
        @Override
        public void close() throws IOException {
            super.close();
        }
    }
    
    private static class DefaultPluginContext implements PluginContext {
        private final Path pluginDataDir;
        private final Map<String, Object> services;
        private final Map<String, Object> config;
        
        public DefaultPluginContext(Path pluginDataDir, Map<String, Object> services) {
            this.pluginDataDir = pluginDataDir;
            this.services = services;
            this.config = new ConcurrentHashMap<>();
        }
        
        @Override
        public Path getPluginDataDir() {
            return pluginDataDir;
        }
        
        @Override
        public Path getConfigDir() {
            return pluginDataDir.getParent().resolve(".config");
        }
        
        @Override
        public Path getWorkDir() {
            return pluginDataDir.getParent();
        }
        
        @Override
        public Object getService(String serviceName) {
            return services.get(serviceName);
        }
        
        @Override
        @SuppressWarnings("unchecked")
        public <T> T getService(Class<T> serviceClass) {
            return services.values().stream()
                .filter(serviceClass::isInstance)
                .map(serviceClass::cast)
                .findFirst()
                .orElse(null);
        }
        
        @Override
        public void registerService(String serviceName, Object service) {
            services.put(serviceName, service);
        }
        
        @Override
        public void logInfo(String message) {
            System.out.println("[Plugin] " + message);
        }
        
        @Override
        public void logWarn(String message) {
            System.err.println("[Plugin WARN] " + message);
        }
        
        @Override
        public void logError(String message) {
            System.err.println("[Plugin ERROR] " + message);
        }
        
        @Override
        public void logError(String message, Throwable throwable) {
            System.err.println("[Plugin ERROR] " + message);
            throwable.printStackTrace();
        }
        
        @Override
        public Map<String, Object> getConfig() {
            return new HashMap<>(config);
        }
        
        @Override
        public Object getConfig(String key) {
            return config.get(key);
        }
        
        @Override
        public void setConfig(String key, Object value) {
            config.put(key, value);
        }
    }
}
