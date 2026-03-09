package ltd.idcu.est.discovery.api;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ServiceInstance {
    private final String serviceId;
    private final String instanceId;
    private final String host;
    private final int port;
    private final Map<String, String> metadata;
    private final Instant registeredAt;
    private Instant lastHeartbeatAt;
    private boolean healthy;
    private ServiceStatus status;
    private int weight;
    private String zone;
    private String region;
    private String version;
    private String group;

    public ServiceInstance(String serviceId, String instanceId, String host, int port) {
        this.serviceId = serviceId;
        this.instanceId = instanceId != null ? instanceId : UUID.randomUUID().toString();
        this.host = host;
        this.port = port;
        this.metadata = new HashMap<>();
        this.registeredAt = Instant.now();
        this.lastHeartbeatAt = Instant.now();
        this.healthy = true;
        this.status = ServiceStatus.UP;
        this.weight = 100;
        this.zone = "default";
        this.region = "default";
        this.version = "1.0.0";
        this.group = "default";
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

    public String getMetadata(String key) {
        return metadata.get(key);
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

    public ServiceStatus getStatus() {
        return status;
    }

    public void setStatus(ServiceStatus status) {
        this.status = status;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        if (weight < 0) {
            throw new IllegalArgumentException("Weight must be non-negative");
        }
        this.weight = weight;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone != null ? zone : "default";
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region != null ? region : "default";
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version != null ? version : "1.0.0";
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group != null ? group : "default";
    }

    public String getUri() {
        return "http://" + host + ":" + port;
    }

    public String getSecureUri() {
        return "https://" + host + ":" + port;
    }

    @Override
    public String toString() {
        return "ServiceInstance{" +
                "serviceId='" + serviceId + '\'' +
                ", instanceId='" + instanceId + '\'' +
                ", host='" + host + '\'' +
                ", port=" + port +
                ", healthy=" + healthy +
                ", status=" + status +
                ", weight=" + weight +
                ", zone='" + zone + '\'' +
                ", version='" + version + '\'' +
                '}';
    }

    public String toJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"serviceId\":\"").append(escapeJson(serviceId)).append("\",");
        sb.append("\"instanceId\":\"").append(escapeJson(instanceId)).append("\",");
        sb.append("\"host\":\"").append(escapeJson(host)).append("\",");
        sb.append("\"port\":").append(port).append(",");
        sb.append("\"healthy\":").append(healthy).append(",");
        sb.append("\"status\":\"").append(status.name()).append("\",");
        sb.append("\"weight\":").append(weight).append(",");
        sb.append("\"zone\":\"").append(escapeJson(zone)).append("\",");
        sb.append("\"region\":\"").append(escapeJson(region)).append("\",");
        sb.append("\"version\":\"").append(escapeJson(version)).append("\",");
        sb.append("\"group\":\"").append(escapeJson(group)).append("\",");
        sb.append("\"registeredAt\":\"").append(registeredAt.toString()).append("\",");
        sb.append("\"lastHeartbeatAt\":\"").append(lastHeartbeatAt.toString()).append("\"");
        sb.append("}");
        return sb.toString();
    }

    public static ServiceInstance fromJson(String json) {
        String serviceId = extractJsonString(json, "serviceId");
        String instanceId = extractJsonString(json, "instanceId");
        String host = extractJsonString(json, "host");
        int port = Integer.parseInt(extractJsonValue(json, "port"));
        
        ServiceInstance instance = new ServiceInstance(serviceId, instanceId, host, port);
        
        String healthyStr = extractJsonValue(json, "healthy");
        instance.setHealthy(Boolean.parseBoolean(healthyStr));
        
        String statusStr = extractJsonString(json, "status");
        instance.setStatus(ServiceStatus.valueOf(statusStr));
        
        String weightStr = extractJsonValue(json, "weight");
        instance.setWeight(Integer.parseInt(weightStr));
        
        instance.setZone(extractJsonString(json, "zone"));
        instance.setRegion(extractJsonString(json, "region"));
        instance.setVersion(extractJsonString(json, "version"));
        instance.setGroup(extractJsonString(json, "group"));
        
        return instance;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String serviceId;
        private String instanceId;
        private String host;
        private int port;
        private Map<String, String> metadata = new HashMap<>();
        private boolean healthy = true;
        private ServiceStatus status = ServiceStatus.UP;
        private int weight = 100;
        private String zone = "default";
        private String region = "default";
        private String version = "1.0.0";
        private String group = "default";

        public Builder serviceId(String serviceId) {
            this.serviceId = serviceId;
            return this;
        }

        public Builder instanceId(String instanceId) {
            this.instanceId = instanceId;
            return this;
        }

        public Builder host(String host) {
            this.host = host;
            return this;
        }

        public Builder port(int port) {
            this.port = port;
            return this;
        }

        public Builder metadata(String key, String value) {
            this.metadata.put(key, value);
            return this;
        }

        public Builder healthy(boolean healthy) {
            this.healthy = healthy;
            return this;
        }

        public Builder status(ServiceStatus status) {
            this.status = status;
            return this;
        }

        public Builder weight(int weight) {
            this.weight = weight;
            return this;
        }

        public Builder zone(String zone) {
            this.zone = zone;
            return this;
        }

        public Builder region(String region) {
            this.region = region;
            return this;
        }

        public Builder version(String version) {
            this.version = version;
            return this;
        }

        public Builder group(String group) {
            this.group = group;
            return this;
        }

        public ServiceInstance build() {
            ServiceInstance instance = new ServiceInstance(serviceId, instanceId, host, port);
            metadata.forEach(instance::addMetadata);
            instance.setHealthy(healthy);
            instance.setStatus(status);
            instance.setWeight(weight);
            instance.setZone(zone);
            instance.setRegion(region);
            instance.setVersion(version);
            instance.setGroup(group);
            return instance;
        }
    }

    private static String escapeJson(String str) {
        if (str == null) {
            return "";
        }
        return str.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r")
                  .replace("\t", "\\t");
    }

    private static String extractJsonString(String json, String key) {
        String searchKey = "\"" + key + "\":";
        int start = json.indexOf(searchKey);
        if (start == -1) {
            return "";
        }
        start += searchKey.length();
        int quoteStart = json.indexOf("\"", start);
        int quoteEnd = json.indexOf("\"", quoteStart + 1);
        return unescapeJson(json.substring(quoteStart + 1, quoteEnd));
    }

    private static String extractJsonValue(String json, String key) {
        String searchKey = "\"" + key + "\":";
        int start = json.indexOf(searchKey);
        if (start == -1) {
            return "";
        }
        start += searchKey.length();
        int end = json.indexOf(",", start);
        if (end == -1) {
            end = json.indexOf("}", start);
        }
        return json.substring(start, end).trim();
    }

    private static String unescapeJson(String str) {
        if (str == null) {
            return "";
        }
        return str.replace("\\\"", "\"")
                  .replace("\\\\", "\\")
                  .replace("\\n", "\n")
                  .replace("\\r", "\r")
                  .replace("\\t", "\t");
    }
}
