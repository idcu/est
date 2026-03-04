package ltd.idcu.est.patterns.api.aop;

import java.lang.reflect.Method;

public interface Pointcut {
    
    boolean matches(Class<?> targetClass, Method method);
    
    String getExpression();
}
