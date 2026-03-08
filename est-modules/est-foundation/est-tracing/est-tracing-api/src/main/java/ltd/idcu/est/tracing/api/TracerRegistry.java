package ltd.idcu.est.tracing.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Optional;

public interface TracerRegistry {
    Tracer create(String serviceName);

    Tracer create(String serviceName, SpanExporter exporter);

    Optional<Tracer> get(String serviceName);

    Tracer getOrCreate(String serviceName);

    void remove(String serviceName);

    Map<String, Tracer> getAll();

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
