package ltd.idcu.est.cache.file;

import ltd.idcu.est.cache.api.Cache;
import ltd.idcu.est.cache.api.CacheConfig;

import java.nio.file.Path;
import java.nio.file.Paths;

public final class FileCaches {
    
    private FileCaches() {
    }
    
    public static Cache<String, String> newFileCache(String directory) {
        return new FileCache(directory);
    }
    
    public static Cache<String, String> newFileCache(String directory, CacheConfig config) {
        return new FileCache(directory, config);
    }
    
    public static Cache<String, String> newFileCache(Path directory) {
        return new FileCache(directory.toString());
    }
    
    public static Cache<String, String> newFileCache(Path directory, CacheConfig config) {
        return new FileCache(directory.toString(), config);
    }
    
    public static Cache<String, String> newTempFileCache() {
        String tempDir = System.getProperty("java.io.tmpdir");
        Path cacheDir = Paths.get(tempDir, "est-cache-" + System.currentTimeMillis());
        return new FileCache(cacheDir.toString());
    }
    
    public static Cache<String, String> newUserFileCache(String subdirectory) {
        String userHome = System.getProperty("user.home");
        Path cacheDir = Paths.get(userHome, ".est-cache", subdirectory);
        return new FileCache(cacheDir.toString());
    }
    
    public static FileCacheBuilder builder() {
        return new FileCacheBuilder();
    }
    
    public static class FileCacheBuilder {
        private String directory;
        private int maxSize = 1000;
        private long defaultTtl = -1;
        private long cleanupInterval = 60000;
        
        public FileCacheBuilder directory(String directory) {
            this.directory = directory;
            return this;
        }
        
        public FileCacheBuilder directory(Path directory) {
            this.directory = directory.toString();
            return this;
        }
        
        public FileCacheBuilder maxSize(int maxSize) {
            this.maxSize = maxSize;
            return this;
        }
        
        public FileCacheBuilder defaultTtl(long ttl) {
            this.defaultTtl = ttl;
            return this;
        }
        
        public FileCacheBuilder cleanupInterval(long interval) {
            this.cleanupInterval = interval;
            return this;
        }
        
        public Cache<String, String> build() {
            if (directory == null) {
                directory = System.getProperty("java.io.tmpdir") + "/est-cache-" + System.currentTimeMillis();
            }
            CacheConfig config = new CacheConfig()
                    .setMaxSize(maxSize)
                    .setDefaultTtl(defaultTtl)
                    .setCleanupInterval(cleanupInterval);
            return new FileCache(directory, config);
        }
    }
}
