package ltd.idcu.est.core.impl;

import ltd.idcu.est.core.api.Container;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class DefaultContainer implements Container {
    
    private final Map<Class<?>, Supplier<?>> registrations = new ConcurrentHashMap<>();
    private final Map<Class<?>, Object> instances = new ConcurrentHashMap<>();
    
    @Override
    public <T> void register(Class<T> type, Class<? extends T> implementation) {
        if (type == null || implementation == null) {
            throw new IllegalArgumentException("Type and implementation cannot be null");
        }
        registrations.put(type, () -> {
            try {
                return implementation.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException("Failed to create instance of " + implementation.getName(), e);
            }
        });
    }
    
    @Override
    public <T> void registerSingleton(Class<T> type, T instance) {
        if (type == null || instance == null) {
            throw new IllegalArgumentException("Type and instance cannot be null");
        }
        instances.put(type, instance);
        registrations.put(type, () -> instance);
    }
    
    @Override
    public <T> void registerSupplier(Class<T> type, Supplier<T> supplier) {
        if (type == null || supplier == null) {
            throw new IllegalArgumentException("Type and supplier cannot be null");
        }
        registrations.put(type, supplier);
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(Class<T> type) {
        if (type == null) {
            throw new IllegalArgumentException("Type cannot be null");
        }
        
        Object instance = instances.get(type);
        if (instance != null) {
            return (T) instance;
        }
        
        Supplier<?> supplier = registrations.get(type);
        if (supplier == null) {
            throw new IllegalStateException("No registration found for type: " + type.getName());
        }
        
        return (T) supplier.get();
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> Optional<T> getIfPresent(Class<T> type) {
        if (type == null) {
            return Optional.empty();
        }
        
        Object instance = instances.get(type);
        if (instance != null) {
            return Optional.of((T) instance);
        }
        
        Supplier<?> supplier = registrations.get(type);
        if (supplier == null) {
            return Optional.empty();
        }
        
        return Optional.of((T) supplier.get());
    }
    
    @Override
    public boolean contains(Class<?> type) {
        return type != null && (registrations.containsKey(type) || instances.containsKey(type));
    }
    
    @Override
    public void clear() {
        registrations.clear();
        instances.clear();
    }
}
