package ltd.idcu.est.patterns.impl.structural;

import ltd.idcu.est.patterns.api.structural.Proxy;

import java.util.function.Supplier;

public class DefaultProxy<T> implements Proxy<T> {
    
    private volatile T target;
    private final Supplier<T> targetSupplier;
    private volatile boolean initialized = false;
    private final Object lock = new Object();
    
    public DefaultProxy(Supplier<T> targetSupplier) {
        if (targetSupplier == null) {
            throw new IllegalArgumentException("Target supplier cannot be null");
        }
        this.targetSupplier = targetSupplier;
    }
    
    public DefaultProxy(T target) {
        if (target == null) {
            throw new IllegalArgumentException("Target cannot be null");
        }
        this.target = target;
        this.targetSupplier = null;
        this.initialized = true;
    }
    
    @Override
    public T getTarget() {
        if (!initialized && targetSupplier != null) {
            synchronized (lock) {
                if (!initialized) {
                    target = targetSupplier.get();
                    initialized = true;
                }
            }
        }
        return target;
    }
    
    @Override
    public void setTarget(T target) {
        synchronized (lock) {
            this.target = target;
            this.initialized = target != null;
        }
    }
    
    @Override
    public boolean isInitialized() {
        return initialized;
    }
    
    public static <T> DefaultProxy<T> lazy(Supplier<T> supplier) {
        return new DefaultProxy<>(supplier);
    }
    
    public static <T> DefaultProxy<T> of(T target) {
        return new DefaultProxy<>(target);
    }
}
