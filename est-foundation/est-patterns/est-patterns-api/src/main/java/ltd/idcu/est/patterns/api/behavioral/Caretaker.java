package ltd.idcu.est.patterns.api.behavioral;

public interface Caretaker {
    
    void addMemento(Memento memento);
    
    Memento getMemento(int index);
}
