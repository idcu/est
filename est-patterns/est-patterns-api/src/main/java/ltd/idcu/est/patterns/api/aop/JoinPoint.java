package ltd.idcu.est.patterns.api.aop;

import java.lang.reflect.Method;

public interface JoinPoint {
    
    Object getTarget();
    
    Method getMethod();
    
    Object[] getArgs();
    
    Object proceed() throws Throwable;
    
    Object proceed(Object[] args) throws Throwable;
    
    String getSignature();
    
    Class<?> getTargetClass();
}
