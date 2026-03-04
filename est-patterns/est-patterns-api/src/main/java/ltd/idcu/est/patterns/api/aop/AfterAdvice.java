package ltd.idcu.est.patterns.api.aop;

public interface AfterAdvice extends Advice {
    
    void after(JoinPoint joinPoint, Object result, Throwable exception) throws Throwable;
}
