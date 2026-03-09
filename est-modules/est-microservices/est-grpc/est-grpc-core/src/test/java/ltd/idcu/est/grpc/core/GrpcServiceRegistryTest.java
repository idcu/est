package ltd.idcu.est.grpc.core;

import io.grpc.BindableService;
import io.grpc.ServerServiceDefinition;
import ltd.idcu.est.grpc.api.GrpcService;
import ltd.idcu.est.test.Assertions;
import ltd.idcu.est.test.annotation.Test;

import java.util.List;

public class GrpcServiceRegistryTest {

    @Test
    public void testSingleton() {
        GrpcServiceRegistry instance1 = GrpcServiceRegistry.getInstance();
        GrpcServiceRegistry instance2 = GrpcServiceRegistry.getInstance();
        
        Assertions.assertNotNull(instance1);
        Assertions.assertEquals(instance1, instance2);
    }

    @Test
    public void testRegisterAndGetService() {
        GrpcServiceRegistry registry = GrpcServiceRegistry.getInstance();
        registry.clear();
        
        TestService service = new TestService();
        registry.register(service);
        
        GrpcServiceRegistry.ServiceRegistration registration = registry.getService("TestService");
        
        Assertions.assertNotNull(registration);
        Assertions.assertEquals("TestService", registration.getName());
        Assertions.assertEquals("1.0.0", registration.getVersion());
        Assertions.assertEquals("", registration.getDescription());
        Assertions.assertEquals(service, registration.getService());
    }

    @Test
    public void testRegisterWithAnnotation() {
        GrpcServiceRegistry registry = GrpcServiceRegistry.getInstance();
        registry.clear();
        
        AnnotatedTestService service = new AnnotatedTestService();
        registry.register(service);
        
        GrpcServiceRegistry.ServiceRegistration registration = registry.getService("CustomService");
        
        Assertions.assertNotNull(registration);
        Assertions.assertEquals("CustomService", registration.getName());
        Assertions.assertEquals("2.0.0", registration.getVersion());
        Assertions.assertEquals("Custom annotated service", registration.getDescription());
    }

    @Test
    public void testUnregister() {
        GrpcServiceRegistry registry = GrpcServiceRegistry.getInstance();
        registry.clear();
        
        TestService service = new TestService();
        registry.register(service);
        
        Assertions.assertNotNull(registry.getService("TestService"));
        
        registry.unregister("TestService");
        
        Assertions.assertNull(registry.getService("TestService"));
    }

    @Test
    public void testGetAllServices() {
        GrpcServiceRegistry registry = GrpcServiceRegistry.getInstance();
        registry.clear();
        
        TestService service1 = new TestService();
        AnnotatedTestService service2 = new AnnotatedTestService();
        
        registry.register(service1);
        registry.register(service2);
        
        List<GrpcServiceRegistry.ServiceRegistration> services = registry.getAllServices();
        
        Assertions.assertEquals(2, services.size());
    }

    @Test
    public void testGetAllBindableServices() {
        GrpcServiceRegistry registry = GrpcServiceRegistry.getInstance();
        registry.clear();
        
        TestService service1 = new TestService();
        AnnotatedTestService service2 = new AnnotatedTestService();
        
        registry.register(service1);
        registry.register(service2);
        
        List<BindableService> bindableServices = registry.getAllBindableServices();
        
        Assertions.assertEquals(2, bindableServices.size());
    }

    @Test
    public void testClear() {
        GrpcServiceRegistry registry = GrpcServiceRegistry.getInstance();
        registry.clear();
        
        TestService service = new TestService();
        registry.register(service);
        
        Assertions.assertEquals(1, registry.getAllServices().size());
        
        registry.clear();
        
        Assertions.assertEquals(0, registry.getAllServices().size());
    }

    private static class TestService implements BindableService {
        @Override
        public ServerServiceDefinition bindService() {
            return null;
        }
    }

    @GrpcService(name = "CustomService", version = "2.0.0", description = "Custom annotated service")
    private static class AnnotatedTestService implements BindableService {
        @Override
        public ServerServiceDefinition bindService() {
            return null;
        }
    }
}
