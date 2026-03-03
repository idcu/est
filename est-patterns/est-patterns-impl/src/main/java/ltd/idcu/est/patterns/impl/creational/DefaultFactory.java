package ltd.idcu.est.patterns.impl.creational;

import ltd.idcu.est.patterns.api.creational.Factory;

import java.util.function.Supplier;

public class DefaultFactory<T> implements Factory<T> {
    
    private final String type;
    private final Supplier<T> supplier;
    
    public DefaultFactory(String type, Supplier<T> supplier) {
        if (type == null || type.isEmpty()) {
            throw new IllegalArgumentException("Type cannot be null or empty");
        }
        if (supplier == null) {
            throw new IllegalArgumentException("Supplier cannot be null");
        }
        this.type = type;
        this.supplier = supplier;
    }
    
    @Override
    public T create() {
        return supplier.get();
    }
    
    @Override
    public String getType() {
        return type;
    }
    
    public static <T> DefaultFactory<T> of(String type, Supplier<T> supplier) {
        return new DefaultFactory<>(type, supplier);
    }
}
