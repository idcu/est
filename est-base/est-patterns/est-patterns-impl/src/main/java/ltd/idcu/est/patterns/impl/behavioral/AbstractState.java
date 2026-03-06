package ltd.idcu.est.patterns.impl.behavioral;

import ltd.idcu.est.patterns.api.behavioral.State;

public abstract class AbstractState implements State {
    
    protected final String name;
    
    public AbstractState(String name) {
        this.name = name;
    }
    
    @Override
    public String getName() {
        return name;
    }
}
