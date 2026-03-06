package ltd.idcu.est.features.discovery.api;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class RandomLoadBalancer implements LoadBalancer {
    private final ServiceRegistry registry;
    private final Random random = new Random();

    public RandomLoadBalancer(ServiceRegistry registry) {
        this.registry = registry;
    }

    @Override
    public Optional<ServiceInstance> choose(String serviceId) {
        List<ServiceInstance> instances = getAvailableInstances(serviceId);
        if (instances.isEmpty()) {
            return Optional.empty();
        }
        int index = random.nextInt(instances.size());
        return Optional.of(instances.get(index));
    }

    @Override
    public List<ServiceInstance> getAvailableInstances(String serviceId) {
        return registry.getInstances(serviceId);
    }
}
