package ltd.idcu.est.patterns.impl.structural;

import ltd.idcu.est.patterns.api.structural.Flyweight;

import java.util.HashMap;
import java.util.Map;

public class FlyweightFactory {
    
    private final Map<String, Flyweight> flyweights = new HashMap<>();
    
    public Flyweight getFlyweight(String key) {
        return flyweights.computeIfAbsent(key, DefaultFlyweight::new);
    }
    
    public int getCount() {
        return flyweights.size();
    }
}
