package ltd.idcu.est.core.aop.impl;

import ltd.idcu.est.core.aop.api.JoinPoint;
import ltd.idcu.est.core.aop.api.ProceedingJoinPoint;

import java.lang.reflect.Method;
import java.util.List;

public class MethodInvocationChain {
    private final Object target;
    private final Method method;
    private final List<AspectRegistry.AspectMethod> aspects;
    private int currentIndex = 0;

    public MethodInvocationChain(Object target, Method method, List<AspectRegistry.AspectMethod> aspects) {
        this.target = target;
        this.method = method;
        this.aspects = aspects;
    }

    public Object proceed(Object target, Method method, Object[] args) throws Throwable {
        if (currentIndex >= aspects.size()) {
            return method.invoke(target, args);
        }

        AspectRegistry.AspectMethod aspectMethod = aspects.get(currentIndex++);
        try {
            switch (aspectMethod.type) {
                case BEFORE:
                    aspectMethod.invokeBefore(new DefaultJoinPoint(target, method, args));
                    return proceed(target, method, args);
                case AROUND:
                    ProceedingJoinPoint pjp = new DefaultProceedingJoinPoint(target, method, args, this);
                    return aspectMethod.invokeAround(pjp);
                case AFTER:
                    try {
                        return proceed(target, method, args);
                    } finally {
                        aspectMethod.invokeAfter(new DefaultJoinPoint(target, method, args));
                    }
                case AFTER_RETURNING:
                    Object result = proceed(target, method, args);
                    aspectMethod.invokeAfterReturning(new DefaultJoinPoint(target, method, args), result);
                    return result;
                case AFTER_THROWING:
                    try {
                        return proceed(target, method, args);
                    } catch (Throwable t) {
                        aspectMethod.invokeAfterThrowing(new DefaultJoinPoint(target, method, args), t);
                        throw t;
                    }
                default:
                    return proceed(target, method, args);
            }
        } finally {
            currentIndex--;
        }
    }
}
