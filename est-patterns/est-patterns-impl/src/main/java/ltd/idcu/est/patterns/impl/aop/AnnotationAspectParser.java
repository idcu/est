package ltd.idcu.est.patterns.impl.aop;

import ltd.idcu.est.core.api.annotation.aop.*;
import ltd.idcu.est.patterns.api.aop.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class AnnotationAspectParser {
    
    public static List<Advisor> parseAspect(Object aspectInstance) {
        List<Advisor> advisors = new ArrayList<>();
        Class<?> aspectClass = aspectInstance.getClass();
        
        if (!aspectClass.isAnnotationPresent(Aspect.class)) {
            return advisors;
        }
        
        Aspect aspectAnnotation = aspectClass.getAnnotation(Aspect.class);
        int order = aspectAnnotation.order();
        
        for (Method method : aspectClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Before.class)) {
                Before before = method.getAnnotation(Before.class);
                Pointcut pointcut = createPointcut(before.value());
                Advice advice = createBeforeAdvice(aspectInstance, method);
                advisors.add(new DefaultAdvisor(pointcut, advice, order));
            }
            
            if (method.isAnnotationPresent(After.class)) {
                After after = method.getAnnotation(After.class);
                Pointcut pointcut = createPointcut(after.value());
                Advice advice = createAfterAdvice(aspectInstance, method);
                advisors.add(new DefaultAdvisor(pointcut, advice, order));
            }
            
            if (method.isAnnotationPresent(AfterReturning.class)) {
                AfterReturning afterReturning = method.getAnnotation(AfterReturning.class);
                Pointcut pointcut = createPointcut(afterReturning.value());
                Advice advice = createAfterReturningAdvice(aspectInstance, method, afterReturning.returning());
                advisors.add(new DefaultAdvisor(pointcut, advice, order));
            }
            
            if (method.isAnnotationPresent(AfterThrowing.class)) {
                AfterThrowing afterThrowing = method.getAnnotation(AfterThrowing.class);
                Pointcut pointcut = createPointcut(afterThrowing.value());
                Advice advice = createAfterThrowingAdvice(aspectInstance, method, afterThrowing.throwing());
                advisors.add(new DefaultAdvisor(pointcut, advice, order));
            }
            
            if (method.isAnnotationPresent(Around.class)) {
                Around around = method.getAnnotation(Around.class);
                Pointcut pointcut = createPointcut(around.value());
                Advice advice = createAroundAdvice(aspectInstance, method);
                advisors.add(new DefaultAdvisor(pointcut, advice, order));
            }
        }
        
        return advisors;
    }
    
    private static Pointcut createPointcut(String expression) {
        return new NameMatchPointcut(expression);
    }
    
    private static BeforeAdvice createBeforeAdvice(Object aspect, Method method) {
        return new BeforeAdvice() {
            @Override
            public void before(JoinPoint joinPoint) throws Throwable {
                invokeAdviceMethod(aspect, method, joinPoint, null, null);
            }
            
            @Override
            public int getOrder() {
                return 0;
            }
        };
    }
    
    private static AfterAdvice createAfterAdvice(Object aspect, Method method) {
        return new AfterAdvice() {
            @Override
            public void after(JoinPoint joinPoint, Object result, Throwable exception) throws Throwable {
                invokeAdviceMethod(aspect, method, joinPoint, result, exception);
            }
            
            @Override
            public int getOrder() {
                return 0;
            }
        };
    }
    
    private static AfterReturningAdvice createAfterReturningAdvice(Object aspect, Method method, String returningParam) {
        return new AfterReturningAdvice() {
            @Override
            public void afterReturning(JoinPoint joinPoint, Object result) throws Throwable {
                invokeAdviceMethod(aspect, method, joinPoint, result, null);
            }
            
            @Override
            public int getOrder() {
                return 0;
            }
        };
    }
    
    private static AfterThrowingAdvice createAfterThrowingAdvice(Object aspect, Method method, String throwingParam) {
        return new AfterThrowingAdvice() {
            @Override
            public void afterThrowing(JoinPoint joinPoint, Throwable exception) throws Throwable {
                invokeAdviceMethod(aspect, method, joinPoint, null, exception);
            }
            
            @Override
            public int getOrder() {
                return 0;
            }
        };
    }
    
    private static AroundAdvice createAroundAdvice(Object aspect, Method method) {
        return new AroundAdvice() {
            @Override
            public Object around(JoinPoint joinPoint) throws Throwable {
                method.setAccessible(true);
                Class<?>[] paramTypes = method.getParameterTypes();
                Object[] args = new Object[paramTypes.length];
                
                for (int i = 0; i < paramTypes.length; i++) {
                    if (JoinPoint.class.isAssignableFrom(paramTypes[i])) {
                        args[i] = joinPoint;
                    }
                }
                
                return method.invoke(aspect, args);
            }
            
            @Override
            public int getOrder() {
                return 0;
            }
        };
    }
    
    private static void invokeAdviceMethod(Object aspect, Method method, JoinPoint joinPoint, Object result, Throwable exception) throws Throwable {
        method.setAccessible(true);
        Class<?>[] paramTypes = method.getParameterTypes();
        Object[] args = new Object[paramTypes.length];
        
        for (int i = 0; i < paramTypes.length; i++) {
            if (JoinPoint.class.isAssignableFrom(paramTypes[i])) {
                args[i] = joinPoint;
            } else if (result != null && paramTypes[i].isInstance(result)) {
                args[i] = result;
            } else if (exception != null && paramTypes[i].isInstance(exception)) {
                args[i] = exception;
            }
        }
        
        method.invoke(aspect, args);
    }
}
