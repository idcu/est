package ltd.idcu.est.patterns.impl.creational;

import ltd.idcu.est.patterns.api.creational.Builder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class AbstractBuilder<T> implements Builder<T> {
    
    private final List<Consumer<T>> configurations = new ArrayList<>();
    
    protected AbstractBuilder<T> configure(Consumer<T> configuration) {
        if (configuration != null) {
            configurations.add(configuration);
        }
        return this;
    }
    
    protected void applyConfigurations(T target) {
        if (target != null) {
            configurations.forEach(config -> config.accept(target));
        }
    }
    
    @Override
    public Builder<T> reset() {
        configurations.clear();
        return this;
    }
}
