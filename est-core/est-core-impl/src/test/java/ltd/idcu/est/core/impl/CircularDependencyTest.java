package ltd.idcu.est.core.impl;

import ltd.idcu.est.core.api.Container;
import ltd.idcu.est.core.api.annotation.Component;
import ltd.idcu.est.core.api.annotation.Inject;
import ltd.idcu.est.test.annotation.Test;

import static ltd.idcu.est.test.Assert.*;

public class CircularDependencyTest {

    @Component
    public static class ServiceA {
        private final ServiceB serviceB;

        @Inject
        public ServiceA(ServiceB serviceB) {
            this.serviceB = serviceB;
        }

        public String getName() {
            return "ServiceA";
        }

        public ServiceB getServiceB() {
            return serviceB;
        }
    }

    @Component
    public static class ServiceB {
        private ServiceA serviceA;

        @Inject
        public void setServiceA(ServiceA serviceA) {
            this.serviceA = serviceA;
        }

        public String getName() {
            return "ServiceB";
        }

        public ServiceA getServiceA() {
            return serviceA;
        }
    }

    @Test
    public void testCircularDependency() {
        Container container = new DefaultContainer();
        container.register(ServiceA.class, ServiceA.class);
        container.register(ServiceB.class, ServiceB.class);

        ServiceA serviceA = container.get(ServiceA.class);
        assertNotNull(serviceA);
        assertEquals("ServiceA", serviceA.getName());
        assertNotNull(serviceA.getServiceB());
        assertEquals("ServiceB", serviceA.getServiceB().getName());
        assertNotNull(serviceA.getServiceB().getServiceA());
        assertSame(serviceA, serviceA.getServiceB().getServiceA());
    }
}
