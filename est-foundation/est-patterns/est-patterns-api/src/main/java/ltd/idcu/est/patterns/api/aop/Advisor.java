package ltd.idcu.est.patterns.api.aop;

public interface Advisor {
    
    Pointcut getPointcut();
    
    Advice getAdvice();
    
    int getOrder();
}
