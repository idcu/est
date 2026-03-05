package ltd.idcu.est.patterns.impl.creational;

import ltd.idcu.est.patterns.api.creational.Singleton;

import java.util.function.Supplier;

public class DefaultSingleton<T> implements Singleton<T> {
    
    private volatile T instance;
    private final Supplier<T> supplier;
    private final Object lock = new Object();
    
    public DefaultSingleton(Supplier<T> supplier) {
        if (supplier == null) {
            throw new IllegalArgumentException("Supplier cannot be null");
        }
        this.supplier = supplier;
    }
    
    @Override
    public T getInstance() {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = supplier.get();
                }
            }
        }
        return instance;
    }
    
    public static <T> DefaultSingleton<T> of(Supplier<T> supplier) {
        return new DefaultSingleton<>(supplier);
    }
}
