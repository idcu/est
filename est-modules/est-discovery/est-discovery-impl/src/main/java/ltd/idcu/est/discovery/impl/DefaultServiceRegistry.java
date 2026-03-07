package ltd.idcu.est.discovery.impl;

import ltd.idcu.est.discovery.api.HealthChecker;
import ltd.idcu.est.discovery.api.HttpHealthCheckConfig;
import ltd.idcu.est.discovery.api.ServiceInstance;
import ltd.idcu.est.discovery.api.ServiceRegistry;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class DefaultServiceRegistry implements ServiceRegistry {
    private final Map<String, Map<String, ServiceInstance>> services = new ConcurrentHashMap<>();
    private final long heartbeatTimeoutMs;
    private HealthChecker healthChecker;
    private HttpHealthCheckConfig healthCheckConfig;
    private boolean autoSaveEnabled = false;
    private String autoSavePath = null;

    public DefaultServiceRegistry() {
        this(30000);
    }

    public DefaultServiceRegistry(long heartbeatTimeoutMs) {
        this.heartbeatTimeoutMs = heartbeatTimeoutMs;
    }

    @Override
    public void register(ServiceInstance instance) {
        Objects.requireNonNull(instance, "ServiceInstance cannot be null");
        services.computeIfAbsent(instance.getServiceId(), k -> new ConcurrentHashMap<>())
                .put(instance.getInstanceId(), instance);
        autoSaveIfEnabled();
    }

    @Override
    public void unregister(String serviceId, String instanceId) {
        Map<String, ServiceInstance> instances = services.get(serviceId);
        if (instances != null) {
            instances.remove(instanceId);
            if (instances.isEmpty()) {
                services.remove(serviceId);
            }
            autoSaveIfEnabled();
        }
    }

    @Override
    public void heartbeat(String serviceId, String instanceId) {
        Optional<ServiceInstance> instance = getInstance(serviceId, instanceId);
        instance.ifPresent(i -> {
            i.setLastHeartbeatAt(Instant.now());
            autoSaveIfEnabled();
        });
    }

    @Override
    public Optional<ServiceInstance> getInstance(String serviceId, String instanceId) {
        Map<String, ServiceInstance> instances = services.get(serviceId);
        if (instances != null) {
            ServiceInstance instance = instances.get(instanceId);
            if (instance != null && isInstanceAlive(instance)) {
                return Optional.of(instance);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<ServiceInstance> getInstances(String serviceId) {
        Map<String, ServiceInstance> instances = services.get(serviceId);
        if (instances == null) {
            return Collections.emptyList();
        }
        return instances.values().stream()
                .filter(this::isInstanceAlive)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getServiceIds() {
        return new ArrayList<>(services.keySet());
    }

    @Override
    public void clear() {
        services.clear();
    }

    private boolean isInstanceAlive(ServiceInstance instance) {
        Instant now = Instant.now();
        Instant lastHeartbeat = instance.getLastHeartbeatAt();
        return Duration.between(lastHeartbeat, now).toMillis() < heartbeatTimeoutMs;
    }

    public void cleanupExpiredInstances() {
        Instant now = Instant.now();
        for (Map.Entry<String, Map<String, ServiceInstance>> serviceEntry : services.entrySet()) {
            Iterator<Map.Entry<String, ServiceInstance>> iterator = serviceEntry.getValue().entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, ServiceInstance> entry = iterator.next();
                ServiceInstance instance = entry.getValue();
                if (Duration.between(instance.getLastHeartbeatAt(), now).toMillis() >= heartbeatTimeoutMs) {
                    iterator.remove();
                }
            }
            if (serviceEntry.getValue().isEmpty()) {
                services.remove(serviceEntry.getKey());
            }
        }
    }

    @Override
    public void setHealthChecker(HealthChecker healthChecker) {
        this.healthChecker = healthChecker;
    }

    @Override
    public void checkAllHealth() {
        if (healthChecker == null) {
            return;
        }
        for (Map<String, ServiceInstance> instances : services.values()) {
            for (ServiceInstance instance : instances.values()) {
                boolean isHealthy = healthChecker.checkHealth(instance);
                instance.setHealthy(isHealthy);
            }
        }
    }

    @Override
    public void setHealthCheckConfig(HttpHealthCheckConfig config) {
        this.healthCheckConfig = config;
        if (healthChecker instanceof HttpHealthChecker) {
            ((HttpHealthChecker) healthChecker).setConfig(config);
        }
    }

    @Override
    public void saveToJson(String path) throws IOException {
        try (OutputStream outputStream = new FileOutputStream(path)) {
            saveToJson(outputStream);
        }
    }

    @Override
    public void saveToJson(OutputStream outputStream) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("  \"services\": [\n");
        boolean firstService = true;
        for (Map.Entry<String, Map<String, ServiceInstance>> serviceEntry : services.entrySet()) {
            if (!firstService) {
                sb.append(",\n");
            }
            sb.append("    {\n");
            sb.append("      \"serviceId\": \"").append(escapeJson(serviceEntry.getKey())).append("\",\n");
            sb.append("      \"instances\": [\n");
            boolean firstInstance = true;
            for (ServiceInstance instance : serviceEntry.getValue().values()) {
                if (!firstInstance) {
                    sb.append(",\n");
                }
                String instanceJson = instance.toJson();
                sb.append("        ").append(instanceJson);
                firstInstance = false;
            }
            sb.append("\n");
            sb.append("      ]\n");
            sb.append("    }");
            firstService = false;
        }
        sb.append("\n");
        sb.append("  ]\n");
        sb.append("}");
        outputStream.write(sb.toString().getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public void loadFromJson(String path) throws IOException {
        try (InputStream inputStream = new FileInputStream(path)) {
            loadFromJson(inputStream);
        }
    }

    @Override
    public void loadFromJson(InputStream inputStream) throws IOException {
        String json = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        services.clear();
        String servicesArray = extractJsonArray(json, "services");
        if (servicesArray != null && !servicesArray.isEmpty()) {
            parseServicesArray(servicesArray);
        }
    }

    @Override
    public void setAutoSave(boolean enabled) {
        this.autoSaveEnabled = enabled;
    }

    @Override
    public void setAutoSavePath(String path) {
        this.autoSavePath = path;
    }

    @Override
    public boolean isAutoSaveEnabled() {
        return autoSaveEnabled;
    }

    @Override
    public String getAutoSavePath() {
        return autoSavePath;
    }

    private void autoSaveIfEnabled() {
        if (autoSaveEnabled && autoSavePath != null) {
            try {
                saveToJson(autoSavePath);
            } catch (IOException e) {
            }
        }
    }

    private String extractJsonArray(String json, String key) {
        String searchKey = "\"" + key + "\":";
        int start = json.indexOf(searchKey);
        if (start == -1) {
            return "";
        }
        start += searchKey.length();
        int bracketStart = json.indexOf("[", start);
        int bracketEnd = bracketStart + 1;
        int count = 1;
        while (count > 0 && bracketEnd < json.length()) {
            char c = json.charAt(bracketEnd);
            if (c == '[') {
                count++;
            } else if (c == ']') {
                count--;
            }
            bracketEnd++;
        }
        return json.substring(bracketStart, bracketEnd);
    }

    private void parseServicesArray(String arrayStr) {
        String content = arrayStr.substring(1, arrayStr.length() - 1).trim();
        if (content.isEmpty()) {
            return;
        }
        int i = 0;
        while (i < content.length()) {
            while (i < content.length() && Character.isWhitespace(content.charAt(i))) {
                i++;
            }
            if (i >= content.length()) {
                break;
            }
            int braceStart = content.indexOf("{", i);
            int braceEnd = braceStart + 1;
            int count = 1;
            while (count > 0 && braceEnd < content.length()) {
                char c = content.charAt(braceEnd);
                if (c == '{') {
                    count++;
                } else if (c == '}') {
                    count--;
                }
                braceEnd++;
            }
            String serviceJson = content.substring(braceStart, braceEnd);
            parseServiceObject(serviceJson);
            i = braceEnd;
            while (i < content.length() && Character.isWhitespace(content.charAt(i))) {
                i++;
            }
            if (i < content.length() && content.charAt(i) == ',') {
                i++;
            }
        }
    }

    private void parseServiceObject(String json) {
        String serviceId = extractJsonString(json, "serviceId");
        String instancesArray = extractJsonArray(json, "instances");
        if (serviceId != null && !serviceId.isEmpty() && instancesArray != null) {
            Map<String, ServiceInstance> instancesMap = services.computeIfAbsent(serviceId, k -> new ConcurrentHashMap<>());
            parseInstancesArray(instancesArray, instancesMap);
        }
    }

    private void parseInstancesArray(String arrayStr, Map<String, ServiceInstance> instancesMap) {
        String content = arrayStr.substring(1, arrayStr.length() - 1).trim();
        if (content.isEmpty()) {
            return;
        }
        int i = 0;
        while (i < content.length()) {
            while (i < content.length() && Character.isWhitespace(content.charAt(i))) {
                i++;
            }
            if (i >= content.length()) {
                break;
            }
            int braceStart = content.indexOf("{", i);
            int braceEnd = braceStart + 1;
            int count = 1;
            while (count > 0 && braceEnd < content.length()) {
                char c = content.charAt(braceEnd);
                if (c == '{') {
                    count++;
                } else if (c == '}') {
                    count--;
                }
                braceEnd++;
            }
            String instanceJson = content.substring(braceStart, braceEnd);
            ServiceInstance instance = ServiceInstance.fromJson(instanceJson);
            instancesMap.put(instance.getInstanceId(), instance);
            i = braceEnd;
            while (i < content.length() && Character.isWhitespace(content.charAt(i))) {
                i++;
            }
            if (i < content.length() && content.charAt(i) == ',') {
                i++;
            }
        }
    }

    private String extractJsonString(String json, String key) {
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

    private String escapeJson(String str) {
        if (str == null) {
            return "";
        }
        return str.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r")
                  .replace("\t", "\\t");
    }

    private String unescapeJson(String str) {
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
