package ltd.idcu.est.patterns.api.behavioral;

public interface Originator {
    
    Memento save();
    
    void restore(Memento memento);
}
