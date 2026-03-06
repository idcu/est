package ltd.idcu.est.core.aop.impl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

public class AopProxyFactory {
    private final AspectRegistry aspectRegistry;

    public AopProxyFactory(AspectRegistry aspectRegistry) {
        this.aspectRegistry = aspectRegistry;
    }

    @SuppressWarnings("unchecked")
    public <T> T createProxy(T target, Class<T> interfaceClass) {
        return (T) Proxy.newProxyInstance(
                interfaceClass.getClassLoader(),
                new Class<?>[]{interfaceClass},
                new AopInvocationHandler(target, aspectRegistry)
        );
    }

    public Object createProxy(Object target) {
        Class<?>[] interfaces = target.getClass().getInterfaces();
        if (interfaces.length == 0) {
            throw new IllegalArgumentException("Target class must implement at least one interface");
        }
        return Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                interfaces,
                new AopInvocationHandler(target, aspectRegistry)
        );
    }

    private static class AopInvocationHandler implements InvocationHandler {
        private final Object target;
        private final AspectRegistry aspectRegistry;

        AopInvocationHandler(Object target, AspectRegistry aspectRegistry) {
            this.target = target;
            this.aspectRegistry = aspectRegistry;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (Object.class.equals(method.getDeclaringClass())) {
                return method.invoke(target, args);
            }

            List<AspectRegistry.AspectMethod> aspects = aspectRegistry.getMatchingAspects(target.getClass(), method);
            if (aspects.isEmpty()) {
                return method.invoke(target, args);
            }

            MethodInvocationChain chain = new MethodInvocationChain(target, method, aspects);
            return chain.proceed(target, method, args);
        }
    }
}
