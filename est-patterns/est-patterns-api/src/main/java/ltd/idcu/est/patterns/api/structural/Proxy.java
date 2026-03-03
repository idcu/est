package ltd.idcu.est.patterns.api.structural;

public interface Proxy<T> {
    
    T getTarget();
    
    void setTarget(T target);
    
    boolean isInitialized();
}
