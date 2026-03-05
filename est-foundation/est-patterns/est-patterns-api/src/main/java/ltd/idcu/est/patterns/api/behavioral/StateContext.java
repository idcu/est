package ltd.idcu.est.patterns.api.behavioral;

public interface StateContext {
    
    void setState(State state);
    
    void request();
}
