package ltd.idcu.est.circuitbreaker.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Optional;

public interface CircuitBreakerRegistry {
    CircuitBreaker create(String name);

    CircuitBreaker create(String name, CircuitBreakerConfig config);

    Optional<CircuitBreaker> get(String name);

    CircuitBreaker getOrCreate(String name);

    void remove(String name);

    Map<String, CircuitBreaker> getAll();

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
