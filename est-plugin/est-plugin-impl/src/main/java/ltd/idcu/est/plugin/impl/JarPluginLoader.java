package ltd.idcu.est.plugin.impl;

import ltd.idcu.est.plugin.api.AbstractPlugin;
import ltd.idcu.est.plugin.api.Plugin;
import ltd.idcu.est.plugin.api.PluginException;
import ltd.idcu.est.plugin.api.PluginInfo;
import ltd.idcu.est.plugin.api.PluginLoader;
import ltd.idcu.est.utils.common.AssertUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarPluginLoader implements PluginLoader {

    private static final String PLUGIN_PROPERTIES_FILE = "plugin.properties";
    private static final String MAIN_CLASS_KEY = "main.class";
    private static final String PLUGIN_NAME_KEY = "plugin.name";
    private static final String PLUGIN_VERSION_KEY = "plugin.version";
    private static final String PLUGIN_DESCRIPTION_KEY = "plugin.description";
    private static final String PLUGIN_AUTHOR_KEY = "plugin.author";
    private static final String PLUGIN_DEPENDENCIES_KEY = "plugin.dependencies";
    private static final String PLUGIN_SOFT_DEPENDENCIES_KEY = "plugin.soft.dependencies";

    @Override
    public Plugin load(String pluginPath) throws PluginException {
        AssertUtils.notBlank(pluginPath, "Plugin path must not be blank");
        
        File jarFile = new File(pluginPath);
        if (!jarFile.exists() || !jarFile.isFile()) {
            throw new PluginException("Plugin file not found: " + pluginPath);
        }
        
        if (!jarFile.getName().toLowerCase().endsWith(".jar")) {
            throw new PluginException("Plugin file must be a JAR file: " + pluginPath);
        }
        
        try {
            PluginInfo info = loadPluginInfoFromJar(jarFile);
            URLClassLoader classLoader = createClassLoader(jarFile);
            
            Class<?> mainClass = classLoader.loadClass(info.getMainClass());
            if (!Plugin.class.isAssignableFrom(mainClass)) {
                throw new PluginException("Main class does not implement Plugin: " + info.getMainClass());
            }
            
            @SuppressWarnings("unchecked")
            Class<? extends Plugin> pluginClass = (Class<? extends Plugin>) mainClass;
            Plugin plugin = createPluginInstance(pluginClass, info);
            plugin.onLoad();
            
            return plugin;
        } catch (PluginException e) {
            throw e;
        } catch (Exception e) {
            throw new PluginException("Failed to load plugin from JAR: " + pluginPath, e);
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
        
        File jarFile = new File(pluginPath);
        if (!jarFile.exists() || !jarFile.isFile()) {
            return Optional.empty();
        }
        
        try {
            return Optional.of(loadPluginInfoFromJar(jarFile));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean isValidPlugin(String pluginPath) {
        AssertUtils.notBlank(pluginPath, "Plugin path must not be blank");
        
        File jarFile = new File(pluginPath);
        if (!jarFile.exists() || !jarFile.isFile()) {
            return false;
        }
        
        if (!jarFile.getName().toLowerCase().endsWith(".jar")) {
            return false;
        }
        
        try {
            loadPluginInfoFromJar(jarFile);
            return true;
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
        
        List<String> jarFiles = new ArrayList<>();
        File[] files = dir.listFiles();
        
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().toLowerCase().endsWith(".jar")) {
                    if (isValidPlugin(file.getAbsolutePath())) {
                        jarFiles.add(file.getAbsolutePath());
                    }
                }
            }
        }
        
        return jarFiles.toArray(new String[0]);
    }

    private PluginInfo loadPluginInfoFromJar(File jarFile) throws IOException, PluginException {
        try (JarFile jar = new JarFile(jarFile)) {
            JarEntry propertiesEntry = jar.getJarEntry(PLUGIN_PROPERTIES_FILE);
            
            if (propertiesEntry != null) {
                return loadInfoFromProperties(jar, propertiesEntry);
            }
            
            return loadInfoFromClassScanning(jar, jarFile);
        }
    }

    private PluginInfo loadInfoFromProperties(JarFile jar, JarEntry propertiesEntry) throws IOException {
        Properties props = new Properties();
        try (InputStream is = jar.getInputStream(propertiesEntry)) {
            props.load(is);
        }
        
        String mainClass = props.getProperty(MAIN_CLASS_KEY);
        if (mainClass == null || mainClass.isEmpty()) {
            throw new PluginException("Missing main.class property in plugin.properties");
        }
        
        String name = props.getProperty(PLUGIN_NAME_KEY, extractNameFromMainClass(mainClass));
        String version = props.getProperty(PLUGIN_VERSION_KEY, "1.0.0");
        String description = props.getProperty(PLUGIN_DESCRIPTION_KEY, "");
        String author = props.getProperty(PLUGIN_AUTHOR_KEY, "Unknown");
        String[] dependencies = parseDependencies(props.getProperty(PLUGIN_DEPENDENCIES_KEY));
        String[] softDependencies = parseDependencies(props.getProperty(PLUGIN_SOFT_DEPENDENCIES_KEY));
        
        return PluginInfo.builder()
                .name(name)
                .version(version)
                .description(description)
                .author(author)
                .mainClass(mainClass)
                .dependencies(dependencies)
                .softDependencies(softDependencies)
                .build();
    }

    private PluginInfo loadInfoFromClassScanning(JarFile jar, File jarFile) throws PluginException {
        String mainClass = findMainPluginClass(jar);
        
        if (mainClass == null) {
            throw new PluginException("No plugin class found in JAR: " + jarFile.getName());
        }
        
        return PluginInfo.builder()
                .name(extractNameFromMainClass(mainClass))
                .version("1.0.0")
                .description("Plugin from " + jarFile.getName())
                .author("Unknown")
                .mainClass(mainClass)
                .build();
    }

    private String findMainPluginClass(JarFile jar) {
        Enumeration<JarEntry> entries = jar.entries();
        
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            String name = entry.getName();
            
            if (name.endsWith(".class")) {
                String className = name.replace('/', '.').replace(".class", "");
                
                if (isPluginClass(jar, entry, className)) {
                    return className;
                }
            }
        }
        
        return null;
    }

    private boolean isPluginClass(JarFile jar, JarEntry entry, String className) {
        try (InputStream is = jar.getInputStream(entry)) {
            byte[] classBytes = readAllBytes(is);
            return implementsPluginInterface(classBytes);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean implementsPluginInterface(byte[] classBytes) {
        try {
            String className = extractClassName(classBytes);
            if (className == null) {
                return false;
            }
            
            String[] interfaces = extractInterfaces(classBytes);
            for (String iface : interfaces) {
                if (iface.equals("ltd/idcu/est/plugin/api/Plugin") ||
                    iface.endsWith("/Plugin")) {
                    return true;
                }
            }
            
            return className.contains("Plugin");
        } catch (Exception e) {
            return false;
        }
    }

    private String extractClassName(byte[] classBytes) {
        if (classBytes.length < 10) {
            return null;
        }
        
        int constantPoolCount = ((classBytes[8] & 0xFF) << 8) | (classBytes[9] & 0xFF);
        int offset = 10;
        String[] constantPool = new String[constantPoolCount];
        
        for (int i = 1; i < constantPoolCount && offset < classBytes.length - 1; i++) {
            int tag = classBytes[offset] & 0xFF;
            offset++;
            
            switch (tag) {
                case 1:
                    if (offset + 1 < classBytes.length) {
                        int length = ((classBytes[offset] & 0xFF) << 8) | (classBytes[offset + 1] & 0xFF);
                        if (offset + 2 + length <= classBytes.length) {
                            constantPool[i] = new String(classBytes, offset + 2, length);
                        }
                        offset += 2 + length;
                    } else {
                        return null;
                    }
                    break;
                case 7:
                    offset += 2;
                    break;
                case 9:
                case 10:
                case 11:
                    offset += 4;
                    break;
                case 8:
                    offset += 2;
                    break;
                case 3:
                case 4:
                    offset += 4;
                    break;
                case 5:
                case 6:
                    offset += 8;
                    i++;
                    break;
                case 12:
                    offset += 4;
                    break;
                case 15:
                    offset += 3;
                    break;
                case 16:
                    offset += 2;
                    break;
                case 17:
                case 18:
                    offset += 4;
                    break;
                default:
                    return null;
            }
        }
        
        if (offset + 2 >= classBytes.length) {
            return null;
        }
        
        int thisClassIndex = ((classBytes[offset] & 0xFF) << 8) | (classBytes[offset + 1] & 0xFF);
        offset += 2;
        
        if (thisClassIndex > 0 && thisClassIndex < constantPoolCount) {
            int nameIndex = 0;
            int tempOffset = 10;
            for (int i = 1; i < thisClassIndex && tempOffset < classBytes.length - 1; i++) {
                int tag = classBytes[tempOffset] & 0xFF;
                tempOffset++;
                switch (tag) {
                    case 1:
                        if (tempOffset + 1 < classBytes.length) {
                            int len = ((classBytes[tempOffset] & 0xFF) << 8) | (classBytes[tempOffset + 1] & 0xFF);
                            tempOffset += 2 + len;
                        }
                        break;
                    case 7:
                        if (i == thisClassIndex - 1) {
                            nameIndex = ((classBytes[tempOffset] & 0xFF) << 8) | (classBytes[tempOffset + 1] & 0xFF);
                        }
                        tempOffset += 2;
                        break;
                    case 9:
                    case 10:
                    case 11:
                        tempOffset += 4;
                        break;
                    case 8:
                        tempOffset += 2;
                        break;
                    case 3:
                    case 4:
                        tempOffset += 4;
                        break;
                    case 5:
                    case 6:
                        tempOffset += 8;
                        i++;
                        break;
                    case 12:
                        tempOffset += 4;
                        break;
                    case 15:
                        tempOffset += 3;
                        break;
                    case 16:
                        tempOffset += 2;
                        break;
                    case 17:
                    case 18:
                        tempOffset += 4;
                        break;
                }
            }
            if (nameIndex > 0 && nameIndex < constantPoolCount) {
                return constantPool[nameIndex];
            }
        }
        return null;
    }

    private String[] extractInterfaces(byte[] classBytes) {
        return new String[0];
    }

    private byte[] readAllBytes(InputStream is) throws IOException {
        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int read;
        while ((read = is.read(buffer)) != -1) {
            baos.write(buffer, 0, read);
        }
        return baos.toByteArray();
    }

    private URLClassLoader createClassLoader(File jarFile) throws IOException {
        URL jarUrl = jarFile.toURI().toURL();
        return new URLClassLoader(new URL[]{jarUrl}, getClass().getClassLoader());
    }

    private Plugin createPluginInstance(Class<? extends Plugin> pluginClass, PluginInfo info) throws Exception {
        try {
            Plugin plugin = pluginClass.getDeclaredConstructor().newInstance();
            
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

    private String extractNameFromMainClass(String mainClass) {
        int lastDot = mainClass.lastIndexOf('.');
        return lastDot >= 0 ? mainClass.substring(lastDot + 1) : mainClass;
    }

    private String[] parseDependencies(String value) {
        if (value == null || value.trim().isEmpty()) {
            return new String[0];
        }
        
        String[] deps = value.split(",");
        List<String> result = new ArrayList<>();
        for (String dep : deps) {
            String trimmed = dep.trim();
            if (!trimmed.isEmpty()) {
                result.add(trimmed);
            }
        }
        
        return result.toArray(new String[0]);
    }
}
