package ltd.idcu.est.core.container.impl.scope;

import ltd.idcu.est.core.container.api.scope.Scope;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class ScopeStrategy {
    
    private final Map<Scope, ScopeHandler> handlers = new ConcurrentHashMap<>();
    
    public ScopeStrategy() {
        handlers.put(Scope.SINGLETON, new SingletonScopeHandler());
        handlers.put(Scope.PROTOTYPE, new PrototypeScopeHandler());
        handlers.put(Scope.REQUEST, new RequestScopeHandler());
        handlers.put(Scope.SESSION, new SessionScopeHandler());
    }
    
    public <T> T get(Scope scope, Class<T> type, String qualifier, Supplier<T> supplier, Map<String, Object> instances) {
        ScopeHandler handler = handlers.get(scope);
        if (handler == null) {
            throw new IllegalStateException("No handler for scope: " + scope);
        }
        return handler.handle(type, qualifier, supplier, instances);
    }
    
    private interface ScopeHandler {
        <T> T handle(Class<T> type, String qualifier, Supplier<T> supplier, Map<String, Object> instances);
    }
    
    private static class SingletonScopeHandler implements ScopeHandler {
        @Override
        @SuppressWarnings("unchecked")
        public <T> T handle(Class<T> type, String qualifier, Supplier<T> supplier, Map<String, Object> instances) {
            String key = buildKey(type, qualifier);
            T instance = (T) instances.get(key);
            if (instance == null) {
                synchronized (instances) {
                    instance = (T) instances.get(key);
                    if (instance == null) {
                        instance = supplier.get();
                        instances.put(key, instance);
                    }
                }
            }
            return instance;
        }
    }
    
    private static class PrototypeScopeHandler implements ScopeHandler {
        @Override
        public <T> T handle(Class<T> type, String qualifier, Supplier<T> supplier, Map<String, Object> instances) {
            return supplier.get();
        }
    }
    
    private static class RequestScopeHandler implements ScopeHandler {
        @Override
        public <T> T handle(Class<T> type, String qualifier, Supplier<T> supplier, Map<String, Object> instances) {
            return supplier.get();
        }
    }
    
    private static class SessionScopeHandler implements ScopeHandler {
        @Override
        public <T> T handle(Class<T> type, String qualifier, Supplier<T> supplier, Map<String, Object> instances) {
            return supplier.get();
        }
    }
    
    private static String buildKey(Class<?> type, String qualifier) {
        return qualifier != null ? type.getName() + "#" + qualifier : type.getName();
    }
}
