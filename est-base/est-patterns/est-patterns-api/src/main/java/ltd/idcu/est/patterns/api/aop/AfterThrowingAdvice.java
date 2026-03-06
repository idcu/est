package ltd.idcu.est.patterns.api.aop;

public interface AfterThrowingAdvice extends Advice {
    
    void afterThrowing(JoinPoint joinPoint, Throwable exception) throws Throwable;
}
