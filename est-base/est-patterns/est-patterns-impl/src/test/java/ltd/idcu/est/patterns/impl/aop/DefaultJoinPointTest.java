package ltd.idcu.est.patterns.impl.aop;

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
    public void testGetTarget() throws Exception {
        TestTarget target = new TestTarget();
        Method method = TestTarget.class.getMethod("testMethod", String.class, int.class);
        Object[] args = new Object[]{"test", 123};
        DefaultJoinPoint.ProceedCallback callback = proceedArgs -> "result";
        
        DefaultJoinPoint joinPoint = new DefaultJoinPoint(target, method, args, callback);
        
        Assertions.assertEquals(target, joinPoint.getTarget());
    }

    @Test
    public void testGetMethod() throws Exception {
        TestTarget target = new TestTarget();
        Method method = TestTarget.class.getMethod("testMethod", String.class, int.class);
        Object[] args = new Object[]{"test", 123};
        DefaultJoinPoint.ProceedCallback callback = proceedArgs -> "result";
        
        DefaultJoinPoint joinPoint = new DefaultJoinPoint(target, method, args, callback);
        
        Assertions.assertEquals(method, joinPoint.getMethod());
    }

    @Test
    public void testGetArgs() throws Exception {
        TestTarget target = new TestTarget();
        Method method = TestTarget.class.getMethod("testMethod", String.class, int.class);
        Object[] args = new Object[]{"test", 123};
        DefaultJoinPoint.ProceedCallback callback = proceedArgs -> "result";
        
        DefaultJoinPoint joinPoint = new DefaultJoinPoint(target, method, args, callback);
        
        Object[] retrievedArgs = joinPoint.getArgs();
        Assertions.assertArrayEquals(args, retrievedArgs);
        Assertions.assertNotSame(args, retrievedArgs);
    }

    @Test
    public void testGetArgsWithNull() throws Exception {
        TestTarget target = new TestTarget();
        Method method = TestTarget.class.getMethod("testMethod", String.class, int.class);
        DefaultJoinPoint.ProceedCallback callback = proceedArgs -> "result";
        
        DefaultJoinPoint joinPoint = new DefaultJoinPoint(target, method, null, callback);
        
        Object[] retrievedArgs = joinPoint.getArgs();
        Assertions.assertNotNull(retrievedArgs);
        Assertions.assertEquals(0, retrievedArgs.length);
    }

    @Test
    public void testProceed() throws Throwable {
        TestTarget target = new TestTarget();
        Method method = TestTarget.class.getMethod("testMethod", String.class, int.class);
        Object[] args = new Object[]{"test", 123};
        String expectedResult = "proceeded";
        DefaultJoinPoint.ProceedCallback callback = proceedArgs -> expectedResult;
        
        DefaultJoinPoint joinPoint = new DefaultJoinPoint(target, method, args, callback);
        
        Object result = joinPoint.proceed();
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void testProceedWithArgs() throws Throwable {
        TestTarget target = new TestTarget();
        Method method = TestTarget.class.getMethod("testMethod", String.class, int.class);
        Object[] args = new Object[]{"test", 123};
        Object[] newArgs = new Object[]{"newTest", 456};
        DefaultJoinPoint.ProceedCallback callback = proceedArgs -> {
            Assertions.assertArrayEquals(newArgs, proceedArgs);
            return "proceeded";
        };
        
        DefaultJoinPoint joinPoint = new DefaultJoinPoint(target, method, args, callback);
        
        Object result = joinPoint.proceed(newArgs);
        Assertions.assertEquals("proceeded", result);
    }

    @Test
    public void testGetSignature() throws Exception {
        TestTarget target = new TestTarget();
        Method method = TestTarget.class.getMethod("testMethod", String.class, int.class);
        Object[] args = new Object[]{"test", 123};
        DefaultJoinPoint.ProceedCallback callback = proceedArgs -> "result";
        
        DefaultJoinPoint joinPoint = new DefaultJoinPoint(target, method, args, callback);
        
        String signature = joinPoint.getSignature();
        Assertions.assertTrue(signature.contains("TestTarget.testMethod"));
    }

    @Test
    public void testGetTargetClass() throws Exception {
        TestTarget target = new TestTarget();
        Method method = TestTarget.class.getMethod("testMethod", String.class, int.class);
        Object[] args = new Object[]{"test", 123};
        DefaultJoinPoint.ProceedCallback callback = proceedArgs -> "result";
        
        DefaultJoinPoint joinPoint = new DefaultJoinPoint(target, method, args, callback);
        
        Assertions.assertEquals(TestTarget.class, joinPoint.getTargetClass());
    }

    @Test
    public void testGetTargetClassWithNullTarget() throws Exception {
        Method method = TestTarget.class.getMethod("testMethod", String.class, int.class);
        Object[] args = new Object[]{"test", 123};
        DefaultJoinPoint.ProceedCallback callback = proceedArgs -> "result";
        
        DefaultJoinPoint joinPoint = new DefaultJoinPoint(null, method, args, callback);
        
        Assertions.assertEquals(TestTarget.class, joinPoint.getTargetClass());
    }
}
