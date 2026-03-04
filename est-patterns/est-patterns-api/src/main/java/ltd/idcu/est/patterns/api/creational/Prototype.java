package ltd.idcu.est.patterns.api.creational;

public interface Prototype<T> extends Cloneable {
    
    T clone();
}
