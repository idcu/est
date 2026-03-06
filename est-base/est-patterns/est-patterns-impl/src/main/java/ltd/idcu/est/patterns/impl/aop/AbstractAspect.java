package ltd.idcu.est.patterns.impl.aop;

import ltd.idcu.est.patterns.api.aop.*;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractAspect implements Aspect {
    
    protected final Pointcut pointcut;
    protected final List<Advice> advices = new ArrayList<>();
    protected final int order;
    
    public AbstractAspect(Pointcut pointcut, int order) {
        this.pointcut = pointcut;
        this.order = order;
    }
    
    public AbstractAspect(Pointcut pointcut) {
        this(pointcut, 0);
    }
    
    @Override
    public Pointcut getPointcut() {
        return pointcut;
    }
    
    @Override
    public List<Advice> getAdvices() {
        return new ArrayList<>(advices);
    }
    
    @Override
    public int getOrder() {
        return order;
    }
    
    public void addAdvice(Advice advice) {
        advices.add(advice);
    }
    
    public void addBeforeAdvice(BeforeAdvice advice) {
        addAdvice(advice);
    }
    
    public void addAfterAdvice(AfterAdvice advice) {
        addAdvice(advice);
    }
    
    public void addAfterReturningAdvice(AfterReturningAdvice advice) {
        addAdvice(advice);
    }
    
    public void addAfterThrowingAdvice(AfterThrowingAdvice advice) {
        addAdvice(advice);
    }
    
    public void addAroundAdvice(AroundAdvice advice) {
        addAdvice(advice);
    }
}
