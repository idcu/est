package ltd.idcu.est.patterns.api.behavioral;

public interface Strategy<T, R> {
    
    R execute(T input);
    
    String getName();
}
