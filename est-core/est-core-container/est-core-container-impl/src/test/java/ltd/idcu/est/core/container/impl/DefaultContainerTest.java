package ltd.idcu.est.core.container.impl;

import ltd.idcu.est.core.container.api.Container;
import ltd.idcu.est.core.container.api.annotation.Component;
import ltd.idcu.est.core.container.api.annotation.Inject;
import ltd.idcu.est.core.container.api.annotation.Primary;
import ltd.idcu.est.core.container.api.annotation.Qualifier;
import ltd.idcu.est.core.container.api.exception.CircularDependencyException;
import ltd.idcu.est.core.container.api.scope.Scope;
import ltd.idcu.est.core.lifecycle.api.DisposableBean;
import ltd.idcu.est.core.lifecycle.api.InitializingBean;
import ltd.idcu.est.core.lifecycle.api.PostConstruct;
import ltd.idcu.est.core.lifecycle.api.PreDestroy;
import ltd.idcu.est.test.Assertions;
import ltd.idcu.est.test.annotation.Test;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public class DefaultContainerTest {

    @Test
    public void testContainerCreation() {
        Container container = new DefaultContainer();
        Assertions.assertNotNull(container);
    }

    @Test
    public void testRegisterAndGet() {
        Container container = new DefaultContainer();
        container.register(Service.class, SimpleService.class);
        
        Service service = container.get(Service.class);
        Assertions.assertNotNull(service);
        Assertions.assertTrue(service instanceof SimpleService);
    }

    @Test
    public void testRegisterSingleton() {
        Container container = new DefaultContainer();
        SimpleService instance = new SimpleService();
        container.registerSingleton(Service.class, instance);
        
        Service service = container.get(Service.class);
        Assertions.assertSame(instance, service);
    }

    @Test
    public void testSingletonScope() {
        Container container = new DefaultContainer();
        container.register(Service.class, SimpleService.class, Scope.SINGLETON);
        
        Service service1 = container.get(Service.class);
        Service service2 = container.get(Service.class);
        
        Assertions.assertSame(service1, service2);
    }

    @Test
    public void testPrototypeScope() {
        Container container = new DefaultContainer();
        container.register(Service.class, SimpleService.class, Scope.PROTOTYPE);
        
        Service service1 = container.get(Service.class);
        Service service2 = container.get(Service.class);
        
        Assertions.assertNotSame(service1, service2);
    }

    @Test
    public void testFieldInjection() {
        Container container = new DefaultContainer();
        container.register(Service.class, SimpleService.class);
        container.register(ClientWithFieldInjection.class, ClientWithFieldInjection.class);
        
        ClientWithFieldInjection client = container.get(ClientWithFieldInjection.class);
        Assertions.assertNotNull(client);
        Assertions.assertNotNull(client.service);
        Assertions.assertTrue(client.service instanceof SimpleService);
    }

    @Test
    public void testConstructorInjection() {
        Container container = new DefaultContainer();
        container.register(Service.class, SimpleService.class);
        container.register(ClientWithConstructorInjection.class, ClientWithConstructorInjection.class);
        
        ClientWithConstructorInjection client = container.get(ClientWithConstructorInjection.class);
        Assertions.assertNotNull(client);
        Assertions.assertNotNull(client.service);
        Assertions.assertTrue(client.service instanceof SimpleService);
    }

    @Test
    public void testQualifier() {
        Container container = new DefaultContainer();
        container.register(Service.class, SimpleService.class, "simple");
        container.register(Service.class, AnotherService.class, "another");
        
        Service simpleService = container.get(Service.class, "simple");
        Service anotherService = container.get(Service.class, "another");
        
        Assertions.assertTrue(simpleService instanceof SimpleService);
        Assertions.assertTrue(anotherService instanceof AnotherService);
    }

    @Test
    public void testPrimary() {
        Container container = new DefaultContainer();
        container.register(Service.class, SimpleService.class);
        container.register(Service.class, PrimaryService.class);
        
        Service service = container.get(Service.class);
        Assertions.assertTrue(service instanceof PrimaryService);
    }

    @Test
    public void testGetIfPresent() {
        Container container = new DefaultContainer();
        container.register(Service.class, SimpleService.class);
        
        Optional<Service> present = container.getIfPresent(Service.class);
        Assertions.assertTrue(present.isPresent());
        
        Optional<NonExistentService> notPresent = container.getIfPresent(NonExistentService.class);
        Assertions.assertFalse(notPresent.isPresent());
    }

    @Test
    public void testContains() {
        Container container = new DefaultContainer();
        container.register(Service.class, SimpleService.class);
        
        Assertions.assertTrue(container.contains(Service.class));
        Assertions.assertFalse(container.contains(NonExistentService.class));
    }

    @Test
    public void testRegisterSupplier() {
        Container container = new DefaultContainer();
        AtomicBoolean created = new AtomicBoolean(false);
        container.registerSupplier(Service.class, () -> {
            created.set(true);
            return new SimpleService();
        });
        
        Assertions.assertFalse(created.get());
        Service service = container.get(Service.class);
        Assertions.assertTrue(created.get());
        Assertions.assertNotNull(service);
    }

    @Test
    public void testCreate() {
        Container container = new DefaultContainer();
        container.register(Service.class, SimpleService.class);
        
        SimpleService service = container.create(SimpleService.class);
        Assertions.assertNotNull(service);
    }

    @Test
    public void testPostConstruct() {
        Container container = new DefaultContainer();
        container.register(ServiceWithLifecycle.class, ServiceWithLifecycle.class);
        
        ServiceWithLifecycle service = container.get(ServiceWithLifecycle.class);
        Assertions.assertTrue(service.postConstructCalled);
    }

    @Test
    public void testInitializingBean() {
        Container container = new DefaultContainer();
        container.register(ServiceWithInitializingBean.class, ServiceWithInitializingBean.class);
        
        ServiceWithInitializingBean service = container.get(ServiceWithInitializingBean.class);
        Assertions.assertTrue(service.afterPropertiesSetCalled);
    }

    @Test
    public void testPreDestroy() {
        DefaultContainer container = new DefaultContainer();
        container.register(ServiceWithLifecycle.class, ServiceWithLifecycle.class);
        
        ServiceWithLifecycle service = container.get(ServiceWithLifecycle.class);
        container.close();
        
        Assertions.assertTrue(service.preDestroyCalled);
    }

    @Test
    public void testDisposableBean() {
        DefaultContainer container = new DefaultContainer();
        container.register(ServiceWithDisposableBean.class, ServiceWithDisposableBean.class);
        
        ServiceWithDisposableBean service = container.get(ServiceWithDisposableBean.class);
        container.close();
        
        Assertions.assertTrue(service.destroyCalled);
    }



    @Test
    public void testClear() {
        Container container = new DefaultContainer();
        container.register(Service.class, SimpleService.class);
        
        Assertions.assertTrue(container.contains(Service.class));
        container.clear();
        Assertions.assertFalse(container.contains(Service.class));
    }

    @Test
    public void testRegisterWithQualifier() {
        Container container = new DefaultContainer();
        container.register(Service.class, SimpleService.class, Scope.SINGLETON, "myService");
        
        Service service = container.get(Service.class, "myService");
        Assertions.assertNotNull(service);
    }

    @Test
    public void testGetNonExistent() {
        Container container = new DefaultContainer();
        
        Assertions.assertThrows(IllegalStateException.class, () -> {
            container.get(NonExistentService.class);
        });
    }

    interface Service {
        String getName();
    }

    @Component
    static class SimpleService implements Service {
        @Override
        public String getName() {
            return "SimpleService";
        }
    }

    @Component
    static class AnotherService implements Service {
        @Override
        public String getName() {
            return "AnotherService";
        }
    }

    @Primary
    @Component
    static class PrimaryService implements Service {
        @Override
        public String getName() {
            return "PrimaryService";
        }
    }

    @Component
    static class ClientWithFieldInjection {
        @Inject
        Service service;
    }

    @Component
    static class ClientWithConstructorInjection {
        final Service service;

        @Inject
        ClientWithConstructorInjection(Service service) {
            this.service = service;
        }
    }

    @Component
    static class ServiceWithLifecycle {
        boolean postConstructCalled = false;
        boolean preDestroyCalled = false;

        @PostConstruct
        void init() {
            postConstructCalled = true;
        }

        @PreDestroy
        void cleanup() {
            preDestroyCalled = true;
        }
    }

    @Component
    static class ServiceWithInitializingBean implements InitializingBean {
        boolean afterPropertiesSetCalled = false;

        @Override
        public void afterPropertiesSet() {
            afterPropertiesSetCalled = true;
        }
    }

    @Component
    static class ServiceWithDisposableBean implements DisposableBean {
        boolean destroyCalled = false;

        @Override
        public void destroy() {
            destroyCalled = true;
        }
    }

    @Component
    static class CircularA {
        @Inject
        CircularB b;
    }

    @Component
    static class CircularB {
        @Inject
        CircularA a;
    }

    interface NonExistentService {
    }
}
