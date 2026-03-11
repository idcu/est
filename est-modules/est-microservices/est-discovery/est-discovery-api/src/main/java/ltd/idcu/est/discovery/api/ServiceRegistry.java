package ltd.idcu.est.discovery.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Optional;

public interface ServiceRegistry {
    void register(ServiceInstance instance);

    void unregister(String serviceId, String instanceId);

    void heartbeat(String serviceId, String instanceId);

    Optional<ServiceInstance> getInstance(String serviceId, String instanceId);

    List<ServiceInstance> getInstances(String serviceId);

    List<String> getServiceIds();

    void clear();

    void setHealthChecker(HealthChecker healthChecker);

    void checkAllHealth();

    void setHealthCheckConfig(HttpHealthCheckConfig config);

    void saveToJson(String path) throws IOException;

    void saveToJson(OutputStream outputStream) throws IOException;

    void loadFromJson(String path) throws IOException;

    void loadFromJson(InputStream inputStream) throws IOException;

    void setAutoSave(boolean enabled);

    void setAutoSavePath(String path);

    boolean isAutoSaveEnabled();

    String getAutoSavePath();
}
