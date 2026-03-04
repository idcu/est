package ltd.idcu.est.patterns.api.aop;

import java.util.List;

public interface Aspect {
    
    Pointcut getPointcut();
    
    List<Advice> getAdvices();
    
    int getOrder();
}
