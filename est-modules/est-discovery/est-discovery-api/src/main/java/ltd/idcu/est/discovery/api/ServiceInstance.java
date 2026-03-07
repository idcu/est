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
    private int weight;

    public ServiceInstance(String serviceId, String instanceId, String host, int port) {
        this(serviceId, instanceId, host, port, 1);
    }

    public ServiceInstance(String serviceId, String instanceId, String host, int port, int weight) {
        this.serviceId = serviceId;
        this.instanceId = instanceId;
        this.host = host;
        this.port = port;
        this.weight = weight;
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

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
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

    public String toJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"serviceId\":\"").append(escapeJson(serviceId)).append("\",");
        sb.append("\"instanceId\":\"").append(escapeJson(instanceId)).append("\",");
        sb.append("\"host\":\"").append(escapeJson(host)).append("\",");
        sb.append("\"port\":").append(port).append(",");
        sb.append("\"weight\":").append(weight).append(",");
        sb.append("\"healthy\":").append(healthy).append(",");
        sb.append("\"registeredAt\":\"").append(registeredAt.toString()).append("\",");
        sb.append("\"lastHeartbeatAt\":\"").append(lastHeartbeatAt.toString()).append("\",");
        sb.append("\"metadata\":{");
        boolean first = true;
        for (Map.Entry<String, String> entry : metadata.entrySet()) {
            if (!first) {
                sb.append(",");
            }
            sb.append("\"").append(escapeJson(entry.getKey())).append("\":\"").append(escapeJson(entry.getValue())).append("\"");
            first = false;
        }
        sb.append("}");
        sb.append("}");
        return sb.toString();
    }

    public static ServiceInstance fromJson(String json) {
        String serviceId = extractJsonString(json, "serviceId");
        String instanceId = extractJsonString(json, "instanceId");
        String host = extractJsonString(json, "host");
        int port = Integer.parseInt(extractJsonValue(json, "port"));
        int weight = Integer.parseInt(extractJsonValue(json, "weight"));
        boolean healthy = Boolean.parseBoolean(extractJsonValue(json, "healthy"));
        String registeredAtStr = extractJsonString(json, "registeredAt");
        String lastHeartbeatAtStr = extractJsonString(json, "lastHeartbeatAt");
        
        ServiceInstance instance = new ServiceInstance(serviceId, instanceId, host, port, weight);
        instance.setHealthy(healthy);
        instance.lastHeartbeatAt = Instant.parse(lastHeartbeatAtStr);
        instance.metadata.clear();
        
        String metadataStr = extractJsonObject(json, "metadata");
        if (metadataStr != null && !metadataStr.isEmpty()) {
            Map<String, String> metadata = parseJsonMap(metadataStr);
            for (Map.Entry<String, String> entry : metadata.entrySet()) {
                instance.addMetadata(entry.getKey(), entry.getValue());
            }
        }
        
        return instance;
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

    private static String extractJsonObject(String json, String key) {
        String searchKey = "\"" + key + "\":";
        int start = json.indexOf(searchKey);
        if (start == -1) {
            return "";
        }
        start += searchKey.length();
        int braceStart = json.indexOf("{", start);
        int braceEnd = braceStart + 1;
        int count = 1;
        while (count > 0 && braceEnd < json.length()) {
            char c = json.charAt(braceEnd);
            if (c == '{') {
                count++;
            } else if (c == '}') {
                count--;
            }
            braceEnd++;
        }
        return json.substring(braceStart, braceEnd);
    }

    private static Map<String, String> parseJsonMap(String json) {
        Map<String, String> map = new HashMap<>();
        String content = json.substring(1, json.length() - 1).trim();
        if (content.isEmpty()) {
            return map;
        }
        
        int i = 0;
        while (i < content.length()) {
            while (i < content.length() && Character.isWhitespace(content.charAt(i))) {
                i++;
            }
            if (i >= content.length()) {
                break;
            }
            
            int keyStart = content.indexOf("\"", i);
            int keyEnd = content.indexOf("\"", keyStart + 1);
            String key = unescapeJson(content.substring(keyStart + 1, keyEnd));
            
            i = keyEnd + 1;
            while (i < content.length() && Character.isWhitespace(content.charAt(i))) {
                i++;
            }
            i++;
            
            while (i < content.length() && Character.isWhitespace(content.charAt(i))) {
                i++;
            }
            
            int valueStart = content.indexOf("\"", i);
            int valueEnd = content.indexOf("\"", valueStart + 1);
            String value = unescapeJson(content.substring(valueStart + 1, valueEnd));
            
            map.put(key, value);
            
            i = valueEnd + 1;
            while (i < content.length() && Character.isWhitespace(content.charAt(i))) {
                i++;
            }
            if (i < content.length() && content.charAt(i) == ',') {
                i++;
            }
        }
        
        return map;
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
