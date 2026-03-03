package ltd.idcu.est.core.api;

import java.util.Optional;

public interface Container {
    
    <T> void register(Class<T> type, Class<? extends T> implementation);
    
    <T> void registerSingleton(Class<T> type, T instance);
    
    <T> void registerSupplier(Class<T> type, java.util.function.Supplier<T> supplier);
    
    <T> T get(Class<T> type);
    
    <T> Optional<T> getIfPresent(Class<T> type);
    
    boolean contains(Class<?> type);
    
    void clear();
}
