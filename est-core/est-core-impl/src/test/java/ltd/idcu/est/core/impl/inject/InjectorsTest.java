package ltd.idcu.est.core.impl.inject;

import ltd.idcu.est.core.api.Container;
import ltd.idcu.est.core.api.annotation.Inject;
import ltd.idcu.est.core.api.annotation.Qualifier;
import ltd.idcu.est.core.impl.DefaultContainer;
import ltd.idcu.est.test.Assertions;
import ltd.idcu.est.test.annotation.Test;

public class InjectorsTest {

    @Test
    public void testFieldInjector() {
        Container container = new DefaultContainer();
        container.register(ServiceInterface.class, ServiceImpl.class);
        
        ClientWithFieldInjection client = new ClientWithFieldInjection();
        FieldInjector fieldInjector = new FieldInjector(container);
        fieldInjector.injectFields(client);
        
        Assertions.assertNotNull(client.getService());
        Assertions.assertTrue(client.getService() instanceof ServiceImpl);
    }

    @Test
    public void testFieldInjectorWithQualifier() {
        Container container = new DefaultContainer();
        container.register(ServiceInterface.class, ServiceImpl.class, "impl1");
        container.register(ServiceInterface.class, ServiceImpl2.class, "impl2");
        
        ClientWithFieldQualifier client = new ClientWithFieldQualifier();
        FieldInjector fieldInjector = new FieldInjector(container);
        fieldInjector.injectFields(client);
        
        Assertions.assertNotNull(client.getService());
        Assertions.assertTrue(client.getService() instanceof ServiceImpl2);
    }

    @Test
    public void testFieldInjectorInheritedFields() {
        Container container = new DefaultContainer();
        container.register(ServiceInterface.class, ServiceImpl.class);
        
        ChildClient client = new ChildClient();
        FieldInjector fieldInjector = new FieldInjector(container);
        fieldInjector.injectFields(client);
        
        Assertions.assertNotNull(client.getParentService());
        Assertions.assertNotNull(client.getChildService());
    }

    @Test
    public void testConstructorInjector() {
        Container container = new DefaultContainer();
        container.register(ServiceInterface.class, ServiceImpl.class);
        
        ConstructorInjector constructorInjector = new ConstructorInjector(container);
        ClientWithConstructorInjection client = constructorInjector.createInstance(ClientWithConstructorInjection.class);
        
        Assertions.assertNotNull(client);
        Assertions.assertNotNull(client.getService());
        Assertions.assertTrue(client.getService() instanceof ServiceImpl);
    }

    @Test
    public void testConstructorInjectorWithQualifier() {
        Container container = new DefaultContainer();
        container.register(ServiceInterface.class, ServiceImpl.class, "impl1");
        container.register(ServiceInterface.class, ServiceImpl2.class, "impl2");
        
        ConstructorInjector constructorInjector = new ConstructorInjector(container);
        ClientWithConstructorQualifier client = constructorInjector.createInstance(ClientWithConstructorQualifier.class);
        
        Assertions.assertNotNull(client);
        Assertions.assertTrue(client.getService() instanceof ServiceImpl2);
    }

    @Test
    public void testConstructorInjectorNoArgsConstructor() {
        Container container = new DefaultContainer();
        ConstructorInjector constructorInjector = new ConstructorInjector(container);
        SimpleClient client = constructorInjector.createInstance(SimpleClient.class);
        
        Assertions.assertNotNull(client);
    }

    @Test
    public void testConstructorInjectorMultipleConstructors() {
        Container container = new DefaultContainer();
        container.register(ServiceInterface.class, ServiceImpl.class);
        
        ConstructorInjector constructorInjector = new ConstructorInjector(container);
        ClientWithMultipleConstructors client = constructorInjector.createInstance(ClientWithMultipleConstructors.class);
        
        Assertions.assertNotNull(client);
        Assertions.assertNotNull(client.getService());
        Assertions.assertTrue(client.isInjectConstructorUsed());
    }

    @Test
    public void testConstructorInjectorMultipleInjectConstructors() {
        Container container = new DefaultContainer();
        ConstructorInjector constructorInjector = new ConstructorInjector(container);
        
        Assertions.assertThrows(RuntimeException.class, () -> {
            constructorInjector.createInstance(ClientWithMultipleInjectConstructors.class);
        });
    }

    @Test
    public void testMethodInjector() {
        Container container = new DefaultContainer();
        container.register(ServiceInterface.class, ServiceImpl.class);
        
        ClientWithMethodInjection client = new ClientWithMethodInjection();
        MethodInjector methodInjector = new MethodInjector(container);
        methodInjector.injectMethods(client);
        
        Assertions.assertNotNull(client.getService());
        Assertions.assertTrue(client.getService() instanceof ServiceImpl);
    }

