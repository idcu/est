package ltd.idcu.est.discovery.api;

import java.util.List;
import java.util.Optional;

public interface LoadBalancer {
    Optional<ServiceInstance> choose(String serviceId);

    List<ServiceInstance> getAvailableInstances(String serviceId);
}
