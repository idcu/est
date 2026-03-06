package ltd.idcu.est.core.aop.impl;

import ltd.idcu.est.core.aop.api.JoinPoint;
import ltd.idcu.est.core.aop.api.ProceedingJoinPoint;
import ltd.idcu.est.core.aop.api.annotation.After;
import ltd.idcu.est.core.aop.api.annotation.AfterReturning;
import ltd.idcu.est.core.aop.api.annotation.AfterThrowing;
import ltd.idcu.est.core.aop.api.annotation.Around;
import ltd.idcu.est.core.aop.api.annotation.Aspect;
import ltd.idcu.est.core.aop.api.annotation.Before;
import ltd.idcu.est.utils.common.AssertUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AspectRegistry {
    private final List<AspectMethod> aspects = new ArrayList<>();
    private final Map<String, List<AspectMethod>> methodCache = new ConcurrentHashMap<>();

    public void registerAspect(Object aspect) {
        Class<?> aspectClass = aspect.getClass();
        Aspect aspectAnnotation = aspectClass.getAnnotation(Aspect.class);
        AssertUtils.notNull(aspectAnnotation, "Class is not annotated with @Aspect: " + aspectClass.getName());

        int order = aspectAnnotation.order();

        for (Method method : aspectClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Before.class)) {
                Before before = method.getAnnotation(Before.class);
                aspects.add(new AspectMethod(aspect, method, AspectType.BEFORE, before.value(), order));
            }
            if (method.isAnnotationPresent(After.class)) {
                After after = method.getAnnotation(After.class);
                aspects.add(new AspectMethod(aspect, method, AspectType.AFTER, after.value(), order));
            }
            if (method.isAnnotationPresent(Around.class)) {
                Around around = method.getAnnotation(Around.class);
                aspects.add(new AspectMethod(aspect, method, AspectType.AROUND, around.value(), order));
            }
            if (method.isAnnotationPresent(AfterReturning.class)) {
                AfterReturning afterReturning = method.getAnnotation(AfterReturning.class);
                aspects.add(new AspectMethod(aspect, method, AspectType.AFTER_RETURNING, afterReturning.value(), order));
            }
            if (method.isAnnotationPresent(AfterThrowing.class)) {
                AfterThrowing afterThrowing = method.getAnnotation(AfterThrowing.class);
                aspects.add(new AspectMethod(aspect, method, AspectType.AFTER_THROWING, afterThrowing.value(), order));
            }
        }

        aspects.sort(Comparator.comparingInt(am -> am.order));
        methodCache.clear();
    }

    public List<AspectMethod> getMatchingAspects(Class<?> targetClass, Method method) {
        String cacheKey = targetClass.getName() + "#" + method.getName();
        return methodCache.computeIfAbsent(cacheKey, k -> {
            List<AspectMethod> matching = new ArrayList<>();
            for (AspectMethod aspectMethod : aspects) {
                if (aspectMethod.matches(targetClass, method)) {
                    matching.add(aspectMethod);
                }
            }
            return matching;
        });
    }

    public enum AspectType {
        BEFORE, AFTER, AROUND, AFTER_RETURNING, AFTER_THROWING
    }

    public static class AspectMethod {
        final Object aspect;
        final Method method;
        final AspectType type;
        final PointcutExpression pointcut;
        final int order;

        AspectMethod(Object aspect, Method method, AspectType type, String pointcut, int order) {
            this.aspect = aspect;
            this.method = method;
            this.type = type;
            this.pointcut = new PointcutExpression(pointcut);
            this.order = order;
            method.setAccessible(true);
        }

        boolean matches(Class<?> clazz, Method method) {
            return pointcut.matches(clazz, method);
        }

        void invokeBefore(JoinPoint joinPoint) throws Throwable {
            method.invoke(aspect, joinPoint);
        }

        void invokeAfter(JoinPoint joinPoint) throws Throwable {
            method.invoke(aspect, joinPoint);
        }

        Object invokeAround(ProceedingJoinPoint joinPoint) throws Throwable {
            return method.invoke(aspect, joinPoint);
        }

        void invokeAfterReturning(JoinPoint joinPoint, Object returnValue) throws Throwable {
            int paramCount = method.getParameterCount();
            if (paramCount == 2) {
                method.invoke(aspect, joinPoint, returnValue);
            } else {
                method.invoke(aspect, joinPoint);
            }
        }

        void invokeAfterThrowing(JoinPoint joinPoint, Throwable throwable) throws Throwable {
            int paramCount = method.getParameterCount();
            if (paramCount == 2) {
                method.invoke(aspect, joinPoint, throwable);
            } else {
                method.invoke(aspect, joinPoint);
            }
        }
    }
}
