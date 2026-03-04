package ltd.idcu.est.patterns.impl.behavioral;

import ltd.idcu.est.patterns.api.behavioral.Caretaker;
import ltd.idcu.est.patterns.api.behavioral.Memento;

import java.util.ArrayList;
import java.util.List;

public class DefaultCaretaker implements Caretaker {
    
    private final List<Memento> mementos = new ArrayList<>();
    
    @Override
    public void addMemento(Memento memento) {
        mementos.add(memento);
    }
    
    @Override
    public Memento getMemento(int index) {
        if (index >= 0 && index < mementos.size()) {
            return mementos.get(index);
        }
        return null;
    }
}
