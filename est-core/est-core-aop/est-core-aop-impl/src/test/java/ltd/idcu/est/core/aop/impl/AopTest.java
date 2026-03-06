package ltd.idcu.est.core.aop.impl;

import ltd.idcu.est.core.aop.api.JoinPoint;
import ltd.idcu.est.core.aop.api.ProceedingJoinPoint;
import ltd.idcu.est.core.aop.api.annotation.After;
import ltd.idcu.est.core.aop.api.annotation.AfterReturning;
import ltd.idcu.est.core.aop.api.annotation.AfterThrowing;
import ltd.idcu.est.core.aop.api.annotation.Around;
import ltd.idcu.est.core.aop.api.annotation.Aspect;
import ltd.idcu.est.core.aop.api.annotation.Before;
import ltd.idcu.est.test.Assertions;
import ltd.idcu.est.test.annotation.Test;

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
        Assertions.assertEquals("Hello, World", result);

        Assertions.assertEquals(1, aspect.beforeCount);
        Assertions.assertEquals(1, aspect.afterCount);
        Assertions.assertEquals(1, aspect.aroundCount);
        Assertions.assertNotNull(aspect.lastMethodName);
        Assertions.assertTrue(aspect.lastMethodName.contains("greet"));
    }
}
