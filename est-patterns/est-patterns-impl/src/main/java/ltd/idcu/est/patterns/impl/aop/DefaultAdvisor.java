package ltd.idcu.est.patterns.impl.aop;

import ltd.idcu.est.patterns.api.aop.Advice;
import ltd.idcu.est.patterns.api.aop.Advisor;
import ltd.idcu.est.patterns.api.aop.Pointcut;

public class DefaultAdvisor implements Advisor {
    
    private final Pointcut pointcut;
    private final Advice advice;
    private final int order;
    
    public DefaultAdvisor(Pointcut pointcut, Advice advice, int order) {
        this.pointcut = pointcut;
        this.advice = advice;
        this.order = order;
    }
    
    public DefaultAdvisor(Pointcut pointcut, Advice advice) {
        this(pointcut, advice, 0);
    }
    
    @Override
    public Pointcut getPointcut() {
        return pointcut;
    }
    
    @Override
    public Advice getAdvice() {
        return advice;
    }
    
    @Override
    public int getOrder() {
        return order;
    }
}
