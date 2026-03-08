package ltd.idcu.est.discovery.api;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class ServiceInstance {
    private final String serviceId;
    private final String instanceId;
    private final String host;
    private final int port;
    private final Map<String, String> metadata;
    private final Instant registeredAt;
    private Instant lastHeartbeatAt;
    private boolean healthy;

    public ServiceInstance(String serviceId, String instanceId, String host, int port) {
        this.serviceId = serviceId;
        this.instanceId = instanceId;
        this.host = host;
        this.port = port;
        this.metadata = new HashMap<>();
        this.registeredAt = Instant.now();
        this.lastHeartbeatAt = Instant.now();
        this.healthy = true;
    }

    public String getServiceId() {
        return serviceId;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public Map<String, String> getMetadata() {
        return new HashMap<>(metadata);
    }

    public ServiceInstance addMetadata(String key, String value) {
        metadata.put(key, value);
        return this;
    }

    public Instant getRegisteredAt() {
        return registeredAt;
    }

    public Instant getLastHeartbeatAt() {
        return lastHeartbeatAt;
    }

    public void setLastHeartbeatAt(Instant lastHeartbeatAt) {
        this.lastHeartbeatAt = lastHeartbeatAt;
    }

    public boolean isHealthy() {
        return healthy;
    }

    public void setHealthy(boolean healthy) {
        this.healthy = healthy;
    }

    public String getUri() {
        return "http://" + host + ":" + port;
    }

    @Override
    public String toString() {
        return "ServiceInstance{" +
                "serviceId='" + serviceId + '\'' +
                ", instanceId='" + instanceId + '\'' +
                ", host='" + host + '\'' +
                ", port=" + port +
                ", healthy=" + healthy +
                '}';
    }
}
