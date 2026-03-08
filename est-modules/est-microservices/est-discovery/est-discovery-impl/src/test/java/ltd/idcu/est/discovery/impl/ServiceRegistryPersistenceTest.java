package ltd.idcu.est.discovery.impl;

import ltd.idcu.est.discovery.api.ServiceInstance;
import ltd.idcu.est.discovery.api.ServiceRegistry;
import ltd.idcu.est.test.Assertions;
import ltd.idcu.est.test.annotation.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class ServiceRegistryPersistenceTest {

    @Test
    public void testServiceInstanceJsonSerialization() {
        ServiceInstance instance = new ServiceInstance("user-service", "inst-1", "localhost", 8080, 10);
        instance.addMetadata("version", "1.0.0");
        instance.addMetadata("region", "us-east");
        
        String json = instance.toJson();
        Assertions.assertNotNull(json);
        Assertions.assertTrue(json.contains("user-service"));
        Assertions.assertTrue(json.contains("inst-1"));
        Assertions.assertTrue(json.contains("localhost"));
        Assertions.assertTrue(json.contains("8080"));
        Assertions.assertTrue(json.contains("1.0.0"));
        Assertions.assertTrue(json.contains("us-east"));
        
        ServiceInstance restored = ServiceInstance.fromJson(json);
        Assertions.assertEquals("user-service", restored.getServiceId());
        Assertions.assertEquals("inst-1", restored.getInstanceId());
        Assertions.assertEquals("localhost", restored.getHost());
        Assertions.assertEquals(8080, restored.getPort());
        Assertions.assertEquals(10, restored.getWeight());
        Assertions.assertEquals("1.0.0", restored.getMetadata().get("version"));
        Assertions.assertEquals("us-east", restored.getMetadata().get("region"));
    }

    @Test
    public void testServiceRegistrySaveAndLoad() throws IOException {
        ServiceRegistry registry1 = new DefaultServiceRegistry();
        
        ServiceInstance instance1 = new ServiceInstance("user-service", "inst-1", "localhost", 8080);
        ServiceInstance instance2 = new ServiceInstance("user-service", "inst-2", "localhost", 8081);
        ServiceInstance instance3 = new ServiceInstance("order-service", "inst-1", "localhost", 9090);
        
        registry1.register(instance1);
        registry1.register(instance2);
        registry1.register(instance3);
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        registry1.saveToJson(outputStream);
        
        ServiceRegistry registry2 = new DefaultServiceRegistry();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        registry2.loadFromJson(inputStream);
        
        List<ServiceInstance> userInstances = registry2.getInstances("user-service");
        Assertions.assertEquals(2, userInstances.size());
        
        List<ServiceInstance> orderInstances = registry2.getInstances("order-service");
        Assertions.assertEquals(1, orderInstances.size());
        
        Optional<ServiceInstance> restoredInstance = registry2.getInstance("user-service", "inst-1");
        Assertions.assertTrue(restoredInstance.isPresent());
        Assertions.assertEquals("localhost", restoredInstance.get().getHost());
        Assertions.assertEquals(8080, restoredInstance.get().getPort());
    }

    @Test
    public void testServiceRegistryAutoSave() throws IOException, InterruptedException {
        DefaultServiceRegistry registry = new DefaultServiceRegistry();
        String tempPath = System.getProperty("java.io.tmpdir") + "/test-service-registry.json";
        
        registry.setAutoSavePath(tempPath);
        registry.setAutoSave(true);
        
        ServiceInstance instance = new ServiceInstance("test-service", "test-inst", "localhost", 8080);
        registry.register(instance);
        
        Thread.sleep(100);
        
        DefaultServiceRegistry registry2 = new DefaultServiceRegistry();
        registry2.loadFromJson(tempPath);
        
        Optional<ServiceInstance> restoredInstance = registry2.getInstance("test-service", "test-inst");
        Assertions.assertTrue(restoredInstance.isPresent());
        
        java.io.File tempFile = new java.io.File(tempPath);
        if (tempFile.exists()) {
            tempFile.delete();
        }
    }
}
