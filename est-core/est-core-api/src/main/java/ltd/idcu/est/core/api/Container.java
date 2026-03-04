package ltd.idcu.est.core.api;

import ltd.idcu.est.core.api.scope.Scope;

import java.util.Optional;
import java.util.function.Supplier;

public interface Container {
    
    <T> void register(Class<T> type, Class<? extends T> implementation);
    
    <T> void register(Class<T> type, Class<? extends T> implementation, Scope scope);
    
    <T> void register(Class<T> type, Class<? extends T> implementation, String qualifier);
    
    <T> void register(Class<T> type, Class<? extends T> implementation, Scope scope, String qualifier);
    
    <T> void registerSingleton(Class<T> type, T instance);
    
    <T> void registerSingleton(Class<T> type, T instance, String qualifier);
    
    <T> void registerSupplier(Class<T> type, Supplier<T> supplier);
    
    <T> void registerSupplier(Class<T> type, Supplier<T> supplier, Scope scope);
    
    <T> void registerSupplier(Class<T> type, Supplier<T> supplier, String qualifier);
    
    <T> void registerSupplier(Class<T> type, Supplier<T> supplier, Scope scope, String qualifier);
    
    <T> T get(Class<T> type);
    
    <T> T get(Class<T> type, String qualifier);
    
    <T> Optional<T> getIfPresent(Class<T> type);
    
    <T> Optional<T> getIfPresent(Class<T> type, String qualifier);
    
    <T> T create(Class<T> type);
    
    boolean contains(Class<?> type);
    
    boolean contains(Class<?> type, String qualifier);
    
    void clear();
}
