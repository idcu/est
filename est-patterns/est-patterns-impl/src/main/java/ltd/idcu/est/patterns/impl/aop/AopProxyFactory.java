package ltd.idcu.est.patterns.impl.aop;

import ltd.idcu.est.patterns.api.aop.*;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.stream.Collectors;

public class AopProxyFactory {
    
    public static <T> T createProxy(T target, List<Advisor> advisors) {
        return createProxy(target, target.getClass().getInterfaces(), advisors);
    }
    
    @SuppressWarnings("unchecked")
    public static <T> T createProxy(T target, Class<?>[] interfaces, List<Advisor> advisors) {
        if (interfaces == null || interfaces.length == 0) {
            interfaces = target.getClass().getInterfaces();
        }
        if (interfaces.length == 0) {
            throw new IllegalArgumentException("Target class must implement at least one interface for JDK dynamic proxy");
        }
        
        AopInvocationHandler handler = new AopInvocationHandler(target, advisors);
        return (T) Proxy.newProxyInstance(
            target.getClass().getClassLoader(),
            interfaces,
            handler
        );
    }
    
    private static class AopInvocationHandler implements InvocationHandler {
        
        private final Object target;
        private final Map<Method, List<AdviceChain>> adviceChains = new HashMap<>();
        
        public AopInvocationHandler(Object target, List<Advisor> advisors) {
            this.target = target;
            buildAdviceChains(advisors);
        }
        
        private void buildAdviceChains(List<Advisor> advisors) {
            Class<?> targetClass = target.getClass();
            
            for (Method method : targetClass.getMethods()) {
                List<AdviceChain> chains = new ArrayList<>();
                
                List<Advisor> matchingAdvisors = advisors.stream()
                    .filter(advisor -> advisor.getPointcut().matches(targetClass, method))
                    .sorted(Comparator.comparingInt(Advisor::getOrder))
                    .toList();
                
                for (Advisor advisor : matchingAdvisors) {
                    chains.add(new AdviceChain(advisor.getAdvice()));
                }
                
                if (!chains.isEmpty()) {
                    adviceChains.put(method, chains);
                }
            }
        }
        
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            List<AdviceChain> chains = adviceChains.get(method);
            
            if (chains == null || chains.isEmpty()) {
                return method.invoke(target, args);
            }
            
            DefaultJoinPoint joinPoint = new DefaultJoinPoint(
                target,
                method,
                args,
                (proceedArgs) -> method.invoke(target, proceedArgs)
            );
            
            return executeAdviceChain(joinPoint, chains, 0);
        }
        
        private Object executeAdviceChain(JoinPoint joinPoint, List<AdviceChain> chains, int index) throws Throwable {
            if (index >= chains.size()) {
                return joinPoint.proceed();
            }
            
            AdviceChain chain = chains.get(index);
            Advice advice = chain.advice;
            
            Object result = null;
            Throwable exception = null;
            
            try {
                if (advice instanceof BeforeAdvice beforeAdvice) {
                    beforeAdvice.before(joinPoint);
                }
                
                if (advice instanceof AroundAdvice aroundAdvice) {
                    result = aroundAdvice.around(new ProceedingJoinPointWrapper(joinPoint, chains, index + 1));
                } else {
                    result = executeAdviceChain(joinPoint, chains, index + 1);
                }
                
                if (advice instanceof AfterReturningAdvice afterReturningAdvice) {
                    afterReturningAdvice.afterReturning(joinPoint, result);
                }
                
            } catch (Throwable t) {
                exception = t;
                if (advice instanceof AfterThrowingAdvice afterThrowingAdvice) {
                    afterThrowingAdvice.afterThrowing(joinPoint, t);
                }
                throw t;
            } finally {
                if (advice instanceof AfterAdvice afterAdvice) {
                    afterAdvice.after(joinPoint, result, exception);
                }
            }
            
            return result;
        }
    }
    
    private static class AdviceChain {
        final Advice advice;
        
        AdviceChain(Advice advice) {
            this.advice = advice;
        }
    }
    
    private static class ProceedingJoinPointWrapper implements JoinPoint {
        
        private final JoinPoint delegate;
        private final List<AdviceChain> chains;
        private final int nextIndex;
        
        ProceedingJoinPointWrapper(JoinPoint delegate, List<AdviceChain> chains, int nextIndex) {
            this.delegate = delegate;
            this.chains = chains;
            this.nextIndex = nextIndex;
        }
        
        @Override
        public Object getTarget() {
            return delegate.getTarget();
        }
        
        @Override
        public Method getMethod() {
            return delegate.getMethod();
        }
        
        @Override
        public Object[] getArgs() {
            return delegate.getArgs();
        }
        
        @Override
        public Object proceed() throws Throwable {
            return delegate.proceed();
        }
        
        @Override
        public Object proceed(Object[] args) throws Throwable {
            return delegate.proceed(args);
        }
        
        @Override
        public String getSignature() {
            return delegate.getSignature();
        }
        
        @Override
        public Class<?> getTargetClass() {
            return delegate.getTargetClass();
        }
    }
}
