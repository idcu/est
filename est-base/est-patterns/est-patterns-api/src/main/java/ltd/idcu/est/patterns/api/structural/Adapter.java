package ltd.idcu.est.patterns.api.structural;

public interface Adapter<S, T> {
    
    T adapt(S source);
    
    Class<S> getSourceType();
    
    Class<T> getTargetType();
}
