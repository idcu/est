package ltd.idcu.est.patterns.impl.aop;

import ltd.idcu.est.patterns.api.aop.JoinPoint;

import java.lang.reflect.Method;

public class DefaultJoinPoint implements JoinPoint {
    
    private final Object target;
    private final Method method;
    private Object[] args;
    private final ProceedCallback proceedCallback;
    
    public interface ProceedCallback {
        Object proceed(Object[] args) throws Throwable;
    }
    
    public DefaultJoinPoint(Object target, Method method, Object[] args, ProceedCallback proceedCallback) {
        this.target = target;
        this.method = method;
        this.args = args != null ? args.clone() : new Object[0];
        this.proceedCallback = proceedCallback;
    }
    
    @Override
    public Object getTarget() {
        return target;
    }
    
    @Override
    public Method getMethod() {
        return method;
    }
    
    @Override
    public Object[] getArgs() {
        return args.clone();
    }
    
    @Override
    public Object proceed() throws Throwable {
        return proceedCallback.proceed(args);
    }
    
    @Override
    public Object proceed(Object[] args) throws Throwable {
        return proceedCallback.proceed(args);
    }
    
    @Override
    public String getSignature() {
        return method.getDeclaringClass().getName() + "." + method.getName();
    }
    
    @Override
    public Class<?> getTargetClass() {
        return target != null ? target.getClass() : method.getDeclaringClass();
    }
}
