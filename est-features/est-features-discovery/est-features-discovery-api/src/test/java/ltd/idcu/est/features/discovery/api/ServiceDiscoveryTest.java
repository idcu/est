package ltd.idcu.est.features.discovery.api;

import ltd.idcu.est.test.annotation.Test;

import java.util.List;
import java.util.Optional;

import static ltd.idcu.est.test.Assert.*;

public class ServiceDiscoveryTest {

    @Test
    public void testServiceInstance() {
        ServiceInstance instance = new ServiceInstance("user-service", "instance-1", "localhost", 8080);
        
        assertEquals("user-service", instance.getServiceId());
        assertEquals("instance-1", instance.getInstanceId());
        assertEquals("localhost", instance.getHost());
        assertEquals(8080, instance.getPort());
        assertEquals("http://localhost:8080", instance.getUri());
        assertTrue(instance.isHealthy());
    }

    @Test
    public void testServiceInstanceMetadata() {
        ServiceInstance instance = new ServiceInstance("user-service", "instance-1", "localhost", 8080);
        instance.addMetadata("version", "1.0.0");
        instance.addMetadata("region", "us-east-1");
        
        assertEquals("1.0.0", instance.getMetadata().get("version"));
        assertEquals("us-east-1", instance.getMetadata().get("region"));
    }

    @Test
    public void testServiceRegistry() {
        ServiceRegistry registry = new DefaultServiceRegistry();
        
        ServiceInstance instance1 = new ServiceInstance("user-service", "instance-1", "localhost", 8080);
        ServiceInstance instance2 = new ServiceInstance("user-service", "instance-2", "localhost", 8081);
        
        registry.register(instance1);
        registry.register(instance2);
        
        List<String> serviceIds = registry.getServiceIds();
        assertEquals(1, serviceIds.size());
        assertTrue(serviceIds.contains("user-service"));
        
        List<ServiceInstance> instances = registry.getInstances("user-service");
        assertEquals(2, instances.size());
    }

    @Test
    public void testServiceRegistryUnregister() {
        ServiceRegistry registry = new DefaultServiceRegistry();
        
        ServiceInstance instance = new ServiceInstance("user-service", "instance-1", "localhost", 8080);
        registry.register(instance);
        
        Optional<ServiceInstance> retrieved = registry.getInstance("user-service", "instance-1");
        assertTrue(retrieved.isPresent());
        
        registry.unregister("user-service", "instance-1");
        retrieved = registry.getInstance("user-service", "instance-1");
        assertFalse(retrieved.isPresent());
    }

    @Test
    public void testRoundRobinLoadBalancer() {
        ServiceRegistry registry = new DefaultServiceRegistry();
        
        registry.register(new ServiceInstance("user-service", "instance-1", "localhost", 8080));
        registry.register(new ServiceInstance("user-service", "instance-2", "localhost", 8081));
        
        LoadBalancer loadBalancer = new RoundRobinLoadBalancer(registry);
        
        Optional<ServiceInstance> instance1 = loadBalancer.choose("user-service");
        Optional<ServiceInstance> instance2 = loadBalancer.choose("user-service");
        Optional<ServiceInstance> instance3 = loadBalancer.choose("user-service");
        
        assertTrue(instance1.isPresent());
        assertTrue(instance2.isPresent());
        assertTrue(instance3.isPresent());
        
        assertEquals("instance-1", instance1.get().getInstanceId());
        assertEquals("instance-2", instance2.get().getInstanceId());
        assertEquals("instance-1", instance3.get().getInstanceId());
    }

    @Test
    public void testRandomLoadBalancer() {
        ServiceRegistry registry = new DefaultServiceRegistry();
        
        registry.register(new ServiceInstance("user-service", "instance-1", "localhost", 8080));
        registry.register(new ServiceInstance("user-service", "instance-2", "localhost", 8081));
        
        LoadBalancer loadBalancer = new RandomLoadBalancer(registry);
        
        Optional<ServiceInstance> instance1 = loadBalancer.choose("user-service");
        Optional<ServiceInstance> instance2 = loadBalancer.choose("user-service");
        
        assertTrue(instance1.isPresent());
        assertTrue(instance2.isPresent());
    }
}
