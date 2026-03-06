package ltd.idcu.est.patterns.impl.behavioral;

import ltd.idcu.est.patterns.api.behavioral.Strategy;

import java.util.function.Function;

public class DefaultStrategy<T, R> implements Strategy<T, R> {
    
    private final String name;
    private final Function<T, R> function;
    
    public DefaultStrategy(String name, Function<T, R> function) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Strategy name cannot be null or empty");
        }
        if (function == null) {
            throw new IllegalArgumentException("Function cannot be null");
        }
        this.name = name;
        this.function = function;
    }
    
    @Override
    public R execute(T input) {
        return function.apply(input);
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    public static <T, R> DefaultStrategy<T, R> of(String name, Function<T, R> function) {
        return new DefaultStrategy<>(name, function);
    }
}
