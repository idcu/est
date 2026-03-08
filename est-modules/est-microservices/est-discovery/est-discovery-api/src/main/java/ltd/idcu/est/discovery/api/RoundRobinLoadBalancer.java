package ltd.idcu.est.discovery.api;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class RoundRobinLoadBalancer implements LoadBalancer {
    private final ServiceRegistry registry;
    private final ConcurrentHashMap<String, AtomicInteger> counters = new ConcurrentHashMap<>();

    public RoundRobinLoadBalancer(ServiceRegistry registry) {
        this.registry = registry;
    }

    @Override
    public Optional<ServiceInstance> choose(String serviceId) {
        List<ServiceInstance> instances = getAvailableInstances(serviceId);
        if (instances.isEmpty()) {
            return Optional.empty();
        }

        AtomicInteger counter = counters.computeIfAbsent(serviceId, k -> new AtomicInteger(0));
        int index = Math.abs(counter.getAndIncrement() % instances.size());
        return Optional.of(instances.get(index));
    }

    @Override
    public List<ServiceInstance> getAvailableInstances(String serviceId) {
        return registry.getInstances(serviceId);
    }
}
