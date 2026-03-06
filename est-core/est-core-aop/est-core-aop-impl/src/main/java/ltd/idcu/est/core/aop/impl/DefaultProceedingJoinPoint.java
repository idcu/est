package ltd.idcu.est.core.aop.impl;

import ltd.idcu.est.core.aop.api.ProceedingJoinPoint;

import java.lang.reflect.Method;

public class DefaultProceedingJoinPoint implements ProceedingJoinPoint {
    private final Object target;
    private final Method method;
    private Object[] args;
    private final MethodInvocationChain chain;

    public DefaultProceedingJoinPoint(Object target, Method method, Object[] args, MethodInvocationChain chain) {
        this.target = target;
        this.method = method;
        this.args = args;
        this.chain = chain;
    }

    @Override
    public Object getTarget() {
        return target;
    }

    @Override
    public Object[] getArgs() {
        return args;
    }

    @Override
    public Object proceed() throws Throwable {
        return chain.proceed(target, method, args);
    }

    @Override
    public Object proceed(Object[] args) throws Throwable {
        this.args = args;
        return chain.proceed(target, method, args);
    }

    @Override
    public String getSignature() {
        return method.getDeclaringClass().getName() + "#" + method.getName();
    }
}
