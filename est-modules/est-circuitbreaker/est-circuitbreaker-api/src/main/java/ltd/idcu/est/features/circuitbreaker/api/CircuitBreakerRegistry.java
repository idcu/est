package ltd.idcu.est.features.circuitbreaker.api;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class CircuitBreakerRegistry {
    private final Map<String, CircuitBreaker> circuitBreakers = new ConcurrentHashMap<>();

    public CircuitBreaker create(String name) {
        return create(name, new CircuitBreakerConfig());
    }

    public CircuitBreaker create(String name, CircuitBreakerConfig config) {
        return circuitBreakers.computeIfAbsent(name, k -> new DefaultCircuitBreaker(name, config));
    }

    public Optional<CircuitBreaker> get(String name) {
        return Optional.ofNullable(circuitBreakers.get(name));
    }

    public CircuitBreaker getOrCreate(String name) {
        return circuitBreakers.computeIfAbsent(name, k -> new DefaultCircuitBreaker(name));
    }

    public void remove(String name) {
        circuitBreakers.remove(name);
    }

    public Map<String, CircuitBreaker> getAll() {
        return new ConcurrentHashMap<>(circuitBreakers);
    }

    public void clear() {
        circuitBreakers.clear();
    }
}
