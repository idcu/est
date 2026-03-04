package ltd.idcu.est.patterns.api.behavioral;

public interface Handler<T> {
    
    void setNext(Handler<T> next);
    
    void handle(T request);
}
