package ltd.idcu.est.patterns.api.aop;

public interface AroundAdvice extends Advice {
    
    Object around(JoinPoint joinPoint) throws Throwable;
}
