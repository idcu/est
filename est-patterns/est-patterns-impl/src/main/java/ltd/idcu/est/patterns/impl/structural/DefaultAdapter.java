package ltd.idcu.est.patterns.impl.structural;

import ltd.idcu.est.patterns.api.structural.Adapter;

import java.util.function.Function;

public class DefaultAdapter<S, T> implements Adapter<S, T> {
    
    private final Class<S> sourceType;
    private final Class<T> targetType;
    private final Function<S, T> adapterFunction;
    
    public DefaultAdapter(Class<S> sourceType, Class<T> targetType, Function<S, T> adapterFunction) {
        if (sourceType == null || targetType == null) {
            throw new IllegalArgumentException("Source type and target type cannot be null");
        }
        if (adapterFunction == null) {
            throw new IllegalArgumentException("Adapter function cannot be null");
        }
        this.sourceType = sourceType;
        this.targetType = targetType;
        this.adapterFunction = adapterFunction;
    }
    
    @Override
    public T adapt(S source) {
        if (source == null) {
            return null;
        }
        return adapterFunction.apply(source);
    }
    
    @Override
    public Class<S> getSourceType() {
        return sourceType;
    }
    
    @Override
    public Class<T> getTargetType() {
        return targetType;
    }
    
    public static <S, T> DefaultAdapter<S, T> of(Class<S> sourceType, Class<T> targetType, Function<S, T> adapterFunction) {
        return new DefaultAdapter<>(sourceType, targetType, adapterFunction);
    }
}
