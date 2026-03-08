package ltd.idcu.est.discovery.impl;

import ltd.idcu.est.discovery.api.LoadBalancer;
import ltd.idcu.est.discovery.api.ServiceInstance;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class WeightedLoadBalancer implements LoadBalancer {
    private final Random random = new Random();
    private final List<ServiceInstance> instances = new ArrayList<>();

    public WeightedLoadBalancer() {
    }

    public WeightedLoadBalancer(List<ServiceInstance> instances) {
        this.instances.addAll(instances);
    }

    public void addInstance(ServiceInstance instance) {
        this.instances.add(instance);
    }

    @Override
    public Optional<ServiceInstance> choose(String serviceId) {
        List<ServiceInstance> availableInstances = getAvailableInstances(serviceId);
        if (availableInstances.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(select(availableInstances));
    }

    @Override
    public List<ServiceInstance> getAvailableInstances(String serviceId) {
        List<ServiceInstance> availableInstances = new ArrayList<>();
        for (ServiceInstance instance : instances) {
            if (instance.isHealthy() && (serviceId == null || serviceId.equals(instance.getServiceId()))) {
                availableInstances.add(instance);
            }
        }
        return availableInstances;
    }

    private ServiceInstance select(List<ServiceInstance> instances) {
        if (instances == null || instances.isEmpty()) {
            return null;
        }

        int totalWeight = 0;
        for (ServiceInstance instance : instances) {
            if (instance.isHealthy()) {
                totalWeight += instance.getWeight();
            }
        }

        if (totalWeight == 0) {
            return instances.get(0);
        }

        int randomValue = random.nextInt(totalWeight);
        int currentWeight = 0;

        for (ServiceInstance instance : instances) {
            if (instance.isHealthy()) {
                currentWeight += instance.getWeight();
                if (randomValue < currentWeight) {
                    return instance;
                }
            }
        }

        return instances.get(0);
    }
}
