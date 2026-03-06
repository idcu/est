package ltd.idcu.est.features.discovery.api;

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
}
