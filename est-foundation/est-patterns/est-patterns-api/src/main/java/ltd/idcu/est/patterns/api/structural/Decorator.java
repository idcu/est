package ltd.idcu.est.patterns.api.structural;

public interface Decorator<T> {
    
    T getDecorated();
    
    void setDecorated(T decorated);
}
