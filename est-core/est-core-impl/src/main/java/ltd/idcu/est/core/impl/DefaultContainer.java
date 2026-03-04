package ltd.idcu.est.core.impl;

import ltd.idcu.est.core.api.Config;
import ltd.idcu.est.core.api.Container;
import ltd.idcu.est.core.api.lifecycle.DisposableBean;
import ltd.idcu.est.core.api.lifecycle.InitializingBean;
import ltd.idcu.est.core.api.lifecycle.PostConstruct;
import ltd.idcu.est.core.api.lifecycle.PreDestroy;
import ltd.idcu.est.core.api.processor.BeanPostProcessor;
import ltd.idcu.est.core.api.scope.Scope;
import ltd.idcu.est.core.impl.inject.ConstructorInjector;
import ltd.idcu.est.core.impl.inject.FieldInjector;
import ltd.idcu.est.core.impl.inject.MethodInjector;
import ltd.idcu.est.core.impl.scope.ScopeStrategy;
import ltd.idcu.est.core.impl.scan.ComponentScanner;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Supplier;

public class DefaultContainer implements Container {

    private static class Registration {
        final Supplier<?> supplier;
        final Scope scope;

        Registration(Supplier<?> supplier, Scope scope) {
            this.supplier = supplier;
            this.scope = scope;
        }
    }

    private final Map<String, Registration> registrations = new ConcurrentHashMap<>();
    private final Map<String, Object> instances = new ConcurrentHashMap<>();
    private final ScopeStrategy scopeStrategy = new ScopeStrategy();
    private final ConstructorInjector constructorInjector;
    private final FieldInjector fieldInjector;
    private final MethodInjector methodInjector;
    private final List<BeanPostProcessor> beanPostProcessors = new CopyOnWriteArrayList<>();
    private final List<DisposableBean> disposableBeans = new CopyOnWriteArrayList<>();
    private final List<Object> preDestroyBeans = new CopyOnWriteArrayList<>();
    private Config config;

    public DefaultContainer() {
        this.constructorInjector = new ConstructorInjector(this);
        this.fieldInjector = new FieldInjector(this);
        this.methodInjector = new MethodInjector(this);
    }

    public DefaultContainer(Config config) {
        this();
        this.config = config;
    }

    public Config getConfig() {
        return config;
    }

    private String buildKey(Class<?> type, String qualifier) {
        return qualifier != null ? type.getName() + "#" + qualifier : type.getName();
    }

    @Override
    public <T> void register(Class<T> type, Class<? extends T> implementation) {
        register(type, implementation, Scope.SINGLETON, null);
    }

    @Override
    public <T> void register(Class<T> type, Class<? extends T> implementation, Scope scope) {
        register(type, implementation, scope, null);
    }

    @Override
    public <T> void register(Class<T> type, Class<? extends T> implementation, String qualifier) {
        register(type, implementation, Scope.SINGLETON, qualifier);
    }

    @Override
    public <T> void register(Class<T> type, Class<? extends T> implementation, Scope scope, String qualifier) {
        if (type == null || implementation == null) {
            throw new IllegalArgumentException("Type and implementation cannot be null");
        }
        String key = buildKey(type, qualifier);
        registrations.put(key, new Registration(() -> createInstanceWithLifecycle(implementation, key), scope));
        instances.remove(key);
    }

    @Override
    public <T> void registerSingleton(Class<T> type, T instance) {
        registerSingleton(type, instance, null);
    }

    @Override
    public <T> void registerSingleton(Class<T> type, T instance, String qualifier) {
        if (type == null || instance == null) {
            throw new IllegalArgumentException("Type and instance cannot be null");
        }
        String key = buildKey(type, qualifier);
        T processedInstance = processBean(instance, key);
        injectDependencies(processedInstance);
        initializeBean(processedInstance, key);
        registerDisposableBean(processedInstance);
        instances.put(key, processedInstance);
        registrations.put(key, new Registration(() -> processedInstance, Scope.SINGLETON));
    }

    @Override
    public <T> void registerSupplier(Class<T> type, Supplier<T> supplier) {
        registerSupplier(type, supplier, Scope.SINGLETON, null);
    }

    @Override
    public <T> void registerSupplier(Class<T> type, Supplier<T> supplier, Scope scope) {
        registerSupplier(type, supplier, scope, null);
    }

    @Override
    public <T> void registerSupplier(Class<T> type, Supplier<T> supplier, String qualifier) {
        registerSupplier(type, supplier, Scope.SINGLETON, qualifier);
    }

    @Override
    public <T> void registerSupplier(Class<T> type, Supplier<T> supplier, Scope scope, String qualifier) {
        if (type == null || supplier == null) {
            throw new IllegalArgumentException("Type and supplier cannot be null");
        }
        String key = buildKey(type, qualifier);
        registrations.put(key, new Registration(() -> {
            T instance = supplier.get();
            return processAndInitializeBean(instance, key);
        }, scope));
        instances.remove(key);
    }

