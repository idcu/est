package ltd.idcu.est.patterns.impl.aop;

import ltd.idcu.est.patterns.api.aop.*;
import ltd.idcu.est.test.Assertions;
import ltd.idcu.est.test.annotation.Test;

public class DefaultAdvisorTest {

    @Test
    public void testConstructorWithOrder() {
        Pointcut pointcut = NameMatchPointcut.forClass(TestService.class);
        BeforeAdvice advice = new BeforeAdvice() {
            @Override
            public void before(JoinPoint joinPoint) {
            }

            @Override
            public int getOrder() {
                return 0;
            }
        };
        int order = 10;
        
        DefaultAdvisor advisor = new DefaultAdvisor(pointcut, advice, order);
        
        Assertions.assertEquals(pointcut, advisor.getPointcut());
        Assertions.assertEquals(advice, advisor.getAdvice());
        Assertions.assertEquals(order, advisor.getOrder());
    }

    @Test
    public void testConstructorWithoutOrder() {
        Pointcut pointcut = NameMatchPointcut.forClass(TestService.class);
        BeforeAdvice advice = new BeforeAdvice() {
            @Override
            public void before(JoinPoint joinPoint) {
            }

            @Override
            public int getOrder() {
                return 0;
            }
        };
        
        DefaultAdvisor advisor = new DefaultAdvisor(pointcut, advice);
        
        Assertions.assertEquals(pointcut, advisor.getPointcut());
        Assertions.assertEquals(advice, advisor.getAdvice());
        Assertions.assertEquals(0, advisor.getOrder());
    }

    @Test
    public void testMultipleAdvisors() {
        Pointcut pointcut1 = NameMatchPointcut.forClass(TestService.class);
        BeforeAdvice advice1 = new BeforeAdvice() {
            @Override
            public void before(JoinPoint joinPoint) {
            }

            @Override
            public int getOrder() {
                return 0;
            }
        };
        DefaultAdvisor advisor1 = new DefaultAdvisor(pointcut1, advice1, 1);
        
        Pointcut pointcut2 = NameMatchPointcut.forClass(TestService.class);
        BeforeAdvice advice2 = new BeforeAdvice() {
            @Override
            public void before(JoinPoint joinPoint) {
            }

            @Override
            public int getOrder() {
                return 0;
            }
        };
        DefaultAdvisor advisor2 = new DefaultAdvisor(pointcut2, advice2, 2);
        
        Assertions.assertNotSame(advisor1, advisor2);
        Assertions.assertEquals(1, advisor1.getOrder());
        Assertions.assertEquals(2, advisor2.getOrder());
    }

    @Test
    public void testWithRealAdvice() {
        Pointcut pointcut = NameMatchPointcut.forClass(TestService.class);
        
        BeforeAdvice advice = new BeforeAdvice() {
            @Override
            public void before(JoinPoint joinPoint) {
            }

            @Override
            public int getOrder() {
                return 0;
            }
        };
        
        DefaultAdvisor advisor = new DefaultAdvisor(pointcut, advice);
        
        Assertions.assertNotNull(advisor.getPointcut());
        Assertions.assertNotNull(advisor.getAdvice());
    }

    interface TestService {
        void test();
    }
}
