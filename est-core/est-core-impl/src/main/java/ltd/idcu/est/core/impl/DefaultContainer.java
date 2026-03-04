package ltd.idcu.est.core.impl;

import ltd.idcu.est.core.api.Container;
import ltd.idcu.est.core.api.scope.Scope;
import ltd.idcu.est.core.impl.inject.ConstructorInjector;
import ltd.idcu.est.core.impl.scope.ScopeStrategy;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
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
    
    public DefaultContainer() {
        this.constructorInjector = new ConstructorInjector(this);
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
        registrations.put(key, new Registration(() -> createInstance(implementation), scope));
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
        instances.put(key, instance);
        registrations.put(key, new Registration(() -> instance, Scope.SINGLETON));
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
        registrations.put(key, new Registration(supplier, scope));
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
        return createInstance(type);
    }
    
    private <T> T createInstance(Class<T> type) {
        return constructorInjector.createInstance(type);
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
    public void clear() {
        registrations.clear();
        instances.clear();
    }
}
