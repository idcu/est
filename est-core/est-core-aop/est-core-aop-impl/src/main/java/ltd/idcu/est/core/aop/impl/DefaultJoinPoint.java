package ltd.idcu.est.core.aop.impl;

import ltd.idcu.est.core.aop.api.JoinPoint;

import java.lang.reflect.Method;

public class DefaultJoinPoint implements JoinPoint {
    private final Object target;
    private final Method method;
    private final Object[] args;

    public DefaultJoinPoint(Object target, Method method, Object[] args) {
        this.target = target;
        this.method = method;
        this.args = args;
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
    public String getSignature() {
        return method.getDeclaringClass().getName() + "#" + method.getName();
    }
}
