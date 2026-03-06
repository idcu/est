package ltd.idcu.est.core.impl;

import ltd.idcu.est.core.api.annotation.*;
import ltd.idcu.est.core.api.aop.JoinPoint;
import ltd.idcu.est.core.api.aop.ProceedingJoinPoint;
import ltd.idcu.est.core.impl.aop.AopProxyFactory;
import ltd.idcu.est.core.impl.aop.AspectRegistry;
import ltd.idcu.est.test.annotation.Test;

import static ltd.idcu.est.test.Assert.*;

public class AopTest {

    public interface GreetingService {
        String greet(String name);
    }

    public static class GreetingServiceImpl implements GreetingService {
        @Override
        public String greet(String name) {
            return "Hello, " + name;
        }
    }

    @Aspect
    public static class LoggingAspect {
        public int beforeCount = 0;
        public int afterCount = 0;
        public int aroundCount = 0;
        public String lastMethodName = null;

        @Before("*")
        public void before(JoinPoint joinPoint) {
            beforeCount++;
            lastMethodName = joinPoint.getSignature();
        }

        @After("*")
        public void after(JoinPoint joinPoint) {
            afterCount++;
        }

        @Around("*")
        public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
            aroundCount++;
            return joinPoint.proceed();
        }
    }

    @Test
    public void testAopProxy() {
        AspectRegistry aspectRegistry = new AspectRegistry();
        LoggingAspect aspect = new LoggingAspect();
        aspectRegistry.registerAspect(aspect);

        AopProxyFactory proxyFactory = new AopProxyFactory(aspectRegistry);
        GreetingService target = new GreetingServiceImpl();
        GreetingService proxy = proxyFactory.createProxy(target, GreetingService.class);

        String result = proxy.greet("World");
        assertEquals("Hello, World", result);

        assertEquals(1, aspect.beforeCount);
        assertEquals(1, aspect.afterCount);
        assertEquals(1, aspect.aroundCount);
        assertNotNull(aspect.lastMethodName);
        assertTrue(aspect.lastMethodName.contains("greet"));
    }
}
