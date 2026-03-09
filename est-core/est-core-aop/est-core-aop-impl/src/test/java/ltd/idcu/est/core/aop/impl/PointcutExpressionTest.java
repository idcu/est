package ltd.idcu.est.core.aop.impl;

import ltd.idcu.est.test.Assertions;
import ltd.idcu.est.test.annotation.Test;

import java.lang.reflect.Method;

public class PointcutExpressionTest {

    static class TestClass {
        public void testMethod() {
        }
        
        public void anotherMethod() {
        }
    }

    static class AnotherClass {
        public void testMethod() {
        }
    }

    @Test
    public void testExactMatch() throws Exception {
        PointcutExpression expr = new PointcutExpression(
            PointcutExpressionTest.class.getName() + ".TestClass.testMethod");
        
        Method method = TestClass.class.getMethod("testMethod");
        
        Assertions.assertTrue(expr.matches(TestClass.class, method));
    }

    @Test
    public void testWildcardClass() throws Exception {
        PointcutExpression expr = new PointcutExpression("*.testMethod");
        
        Method method1 = TestClass.class.getMethod("testMethod");
        Method method2 = AnotherClass.class.getMethod("testMethod");
        
        Assertions.assertTrue(expr.matches(TestClass.class, method1));
        Assertions.assertTrue(expr.matches(AnotherClass.class, method2));
    }

    @Test
    public void testWildcardMethod() throws Exception {
        PointcutExpression expr = new PointcutExpression(
            PointcutExpressionTest.class.getName() + ".TestClass.*");
        
        Method method1 = TestClass.class.getMethod("testMethod");
        Method method2 = TestClass.class.getMethod("anotherMethod");
        
        Assertions.assertTrue(expr.matches(TestClass.class, method1));
        Assertions.assertTrue(expr.matches(TestClass.class, method2));
    }

    @Test
    public void testWildcardBoth() throws Exception {
        PointcutExpression expr = new PointcutExpression("*.*");
        
        Method method1 = TestClass.class.getMethod("testMethod");
        Method method2 = AnotherClass.class.getMethod("testMethod");
        
        Assertions.assertTrue(expr.matches(TestClass.class, method1));
        Assertions.assertTrue(expr.matches(AnotherClass.class, method2));
    }

    @Test
    public void testNoMatch() throws Exception {
        PointcutExpression expr = new PointcutExpression(
            "com.other.OtherClass.otherMethod");
        
        Method method = TestClass.class.getMethod("testMethod");
        
        Assertions.assertFalse(expr.matches(TestClass.class, method));
    }

    @Test
    public void testClassNoMatch() throws Exception {
        PointcutExpression expr = new PointcutExpression(
            "com.other.OtherClass.testMethod");
        
        Method method = TestClass.class.getMethod("testMethod");
        
        Assertions.assertFalse(expr.matches(TestClass.class, method));
    }

    @Test
    public void testMethodNoMatch() throws Exception {
        PointcutExpression expr = new PointcutExpression(
            PointcutExpressionTest.class.getName() + ".TestClass.otherMethod");
        
        Method method = TestClass.class.getMethod("testMethod");
        
        Assertions.assertFalse(expr.matches(TestClass.class, method));
    }
}
