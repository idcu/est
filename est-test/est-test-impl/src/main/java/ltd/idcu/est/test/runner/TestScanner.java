package ltd.idcu.est.test.runner;

import ltd.idcu.est.test.annotation.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class TestScanner {

    public List<Class<?>> scanPackage(String packageName) throws IOException, ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        String path = packageName.replace('.', '/');
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> resources = classLoader.getResources(path);

        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            if (resource.getProtocol().equals("file")) {
                File directory = new File(resource.getFile());
                scanDirectory(directory, packageName, classes);
            }
        }

        return classes;
    }

    public List<Class<?>> scanPackageForTests(String packageName) throws IOException, ClassNotFoundException {
        List<Class<?>> allClasses = scanPackage(packageName);
        List<Class<?>> testClasses = new ArrayList<>();

        for (Class<?> clazz : allClasses) {
            if (isTestClass(clazz)) {
                testClasses.add(clazz);
            }
        }

        return testClasses;
    }

    public boolean isTestClass(Class<?> clazz) {
        if (clazz.isInterface() || clazz.isAnonymousClass() || clazz.isLocalClass()) {
            return false;
        }

        for (java.lang.reflect.Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Test.class) || 
                method.isAnnotationPresent(ltd.idcu.est.test.annotation.ParameterizedTest.class)) {
                return true;
            }
        }

        return false;
    }

    private void scanDirectory(File directory, String packageName, List<Class<?>> classes) throws ClassNotFoundException {
        if (!directory.exists()) {
            return;
        }

        File[] files = directory.listFiles();
        if (files == null) {
            return;
        }

        for (File file : files) {
            if (file.isDirectory()) {
                String subPackageName = packageName + "." + file.getName();
                scanDirectory(file, subPackageName, classes);
            } else if (file.getName().endsWith(".class")) {
                String className = packageName + "." + file.getName().substring(0, file.getName().length() - 6);
                try {
                    Class<?> clazz = Class.forName(className);
                    classes.add(clazz);
                } catch (NoClassDefFoundError | UnsatisfiedLinkError e) {
                }
            }
        }
    }

    public List<Class<?>> scanClasses(Class<?>... classes) {
        List<Class<?>> testClasses = new ArrayList<>();
        for (Class<?> clazz : classes) {
            if (isTestClass(clazz)) {
                testClasses.add(clazz);
            }
        }
        return testClasses;
    }
}
