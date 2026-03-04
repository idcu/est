package ltd.idcu.est.patterns.api.behavioral;

public interface Iterator<T> {
    
    boolean hasNext();
    
    T next();
    
    void reset();
}
