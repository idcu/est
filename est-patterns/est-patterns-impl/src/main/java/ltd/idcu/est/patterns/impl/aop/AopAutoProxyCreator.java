package ltd.idcu.est.patterns.impl.aop;

import ltd.idcu.est.core.api.processor.BeanPostProcessor;
import ltd.idcu.est.patterns.api.aop.Advisor;

import java.util.ArrayList;
import java.util.List;

public class AopAutoProxyCreator implements BeanPostProcessor {
    
    private final List<Advisor> advisors = new ArrayList<>();
    
    public AopAutoProxyCreator() {
    }
    
    public AopAutoProxyCreator(List<Advisor> advisors) {
        this.advisors.addAll(advisors);
    }
    
    public void addAdvisor(Advisor advisor) {
        advisors.add(advisor);
    }
    
    public void addAspect(Object aspectInstance) {
        List<Advisor> parsedAdvisors = AnnotationAspectParser.parseAspect(aspectInstance);
        advisors.addAll(parsedAdvisors);
    }
    
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        if (advisors.isEmpty()) {
            return bean;
        }
        
        Class<?>[] interfaces = bean.getClass().getInterfaces();
        if (interfaces.length == 0) {
            return bean;
        }
        
        return AopProxyFactory.createProxy(bean, interfaces, advisors);
    }
}
