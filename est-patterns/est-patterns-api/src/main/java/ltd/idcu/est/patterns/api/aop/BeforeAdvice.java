package ltd.idcu.est.patterns.api.aop;

public interface BeforeAdvice extends Advice {
    
    void before(JoinPoint joinPoint) throws Throwable;
}
