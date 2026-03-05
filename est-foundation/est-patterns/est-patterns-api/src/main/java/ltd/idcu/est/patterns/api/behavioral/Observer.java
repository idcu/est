package ltd.idcu.est.patterns.api.behavioral;

public interface Observer<T> {
    
    void update(T data);
    
    String getId();
}
