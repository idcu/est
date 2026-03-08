package ltd.idcu.est.ai.impl.storage;

import ltd.idcu.est.ai.api.storage.StorageProvider;
import ltd.idcu.est.utils.format.json.JsonUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class JsonFileStorageProvider implements StorageProvider {
    
    private final Path storageDir;
    private final Map<String, String> cache = new ConcurrentHashMap<>();
    
    public JsonFileStorageProvider() {
        this("est-ai-storage");
    }
    
    public JsonFileStorageProvider(String storageDirPath) {
        this.storageDir = Paths.get(storageDirPath);
        initializeStorage();
        loadAllFromDisk();
    }
    
    private void initializeStorage() {
        try {
            if (!Files.exists(storageDir)) {
                Files.createDirectories(storageDir);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize storage directory", e);
        }
    }
    
    private void loadAllFromDisk() {
        try {
            if (Files.exists(storageDir)) {
                Files.list(storageDir)
                     .filter(path -> path.toString().endsWith(".json"))
                     .forEach(this::loadFile);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load storage from disk", e);
        }
    }
    
    private void loadFile(Path path) {
        try {
            String key = path.getFileName().toString().replace(".json", "");
            String content = Files.readString(path, StandardCharsets.UTF_8);
            cache.put(key, content);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load file: " + path, e);
        }
    }
    
    @Override
    public String getName() {
        return "json-file";
    }
    
    @Override
    public boolean exists(String key) {
        return cache.containsKey(key);
    }
    
    @Override
    public String load(String key) {
        return cache.get(key);
    }
    
    @Override
    public void save(String key, String data) {
        cache.put(key, data);
        saveToDisk(key, data);
    }
    
    private void saveToDisk(String key, String data) {
        try {
            Path filePath = storageDir.resolve(key + ".json");
            Files.writeString(filePath, data, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save file: " + key, e);
        }
    }
    
    @Override
    public void delete(String key) {
        cache.remove(key);
        deleteFromDisk(key);
    }
    
    private void deleteFromDisk(String key) {
        try {
            Path filePath = storageDir.resolve(key + ".json");
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete file: " + key, e);
        }
    }
    
    @Override
    public Map<String, String> loadAll(String prefix) {
        return cache.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(prefix))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
    
    @Override
    public void saveAll(Map<String, String> data) {
        data.forEach(this::save);
    }
    
    @Override
    public void deleteAll(String prefix) {
        cache.keySet().stream()
             .filter(key -> key.startsWith(prefix))
             .toList()
             .forEach(this::delete);
    }
    
    @Override
    public void clear() {
        cache.clear();
        clearDisk();
    }
    
    private void clearDisk() {
        try {
            if (Files.exists(storageDir)) {
                Files.list(storageDir)
                     .filter(path -> path.toString().endsWith(".json"))
                     .forEach(this::deleteFileQuietly);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to clear storage", e);
        }
    }
    
    private void deleteFileQuietly(Path path) {
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
        }
    }
}
