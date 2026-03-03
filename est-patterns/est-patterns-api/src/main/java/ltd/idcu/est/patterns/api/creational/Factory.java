package ltd.idcu.est.patterns.api.creational;

public interface Factory<T> {
    
    T create();
    
    String getType();
}
