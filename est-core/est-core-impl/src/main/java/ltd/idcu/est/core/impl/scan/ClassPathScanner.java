package ltd.idcu.est.core.impl.scan;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClassPathScanner {

    public static List<Class<?>> scan(String basePackage) {
        List<Class<?>> classes = new ArrayList<>();
        String path = basePackage.replace('.', '/');
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        
        try {
            Enumeration<URL> resources = classLoader.getResources(path);
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                String protocol = resource.getProtocol();
                
                if ("file".equals(protocol)) {
                    classes.addAll(scanFromDirectory(basePackage, new File(resource.getFile())));
                } else if ("jar".equals(protocol)) {
                    classes.addAll(scanFromJar(basePackage, resource));
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to scan classpath: " + basePackage, e);
        }
        
        return classes;
    }

    private static List<Class<?>> scanFromDirectory(String basePackage, File directory) throws ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        
        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        classes.addAll(scanFromDirectory(basePackage + "." + file.getName(), file));
                    } else if (file.getName().endsWith(".class")) {
                        String className = basePackage + "." + file.getName().substring(0, file.getName().length() - 6);
                        classes.add(Class.forName(className));
                    }
                }
            }
        }
        
        return classes;
    }

    private static List<Class<?>> scanFromJar(String basePackage, URL resource) throws IOException, ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        
        JarURLConnection jarConnection = (JarURLConnection) resource.openConnection();
        try (JarFile jarFile = jarConnection.getJarFile()) {
            Enumeration<JarEntry> entries = jarFile.entries();
            String path = basePackage.replace('.', '/');
            
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String entryName = entry.getName();
                
                if (entryName.startsWith(path) && entryName.endsWith(".class")) {
                    String className = entryName.substring(0, entryName.length() - 6).replace('/', '.');
                    classes.add(Class.forName(className));
                }
            }
        }
        
        return classes;
    }
}
