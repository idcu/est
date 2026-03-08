package ltd.idcu.est.cache.file;

import ltd.idcu.est.cache.api.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class FileCache implements Cache<String, String> {
    
    private final Path cacheDirectory;
    private final CacheConfig config;
    private final CacheStats stats;
    private final Map<String, FileCacheEntry> index;
    private final List<CacheListener<String, String>> listeners;
    private final ScheduledExecutorService cleanupExecutor;
    private final AtomicBoolean running;
    private final String fileExtension;
    private CacheLoader<String, String> loader;
    
    public FileCache(String cacheDirectory) {
        this(cacheDirectory, CacheConfig.defaultConfig());
    }
    
    public FileCache(String cacheDirectory, CacheConfig config) {
        this.cacheDirectory = Paths.get(cacheDirectory);
        this.config = config;
        this.stats = new CacheStats();
        this.index = new ConcurrentHashMap<>();
        this.listeners = new ArrayList<>();
        this.running = new AtomicBoolean(true);
        this.fileExtension = ".cache";
        
        try {
            Files.createDirectories(this.cacheDirectory);
        } catch (IOException e) {
            throw new CacheException("Failed to create cache directory: " + cacheDirectory, e);
        }
        
        loadIndex();
        
        if (config.getCleanupInterval() > 0) {
            this.cleanupExecutor = Executors.newSingleThreadScheduledExecutor(r -> {
                Thread t = new Thread(r, "file-cache-cleanup");
                t.setDaemon(true);
                return t;
            });
            this.cleanupExecutor.scheduleAtFixedRate(
                    this::cleanupExpired,
                    config.getCleanupInterval(),
                    config.getCleanupInterval(),
                    TimeUnit.MILLISECONDS
            );
        } else {
            this.cleanupExecutor = null;
        }
    }
    
    private void loadIndex() {
        try {
            Files.walk(cacheDirectory)
                    .filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(fileExtension))
                    .forEach(this::loadEntryFromFile);
        } catch (IOException e) {
            throw new CacheException("Failed to load cache index", e);
        }
    }
    
    private void loadEntryFromFile(Path file) {
        try {
            String key = decodeKey(file.getFileName().toString());
            String content = Files.readString(file, StandardCharsets.UTF_8);
            FileCacheEntry entry = parseEntry(content);
            if (entry != null && !entry.isExpired()) {
                index.put(key, entry);
            } else if (entry != null) {
                Files.deleteIfExists(file);
            }
        } catch (IOException ignored) {
        }
    }
    
    private FileCacheEntry parseEntry(String content) {
        int separatorIndex = content.indexOf('\n');
        if (separatorIndex < 0) {
            return null;
        }
        String header = content.substring(0, separatorIndex);
        String value = content.substring(separatorIndex + 1);
        
        String[] parts = header.split("\\|");
        if (parts.length < 3) {
            return null;
        }
        
        try {
            long createdAt = Long.parseLong(parts[0]);
            long ttlMillis = Long.parseLong(parts[1]);
            int accessCount = Integer.parseInt(parts[2]);
            
            FileCacheEntry entry = new FileCacheEntry(value, createdAt, ttlMillis);
            entry.setAccessCount(accessCount);
            return entry;
        } catch (NumberFormatException e) {
            return null;
        }
    }
    
    @Override
    public void put(String key, String value) {
        long ttl = config.getDefaultTtlMillis();
        if (ttl > 0) {
            put(key, value, ttl, TimeUnit.MILLISECONDS);
        } else {
            putInternal(key, value, -1);
        }
    }
    
    @Override
    public void put(String key, String value, long ttl, TimeUnit timeUnit) {
        putInternal(key, value, timeUnit.toMillis(ttl));
    }
    
    private void putInternal(String key, String value, long ttlMillis) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }
        
        while (index.size() >= config.getMaxSize()) {
            evictOldest();
        }
        
        long createdAt = System.currentTimeMillis();
        FileCacheEntry entry = new FileCacheEntry(value, createdAt, ttlMillis);
        
        Path filePath = getFilePath(key);
        try {
            writeEntryToFile(filePath, entry);
            index.put(key, entry);
            stats.incrementPutCount();
            notifyPut(key, value);
        } catch (IOException e) {
            throw new CacheException("Failed to write cache entry: " + key, e);
        }
    }
    
    private void writeEntryToFile(Path file, FileCacheEntry entry) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append(entry.getCreatedAt()).append("|");
        sb.append(entry.getTtlMillis()).append("|");
        sb.append(entry.getAccessCount()).append("\n");
        sb.append(entry.getValue());
        
        Files.writeString(file, sb.toString(), StandardCharsets.UTF_8);
    }
    
    @Override
    public Optional<String> get(String key) {
        if (key == null) {
            stats.incrementMissCount();
            return Optional.empty();
        }
        
        FileCacheEntry entry = index.get(key);
        
        if (entry == null) {
            stats.incrementMissCount();
            if (loader != null) {
                return loadValue(key);
            }
            return Optional.empty();
        }
        
        if (entry.isExpired()) {
            removeFile(key);
            index.remove(key);
            stats.incrementMissCount();
            notifyExpire(key, entry.getValue());
            if (loader != null) {
                return loadValue(key);
            }
            return Optional.empty();
        }
        
        entry.incrementAccessCount();
        stats.incrementHitCount();
        return Optional.of(entry.getValue());
    }
    
    private Optional<String> loadValue(String key) {
        try {
            long start = System.currentTimeMillis();
            String value = loader.load(key);
            long loadTime = System.currentTimeMillis() - start;
            stats.addLoadTime(loadTime);
            
            if (value != null) {
                put(key, value);
                return Optional.of(value);
            }
        } catch (Exception e) {
            throw new CacheException("Failed to load value for key: " + key, e);
        }
        return Optional.empty();
    }
    
    @Override
    public String get(String key, String defaultValue) {
        return get(key).orElse(defaultValue);
    }
    
    @Override
    public boolean containsKey(String key) {
        if (key == null) {
            return false;
        }
        FileCacheEntry entry = index.get(key);
        if (entry == null) {
            return false;
        }
        if (entry.isExpired()) {
            removeFile(key);
            index.remove(key);
            notifyExpire(key, entry.getValue());
            return false;
        }
        return true;
    }
    
    @Override
    public void remove(String key) {
        if (key == null) {
            return;
        }
        FileCacheEntry entry = index.remove(key);
        if (entry != null) {
            removeFile(key);
            stats.incrementRemoveCount();
            notifyRemove(key, entry.getValue());
        }
    }
    
    private void removeFile(String key) {
        try {
            Path filePath = getFilePath(key);
            Files.deleteIfExists(filePath);
        } catch (IOException ignored) {
        }
    }
    
    @Override
    public void clear() {
        for (String key : index.keySet()) {
            removeFile(key);
        }
        index.clear();
        notifyClear();
    }
    
    @Override
    public int size() {
        return index.size();
    }
    
    @Override
    public boolean isEmpty() {
        return index.isEmpty();
    }
    
    @Override
    public CacheStats getStats() {
        return stats.snapshot();
    }
    
    public void setLoader(CacheLoader<String, String> loader) {
        this.loader = loader;
    }
    
    public void addListener(CacheListener<String, String> listener) {
        if (listener != null) {
            listeners.add(listener);
        }
    }
    
    public void removeListener(CacheListener<String, String> listener) {
        listeners.remove(listener);
    }
    
    public Set<String> keySet() {
        return new HashSet<>(index.keySet());
    }
    
    public Path getCacheDirectory() {
        return cacheDirectory;
    }
    
    private Path getFilePath(String key) {
        String encodedKey = encodeKey(key);
        return cacheDirectory.resolve(encodedKey + fileExtension);
    }
    
    private String encodeKey(String key) {
        return Base64.getUrlEncoder().encodeToString(key.getBytes(StandardCharsets.UTF_8));
    }
    
    private String decodeKey(String encodedName) {
        String encodedKey = encodedName.replace(fileExtension, "");
        return new String(Base64.getUrlDecoder().decode(encodedKey), StandardCharsets.UTF_8);
    }
    
    private void evictOldest() {
        Optional<Map.Entry<String, FileCacheEntry>> oldest = index.entrySet().stream()
                .min(Comparator.comparingLong(e -> e.getValue().getCreatedAt()));
        
        oldest.ifPresent(entry -> {
            String key = entry.getKey();
            FileCacheEntry cacheEntry = entry.getValue();
            removeFile(key);
            index.remove(key);
            stats.incrementEvictionCount();
            notifyEvict(key, cacheEntry.getValue());
        });
    }
    
    private void cleanupExpired() {
        if (!running.get()) {
            return;
        }
        List<String> expiredKeys = new ArrayList<>();
        for (Map.Entry<String, FileCacheEntry> entry : index.entrySet()) {
            if (entry.getValue().isExpired()) {
                expiredKeys.add(entry.getKey());
            }
        }
        for (String key : expiredKeys) {
            FileCacheEntry entry = index.remove(key);
            if (entry != null) {
                removeFile(key);
                stats.incrementEvictionCount();
                notifyExpire(key, entry.getValue());
            }
        }
    }
    
    public void shutdown() {
        running.set(false);
        if (cleanupExecutor != null) {
            cleanupExecutor.shutdown();
            try {
                if (!cleanupExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                    cleanupExecutor.shutdownNow();
                }
            } catch (InterruptedException e) {
                cleanupExecutor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }
    
    private void notifyPut(String key, String value) {
        for (CacheListener<String, String> listener : listeners) {
            try {
                listener.onPut(key, value);
            } catch (Exception ignored) {
            }
        }
    }
    
    private void notifyRemove(String key, String value) {
        for (CacheListener<String, String> listener : listeners) {
            try {
                listener.onRemove(key, value);
            } catch (Exception ignored) {
            }
        }
    }
    
    private void notifyEvict(String key, String value) {
        for (CacheListener<String, String> listener : listeners) {
            try {
                listener.onEvict(key, value);
            } catch (Exception ignored) {
            }
        }
    }
    
    private void notifyExpire(String key, String value) {
        for (CacheListener<String, String> listener : listeners) {
            try {
                listener.onExpire(key, value);
            } catch (Exception ignored) {
            }
        }
    }
    
    private void notifyClear() {
        for (CacheListener<String, String> listener : listeners) {
            try {
                listener.onClear();
            } catch (Exception ignored) {
            }
        }
    }
    
    private static class FileCacheEntry {
        private final String value;
        private final long createdAt;
        private final long ttlMillis;
        private volatile int accessCount;
        
        FileCacheEntry(String value, long createdAt, long ttlMillis) {
            this.value = value;
            this.createdAt = createdAt;
            this.ttlMillis = ttlMillis;
            this.accessCount = 0;
        }
        
        String getValue() {
            return value;
        }
        
        long getCreatedAt() {
            return createdAt;
        }
        
        long getTtlMillis() {
            return ttlMillis;
        }
        
        int getAccessCount() {
            return accessCount;
        }
        
        void setAccessCount(int count) {
            this.accessCount = count;
        }
        
        void incrementAccessCount() {
            this.accessCount++;
        }
        
        boolean isExpired() {
            if (ttlMillis < 0) {
                return false;
            }
            return System.currentTimeMillis() > (createdAt + ttlMillis);
        }
    }
}
