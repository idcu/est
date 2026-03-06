package ltd.idcu.est.patterns.api.behavioral;

public interface Colleague {
    
    void send(String message);
    
    void receive(String message);
    
    String getName();
}