    @Test
    public void testMethodInjectorWithQualifier() {
        Container container = new DefaultContainer();
        container.register(ServiceInterface.class, ServiceImpl.class, "impl1");
        container.register(ServiceInterface.class, ServiceImpl2.class, "impl2");
        
        ClientWithMethodQualifier client = new ClientWithMethodQualifier();
        MethodInjector methodInjector = new MethodInjector(container);
        methodInjector.injectMethods(client);
        
        Assertions.assertNotNull(client.getService());
        Assertions.assertTrue(client.getService() instanceof ServiceImpl2);
    }

    @Test
    public void testMethodInjectorMultipleMethods() {
        Container container = new DefaultContainer();
        container.register(ServiceInterface.class, ServiceImpl.class);
        container.register(OtherServiceInterface.class, OtherServiceImpl.class);
        
        ClientWithMultipleMethods client = new ClientWithMultipleMethods();
        MethodInjector methodInjector = new MethodInjector(container);
        methodInjector.injectMethods(client);
        
        Assertions.assertNotNull(client.getService());
        Assertions.assertNotNull(client.getOtherService());
    }

    @Test
    public void testMethodInjectorInheritedMethods() {
        Container container = new DefaultContainer();
        container.register(ServiceInterface.class, ServiceImpl.class);
        container.register(OtherServiceInterface.class, OtherServiceImpl.class);
        
        ChildMethodClient client = new ChildMethodClient();
        MethodInjector methodInjector = new MethodInjector(container);
        methodInjector.injectMethods(client);
        
        Assertions.assertNotNull(client.getParentService());
        Assertions.assertNotNull(client.getChildOtherService());
    }

    interface ServiceInterface {
    }

    static class ServiceImpl implements ServiceInterface {
    }

    static class ServiceImpl2 implements ServiceInterface {
    }

    interface OtherServiceInterface {
    }

    static class OtherServiceImpl implements OtherServiceInterface {
    }

    static class ClientWithFieldInjection {
        @Inject
        private ServiceInterface service;
        public ServiceInterface getService() { return service; }
    }

    static class ClientWithFieldQualifier {
        @Inject
        @Qualifier("impl2")
        private ServiceInterface service;
        public ServiceInterface getService() { return service; }
    }

    static class ParentClient {
        @Inject
        private ServiceInterface parentService;
        public ServiceInterface getParentService() { return parentService; }
    }

    static class ChildClient extends ParentClient {
        @Inject
        private ServiceInterface childService;
        public ServiceInterface getChildService() { return childService; }
    }

    static class ClientWithConstructorInjection {
        private final ServiceInterface service;
        @Inject
        public ClientWithConstructorInjection(ServiceInterface service) {
            this.service = service;
        }
        public ServiceInterface getService() { return service; }
    }

    static class ClientWithConstructorQualifier {
        private final ServiceInterface service;
        @Inject
        public ClientWithConstructorQualifier(@Qualifier("impl2") ServiceInterface service) {
            this.service = service;
        }
        public ServiceInterface getService() { return service; }
    }

    static class SimpleClient {
    }

    static class ClientWithMultipleConstructors {
        private final ServiceInterface service;
        private boolean injectConstructorUsed = false;

        public ClientWithMultipleConstructors() {
            this.service = null;
        }

        @Inject
        public ClientWithMultipleConstructors(ServiceInterface service) {
            this.service = service;
            this.injectConstructorUsed = true;
        }

        public ServiceInterface getService() { return service; }
        public boolean isInjectConstructorUsed() { return injectConstructorUsed; }
    }

    static class ClientWithMultipleInjectConstructors {
        @Inject
        public ClientWithMultipleInjectConstructors() {
        }

        @Inject
        public ClientWithMultipleInjectConstructors(String dummy) {
        }
    }

    static class ClientWithMethodInjection {
        private ServiceInterface service;
        @Inject
        public void setService(ServiceInterface service) {
            this.service = service;
        }
        public ServiceInterface getService() { return service; }
    }

    static class ClientWithMethodQualifier {
        private ServiceInterface service;
        @Inject
        public void setService(@Qualifier("impl2") ServiceInterface service) {
            this.service = service;
        }
        public ServiceInterface getService() { return service; }
    }

    static class ClientWithMultipleMethods {
        private ServiceInterface service;
        private OtherServiceInterface otherService;
        
        @Inject
        public void setService(ServiceInterface service) {
            this.service = service;
        }
        
        @Inject
        public void setOtherService(OtherServiceInterface otherService) {
            this.otherService = otherService;
        }
        
        public ServiceInterface getService() { return service; }
        public OtherServiceInterface getOtherService() { return otherService; }
    }

    static class ParentMethodClient {
        protected ServiceInterface parentService;
        @Inject
        public void setParentService(ServiceInterface service) {
            this.parentService = service;
        }
        public ServiceInterface getParentService() { return parentService; }
    }

    static class ChildMethodClient extends ParentMethodClient {
        private OtherServiceInterface childOtherService;
        @Inject
        public void setChildOtherService(OtherServiceInterface otherService) {
            this.childOtherService = otherService;
        }
        public OtherServiceInterface getChildOtherService() { return childOtherService; }
    }
}
