package ltd.idcu.est.patterns.api.behavioral;

public interface Mediator {
    
    void send(String message, Colleague colleague);
    
    void register(Colleague colleague);
}
