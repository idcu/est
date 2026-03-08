package ltd.idcu.est.discovery.api;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class DefaultServiceRegistry implements ServiceRegistry {
    private final Map<String, Map<String, ServiceInstance>> services = new ConcurrentHashMap<>();
    private final long heartbeatTimeoutMs;

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
    }

    @Override
    public void unregister(String serviceId, String instanceId) {
        Map<String, ServiceInstance> instances = services.get(serviceId);
        if (instances != null) {
            instances.remove(instanceId);
            if (instances.isEmpty()) {
                services.remove(serviceId);
            }
        }
    }

    @Override
    public void heartbeat(String serviceId, String instanceId) {
        Optional<ServiceInstance> instance = getInstance(serviceId, instanceId);
        instance.ifPresent(i -> i.setLastHeartbeatAt(Instant.now()));
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
}
