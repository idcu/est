package ltd.idcu.est.patterns.api.behavioral;

public interface Aggregate<T> {
    
    Iterator<T> createIterator();
    
    int size();
    
    T get(int index);
}
