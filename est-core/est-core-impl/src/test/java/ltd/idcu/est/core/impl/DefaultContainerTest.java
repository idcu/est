package ltd.idcu.est.core.impl;

import ltd.idcu.est.core.api.Container;
import ltd.idcu.est.core.api.annotation.Component;
import ltd.idcu.est.core.api.annotation.Inject;
import ltd.idcu.est.core.api.annotation.Qualifier;
import ltd.idcu.est.core.api.annotation.Service;
import ltd.idcu.est.core.api.lifecycle.DisposableBean;
import ltd.idcu.est.core.api.lifecycle.InitializingBean;
import ltd.idcu.est.core.api.lifecycle.PostConstruct;
import ltd.idcu.est.core.api.lifecycle.PreDestroy;
import ltd.idcu.est.core.api.processor.BeanPostProcessor;
import ltd.idcu.est.core.api.scope.Scope;
import ltd.idcu.est.test.Assertions;
import ltd.idcu.est.test.annotation.Test;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class DefaultContainerTest {

    @Test
    public void testCreateContainer() {
        Container container = new DefaultContainer();
        Assertions.assertNotNull(container);
    }

    @Test
    public void testRegisterAndGet() {
        Container container = new DefaultContainer();
        container.register(ServiceInterface.class, ServiceImpl.class);
        
        ServiceInterface service = container.get(ServiceInterface.class);
        Assertions.assertNotNull(service);
        Assertions.assertTrue(service instanceof ServiceImpl);
    }

    @Test
    public void testRegisterWithQualifier() {
        Container container = new DefaultContainer();
        container.register(ServiceInterface.class, ServiceImpl.class, "impl1");
        container.register(ServiceInterface.class, ServiceImpl2.class, "impl2");
        
        ServiceInterface service1 = container.get(ServiceInterface.class, "impl1");
        ServiceInterface service2 = container.get(ServiceInterface.class, "impl2");
        
        Assertions.assertNotNull(service1);
        Assertions.assertNotNull(service2);
        Assertions.assertTrue(service1 instanceof ServiceImpl);
        Assertions.assertTrue(service2 instanceof ServiceImpl2);
    }

    @Test
    public void testSingletonScope() {
        Container container = new DefaultContainer();
        container.register(ServiceInterface.class, ServiceImpl.class, Scope.SINGLETON);
        
        ServiceInterface service1 = container.get(ServiceInterface.class);
        ServiceInterface service2 = container.get(ServiceInterface.class);
        
        Assertions.assertSame(service1, service2);
    }

    @Test
    public void testPrototypeScope() {
        Container container = new DefaultContainer();
        container.register(ServiceInterface.class, ServiceImpl.class, Scope.PROTOTYPE);
        
        ServiceInterface service1 = container.get(ServiceInterface.class);
        ServiceInterface service2 = container.get(ServiceInterface.class);
        
        Assertions.assertNotSame(service1, service2);
    }

    @Test
    public void testRegisterSingleton() {
        Container container = new DefaultContainer();
        ServiceInterface service = new ServiceImpl();
        container.registerSingleton(ServiceInterface.class, service);
        
        ServiceInterface retrieved = container.get(ServiceInterface.class);
        Assertions.assertSame(service, retrieved);
    }

    @Test
    public void testRegisterSupplier() {
        Container container = new DefaultContainer();
        AtomicInteger counter = new AtomicInteger(0);
        container.registerSupplier(ServiceInterface.class, () -> {
            ServiceImpl service = new ServiceImpl();
            service.setId(counter.incrementAndGet());
            return service;
        });
        
        ServiceInterface service1 = container.get(ServiceInterface.class);
        ServiceInterface service2 = container.get(ServiceInterface.class);
        
        Assertions.assertEquals(1, ((ServiceImpl) service1).getId());
        Assertions.assertEquals(1, ((ServiceImpl) service2).getId());
    }

    @Test
    public void testRegisterSupplierPrototype() {
        Container container = new DefaultContainer();
        AtomicInteger counter = new AtomicInteger(0);
        container.registerSupplier(ServiceInterface.class, () -> {
            ServiceImpl service = new ServiceImpl();
            service.setId(counter.incrementAndGet());
            return service;
        }, Scope.PROTOTYPE);
        
        ServiceInterface service1 = container.get(ServiceInterface.class);
        ServiceInterface service2 = container.get(ServiceInterface.class);
        
        Assertions.assertEquals(1, ((ServiceImpl) service1).getId());
        Assertions.assertEquals(2, ((ServiceImpl) service2).getId());
    }

    @Test
    public void testGetIfPresent() {
        Container container = new DefaultContainer();
        container.register(ServiceInterface.class, ServiceImpl.class);
        
        Optional<ServiceInterface> present = container.getIfPresent(ServiceInterface.class);
        Optional<?> notPresent = container.getIfPresent(OtherServiceInterface.class);
        
        Assertions.assertTrue(present.isPresent());
        Assertions.assertFalse(notPresent.isPresent());
    }

    @Test
    public void testContains() {
        Container container = new DefaultContainer();
        container.register(ServiceInterface.class, ServiceImpl.class);
        
        Assertions.assertTrue(container.contains(ServiceInterface.class));
        Assertions.assertFalse(container.contains(OtherServiceInterface.class));
    }

    @Test
    public void testContainsWithQualifier() {
        Container container = new DefaultContainer();
        container.register(ServiceInterface.class, ServiceImpl.class, "test");
        
        Assertions.assertTrue(container.contains(ServiceInterface.class, "test"));
        Assertions.assertFalse(container.contains(ServiceInterface.class, "other"));
    }

    @Test
    public void testCreate() {
        Container container = new DefaultContainer();
        ServiceImpl service = container.create(ServiceImpl.class);
        Assertions.assertNotNull(service);
    }

    @Test
    public void testClear() {
        Container container = new DefaultContainer();
        container.register(ServiceInterface.class, ServiceImpl.class);
        Assertions.assertTrue(container.contains(ServiceInterface.class));
        
        container.clear();
        Assertions.assertFalse(container.contains(ServiceInterface.class));
    }

    @Test
    public void testFieldInjection() {
        Container container = new DefaultContainer();
        container.register(ServiceInterface.class, ServiceImpl.class);
        container.register(ClientWithFieldInjection.class, ClientWithFieldInjection.class);
        
        ClientWithFieldInjection client = container.get(ClientWithFieldInjection.class);
        Assertions.assertNotNull(client.getService());
    }

    @Test
    public void testConstructorInjection() {
        Container container = new DefaultContainer();
        container.register(ServiceInterface.class, ServiceImpl.class);
        container.register(ClientWithConstructorInjection.class, ClientWithConstructorInjection.class);
        
        ClientWithConstructorInjection client = container.get(ClientWithConstructorInjection.class);
        Assertions.assertNotNull(client.getService());
    }

    @Test
    public void testMethodInjection() {
        Container container = new DefaultContainer();
        container.register(ServiceInterface.class, ServiceImpl.class);
        container.register(ClientWithMethodInjection.class, ClientWithMethodInjection.class);
        
        ClientWithMethodInjection client = container.get(ClientWithMethodInjection.class);
        Assertions.assertNotNull(client.getService());
    }

    @Test
    public void testQualifierInjection() {
        Container container = new DefaultContainer();
        container.register(ServiceInterface.class, ServiceImpl.class, "impl1");
        container.register(ServiceInterface.class, ServiceImpl2.class, "impl2");
        container.register(ClientWithQualifier.class, ClientWithQualifier.class);
        
        ClientWithQualifier client = container.get(ClientWithQualifier.class);
        Assertions.assertTrue(client.getService() instanceof ServiceImpl2);
    }

    @Test
    public void testPostConstruct() {
        Container container = new DefaultContainer();
        container.register(LifecycleBean.class, LifecycleBean.class);
        
        LifecycleBean bean = container.get(LifecycleBean.class);
        Assertions.assertTrue(bean.isPostConstructCalled());
    }

    @Test
    public void testInitializingBean() {
        Container container = new DefaultContainer();
        container.register(LifecycleBean.class, LifecycleBean.class);
        
        LifecycleBean bean = container.get(LifecycleBean.class);
        Assertions.assertTrue(bean.isAfterPropertiesSetCalled());
    }

    @Test
    public void testPreDestroyAndDisposableBean() {
        DefaultContainer container = new DefaultContainer();
        container.register(LifecycleBean.class, LifecycleBean.class);
        
        LifecycleBean bean = container.get(LifecycleBean.class);
        container.close();
        
        Assertions.assertTrue(bean.isPreDestroyCalled());
        Assertions.assertTrue(bean.isDestroyCalled());
    }

    @Test
    public void testBeanPostProcessor() {
        DefaultContainer container = new DefaultContainer();
        TestBeanPostProcessor processor = new TestBeanPostProcessor();
        container.addBeanPostProcessor(processor);
        container.register(ServiceInterface.class, ServiceImpl.class);
        
        container.get(ServiceInterface.class);
        Assertions.assertTrue(processor.isBeforeCalled());
        Assertions.assertTrue(processor.isAfterCalled());
    }

    @Test
    public void testGetNonExistent() {
        Container container = new DefaultContainer();
        Assertions.assertThrows(IllegalStateException.class, () -> {
            container.get(ServiceInterface.class);
        });
    }

    @Test
    public void testRegisterNullType() {
        Container container = new DefaultContainer();
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            container.register(null, ServiceImpl.class);
        });
    }

    @Test
    public void testRegisterNullImplementation() {
        Container container = new DefaultContainer();
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            container.register(ServiceInterface.class, null);
        });
    }

    @Test
    public void testRegisterSingletonNullType() {
        Container container = new DefaultContainer();
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            container.registerSingleton(null, new ServiceImpl());
        });
    }

    @Test
    public void testRegisterSingletonNullInstance() {
        Container container = new DefaultContainer();
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            container.registerSingleton(ServiceInterface.class, null);
        });
    }

    @Test
    public void testRegisterSupplierNullType() {
        Container container = new DefaultContainer();
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            container.registerSupplier(null, () -> new ServiceImpl());
        });
    }

    @Test
    public void testRegisterSupplierNullSupplier() {
        Container container = new DefaultContainer();
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            container.registerSupplier(ServiceInterface.class, null);
        });
    }

    @Test
    public void testGetNullType() {
        Container container = new DefaultContainer();
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            container.get(null);
        });
    }

    interface ServiceInterface {
    }

    static class ServiceImpl implements ServiceInterface {
        private int id;
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
    }

    static class ServiceImpl2 implements ServiceInterface {
    }

    interface OtherServiceInterface {
    }

    static class ClientWithFieldInjection {
        @Inject
        private ServiceInterface service;
        public ServiceInterface getService() { return service; }
    }

    static class ClientWithConstructorInjection {
        private final ServiceInterface service;
        @Inject
        public ClientWithConstructorInjection(ServiceInterface service) {
            this.service = service;
        }
        public ServiceInterface getService() { return service; }
    }

    static class ClientWithMethodInjection {
        private ServiceInterface service;
        @Inject
        public void setService(ServiceInterface service) {
            this.service = service;
        }
        public ServiceInterface getService() { return service; }
    }

    static class ClientWithQualifier {
        @Inject
        @Qualifier("impl2")
        private ServiceInterface service;
        public ServiceInterface getService() { return service; }
    }

    static class LifecycleBean implements InitializingBean, DisposableBean {
        private boolean postConstructCalled = false;
        private boolean afterPropertiesSetCalled = false;
        private boolean preDestroyCalled = false;
        private boolean destroyCalled = false;

        @PostConstruct
        public void postConstruct() {
            postConstructCalled = true;
        }

        @Override
        public void afterPropertiesSet() {
            afterPropertiesSetCalled = true;
        }

        @PreDestroy
        public void preDestroy() {
            preDestroyCalled = true;
        }

        @Override
        public void destroy() {
            destroyCalled = true;
        }

        public boolean isPostConstructCalled() { return postConstructCalled; }
        public boolean isAfterPropertiesSetCalled() { return afterPropertiesSetCalled; }
        public boolean isPreDestroyCalled() { return preDestroyCalled; }
        public boolean isDestroyCalled() { return destroyCalled; }
    }

    static class TestBeanPostProcessor implements BeanPostProcessor {
        private boolean beforeCalled = false;
        private boolean afterCalled = false;

        @Override
        public Object postProcessBeforeInitialization(Object bean, String beanName) {
            beforeCalled = true;
            return bean;
        }

        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) {
            afterCalled = true;
            return bean;
        }

        public boolean isBeforeCalled() { return beforeCalled; }
        public boolean isAfterCalled() { return afterCalled; }
    }
}
