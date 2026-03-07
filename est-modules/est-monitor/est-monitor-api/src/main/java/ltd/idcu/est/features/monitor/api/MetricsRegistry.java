package ltd.idcu.est.features.monitor.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Optional;

public interface MetricsRegistry {
    Metrics create(String name);

    Optional<Metrics> get(String name);

    Metrics getOrCreate(String name);

    void remove(String name);

    Map<String, Metrics> getAll();

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
