package ltd.idcu.est.patterns.api.structural;

public interface Flyweight {
    
    void operation(String extrinsicState);
    
    String getIntrinsicState();
}
