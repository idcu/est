package ltd.idcu.est.patterns.api.behavioral;

import java.util.List;

public interface StrategyContext<T, R> {
    
    void registerStrategy(String name, Strategy<T, R> strategy);
    
    void unregisterStrategy(String name);
    
    Strategy<T, R> getStrategy(String name);
    
    R execute(String strategyName, T input);
    
    List<String> getAvailableStrategies();
}
