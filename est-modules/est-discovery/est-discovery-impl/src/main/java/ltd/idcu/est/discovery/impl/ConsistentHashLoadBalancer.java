package ltd.idcu.est.discovery.impl;

import ltd.idcu.est.discovery.api.LoadBalancer;
import ltd.idcu.est.discovery.api.ServiceInstance;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

public class ConsistentHashLoadBalancer implements LoadBalancer {
    private static final int VIRTUAL_NODE_COUNT = 160;
    private final TreeMap<Long, ServiceInstance> circle = new TreeMap<>();
    private final Map<String, ServiceInstance> instances = new ConcurrentHashMap<>();
    private final List<ServiceInstance> instanceList = new ArrayList<>();

    @Override
    public Optional<ServiceInstance> choose(String serviceId) {
        if (instanceList.isEmpty()) {
            return Optional.empty();
        }

        String key = Thread.currentThread().getName() + System.nanoTime();
        long hash = hash(key);
        
        Map.Entry<Long, ServiceInstance> entry = circle.ceilingEntry(hash);
        if (entry == null) {
            entry = circle.firstEntry();
        }
        
        return entry != null ? Optional.of(entry.getValue()) : Optional.of(instanceList.get(0));
    }

    @Override
    public List<ServiceInstance> getAvailableInstances(String serviceId) {
        return new ArrayList<>(instanceList);
    }

    public void setInstances(List<ServiceInstance> instances) {
        this.instanceList.clear();
        this.instanceList.addAll(instances);
        updateCircle(instances);
    }

    private void updateCircle(List<ServiceInstance> serviceInstances) {
        circle.clear();
        instances.clear();
        
        for (ServiceInstance instance : serviceInstances) {
            String key = instance.getInstanceId();
            instances.put(key, instance);
            addInstanceToCircle(instance);
        }
    }

    private void addInstanceToCircle(ServiceInstance instance) {
        for (int i = 0; i < VIRTUAL_NODE_COUNT; i++) {
            long hash = hash(instance.getInstanceId() + "#" + i);
            circle.put(hash, instance);
        }
    }

    private long hash(String key) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(key.getBytes(StandardCharsets.UTF_8));
            return ((long) (digest[3] & 0xFF) << 24)
                    | ((long) (digest[2] & 0xFF) << 16)
                    | ((long) (digest[1] & 0xFF) << 8)
                    | ((long) (digest[0] & 0xFF));
        } catch (NoSuchAlgorithmException e) {
            return key.hashCode() & 0xFFFFFFFFL;
        }
    }
}
