package ltd.idcu.est.patterns.impl.behavioral;

import ltd.idcu.est.patterns.api.behavioral.Strategy;
import ltd.idcu.est.patterns.api.behavioral.StrategyContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultStrategyContext<T, R> implements StrategyContext<T, R> {
    
    private final Map<String, Strategy<T, R>> strategies = new ConcurrentHashMap<>();
    
    @Override
    public void registerStrategy(String name, Strategy<T, R> strategy) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Strategy name cannot be null or empty");
        }
        if (strategy == null) {
            throw new IllegalArgumentException("Strategy cannot be null");
        }
        strategies.put(name, strategy);
    }
    
    @Override
    public void unregisterStrategy(String name) {
        if (name != null) {
            strategies.remove(name);
        }
    }
    
    @Override
    public Strategy<T, R> getStrategy(String name) {
        if (name == null) {
            return null;
        }
        return strategies.get(name);
    }
    
    @Override
    public R execute(String strategyName, T input) {
        Strategy<T, R> strategy = getStrategy(strategyName);
        if (strategy == null) {
            throw new IllegalStateException("Strategy not found: " + strategyName);
        }
        return strategy.execute(input);
    }
    
    @Override
    public List<String> getAvailableStrategies() {
        return new ArrayList<>(strategies.keySet());
    }
}
