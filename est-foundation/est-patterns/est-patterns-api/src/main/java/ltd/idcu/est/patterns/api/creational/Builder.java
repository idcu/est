package ltd.idcu.est.patterns.api.creational;

public interface Builder<T> {
    
    Builder<T> reset();
    
    T build();
}
