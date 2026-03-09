package ltd.idcu.est.patterns.impl.aop;

import ltd.idcu.est.patterns.api.aop.*;
import ltd.idcu.est.test.Assertions;
import ltd.idcu.est.test.annotation.Test;

import java.util.ArrayList;
import java.util.List;

public class SimpleAopTests {

    public interface SimpleService {
        int add(int a, int b);
        String getName();
    }

    public static class SimpleServiceImpl implements SimpleService {
        @Override
        public int add(int a, int b) {
            return a + b;
        }

        @Override
        public String getName() {
            return "simple";
        }
    }

    @Test
    public void testProxyWithoutAdvice() {
        SimpleService target = new SimpleServiceImpl();
        SimpleService proxy = AopProxyFactory.createProxy(target, List.of());
        
        Assertions.assertNotNull(proxy);
        Assertions.assertEquals(5, proxy.add(2, 3));
        Assertions.assertEquals("simple", proxy.getName());
    }

    @Test
    public void testNameMatchPointcutWithRealClass() throws Exception {
        SimpleServiceImpl target = new SimpleServiceImpl();
        String className = SimpleServiceImpl.class.getName();
        
        NameMatchPointcut pointcut = new NameMatchPointcut(className + ".*");
        
        Assertions.assertNotNull(pointcut.getExpression());
        Assertions.assertEquals(className + ".*", pointcut.getExpression());
    }

    @Test
    public void testDefaultAdvisor() {
        Pointcut pointcut = NameMatchPointcut.forClass(SimpleServiceImpl.class);
        BeforeAdvice advice = new BeforeAdvice() {
            @Override
            public void before(JoinPoint joinPoint) {
            }

            @Override
            public int getOrder() {
                return 0;
            }
        };
        
        DefaultAdvisor advisor = new DefaultAdvisor(pointcut, advice, 5);
        
        Assertions.assertEquals(pointcut, advisor.getPointcut());
        Assertions.assertEquals(advice, advisor.getAdvice());
        Assertions.assertEquals(5, advisor.getOrder());
    }

    @Test
    public void testDefaultJoinPoint() throws Exception {
        SimpleServiceImpl target = new SimpleServiceImpl();
        java.lang.reflect.Method method = SimpleServiceImpl.class.getMethod("add", int.class, int.class);
        Object[] args = new Object[]{1, 2};
        
        DefaultJoinPoint joinPoint = new DefaultJoinPoint(
            target, 
            method, 
            args, 
            proceedArgs -> 3
        );
        
        Assertions.assertEquals(target, joinPoint.getTarget());
        Assertions.assertEquals(method, joinPoint.getMethod());
        Assertions.assertArrayEquals(args, joinPoint.getArgs());
        Assertions.assertNotNull(joinPoint.getSignature());
    }
}
