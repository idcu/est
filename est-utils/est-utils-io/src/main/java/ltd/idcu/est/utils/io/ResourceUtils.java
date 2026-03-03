package ltd.idcu.est.utils.io;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class ResourceUtils {

    public static final String CLASSPATH_URL_PREFIX = "classpath:";
    public static final String FILE_URL_PREFIX = "file:";
    public static final String JAR_URL_PREFIX = "jar:";
    public static final String JAR_URL_SEPARATOR = "!/";
    public static final String ZIP_URL_PREFIX = "zip:";

    private ResourceUtils() {
    }

    public static boolean isUrl(String resourceLocation) {
        if (resourceLocation == null) {
            return false;
        }
        if (resourceLocation.startsWith(CLASSPATH_URL_PREFIX)) {
            return true;
        }
        try {
            new URL(resourceLocation);
            return true;
        } catch (MalformedURLException ex) {
            return false;
        }
    }

    public static URL getURL(String resourceLocation) throws FileNotFoundException {
        if (resourceLocation == null) {
            throw new IllegalArgumentException("Resource location must not be null");
        }
        if (resourceLocation.startsWith(CLASSPATH_URL_PREFIX)) {
            String path = resourceLocation.substring(CLASSPATH_URL_PREFIX.length());
            ClassLoader cl = getDefaultClassLoader();
            URL url = cl != null ? cl.getResource(path) : ClassLoader.getSystemResource(path);
            if (url == null) {
                throw new FileNotFoundException("Classpath resource not found: " + path);
            }
            return url;
        }
        try {
            return new URL(resourceLocation);
        } catch (MalformedURLException ex) {
            try {
                return new File(resourceLocation).toURI().toURL();
            } catch (MalformedURLException ex2) {
                throw new FileNotFoundException("Resource location [" + resourceLocation + "] is neither a URL nor a valid file path");
            }
        }
    }

    public static File getFile(String resourceLocation) throws FileNotFoundException {
        if (resourceLocation == null) {
            throw new IllegalArgumentException("Resource location must not be null");
        }
        if (resourceLocation.startsWith(CLASSPATH_URL_PREFIX)) {
            String path = resourceLocation.substring(CLASSPATH_URL_PREFIX.length());
            ClassLoader cl = getDefaultClassLoader();
            URL url = cl != null ? cl.getResource(path) : ClassLoader.getSystemResource(path);
            if (url == null) {
                throw new FileNotFoundException("Classpath resource not found: " + path);
            }
            return getFile(url);
        }
        try {
            return getFile(new URL(resourceLocation));
        } catch (MalformedURLException ex) {
            return new File(resourceLocation);
        }
    }

    public static File getFile(URL resourceUrl) throws FileNotFoundException {
        if (resourceUrl == null) {
            throw new IllegalArgumentException("Resource URL must not be null");
        }
        if (!URL_PROTOCOL_FILE.equals(resourceUrl.getProtocol())) {
            throw new FileNotFoundException("URL cannot be resolved to absolute file path: " + resourceUrl);
        }
        try {
            return new File(toURI(resourceUrl).getSchemeSpecificPart());
        } catch (URISyntaxException ex) {
            return new File(resourceUrl.getFile());
        }
    }

    public static File getFile(URL resourceUrl, String description) throws FileNotFoundException {
        if (resourceUrl == null) {
            throw new IllegalArgumentException("Resource URL must not be null");
        }
        if (!URL_PROTOCOL_FILE.equals(resourceUrl.getProtocol())) {
            throw new FileNotFoundException(description + " cannot be resolved to absolute file path: " + resourceUrl);
        }
        try {
            return new File(toURI(resourceUrl).getSchemeSpecificPart());
        } catch (URISyntaxException ex) {
            return new File(resourceUrl.getFile());
        }
    }

    public static boolean isFileURL(URL url) {
        if (url == null) {
            return false;
        }
        String protocol = url.getProtocol();
        return URL_PROTOCOL_FILE.equals(protocol) ||
                URL_PROTOCOL_VFSFILE.equals(protocol) ||
                URL_PROTOCOL_VFSZIP.equals(protocol);
    }

    public static boolean isJarURL(URL url) {
        if (url == null) {
            return false;
        }
        String protocol = url.getProtocol();
        return URL_PROTOCOL_JAR.equals(protocol) ||
                URL_PROTOCOL_WAR.equals(protocol) ||
                URL_PROTOCOL_ZIP.equals(protocol) ||
                URL_PROTOCOL_VFSZIP.equals(protocol);
    }

    public static boolean isResourceUrl(String resourceLocation) {
        return resourceLocation != null && resourceLocation.startsWith(CLASSPATH_URL_PREFIX);
    }

    public static boolean exists(String resourceLocation) {
        try {
            URL url = getURL(resourceLocation);
            if (URL_PROTOCOL_FILE.equals(url.getProtocol())) {
                File file = getFile(url);
                return file.exists();
            }
            try (InputStream is = url.openStream()) {
                return true;
            } catch (IOException ex) {
                return false;
            }
        } catch (FileNotFoundException ex) {
            return false;
        }
    }

    public static boolean exists(URL url) {
        if (url == null) {
            return false;
        }
        if (URL_PROTOCOL_FILE.equals(url.getProtocol())) {
            try {
                File file = getFile(url);
                return file.exists();
            } catch (FileNotFoundException ex) {
                return false;
            }
        }
        try (InputStream is = url.openStream()) {
            return true;
        } catch (IOException ex) {
            return false;
        }
    }

    public static InputStream getInputStream(String resourceLocation) throws IOException {
        if (resourceLocation == null) {
            throw new IllegalArgumentException("Resource location must not be null");
        }
        if (resourceLocation.startsWith(CLASSPATH_URL_PREFIX)) {
            String path = resourceLocation.substring(CLASSPATH_URL_PREFIX.length());
            ClassLoader cl = getDefaultClassLoader();
            InputStream is = cl != null ? cl.getResourceAsStream(path) : ClassLoader.getSystemResourceAsStream(path);
            if (is == null) {
                throw new FileNotFoundException("Classpath resource not found: " + path);
            }
            return is;
        }
        try {
            URL url = new URL(resourceLocation);
            return url.openStream();
        } catch (MalformedURLException ex) {
            return new FileInputStream(resourceLocation);
        }
    }

    public static InputStream getInputStream(URL url) throws IOException {
        if (url == null) {
            throw new IllegalArgumentException("URL must not be null");
        }
        return url.openStream();
    }

    public static byte[] getBytes(String resourceLocation) throws IOException {
        try (InputStream is = getInputStream(resourceLocation)) {
            return IOUtils.toByteArray(is);
        }
    }

    public static byte[] getBytes(URL url) throws IOException {
        try (InputStream is = getInputStream(url)) {
            return IOUtils.toByteArray(is);
        }
    }

    public static String getContent(String resourceLocation) throws IOException {
        return getContent(resourceLocation, StandardCharsets.UTF_8);
    }

    public static String getContent(String resourceLocation, Charset charset) throws IOException {
        try (InputStream is = getInputStream(resourceLocation)) {
            return IOUtils.toString(is, charset);
        }
    }

    public static String getContent(URL url) throws IOException {
        return getContent(url, StandardCharsets.UTF_8);
    }

    public static String getContent(URL url, Charset charset) throws IOException {
        try (InputStream is = getInputStream(url)) {
            return IOUtils.toString(is, charset);
        }
    }

    public static URL extractJarFileURL(URL jarUrl) throws MalformedURLException {
        if (jarUrl == null) {
            return null;
        }
        String urlFile = jarUrl.getFile();
        int separatorIndex = urlFile.indexOf(JAR_URL_SEPARATOR);
        if (separatorIndex != -1) {
            String jarFile = urlFile.substring(0, separatorIndex);
            if (jarFile.startsWith(FILE_URL_PREFIX)) {
                jarFile = jarFile.substring(FILE_URL_PREFIX.length());
            }
            try {
                return new URL(jarFile);
            } catch (MalformedURLException ex) {
                if (!jarFile.startsWith("/")) {
                    jarFile = "/" + jarFile;
                }
                return new URL(FILE_URL_PREFIX + jarFile);
            }
        }
        return jarUrl;
    }

    public static URI toURI(URL url) throws URISyntaxException {
        if (url == null) {
            return null;
        }
        return url.toURI();
    }

    public static URI toURI(String location) throws URISyntaxException {
        if (location == null) {
            return null;
        }
        return new URI(location.replace(" ", "%20"));
    }

    public static URL extractArchiveURL(URL jarUrl) throws MalformedURLException {
        if (jarUrl == null) {
            return null;
        }
        String urlFile = jarUrl.getFile();
        int separatorIndex = urlFile.indexOf(JAR_URL_SEPARATOR);
        if (separatorIndex != -1) {
            String jarFile = urlFile.substring(0, separatorIndex);
            try {
                return new URL(jarFile);
            } catch (MalformedURLException ex) {
                if (!jarFile.startsWith("/")) {
                    jarFile = "/" + jarFile;
                }
                return new URL(FILE_URL_PREFIX + jarFile);
            }
        }
        return jarUrl;
    }

    public static String getFilename(URL url) {
        if (url == null) {
            return null;
        }
        String path = url.getPath();
        int separatorIndex = path.lastIndexOf(JAR_URL_SEPARATOR);
        if (separatorIndex != -1) {
            path = path.substring(separatorIndex + 1);
        }
        int lastSlashIndex = path.lastIndexOf('/');
        if (lastSlashIndex != -1) {
            path = path.substring(lastSlashIndex + 1);
        }
        return path;
    }

    public static String getFilename(String resourceLocation) {
        if (resourceLocation == null) {
            return null;
        }
        if (resourceLocation.startsWith(CLASSPATH_URL_PREFIX)) {
            resourceLocation = resourceLocation.substring(CLASSPATH_URL_PREFIX.length());
        }
        int lastSlashIndex = resourceLocation.lastIndexOf('/');
        if (lastSlashIndex != -1) {
            return resourceLocation.substring(lastSlashIndex + 1);
        }
        return resourceLocation;
    }

    public static String getClasspathResourceName(Class<?> clazz) {
        if (clazz == null) {
            return null;
        }
        return clazz.getName().replace('.', '/') + ".class";
    }

    public static URL getClasspathResource(Class<?> clazz) {
        if (clazz == null) {
            return null;
        }
        return clazz.getResource("/" + getClasspathResourceName(clazz));
    }

    public static String getClasspathLocation(Class<?> clazz) {
        URL resource = getClasspathResource(clazz);
        if (resource == null) {
            return null;
        }
        return resource.toString();
    }

    public static Path getClasspathPath(Class<?> clazz) {
        URL resource = getClasspathResource(clazz);
        if (resource == null) {
            return null;
        }
        try {
            return Paths.get(toURI(resource));
        } catch (URISyntaxException e) {
            return null;
        }
    }

    public static boolean isClasspathResource(String resourceLocation) {
        return resourceLocation != null && resourceLocation.startsWith(CLASSPATH_URL_PREFIX);
    }

    public static String stripClasspathPrefix(String resourceLocation) {
        if (resourceLocation != null && resourceLocation.startsWith(CLASSPATH_URL_PREFIX)) {
            return resourceLocation.substring(CLASSPATH_URL_PREFIX.length());
        }
        return resourceLocation;
    }

    private static final String URL_PROTOCOL_FILE = "file";
    private static final String URL_PROTOCOL_JAR = "jar";
    private static final String URL_PROTOCOL_ZIP = "zip";
    private static final String URL_PROTOCOL_WAR = "war";
    private static final String URL_PROTOCOL_VFSZIP = "vfszip";
    private static final String URL_PROTOCOL_VFSFILE = "vfsfile";

    private static ClassLoader getDefaultClassLoader() {
        ClassLoader cl = null;
        try {
            cl = Thread.currentThread().getContextClassLoader();
        } catch (Throwable ignored) {
        }
        if (cl == null) {
            try {
                cl = ResourceUtils.class.getClassLoader();
            } catch (Throwable ignored) {
            }
            if (cl == null) {
                try {
                    cl = ClassLoader.getSystemClassLoader();
                } catch (Throwable ignored) {
                }
            }
        }
        return cl;
    }
}
