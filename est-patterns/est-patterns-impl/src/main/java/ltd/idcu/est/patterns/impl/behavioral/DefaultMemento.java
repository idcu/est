package ltd.idcu.est.patterns.impl.behavioral;

import ltd.idcu.est.patterns.api.behavioral.Memento;

public class DefaultMemento implements Memento {
    
    private final String state;
    
    public DefaultMemento(String state) {
        this.state = state;
    }
    
    @Override
    public String getState() {
        return state;
    }
}
