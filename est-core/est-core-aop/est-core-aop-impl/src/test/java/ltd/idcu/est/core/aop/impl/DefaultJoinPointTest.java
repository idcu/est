package ltd.idcu.est.core.aop.impl;

import ltd.idcu.est.test.Assertions;
import ltd.idcu.est.test.annotation.Test;

import java.lang.reflect.Method;

public class DefaultJoinPointTest {

    static class TestTarget {
        public String testMethod(String arg1, int arg2) {
            return arg1 + arg2;
        }
    }

    @Test
    public void testJoinPointCreation() throws Exception {
        TestTarget target = new TestTarget();
        Method method = TestTarget.class.getMethod("testMethod", String.class, int.class);
        Object[] args = new Object[]{"test", 123};
        
        DefaultJoinPoint joinPoint = new DefaultJoinPoint(target, method, args);
        
        Assertions.assertNotNull(joinPoint);
    }

    @Test
    public void testGetTarget() throws Exception {
        TestTarget target = new TestTarget();
        Method method = TestTarget.class.getMethod("testMethod", String.class, int.class);
        Object[] args = new Object[]{"test", 123};
        
        DefaultJoinPoint joinPoint = new DefaultJoinPoint(target, method, args);
        
        Assertions.assertEquals(target, joinPoint.getTarget());
    }

    @Test
    public void testGetArgs() throws Exception {
        TestTarget target = new TestTarget();
        Method method = TestTarget.class.getMethod("testMethod", String.class, int.class);
        Object[] args = new Object[]{"test", 123};
        
        DefaultJoinPoint joinPoint = new DefaultJoinPoint(target, method, args);
        
        Object[] retrievedArgs = joinPoint.getArgs();
        Assertions.assertArrayEquals(args, retrievedArgs);
    }

    @Test
    public void testGetArgsWithNull() throws Exception {
        TestTarget target = new TestTarget();
        Method method = TestTarget.class.getMethod("testMethod", String.class, int.class);
        
        DefaultJoinPoint joinPoint = new DefaultJoinPoint(target, method, null);
        
        Object[] retrievedArgs = joinPoint.getArgs();
        Assertions.assertNull(retrievedArgs);
    }

    @Test
    public void testGetSignature() throws Exception {
        TestTarget target = new TestTarget();
        Method method = TestTarget.class.getMethod("testMethod", String.class, int.class);
        Object[] args = new Object[]{"test", 123};
        
        DefaultJoinPoint joinPoint = new DefaultJoinPoint(target, method, args);
        
        String signature = joinPoint.getSignature();
        Assertions.assertTrue(signature.contains("TestTarget"));
        Assertions.assertTrue(signature.contains("testMethod"));
    }

    @Test
    public void testGetSignatureFormat() throws Exception {
        TestTarget target = new TestTarget();
        Method method = TestTarget.class.getMethod("testMethod", String.class, int.class);
        Object[] args = new Object[]{"test", 123};
        
        DefaultJoinPoint joinPoint = new DefaultJoinPoint(target, method, args);
        
        String signature = joinPoint.getSignature();
        String expected = TestTarget.class.getName() + "#testMethod";
        Assertions.assertEquals(expected, signature);
    }
}