    @Override
    public <T> T get(Class<T> type) {
        return get(type, null);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(Class<T> type, String qualifier) {
        if (type == null) {
            throw new IllegalArgumentException("Type cannot be null");
        }

        String key = buildKey(type, qualifier);
        Registration registration = registrations.get(key);
        if (registration == null) {
            throw new IllegalStateException("No registration found for type: " + type.getName() +
                (qualifier != null ? " with qualifier: " + qualifier : ""));
        }

        return (T) scopeStrategy.get(registration.scope, type, qualifier,
            (Supplier<T>) registration.supplier, instances);
    }

    @Override
    public <T> Optional<T> getIfPresent(Class<T> type) {
        return getIfPresent(type, null);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Optional<T> getIfPresent(Class<T> type, String qualifier) {
        if (type == null) {
            return Optional.empty();
        }

        String key = buildKey(type, qualifier);
        Registration registration = registrations.get(key);
        if (registration == null) {
            return Optional.empty();
        }

        try {
            T instance = (T) scopeStrategy.get(registration.scope, type, qualifier,
                (Supplier<T>) registration.supplier, instances);
            return Optional.of(instance);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public <T> T create(Class<T> type) {
        return createInstanceWithLifecycle(type, type.getName());
    }

    private <T> T createInstance(Class<T> type) {
        return constructorInjector.createInstance(type);
    }

    @SuppressWarnings("unchecked")
    private <T> T createInstanceWithLifecycle(Class<T> type, String beanName) {
        T instance = createInstance(type);
        return processAndInitializeBean(instance, beanName);
    }

    @SuppressWarnings("unchecked")
    private <T> T processAndInitializeBean(T instance, String beanName) {
        T processed = processBean(instance, beanName);
        injectDependencies(processed);
        initializeBean(processed, beanName);
        registerDisposableBean(processed);
        return processed;
    }

    private void injectDependencies(Object instance) {
        fieldInjector.injectFields(instance);
        methodInjector.injectMethods(instance);
    }

    @SuppressWarnings("unchecked")
    private <T> T processBean(T bean, String beanName) {
        T processed = bean;
        for (BeanPostProcessor processor : beanPostProcessors) {
            processed = (T) processor.postProcessBeforeInitialization(processed, beanName);
        }
        return processed;
    }

    private void initializeBean(Object bean, String beanName) {
        invokePostConstruct(bean);
        if (bean instanceof InitializingBean) {
            try {
                ((InitializingBean) bean).afterPropertiesSet();
            } catch (Exception e) {
                throw new RuntimeException("Failed to initialize bean: " + beanName, e);
            }
        }
        for (BeanPostProcessor processor : beanPostProcessors) {
            processor.postProcessAfterInitialization(bean, beanName);
        }
    }

    private void invokePostConstruct(Object bean) {
        Method[] methods = bean.getClass().getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(PostConstruct.class)) {
                try {
                    method.setAccessible(true);
                    method.invoke(bean);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to invoke @PostConstruct method on bean: " + bean.getClass().getName(), e);
                }
            }
        }
    }

    private void registerDisposableBean(Object bean) {
        if (bean instanceof DisposableBean) {
            disposableBeans.add((DisposableBean) bean);
        }
        if (hasPreDestroyMethod(bean)) {
            preDestroyBeans.add(bean);
        }
    }

    private boolean hasPreDestroyMethod(Object bean) {
        Method[] methods = bean.getClass().getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(PreDestroy.class)) {
                return true;
            }
        }
        return false;
    }

    private void invokePreDestroy(Object bean) {
        Method[] methods = bean.getClass().getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(PreDestroy.class)) {
                try {
                    method.setAccessible(true);
                    method.invoke(bean);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to invoke @PreDestroy method on bean: " + bean.getClass().getName(), e);
                }
            }
        }
    }

    @Override
    public boolean contains(Class<?> type) {
        return contains(type, null);
    }

    @Override
    public boolean contains(Class<?> type, String qualifier) {
        if (type == null) {
            return false;
        }
        String key = buildKey(type, qualifier);
        return registrations.containsKey(key);
    }

    @Override
    public void addBeanPostProcessor(BeanPostProcessor processor) {
        if (processor != null) {
            beanPostProcessors.add(processor);
        }
    }

    @Override
    public void close() {
        for (DisposableBean bean : disposableBeans) {
            try {
                bean.destroy();
            } catch (Exception e) {
                throw new RuntimeException("Failed to destroy bean", e);
            }
        }
        disposableBeans.clear();
        for (Object bean : preDestroyBeans) {
            invokePreDestroy(bean);
        }
        preDestroyBeans.clear();
        clear();
    }

    @Override
    public void scan(String... basePackages) {
        ComponentScanner.scanAndRegister(this, basePackages);
    }

    @Override
    public void clear() {
        registrations.clear();
        instances.clear();
    }
}
