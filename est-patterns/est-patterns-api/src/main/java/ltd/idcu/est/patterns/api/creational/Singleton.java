package ltd.idcu.est.patterns.api.creational;

import java.util.function.Supplier;

public interface Singleton<T> {
    
    T getInstance();
    
    static <T> Singleton<T> of(Supplier<T> supplier) {
        return new Singleton<>() {
            private volatile T instance;
            
            @Override
            public T getInstance() {
                if (instance == null) {
                    synchronized (this) {
                        if (instance == null) {
                            instance = supplier.get();
                        }
                    }
                }
                return instance;
            }
        };
    }
}
