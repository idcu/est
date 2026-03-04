package ltd.idcu.est.patterns.api.aop;

public interface AfterReturningAdvice extends Advice {
    
    void afterReturning(JoinPoint joinPoint, Object result) throws Throwable;
}
