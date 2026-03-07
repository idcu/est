package ltd.idcu.est.gateway.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Optional;

public interface RateLimiterRegistry {
    RateLimiter create(String name, long capacity, long refillRate);

    Optional<RateLimiter> get(String name);

    RateLimiter getOrCreate(String name, long capacity, long refillRate);

    void remove(String name);

    Map<String, RateLimiter> getAll();

    void clear();

    void saveToJson(String path) throws IOException;

    void saveToJson(OutputStream outputStream) throws IOException;

    void loadFromJson(String path) throws IOException;

    void loadFromJson(InputStream inputStream) throws IOException;

    void setAutoSave(boolean enabled);

    void setAutoSavePath(String path);

    boolean isAutoSaveEnabled();

    String getAutoSavePath();
}
